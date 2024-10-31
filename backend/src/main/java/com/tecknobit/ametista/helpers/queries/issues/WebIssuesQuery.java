package com.tecknobit.ametista.helpers.queries.issues;

import com.tecknobit.ametistacore.models.analytics.issues.WebIssueAnalytic;
import com.tecknobit.apimanager.annotations.Wrapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.tecknobit.ametistacore.models.Platform.WEB;
import static com.tecknobit.ametistacore.models.analytics.issues.WebIssueAnalytic.BROWSER_KEY;

public class WebIssuesQuery extends IssuesQuery<WebIssueAnalytic> {

    public WebIssuesQuery(EntityManager entityManager, String applicationId, Set<String> rawFilters) {
        super(WebIssueAnalytic.class, entityManager, WEB, applicationId, rawFilters);
    }

    @Override
    protected void fillPredicates() {
        super.fillPredicates();
        addBrowserPredicates();
    }

    @Override
    protected ArrayList<Predicate> getVersionPredicates(HashSet<String> versions) {
        ArrayList<Predicate> predicates = super.getVersionPredicates(versions);
        Predicate browserVersionIn = issue.get("browserVersion").in(versions);
        predicates.add(browserVersionIn);
        return predicates;
    }

    private void addBrowserPredicates() {
        HashSet<String> browsers = getBrowserFilters();
        if (browsers != null) {
            Predicate browserIn = issue.get(BROWSER_KEY).in(browsers);
            predicates.add(browserIn);
        }
    }

    @Wrapper
    private HashSet<String> getBrowserFilters() {
        return getFiltersList(BROWSER_PATTERN);
    }

}
