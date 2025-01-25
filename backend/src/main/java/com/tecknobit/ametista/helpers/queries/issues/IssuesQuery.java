package com.tecknobit.ametista.helpers.queries.issues;

import com.tecknobit.ametista.services.applications.entities.AmetistaDevice;
import com.tecknobit.ametista.services.collector.entities.issues.IssueAnalytic;
import com.tecknobit.ametistacore.enums.Platform;
import com.tecknobit.apimanager.annotations.Wrapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tecknobit.ametista.services.applications.entities.AmetistaApplication.APPLICATION_KEY;
import static com.tecknobit.ametista.services.applications.entities.AmetistaDevice.*;
import static com.tecknobit.ametista.services.collector.entities.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.NAME_KEY;
import static jakarta.persistence.criteria.JoinType.INNER;

/**
 * The {@code IssuesQuery} class is useful to execute the query to retrieve the issues data from the database with the
 * possibility to dynamically filter that query with the {@link CriteriaBuilder}'s logic
 *
 * @param <T> The type of the issues to retrieve
 * @author N7ghtm4r3 - Tecknobit
 */
public class IssuesQuery<T extends IssueAnalytic> {

    /**
     * {@code NAME_REGEX} regex to validate the exception name
     */
    private static final String NAME_REGEX = "\\b(\\w+Exception)\\b";

    /**
     * {@code DATE_REGEX} regex to validate the date values
     */
    private static final String DATE_REGEX = "\\b(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/\\d{4}(\\s+([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9])?\\b";

    /**
     * {@code VERSION_REGEX} regex to validate the versions values
     */
    private static final String VERSION_REGEX = "\\b(v?)(\\d+(\\.\\d+)*)([-_]?([a-zA-Z]+[-_]?\\d*)?)?\\b";

    /**
     * {@code BRAND_REGEX} regex to validate the brands values
     */
    private static final String BRAND_REGEX = "^(Pixel|Apple|Samsung|Google|OnePlus|Sony|Motorola|Oppo|Vivo|Nokia|HTC|Asus|Realme|Honor|ZTE|Dell|HP|Lenovo|Acer|Razer|Microsoft|MSI|Toshiba|LG|Huawei|Amazon|Xiaomi|RCA|iMac|Alienware|ASUS ROG|HP Omen|Gigabyte AORUS|Corsair|Lenovo Legion|Dell G Series)$";

    /**
     * {@code BROWSER_REGEX} regex to validate the browsers values
     */
    private static final String BROWSER_REGEX = "^(Google Chrome|Safari|Microsoft Edge|Mozilla Firefox|Opera|Samsung Internet|Brave|Vivaldi|Tor Browser|Epic Privacy Browser|Maxthon)$";

    /**
     * {@code MODEL_REGEX} regex to validate the models values
     */
    private static final String MODEL_REGEX = "^(?!" + BROWSER_REGEX + ")(\\d{1,2}[A-Za-z]?[- ]?\\d?[A-Za-z]?|[A-Za-z]+[ -]?\\d{1,2}([A-Za-z]?|\\d*)|[A-Za-z0-9]+[- ]?\\d{1,2}|\\d{1,2}[- ]?[A-Za-z0-9]+|[A-Za-z0-9]+[ -]?[A-Za-z0-9]+|\\d{1,2}[A-Za-z]? ?[A-Za-z]+|\\w+ ?[A-Za-z0-9]+)$";

    /**
     * {@code NAME_PATTERN} the pattern to validate the names values
     */
    public static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    /**
     * {@code DATE_PATTERN} the pattern to validate the date values
     */
    private static final Pattern DATE_PATTERN = Pattern.compile(DATE_REGEX);

    /**
     * {@code VERSION_PATTERN} the pattern to validate the versions values
     */
    private static final Pattern VERSION_PATTERN = Pattern.compile(VERSION_REGEX);

    /**
     * {@code BRAND_PATTERN} the pattern to validate the brands values
     */
    private static final Pattern BRAND_PATTERN = Pattern.compile(BRAND_REGEX);

    /**
     * {@code MODEL_PATTERN} the pattern to validate the models values
     */
    private static final Pattern MODEL_PATTERN = Pattern.compile(MODEL_REGEX);

    /**
     * {@code BROWSER_PATTERN} the pattern to validate the browsers values
     */
    protected static final Pattern BROWSER_PATTERN = Pattern.compile(BROWSER_REGEX);

    /**
     * {@code entityManager} manage the entities
     */
    protected final EntityManager entityManager;

    /**
     * {@code platformName} the name of the platform
     */
    protected final Platform platform;

    /**
     * {@code applicationId} the identifier of the application
     */
    protected final String applicationId;

    /**
     * {@code rawFilters} the filters all together in raw format
     */
    protected final CopyOnWriteArraySet<String> rawFilters;

    /**
     * {@code criteriaBuilder} the builder to add the criteria to filter the query
     */
    protected final CriteriaBuilder criteriaBuilder;

    /**
     * {@code query} the criteria prepared query to execute
     */
    protected final CriteriaQuery<T> query;

    /**
     * {@code issue} the root table
     */
    protected final Root<T> issue;

