package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AlfaFitLogo
import com.example.ui.components.AlfaPrimaryButton
import com.example.ui.components.AlfaSecondaryButton
import com.example.ui.components.AlfaTextField
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaBorder
import com.example.ui.theme.AlfaDarkNavy
import com.example.ui.theme.AlfaError
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaNeonLimeGlow
import com.example.ui.theme.AlfaSurfaceCard
import com.example.ui.theme.AlfaTextGray
import com.example.ui.theme.AlfaTextMuted
import com.example.ui.theme.AlfaTextWhite
import com.example.ui.viewmodel.AlfaFitViewModel

@Composable
fun LoginScreen(
    viewModel: AlfaFitViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("atleta@alfafit.com") }
    var password by remember { mutableStateOf("123456") }

    val isLoading by viewModel.authLoading.collectAsState()
    val authError by viewModel.authError.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AlfaBlack,
                        AlfaDarkNavy,
                        AlfaBlack
                    )
                )
            )
    ) {
        // Subtle decorative neon glow at the top center
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp)
                .size(240.dp)
                .clip(RoundedCornerShape(120.dp))
                .background(AlfaNeonLimeGlow.copy(alpha = 0.12f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // ALFA FIT Logo with Pitbull Mascot
            AlfaFitLogo(
                size = 110.dp,
                textSize = 34.sp,
                showSlogan = true
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Welcome Back Greeting
            Text(
                text = "Bem-vindo de volta!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = AlfaTextWhite
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Entre na sua conta para continuar evoluindo.",
                fontSize = 14.sp,
                color = AlfaTextGray
            )

            Spacer(modifier = Modifier.height(26.dp))

            // E-mail Field
            AlfaTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.clearAuthError()
                },
                label = "E-mail",
                placeholder = "Digite seu e-mail",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                testTag = "login_email_input"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Senha Field with Eye Toggle
            AlfaTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.clearAuthError()
                },
                label = "Senha",
                placeholder = "Digite sua senha",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                testTag = "login_password_input"
            )

            // Esqueceu a Senha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Esqueceu sua senha?",
                    color = AlfaNeonLime,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { onNavigateToForgotPassword() }
                        .testTag("forgot_password_link")
                )
            }

            // Error Display if any
            if (authError != null) {
                Text(
                    text = authError!!,
                    color = AlfaError,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Big ENTRAR Button
            AlfaPrimaryButton(
                text = "ENTRAR",
                onClick = {
                    viewModel.login(email, password, onLoginSuccess)
                },
                isLoading = isLoading,
                testTag = "login_submit_button"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Separator: ──────── OU ────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = AlfaBorder)
                Text(
                    text = " OU ",
                    color = AlfaTextMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = AlfaBorder)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Google Login Button
            AlfaSecondaryButton(
                text = "Entrar com Google",
                onClick = {
                    viewModel.loginWithGoogle(onLoginSuccess)
                },
                testTag = "google_login_button"
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Create Account Link at bottom
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 30.dp)
            ) {
                Text(
                    text = "Ainda não tem uma conta? ",
                    color = AlfaTextGray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Criar conta",
                    color = AlfaNeonLime,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onNavigateToRegister() }
                        .testTag("register_link")
                )
            }
        }
    }
}
