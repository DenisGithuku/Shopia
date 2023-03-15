package com.githukudenis.auth.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.githukudenis.auth.R
import com.githukudenis.auth.api.User

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    snackbarHostState: SnackbarHostState, modifier: Modifier = Modifier, onLoggedIn: () -> Unit
) {

    val loginViewModel: LoginViewModel = hiltViewModel()
    val uiState by loginViewModel.state
    val passwordIsVisible = uiState.formState.passwordIsVisible


    if (uiState.userMessages.isNotEmpty()) {
        LaunchedEffect(key1 = uiState.userMessages) {
            val userMessage = uiState.userMessages[0]
            snackbarHostState.showSnackbar(
                message = userMessage.message ?: "An error occurred"
            )
            userMessage.id?.let { LoginUiEvent.OnUserMessageShown(it) }
                ?.let { loginViewModel.onEvent(it) }
        }
    }

    if (uiState.isLoading) {
        ShowDialog(message = "Please wait...")
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back", style = TextStyle(
                fontSize = 40.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = modifier.height(16.dp))
        OutlinedTextField(value = uiState.formState.username,
            onValueChange = { value -> loginViewModel.onEvent(LoginUiEvent.OnUsernameChange(value)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false
            ),
            shape = RoundedCornerShape(4.dp),
            singleLine = true,
            placeholder = {
                Text(
                    text = "Username"
                )
            })
        Spacer(modifier = modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.formState.password,
            onValueChange = { value -> loginViewModel.onEvent(LoginUiEvent.OnPasswordChange(value)) },
            placeholder = {
                Text(
                    text = "Password"
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = {
                    loginViewModel.onEvent(LoginUiEvent.OnTogglePasswordVisibility)
                }) {
                    AnimatedContent(
                        targetState = if (passwordIsVisible) R.drawable.ic_close else R.drawable.ic_eye,
                    ) { icon ->
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            },
            visualTransformation = if (passwordIsVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(6.dp),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = {
                if (uiState.formIsValid) {
                    val (username, password) = uiState.formState
                    val user = User(username, password)
                    loginViewModel.onEvent(LoginUiEvent.OnLogin(user)).also {
                        if (uiState.loginSuccess) {
                            onLoggedIn()
                        }
                    }
                } else {
                    val userMessage = UserMessage(id = 0, message = "Invalid details")
                    loginViewModel.onEvent(LoginUiEvent.OnShowUserMessage(userMessage))
                }
            })
        )
        Spacer(modifier = modifier.height(16.dp))

        Button(onClick = {
            if (uiState.formIsValid) {
                val (username, password) = uiState.formState
                val user = User(username, password)
                loginViewModel.onEvent(LoginUiEvent.OnLogin(user))

                if (uiState.loginSuccess) {
                    onLoggedIn()
                }
            } else {
                val userMessage = UserMessage(id = 0, message = "Invalid details")
                loginViewModel.onEvent(LoginUiEvent.OnShowUserMessage(userMessage))
            }
        }) {
            Text(
                text = "Login"
            )
        }
    }
}

@Composable
private fun ShowDialog(
    message: String, modifier: Modifier = Modifier
) {
    val properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    Dialog(properties = properties, onDismissRequest = {}) {
        Box(
            modifier = modifier
                .requiredSize(DpSize(200.dp, 200.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White), contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(text = message, color = Color.Black.copy(alpha = .8f))
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(snackbarHostState = SnackbarHostState()) {

    }
}