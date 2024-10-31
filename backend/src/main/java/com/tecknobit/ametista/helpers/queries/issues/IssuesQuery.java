package com.tecknobit.ametista.helpers.queries.issues;

import com.tecknobit.ametistacore.models.AmetistaDevice;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic;
import com.tecknobit.apimanager.annotations.Wrapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATION_KEY;
import static com.tecknobit.ametistacore.models.AmetistaDevice.*;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY;
import static jakarta.persistence.criteria.JoinType.INNER;

public class IssuesQuery<T extends IssueAnalytic> {

    private static final String DATE_REGEX = "\\b(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/\\d{4}(\\s+([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9])?\\b";

    private static final String VERSION_REGEX = "\\b(v?)(\\d+(\\.\\d+)*)([-_]?([a-zA-Z]+[-_]?\\d*)?)?\\b";

    private static final String BRAND_REGEX = "^(Pixel|Apple|Samsung|Google|OnePlus|Sony|Motorola|Oppo|Vivo|Nokia|HTC|Asus|Realme|Honor|ZTE|Dell|HP|Lenovo|Acer|Razer|Microsoft|MSI|Toshiba|LG|Huawei|Amazon|Xiaomi|RCA|iMac|Alienware|ASUS ROG|HP Omen|Gigabyte AORUS|Corsair|Lenovo Legion|Dell G Series)$";

    private static final String BROWSER_REGEX = "^(Google Chrome|Safari|Microsoft Edge|Mozilla Firefox|Opera|Samsung Internet|Brave|Vivaldi|Tor Browser|Epic Privacy Browser|Maxthon)$";

    private static final String MODEL_REGEX = "^(?!" + BROWSER_REGEX + ")(\\d{1,2}[A-Za-z]?[- ]?\\d?[A-Za-z]?|[A-Za-z]+[ -]?\\d{1,2}([A-Za-z]?|\\d*)|[A-Za-z0-9]+[- ]?\\d{1,2}|\\d{1,2}[- ]?[A-Za-z0-9]+|[A-Za-z0-9]+[ -]?[A-Za-z0-9]+|\\d{1,2}[A-Za-z]? ?[A-Za-z]+|\\w+ ?[A-Za-z0-9]+)$";

    private static final Pattern DATE_PATTERN = Pattern.compile(DATE_REGEX);

    private static final Pattern VERSION_PATTERN = Pattern.compile(VERSION_REGEX);

    private static final Pattern BRAND_PATTERN = Pattern.compile(BRAND_REGEX);

    private static final Pattern MODEL_PATTERN = Pattern.compile(MODEL_REGEX);

    protected static final Pattern BROWSER_PATTERN = Pattern.compile(BROWSER_REGEX);

    protected final EntityManager entityManager;

    protected final Platform platform;

    protected final String applicationId;

    protected final CopyOnWriteArraySet<String> rawFilters;

    protected final CriteriaBuilder criteriaBuilder;

    protected final CriteriaQuery<T> query;

    protected final Root<T> issue;

    protected final ArrayList<Predicate> predicates;

    protected final Join<T, AmetistaDevice> device;

    public IssuesQuery(Class<T> issueType, EntityManager entityManager, Platform platform, String applicationId,
                       Set<String> rawFilters) {
        this.entityManager = entityManager;
        this.platform = platform;
        this.applicationId = applicationId;
        this.rawFilters = new CopyOnWriteArraySet<>(rawFilters);
        criteriaBuilder = entityManager.getCriteriaBuilder();
        query = criteriaBuilder.createQuery(issueType);
        issue = query.from(issueType);
        device = issue.join(DEVICE_KEY, INNER);
        predicates = new ArrayList<>();
    }

    public List<T> getIssues(Pageable pageable) {
        prepareQuery();
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

    public List<T> getIssues() {
        if (predicates.isEmpty())
            prepareQuery();
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    private void prepareQuery() {
        fillPredicates();
        query.select(issue).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    }

    protected void fillPredicates() {
        addMandatoryFilters();
        addDateFilters();
        addVersionFilters();
        addBrandFilters();
        addModelFilters();
    }

    private void addMandatoryFilters() {
        predicates.add(criteriaBuilder.equal(issue.get(APPLICATION_KEY).get(IDENTIFIER_KEY), applicationId));
        predicates.add(criteriaBuilder.equal(issue.get(PLATFORM_KEY), platform));
    }

    private void addDateFilters() {
        HashSet<String> dates = getDateFilters();
        if (dates != null) {
            Expression<String> formattedDate = criteriaBuilder.function("DATE_FORMAT", String.class,
                    criteriaBuilder.function("FROM_UNIXTIME", Date.class, criteriaBuilder.quot(issue.get("creationDate"), 1000)),
                    criteriaBuilder.literal("%d/%m/%Y")
            );
            predicates.add(formattedDate.in(dates));
        }
    }

    @Wrapper
    private HashSet<String> getDateFilters() {
        return extractFiltersByPattern(DATE_PATTERN);
    }

    private void addVersionFilters() {
        HashSet<String> versions = getVersionFilters();
        if (versions != null) {
            Predicate[] versionPredicates = getVersionPredicates(versions).toArray(new Predicate[0]);
            Predicate versionCriteria = criteriaBuilder.or(versionPredicates);
            predicates.add(versionCriteria);
        }
    }

    @Wrapper
    private HashSet<String> getVersionFilters() {
        return extractFiltersByPattern(VERSION_PATTERN);
    }

    protected ArrayList<Predicate> getVersionPredicates(HashSet<String> versions) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        Predicate appVersionIn = issue.get("appVersion").in(versions);
        predicates.add(appVersionIn);
        Predicate osVersionIn = device.get("osVersion").in(versions);
        predicates.add(osVersionIn);
        return predicates;
    }

    private void addBrandFilters() {
        HashSet<String> brands = getBrandFilters();
        if (brands != null) {
            Predicate brandIn = device.get(BRAND_KEY).in(brands);
            predicates.add(brandIn);
        }
    }

    @Wrapper
    private HashSet<String> getBrandFilters() {
        return extractFiltersByPattern(BRAND_PATTERN);
    }

    private void addModelFilters() {
        HashSet<String> models = getModelFilters();
        if (models != null) {
            Predicate modelIn = device.get(MODEL_KEY).in(models);
            predicates.add(modelIn);
        }
    }

    @Wrapper
    private HashSet<String> getModelFilters() {
        return extractFiltersByPattern(MODEL_PATTERN);
    }

    protected HashSet<String> extractFiltersByPattern(Pattern pattern) {
        HashSet<String> filtersList = new HashSet<>();
        for (String filter : rawFilters) {
            Matcher matcher = pattern.matcher(filter);
            if (matcher.matches()) {
                filtersList.add(filter.trim());
                rawFilters.remove(filter);
            }
        }
        if (filtersList.isEmpty())
            return null;
        return filtersList;
    }

}