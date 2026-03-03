package com.example.fitnessapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fitnessapp.view.LoginBody
import com.example.fitnessapp.view.RegisterBody
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FitLifeInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun login_ui_elements_displayed() {
        // Start the LoginBody composable
        composeTestRule.setContent {
            LoginBody(userViewModel = null, onLoginSuccess = {})
        }

        // 1. Check if the screen title is displayed
        composeTestRule.onNodeWithText("Sign In").assertIsDisplayed()
        
        // 2. Check if the Email placeholder is present
        composeTestRule.onNodeWithText("example@gmail.com").assertIsDisplayed()
        
        // 3. Check if the "Sign in" button is visible
        composeTestRule.onNodeWithText("Sign in").assertIsDisplayed()
    }

    @Test
    fun registration_ui_elements_displayed() {
        // Start the RegisterBody composable
        composeTestRule.setContent {
            RegisterBody(userViewModel = null, onRegistrationSuccess = {})
        }

        // 1. Check if the registration title is displayed
        composeTestRule.onNodeWithText("Create Account").assertIsDisplayed()
        
        // 2. Check if the Full Name field placeholder is present
        composeTestRule.onNodeWithText("Full Name").assertIsDisplayed()
        
        // 3. Check if the "Sign Up" button is visible
        composeTestRule.onNodeWithText("Sign Up").assertIsDisplayed()
    }
}
