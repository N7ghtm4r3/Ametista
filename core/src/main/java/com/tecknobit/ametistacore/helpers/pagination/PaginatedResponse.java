package com.tecknobit.ametistacore.helpers.pagination;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public class PaginatedResponse<T> {

    public static final String PAGE_KEY = "page";

    public static final String DATA_SIZE_KEY = "data_size";

    public static final String PAGE_SIZE_KEY = "page_size";

    public static final String IS_LAST_PAGE_KEY = "is_last_page";

    public static final String DEFAULT_PAGE = "0";

    public static final String DEFAULT_PAGE_SIZE = "10";

    private final List<T> data;

    private final int page;

    private final int pageSize;

    private final boolean isLastPage;

    public PaginatedResponse(List<T> data, int page, int pageSize, CrudRepository<?, ?> repository) {
        this.data = data;
        this.page = page;
        this.pageSize = pageSize;
        long totalData = repository.count();
        int balancer;
        if ((totalData % pageSize) == 0)
            balancer = 1;
        else
            balancer = 0;
        int maxPages = (int) (totalData / pageSize) - balancer;
        this.isLastPage = totalData < pageSize || page >= maxPages;
    }

    public PaginatedResponse(List<T> data, int page, int pageSize, boolean isLastPage) {
        this.data = data;
        this.page = page;
        this.pageSize = pageSize;
        this.isLastPage = isLastPage;
    }

    // TODO: 29/10/2024 CREATE JSONHELPER CONSTR 

    public List<T> getData() {
        return data;
    }

    @JsonGetter(DATA_SIZE_KEY)
    public int getDataSize() {
        return data.size();
    }

    public int getPage() {
        return page;
    }

    @JsonGetter(PAGE_SIZE_KEY)
    public int getPageSize() {
        return pageSize;
    }

    @JsonGetter(IS_LAST_PAGE_KEY)
    public boolean isLastPage() {
        return isLastPage;
    }

}
