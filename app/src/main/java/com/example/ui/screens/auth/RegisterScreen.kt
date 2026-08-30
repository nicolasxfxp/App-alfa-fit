package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AlfaPrimaryButton
import com.example.ui.components.AlfaSecondaryButton
import com.example.ui.components.AlfaTextField
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaBorder
import com.example.ui.theme.AlfaBorderSubtle
import com.example.ui.theme.AlfaDarkNavy
import com.example.ui.theme.AlfaError
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaNeonLimeGlow
import com.example.ui.theme.AlfaSurfaceCard
import com.example.ui.theme.AlfaSurfaceDark
import com.example.ui.theme.AlfaTextGray
import com.example.ui.theme.AlfaTextMuted
import com.example.ui.theme.AlfaTextWhite
import com.example.ui.viewmodel.AlfaFitViewModel

@Composable
fun RegisterScreen(
    viewModel: AlfaFitViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableStateOf(1) } // Step 1: Info, Step 2: Body & Goals

    // Form fields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("15/05/1998") }
    var heightCmStr by remember { mutableStateOf("178") }
    var weightKgStr by remember { mutableStateOf("72.4") }
    var selectedGoal by remember { mutableStateOf("Ganhar massa muscular") }

    val goals = listOf(
        "Ganhar massa muscular",
        "Perder peso",
        "Definir o corpo",
        "Melhorar condicionamento",
        "Manter o peso",
        "Melhorar performance"
    )

    val isLoading by viewModel.authLoading.collectAsState()
    val authError by viewModel.authError.collectAsState()

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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Back button
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (step > 1) step = 1 else onNavigateToLogin()
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = AlfaTextWhite
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Criar conta",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = AlfaTextWhite
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Vamos começar! Personalize seu treino e evolução.",
                fontSize = 14.sp,
                color = AlfaTextGray,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Step Progress Indicator
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (step == 1) "Etapa 1 de 2: Dados de Acesso" else "Etapa 2 de 2: Perfil Físico",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlfaNeonLime
                    )
                    Text(
                        text = if (step == 1) "50%" else "100%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlfaTextGray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { if (step == 1) 0.5f else 1.0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = AlfaNeonLime,
                    trackColor = AlfaSurfaceCard
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (step == 1) {
                // STEP 1: Basic Credentials
                AlfaTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nome Completo",
                    placeholder = "Digite seu nome completo",
                    leadingIcon = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    testTag = "register_name_input"
                )

                Spacer(modifier = Modifier.height(16.dp))

                AlfaTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "E-mail",
                    placeholder = "Digite seu melhor e-mail",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    testTag = "register_email_input"
                )

                Spacer(modifier = Modifier.height(16.dp))

                AlfaTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Senha",
                    placeholder = "Crie uma senha forte",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    testTag = "register_password_input"
                )

                Spacer(modifier = Modifier.height(16.dp))

                AlfaTextField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    label = "Data de Nascimento",
                    placeholder = "DD/MM/AAAA",
                    leadingIcon = Icons.Default.Cake,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    testTag = "register_birthdate_input"
                )

                Spacer(modifier = Modifier.height(30.dp))

                AlfaPrimaryButton(
                    text = "CONTINUAR",
                    onClick = {
                        if (name.isBlank() || email.isBlank() || password.isBlank()) {
                            // Validation alert
                        } else {
                            step = 2
                        }
                    },
                    testTag = "register_next_button"
                )
            } else {
                // STEP 2: Physical measurements & Goals
                Row(modifier = Modifier.fillMaxWidth()) {
                    AlfaTextField(
                        value = heightCmStr,
                        onValueChange = { heightCmStr = it },
                        label = "Altura (cm)",
                        placeholder = "Ex: 178",
                        leadingIcon = Icons.Default.Height,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        modifier = Modifier.weight(1f),
                        testTag = "register_height_input"
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    AlfaTextField(
                        value = weightKgStr,
                        onValueChange = { weightKgStr = it },
                        label = "Peso Atual (kg)",
                        placeholder = "Ex: 72.4",
                        leadingIcon = Icons.Default.MonitorWeight,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                        modifier = Modifier.weight(1f),
                        testTag = "register_weight_input"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Qual é o seu objetivo principal?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlfaTextWhite
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Goals options chips
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    goals.forEach { goal ->
                        val isSelected = selectedGoal == goal
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) AlfaNeonLimeGlow else AlfaSurfaceDark)
                                .border(
                                    1.dp,
                                    if (isSelected) AlfaNeonLime else AlfaBorderSubtle,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedGoal = goal }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = goal,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) AlfaNeonLime else AlfaTextWhite
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(AlfaNeonLime)
                                    )
                                }
                            }
                        }
                    }
                }

                if (authError != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = authError!!,
                        color = AlfaError,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                AlfaPrimaryButton(
                    text = "CRIAR CONTA",
                    onClick = {
                        val h = heightCmStr.toFloatOrNull() ?: 178f
                        val w = weightKgStr.toFloatOrNull() ?: 72.4f
                        viewModel.register(
                            name = name,
                            email = email,
                            passwordRaw = password,
                            birthDate = birthDate,
                            heightCm = h,
                            weightKg = w,
                            goal = selectedGoal,
                            onSuccess = onRegisterSuccess
                        )
                    },
                    isLoading = isLoading,
                    testTag = "register_submit_button"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom Already has account link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
            ) {
                Text(
                    text = "Já tem uma conta? ",
                    color = AlfaTextGray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Entrar",
                    color = AlfaNeonLime,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onNavigateToLogin() }
                        .testTag("already_have_account_link")
                )
            }
        }
    }
}
