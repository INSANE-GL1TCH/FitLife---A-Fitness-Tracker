package com.example.fitnessapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fitnessapp.repository.UserRepository
import com.example.fitnessapp.viewmodel.UserViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repo = mock<UserRepository>()
    private val viewModel = UserViewModel(repo)

    @Test
    fun login_success_test() {
        // Arrange
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("test@gmail.com"), eq("123456"), any())

        var successResult = false
        var messageResult = ""

        // Act
        viewModel.login("test@gmail.com", "123456") { success, msg ->
            successResult = success
            messageResult = msg
        }

        // Assert
        assertTrue(successResult)
        assertEquals("Login success", messageResult)
        verify(repo).login(eq("test@gmail.com"), eq("123456"), any())
    }

    @Test
    fun forgetPassword_success_test() {
        // Arrange
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Reset email sent")
            null
        }.`when`(repo).forgetPassword(eq("test@gmail.com"), any())

        var successResult = false
        var messageResult = ""

        // Act
        viewModel.forgetPassword("test@gmail.com") { success, msg ->
            successResult = success
            messageResult = msg
        }

        // Assert
        assertTrue(successResult)
        assertEquals("Reset email sent", messageResult)
        verify(repo).forgetPassword(eq("test@gmail.com"), any())
    }
}
