package com.tecknobit.ametistacore.helpers.pagination

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.tecknobit.apimanager.formatters.JsonHelper
import org.json.JSONObject
import org.springframework.data.repository.CrudRepository

class PaginatedResponse<T> {

    companion object {

        const val DATA_KEY: String = "data"

        const val PAGE_KEY: String = "page"

        const val DATA_SIZE_KEY: String = "data_size"

        const val PAGE_SIZE_KEY: String = "page_size"

        const val IS_LAST_PAGE_KEY: String = "is_last_page"

        const val DEFAULT_PAGE: Int = 0

        const val DEFAULT_PAGE_HEADER_VALUE: String = "0"

        const val DEFAULT_PAGE_SIZE: Int = 10

        const val DEFAULT_PAGE_SIZE_HEADER_VALUE: String = "10"

    }

    val data: List<T>

    val page: Int

    @get:JsonGetter(PAGE_SIZE_KEY)
    val pageSize: Int

    @get:JsonIgnore
    val previousPage: Int
        get() = page - 1

    @get:JsonIgnore
    val nextPage: Int
        get() = page + 1

    @get:JsonGetter(IS_LAST_PAGE_KEY)
    val isLastPage: Boolean

    @get:JsonGetter(DATA_SIZE_KEY)
    val dataSize: Int
        get() = data.size

    constructor(data: List<T>, page: Int, pageSize: Int, repository: CrudRepository<*, *>) : this(
        data,
        page,
        pageSize,
        repository.count()
    )

    constructor(data: List<T>, page: Int, pageSize: Int, totalDataCount: Long) {
        this.data = data
        this.page = page
        this.pageSize = pageSize
        val balancer = if ((totalDataCount % pageSize) == 0L) 1
        else 0
        val maxPages = (totalDataCount / pageSize).toInt() - balancer
        this.isLastPage = totalDataCount < pageSize || page >= maxPages
    }

    constructor(data: List<T>, page: Int, pageSize: Int, isLastPage: Boolean) {
        this.data = data
        this.page = page
        this.pageSize = pageSize
        this.isLastPage = isLastPage
    }

    constructor(
        hPage: JsonHelper,
        supplier: (JSONObject) -> T
    ) {
        val jData: ArrayList<JSONObject> = hPage.fetchList(DATA_KEY)
        val data = arrayListOf<T>()
        jData.forEach { item ->
            val instantiatedItem = supplier.invoke(item)
            data.add(instantiatedItem)
        }
        this.data = data
        page = hPage.getInt(PAGE_KEY)
        pageSize = hPage.getInt(PAGE_SIZE_KEY)
        isLastPage = hPage.getBoolean(IS_LAST_PAGE_KEY)
    }

}
