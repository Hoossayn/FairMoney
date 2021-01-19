package com.example.fairmoney.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fairmoney.data.Model.entities.User
import com.example.fairmoney.data.repository.UserRepository
import com.example.fairmoney.utils.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel for Movie details screen
 */
class DetailsViewModel @ViewModelInject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var _id = MutableLiveData<String>()
    private val _user: LiveData<Resource<User>?> = _id.distinctUntilChanged().switchMap {
        liveData {
            userRepository.fetchUserDetails(it).onStart {
                emit(Resource.loading())
            }.collect {
                emit(it)
            }
        }
    }
    val user = _user

    fun getMovieDetail(id: String) {
        _id.value = id
    }
}