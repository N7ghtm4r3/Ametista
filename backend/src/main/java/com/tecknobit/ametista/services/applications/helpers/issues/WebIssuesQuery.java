package com.tecknobit.ametista.services.applications.helpers.issues;

import com.tecknobit.ametista.services.collector.entities.issues.WebIssueAnalytic;
import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.equinoxbackend.annotations.FiltersAdder;
import com.tecknobit.equinoxbackend.annotations.FiltersExtractor;
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.tecknobit.ametistacore.ConstantsKt.BROWSER_KEY;
import static com.tecknobit.ametistacore.enums.Platform.WEB;

/**
 * The {@code WebIssuesQuery} class is useful to execute the query to retrieve the web-issues data from the database with the
 * possibility to dynamically filter that query with the {@link CriteriaBuilder}'s logic
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class WebIssuesQuery extends IssuesQuery<WebIssueAnalytic> {

    /**
     * Constructor to init the {@link IssuesQuery}
     *
     * @param entityManager Manage the entities
     * @param applicationId The identifier of the application
     * @param rawFilters    The filters all together in raw format
     */
    public WebIssuesQuery(EntityManager entityManager, String applicationId, Set<String> rawFilters) {
        super(WebIssueAnalytic.class, entityManager, WEB, applicationId, rawFilters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequiresSuperCall
    protected void fillPredicates() {
        super.fillPredicates();
        addBrowserPredicates();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArrayList<Predicate> getVersionPredicates(HashSet<String> versions) {
        ArrayList<Predicate> predicates = super.getVersionPredicates(versions);
        Predicate browserVersionIn = root.get("browserVersion").in(versions);
        predicates.add(browserVersionIn);
        return predicates;
    }

    /**
     * Method to add the browsers filter
     */
    @FiltersAdder
    private void addBrowserPredicates() {
        HashSet<String> browsers = getBrowserFilters();
        if (browsers != null) {
            Predicate browserIn = root.get(BROWSER_KEY).in(browsers);
            predicates.add(browserIn);
        }
    }

    /**
     * Method to get from the {@link #rawFilters} the browsers filter related
     *
     * @return the browsers filter as {@link HashSet} of {@link String}
     */
    @Wrapper
    @FiltersExtractor
    private HashSet<String> getBrowserFilters() {
        return extractFiltersByPattern(BROWSER_PATTERN);
    }

}
