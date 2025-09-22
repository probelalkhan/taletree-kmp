package dev.belalkhan.taletree.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.belalkhan.taletree.utils.consume
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import taletree.composeapp.generated.resources.Res
import taletree.composeapp.generated.resources.ic_eye_closed
import taletree.composeapp.generated.resources.ic_eye_open
import taletree.composeapp.generated.resources.ic_google
import taletree.composeapp.generated.resources.logo_tale_tree


@Composable
fun AuthView(
    viewModel: AuthViewModel = koinViewModel<AuthViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Auth(
        state = state,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onLogin = viewModel::onLogin,
        onForgotPassword = {},
        onSignUp = {}
    )
}

@Composable
private fun Auth(
    state: AuthState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignUp: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Logo
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(Res.drawable.logo_tale_tree),
                contentDescription = null,
            )

            Text(
                text = "Welcome Back!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = "Login/Signup to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            // Email
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = onEmailChanged,
                    label = { Text("Email") },
                    placeholder = { Text("Enter your email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = state.emailError != null,
                )
                AnimatedVisibility(visible = state.emailError != null) {
                    Text(
                        text = state.emailError.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            // Password
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.password,
                    onValueChange = onPasswordChanged,
                    label = { Text("Password") },
                    placeholder = { Text("Enter your password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation =
                        if (state.isPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(
                                painter = if (state.isPasswordVisible) painterResource(Res.drawable.ic_eye_open) else painterResource(
                                    Res.drawable.ic_eye_closed
                                ),
                                contentDescription = "Toggle Password Visibility",
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = state.passwordError != null,
                )
                AnimatedVisibility(visible = state.passwordError != null) {
                    Text(
                        text = state.passwordError.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            // Error message
            state.loginError?.consume {
                AnimatedVisibility(visible = true) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            // Login button
            Button(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text("Login")
                }
            }

            // Google login
            OutlinedButton(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_google),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Login with Google")
            }

            // Bottom links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(onClick = onForgotPassword) {
                    Text("Forgot Password?", fontSize = 12.sp)
                }
                TextButton(onClick = onSignUp) {
                    Text("Sign Up", fontSize = 12.sp)
                }
            }
        }
    }
}
