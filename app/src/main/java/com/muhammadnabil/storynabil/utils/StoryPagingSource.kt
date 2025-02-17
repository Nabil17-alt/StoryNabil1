package com.muhammadnabil.storynabil.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muhammadnabil.storynabil.api.StoryAPI
import com.muhammadnabil.storynabil.model.Story


class StoryPagingSource(private val apiService: StoryAPI, private val token: String) : PagingSource<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllStories(token, position, params.loadSize)

            if (response.error) {
                throw Exception(response.message)
            }

            LoadResult.Page(
                data = response.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}