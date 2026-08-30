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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AlfaPrimaryButton
import com.example.ui.components.AlfaTextField
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaDarkNavy
import com.example.ui.theme.AlfaNeonGreen
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaSurfaceCard
import com.example.ui.theme.AlfaTextGray
import com.example.ui.theme.AlfaTextWhite

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(AlfaBlack, AlfaDarkNavy, AlfaBlack)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = AlfaTextWhite
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Recuperar senha",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = AlfaTextWhite
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Digite seu e-mail para receber as instruções de recuperação de senha.",
                fontSize = 14.sp,
                color = AlfaTextGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isSubmitted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AlfaNeonGreen,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Instruções Enviadas!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AlfaTextWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Enviamos um link de redefinição para $email. Verifique sua caixa de entrada.",
                            fontSize = 13.sp,
                            color = AlfaTextGray,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                AlfaPrimaryButton(
                    text = "VOLTAR AO LOGIN",
                    onClick = onNavigateBack,
                    testTag = "back_to_login_button"
                )
            } else {
                AlfaTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "E-mail",
                    placeholder = "Digite seu e-mail",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    testTag = "forgot_email_input"
                )

                Spacer(modifier = Modifier.height(24.dp))

                AlfaPrimaryButton(
                    text = "ENVIAR",
                    onClick = {
                        if (email.isNotBlank()) {
                            isSubmitted = true
                        }
                    },
                    testTag = "send_recovery_button"
                )
            }
        }
    }
}
