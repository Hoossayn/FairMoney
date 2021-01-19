package com.example.fairmoney.data.repository

import com.example.fairmoney.data.Model.AllUsers
import com.example.fairmoney.data.Model.entities.User
import com.example.fairmoney.data.local.UserDao
import com.example.fairmoney.data.remote.DummyRemoteDataSource
import com.example.fairmoney.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Repository which fetches data from Remote or Local data sources
 */
class UserRepository @Inject constructor(
        private val dummyRemoteDataSource: DummyRemoteDataSource,
        private val userDao: UserDao
) {

    suspend fun fetchAllUsers(): Flow<Resource<AllUsers>?> {
        return flow {
            emit(fetchAllUserCached())
            emit(Resource.loading())
            val result = dummyRemoteDataSource.fetchTrendingMovies()

            //Cache to database if response is successful
            if (result.status == Resource.Status.SUCCESS) {
                result.data?.data?.let { it ->
                    userDao.deleteAll(it)
                    userDao.insertAll(it)
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    private fun fetchAllUserCached(): Resource<AllUsers>? =
            userDao.getAllUsers()?.let {
                Resource.success(AllUsers(it))
            }

    private fun fetchUserDetailCached(id: String):Resource<User>?=
        userDao.getUserDetail(id)?.let {
            Resource.success(userDao.getUserDetail(id))
        }

    suspend fun fetchUserDetails(id: String): Flow<Resource<User>?> {
        return flow {
            emit(fetchUserDetailCached(id))
            emit(Resource.loading())

            val result =  dummyRemoteDataSource.fetchMovie(id)
            if (result.status == Resource.Status.SUCCESS) {
                result.data?.let { it ->
                    userDao.insert(it)
                }
            }
            emit(result)

        }.flowOn(Dispatchers.IO)
    }
}