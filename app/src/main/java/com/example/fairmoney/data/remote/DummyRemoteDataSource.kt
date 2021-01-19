package com.example.fairmoney.data.remote

import com.example.fairmoney.data.Model.AllUsers
import com.example.fairmoney.data.Model.entities.User
import com.example.fairmoney.utils.ErrorUtils
import com.example.fairmoney.utils.Resource
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * fetches data from remote source
 */
class DummyRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchTrendingMovies(): Resource<AllUsers> {
        val movieService = retrofit.create(DummyService::class.java);
        return getResponse(
                request = { movieService.getAllUsers() },
                defaultErrorMessage = "Error fetching Movie list")

    }

    suspend fun fetchMovie(id: String): Resource<User> {
        val movieService = retrofit.create(DummyService::class.java);
        return getResponse(
                request = { movieService.getUserDetails(id) },
                defaultErrorMessage = "Error fetching Movie Description")
    }

    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Resource<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Resource.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Resource.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Resource.error("Unknown Error", null)
        }
    }
}