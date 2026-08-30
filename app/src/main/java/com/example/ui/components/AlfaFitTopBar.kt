package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaBorder
import com.example.ui.theme.AlfaBorderSubtle
import com.example.ui.theme.AlfaDarkNavy
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaNeonLimeGlow
import com.example.ui.theme.AlfaSurfaceCard
import com.example.ui.theme.AlfaSurfaceDark
import com.example.ui.theme.AlfaSurfaceElevated
import com.example.ui.theme.AlfaTextGray
import com.example.ui.theme.AlfaTextMuted
import com.example.ui.theme.AlfaTextWhite

data class AlfaNotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val icon: ImageVector,
    val isUnread: Boolean = true
)

@Composable
fun AlfaFitTopBar(
    userName: String,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    unreadNotificationsCount: Int = 3
) {
    var showNotificationsDialog by remember { mutableStateOf(false) }

    val notifications = remember {
        listOf(
            AlfaNotificationItem(
                id = "1",
                title = "Treino de Hoje Esperando",
                message = "Seu treino de Pernas está esperando por você! Supere seus limites hoje 💪",
                timeAgo = "Há 10 min",
                icon = Icons.Default.FitnessCenter
            ),
            AlfaNotificationItem(
                id = "2",
                title = "Hora de Correr?",
                message = "O clima está excelente para uma corrida ao ar livre. Meta sugerida: 5 km 🏃",
                timeAgo = "Há 2 horas",
                icon = Icons.Outlined.DirectionsRun
            ),
            AlfaNotificationItem(
                id = "3",
                title = "Evolução Contínua!",
                message = "Você está evoluindo! Sequência de 12 dias mantida. Continue assim 🔥",
                timeAgo = "Ontem",
                icon = Icons.Default.Whatshot
            )
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Olá, ${userName.ifBlank { "Atleta" }}! 👋",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = AlfaTextWhite
            )
            Text(
                text = "Pronto para evoluir hoje?",
                fontSize = 13.sp,
                color = AlfaNeonLime,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Notification Bell Icon with Badge
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(AlfaSurfaceCard)
                .border(1.dp, AlfaBorder, CircleShape)
                .clickable { showNotificationsDialog = true }
                .testTag("notifications_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notificações",
                tint = if (unreadNotificationsCount > 0) AlfaNeonLime else AlfaTextGray,
                modifier = Modifier.size(22.dp)
            )

            if (unreadNotificationsCount > 0) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                        .clip(CircleShape)
                        .background(AlfaNeonLime)
                )
            }
        }
    }

    if (showNotificationsDialog) {
        NotificationsDialog(
            notifications = notifications,
            onDismiss = { showNotificationsDialog = false }
        )
    }
}

@Composable
fun NotificationsDialog(
    notifications: List<AlfaNotificationItem>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AlfaSurfaceDark,
        titleContentColor = AlfaTextWhite,
        textContentColor = AlfaTextGray,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = AlfaNeonLime,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Notificações ALFA FIT",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlfaTextWhite
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = AlfaTextGray
                    )
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notifications) { notif ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AlfaSurfaceElevated),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(AlfaNeonLimeGlow),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = notif.icon,
                                    contentDescription = null,
                                    tint = AlfaNeonLime,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = notif.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AlfaTextWhite
                                    )
                                    Text(
                                        text = notif.timeAgo,
                                        fontSize = 10.sp,
                                        color = AlfaTextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = notif.message,
                                    fontSize = 12.sp,
                                    color = AlfaTextGray
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Entendido", color = AlfaNeonLime, fontWeight = FontWeight.Bold)
            }
        }
    )
}
