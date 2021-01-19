package com.example.fairmoney.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairmoney.data.Model.AllUsers
import com.example.fairmoney.data.repository.UserRepository
import com.example.fairmoney.utils.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val userRepository: UserRepository):ViewModel() {

    private val _usersList = MutableLiveData<Resource<AllUsers>>()
    val usersList = _usersList

    init {
        fetchAllUsers()
    }

    private fun fetchAllUsers() {
        viewModelScope.launch {
            userRepository.fetchAllUsers().collect{
                _usersList.value = it
            }
        }
    }
}