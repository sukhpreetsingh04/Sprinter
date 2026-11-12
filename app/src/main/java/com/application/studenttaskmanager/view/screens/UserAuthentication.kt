package com.application.studenttaskmanager.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.application.studenttaskmanager.data.User
import com.application.studenttaskmanager.data.UserRepository
import com.application.studenttaskmanager.view.ui.design.AppBackground
import com.application.studenttaskmanager.view.ui.design.AppLogo
import com.application.studenttaskmanager.view.ui.screen.WelcomeScreen

@Composable
fun UserAuthentication(
    modifier: Modifier = Modifier,
    userRepository: UserRepository,
    onAuthenticated: (User) -> Unit
) {

    Box(modifier = modifier.fillMaxSize()) {

        AppBackground()

        AppLogo()

        WelcomeScreen(
            onLogin = { email, password ->
                userRepository.login(email, password)
            },
            onRegister = { name, email, password ->
                userRepository.register(name, email, password)
            },
            onAuthenticated = onAuthenticated
        )
    }
}