    /**
     * {@code predicates} the list of predicates to apply to the query
     */
    protected final ArrayList<Predicate> predicates;

    /**
     * {@code device} the device related to the issue retrieved with the {@link Join}
     */
    protected final Join<T, AmetistaDevice> device;

    /**
     * Constructor to init the {@link IssuesQuery}
     *
     * @param issueType     The type of the issue to retrieve
     * @param entityManager Manage the entities
     * @param platform      The name of the platform
     * @param applicationId The identifier of the application
     * @param rawFilters    The filters all together in raw format
     */
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

    /**
     * Method to get the issue executing the query
     *
     * @param pageable The pageable value to use to paginate the query
     *
     * @return the issues retrieved from database as {@link List} of {@link T}
     */
    public List<T> getIssues(Pageable pageable) {
        prepareQuery();
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

    /**
     * Method to get the issue executing the query
     *
     * @return the issues retrieved from database as {@link List} of {@link T}
     */
    public List<T> getIssues() {
        if (predicates.isEmpty())
            prepareQuery();
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    /**
     * Method to prepare the query adding the predicates
     */
    private void prepareQuery() {
        fillPredicates();
        query.select(issue).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    }

    /**
     * Wrapper method to fill all the predicates dynamically
     */
    @Wrapper
    protected void fillPredicates() {
        addMandatoryFilters();
        addNameFilters();
        addDateFilters();
        addVersionFilters();
        addBrandFilters();
        addModelFilters();
    }

    /**
     * Method to add the mandatory filters such {@link #applicationId} and {@link #platform}
     */
    private void addMandatoryFilters() {
        predicates.add(criteriaBuilder.equal(issue.get(APPLICATION_KEY).get(IDENTIFIER_KEY), applicationId));
        predicates.add(criteriaBuilder.equal(issue.get(PLATFORM_KEY), platform));
    }

    /**
     * Method to add the names filter
     */
    private void addNameFilters() {
        HashSet<String> names = getNameFilters();
        if (names != null) {
            Predicate nameIn = issue.get(NAME_KEY).in(names);
            predicates.add(nameIn);
        }
    }

    /**
     * Method to get from the {@link #rawFilters} the names filter related
     *
     * @return the names filter as {@link HashSet} of {@link String}
     */
    @Wrapper
    private HashSet<String> getNameFilters() {
        return extractFiltersByPattern(NAME_PATTERN);
    }

    /**
     * Method to add the dates filter
     */
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

    /**
     * Method to get from the {@link #rawFilters} the dates filter related
     *
     * @return the dates filter as {@link HashSet} of {@link String}
     */
    @Wrapper
    private HashSet<String> getDateFilters() {
        return extractFiltersByPattern(DATE_PATTERN);
    }

    /**
     * Method to add the versions filter
     */
    private void addVersionFilters() {
        HashSet<String> versions = getVersionFilters();
        if (versions != null) {
            Predicate[] versionPredicates = getVersionPredicates(versions).toArray(new Predicate[0]);
            Predicate versionCriteria = criteriaBuilder.or(versionPredicates);
            predicates.add(versionCriteria);
        }
    }

    /**
     * Method to get from the {@link #rawFilters} the versions filter related
     *
     * @return the versions filter as {@link HashSet} of {@link String}
     */
    @Wrapper
    private HashSet<String> getVersionFilters() {
        return extractFiltersByPattern(VERSION_PATTERN);
    }

    /**
     * Method to get the version predicates dynamically
     */
    protected ArrayList<Predicate> getVersionPredicates(HashSet<String> versions) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        Predicate appVersionIn = issue.get("appVersion").in(versions);
        predicates.add(appVersionIn);
        Predicate osVersionIn = device.get("osVersion").in(versions);
        predicates.add(osVersionIn);
        return predicates;
    }

    /**
     * Method to add the brands filter
     */
    private void addBrandFilters() {
        HashSet<String> brands = getBrandFilters();
        if (brands != null) {
            Predicate brandIn = device.get(BRAND_KEY).in(brands);
            predicates.add(brandIn);
        }
    }

    /**
     * Method to get from the {@link #rawFilters} the brands filter related
     *
     * @return the brands filter as {@link HashSet} of {@link String}
     */
    @Wrapper
    private HashSet<String> getBrandFilters() {
        return extractFiltersByPattern(BRAND_PATTERN);
    }

    /**
     * Method to add the models filter
     */
    private void addModelFilters() {
        HashSet<String> models = getModelFilters();
        if (models != null) {
            Predicate modelIn = device.get(MODEL_KEY).in(models);
            predicates.add(modelIn);
        }
    }

    /**
     * Method to get from the {@link #rawFilters} the models filter related
     *
     * @return the models filter as {@link HashSet} of {@link String}
     */
    @Wrapper
    private HashSet<String> getModelFilters() {
        return extractFiltersByPattern(MODEL_PATTERN);
    }

    /**
     * Method to extract from the {@link #rawFilters} the specific set of filters
     *
     * @param pattern The pattern to use to extract the specific set
     *
     * @return the specific filters as {@link HashSet} of {@link String}
     */
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