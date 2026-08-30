package com.example.ui.screens.main

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
fun ProfileScreen(
    viewModel: AlfaFitViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by viewModel.loggedInUser.collectAsState()
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AlfaBlack)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Perfil & Configurações",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = AlfaTextWhite
        )

        Spacer(modifier = Modifier.height(20.dp))

        // USER IDENTITY CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // User Avatar with Pitbull Badge
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(AlfaNeonLimeGlow, AlfaDarkNavy, AlfaBlack)
                            )
                        )
                        .border(2.dp, AlfaNeonLime, CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_alfa_pitbull_logo),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user?.name ?: "Nicolas Cauã",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = AlfaTextWhite
                )

                Text(
                    text = user?.email ?: "nicolas@alfafit.com",
                    fontSize = 13.sp,
                    color = AlfaTextGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Physical Metrics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileMetric(
                        label = "PESO",
                        value = "${String.format(java.util.Locale.US, "%.1f", user?.weightKg ?: 72.4f)} kg"
                    )
                    ProfileMetric(
                        label = "ALTURA",
                        value = "${user?.heightCm?.toInt() ?: 178} cm"
                    )
                    ProfileMetric(
                        label = "NASCIMENTO",
                        value = user?.birthDate ?: "15/05/1998"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Goal Badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AlfaSurfaceDark)
                        .border(1.dp, AlfaBorderSubtle, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrackChanges,
                            contentDescription = null,
                            tint = AlfaNeonLime,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Objetivo: ${user?.goal ?: "Ganhar massa muscular"}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AlfaTextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                AlfaSecondaryButton(
                    text = "Editar Perfil",
                    icon = Icons.Default.Edit,
                    onClick = { showEditProfileDialog = true },
                    height = 42.dp,
                    testTag = "edit_profile_button"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SETTINGS MENU
        Text(
            text = "Preferências",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = AlfaTextWhite
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorderSubtle)
        ) {
            Column {
                // Notifications switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = AlfaNeonLime,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Notificações de Treino",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AlfaTextWhite
                        )
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AlfaBlack,
                            checkedTrackColor = AlfaNeonLime,
                            uncheckedThumbColor = AlfaTextGray,
                            uncheckedTrackColor = AlfaSurfaceDark
                        )
                    )
                }

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AlfaBorderSubtle))

                // Terms of Use
                SettingsRowItem(
                    icon = Icons.Default.Description,
                    title = "Termos de Uso",
                    onClick = { showTermsDialog = true }
                )

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AlfaBorderSubtle))

                // Privacy
                SettingsRowItem(
                    icon = Icons.Default.PrivacyTip,
                    title = "Política de Privacidade",
                    onClick = { showPrivacyDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // LOGOUT BUTTON
        AlfaSecondaryButton(
            text = "Sair da conta",
            icon = Icons.AutoMirrored.Filled.Logout,
            onClick = {
                viewModel.logout(onLogout)
            },
            testTag = "logout_button"
        )

        Spacer(modifier = Modifier.height(30.dp))
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            user = user,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, w, h, goal ->
                viewModel.updateUserProfile(name, w, h, goal)
                showEditProfileDialog = false
            }
        )
    }

    if (showTermsDialog) {
        SimpleInfoDialog(
            title = "Termos de Uso — ALFA FIT",
            content = "O aplicativo ALFA FIT é destinado ao acompanhamento de treinos de musculação, corridas e evolução física. O usuário é responsável por realizar as atividades conforme suas capacidades físicas e consultar profissionais qualificados.",
            onDismiss = { showTermsDialog = false }
        )
    }

    if (showPrivacyDialog) {
        SimpleInfoDialog(
            title = "Política de Privacidade — ALFA FIT",
            content = "Seus dados de treinos, evolução física, métricas de corrida e análises nutricionais são armazenados localmente e com segurança em seu dispositivo.",
            onDismiss = { showPrivacyDialog = false }
        )
    }
}

@Composable
fun ProfileMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = AlfaTextMuted
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            color = AlfaTextWhite
        )
    }
}

@Composable
fun SettingsRowItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AlfaNeonLime,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AlfaTextWhite
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = AlfaTextGray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun EditProfileDialog(
    user: com.example.data.model.UserEntity?,
    onDismiss: () -> Unit,
    onSave: (String, Float, Float, String) -> Unit
) {
    var name by remember { mutableStateOf(user?.name ?: "Nicolas Cauã") }
    var weightStr by remember { mutableStateOf(String.format(java.util.Locale.US, "%.1f", user?.weightKg ?: 72.4f)) }
    var heightStr by remember { mutableStateOf(user?.heightCm?.toInt()?.toString() ?: "178") }
    var goal by remember { mutableStateOf(user?.goal ?: "Ganhar massa muscular") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AlfaSurfaceDark,
        titleContentColor = AlfaTextWhite,
        textContentColor = AlfaTextGray,
        title = {
            Text(text = "Editar Perfil", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AlfaTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nome",
                    placeholder = "Seu nome",
                    testTag = "edit_name_input"
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AlfaTextField(
                        value = weightStr,
                        onValueChange = { weightStr = it },
                        label = "Peso (kg)",
                        placeholder = "72.4",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        testTag = "edit_weight_input"
                    )
                    AlfaTextField(
                        value = heightStr,
                        onValueChange = { heightStr = it },
                        label = "Altura (cm)",
                        placeholder = "178",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        testTag = "edit_height_input"
                    )
                }
                AlfaTextField(
                    value = goal,
                    onValueChange = { goal = it },
                    label = "Objetivo",
                    placeholder = "Seu objetivo",
                    testTag = "edit_goal_input"
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val w = weightStr.toFloatOrNull() ?: 72.4f
                    val h = heightStr.toFloatOrNull() ?: 178f
                    onSave(name, w, h, goal)
                }
            ) {
                Text("Salvar", color = AlfaNeonLime, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = AlfaTextGray)
            }
        }
    )
}

@Composable
fun SimpleInfoDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AlfaSurfaceDark,
        titleContentColor = AlfaTextWhite,
        textContentColor = AlfaTextGray,
        title = {
            Text(text = title, fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text = content, lineHeight = 20.sp)
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar", color = AlfaNeonLime, fontWeight = FontWeight.Bold)
            }
        }
    )
}
