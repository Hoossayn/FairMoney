package com.example.fairmoney.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.fairmoney.data.Model.AllUsers
import com.example.fairmoney.data.Model.entities.Data
import com.example.fairmoney.data.remote.DummyService
import com.example.fairmoney.data.repository.UserRepository
import com.example.fairmoney.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.mockito.ArgumentCaptor.*
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private lateinit var mainActivityViewModel: MainViewModel

    @Mock
    lateinit var repository: UserRepository

    @Mock
    private lateinit var dataObserver: Observer<Resource<List<AllUsers>>>

    @Mock
    private lateinit var dummyService: DummyService

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        mainActivityViewModel = MainViewModel(repository)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getUsers_whenRequest_ReturnSuccessful() = runBlockingTest{

        doReturn(emptyList<Data>()).`when`(dummyService).getAllUsers()
     //   mainActivityViewModel.usersList.observeForever(dataObserver)
        verify(dummyService).getAllUsers()
        verify(dataObserver).onChanged(Resource.success(emptyList()))
    //    mainActivityViewModel.usersList.removeObserver(dataObserver)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun returnError_whenRequest_shouldReturnError() = runBlockingTest {
        val errorMessage = "Error Message For You"
        doThrow(RuntimeException(errorMessage)).`when`(dummyService).getAllUsers()
     //   mainActivityViewModel.usersList.observeForever(dataObserver)
        verify(dummyService).getAllUsers()
        verify(dataObserver).onChanged(Resource.error(RuntimeException(errorMessage).toString(), null))
   //     mainActivityViewModel.usersList.removeObserver(dataObserver)
    }

}