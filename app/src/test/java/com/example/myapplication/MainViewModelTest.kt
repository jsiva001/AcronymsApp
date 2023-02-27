package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.model.data.AcronymsDataItem
import com.example.myapplication.model.repository.AcronymsDataRepo
import com.example.myapplication.util.ApiState
import com.example.myapplication.util.NetworkHelper
import com.example.myapplication.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var mainViewModel: MainViewModel

    @Mock
    private lateinit var acronymsDataRepo: AcronymsDataRepo

    @Mock
    private lateinit var acronymsLiveDataObserver: Observer<ApiState>

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mainViewModel = MainViewModel(acronymsDataRepo, networkHelper).apply {
            acronymsData.observeForever(acronymsLiveDataObserver)
        }
    }

    @Test
    fun executeApiWithSuccessCase() {
        testCoroutineRule.runBlockingTest {
            doReturn(emptyList<AcronymsDataItem>())
                .`when`(acronymsDataRepo)
                .getAcronymsData("AIMS")
            mainViewModel.fetchAcronymsData("AIMS")
            verify(acronymsLiveDataObserver).onChanged(ApiState.Loading)
            verify(acronymsLiveDataObserver).onChanged(ApiState.Success(emptyList<AcronymsDataItem>()))
            mainViewModel.acronymsData.removeObserver(acronymsLiveDataObserver)
        }
    }

    @Test
    fun executeApiWithFailureCase() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Run Time Exception"
            doThrow(RuntimeException(errorMessage))
                .`when`(acronymsDataRepo)
                .getAcronymsData("____")
            mainViewModel.fetchAcronymsData("____")
            verify(acronymsLiveDataObserver).onChanged(ApiState.Loading)
            verify(acronymsLiveDataObserver).onChanged(
                ApiState.Failure(RuntimeException(errorMessage))
            )
            mainViewModel.acronymsData.removeObserver(acronymsLiveDataObserver)
        }
    }
}