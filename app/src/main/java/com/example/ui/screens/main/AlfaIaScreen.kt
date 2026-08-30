package com.example.ui.screens.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ai.MealNutrientResult
import com.example.ui.components.AlfaPrimaryButton
import com.example.ui.components.AlfaSecondaryButton
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaBorder
import com.example.ui.theme.AlfaBorderSubtle
import com.example.ui.theme.AlfaDarkNavy
import com.example.ui.theme.AlfaNeonGreen
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaNeonLimeGlow
import com.example.ui.theme.AlfaSurfaceCard
import com.example.ui.theme.AlfaSurfaceDark
import com.example.ui.theme.AlfaSurfaceElevated
import com.example.ui.theme.AlfaTextGray
import com.example.ui.theme.AlfaTextMuted
import com.example.ui.theme.AlfaTextWhite
import com.example.ui.viewmodel.AlfaFitViewModel

data class SampleMeal(val title: String, val hint: String, val imageRes: Int)

@Composable
fun AlfaIaScreen(
    viewModel: AlfaFitViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAnalyzing by viewModel.mealAnalyzing.collectAsState()
    val lastResult by viewModel.lastMealAnalysisResult.collectAsState()
    val mealLogs by viewModel.allMealAnalyses.collectAsState()

    var selectedSampleHint by remember { mutableStateOf("Frango com arroz e legumes") }

    val sampleMeals = listOf(
        SampleMeal("Frango & Arroz", "Frango grelhado com arroz branco e brócolis", R.drawable.img_meal_hero),
        SampleMeal("Salmão & Batata Doce", "Salmão grelhado com batata doce e aspargos", R.drawable.img_meal_hero),
        SampleMeal("Omelete Fit", "Omelete de claras com queijo branco e pão integral", R.drawable.img_meal_hero),
        SampleMeal("Shake Proteico", "Shake de whey protein com banana e aveia", R.drawable.img_meal_hero)
    )

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            viewModel.analyzeMealPhoto(bitmap, "Foto da câmera de refeição fitness")
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                viewModel.analyzeMealPhoto(bitmap, "Foto da galeria de refeição fitness")
            } catch (_: Exception) {
                viewModel.analyzeMealPhoto(null, "Refeição saudável")
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AlfaBlack)
            .verticalScroll(rememberScrollState())
    ) {
        // TOP BANNER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_meal_hero),
                contentDescription = "Alfa IA",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AlfaBlack.copy(alpha = 0.4f),
                                AlfaBlack.copy(alpha = 0.85f),
                                AlfaBlack
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AlfaNeonLime)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "INTELIGÊNCIA ARTIFICIAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = AlfaBlack
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Alfa IA — Análise de Refeição",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = AlfaTextWhite
                )
                Text(
                    text = "Tire uma foto do seu prato e obtenha estimativas de calorias e macros",
                    fontSize = 13.sp,
                    color = AlfaTextGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // UPLOAD & PHOTO BUTTONS
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AlfaSecondaryButton(
                    text = "Câmera",
                    icon = Icons.Default.CameraAlt,
                    onClick = { photoLauncher.launch(null) },
                    modifier = Modifier.weight(1f),
                    testTag = "camera_button"
                )

                AlfaSecondaryButton(
                    text = "Galeria",
                    icon = Icons.Default.PhotoLibrary,
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f),
                    testTag = "gallery_button"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PRESET SAMPLES PICKER
            Text(
                text = "Ou selecione uma refeição para análise rápida:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = AlfaTextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(sampleMeals) { meal ->
                    val isSelected = selectedSampleHint == meal.hint
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) AlfaNeonLime else AlfaSurfaceCard)
                            .border(
                                1.dp,
                                if (isSelected) AlfaNeonLime else AlfaBorderSubtle,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                selectedSampleHint = meal.hint
                                viewModel.analyzeMealPhoto(null, meal.hint)
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = meal.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                            color = if (isSelected) AlfaBlack else AlfaTextWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ANALYZE BUTTON
            AlfaPrimaryButton(
                text = "ANALISAR REFEIÇÃO COM ALFA IA",
                icon = Icons.Default.AutoAwesome,
                onClick = {
                    viewModel.analyzeMealPhoto(null, selectedSampleHint)
                },
                isLoading = isAnalyzing,
                testTag = "analyze_meal_button"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // RESULT CARD
            if (lastResult != null) {
                MealAnalysisResultCard(result = lastResult!!)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // HISTORICAL LOGS
            if (mealLogs.isNotEmpty()) {
                Text(
                    text = "Histórico de Análises",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlfaTextWhite
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    mealLogs.take(5).forEach { log ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = log.foodItemsSummary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AlfaTextWhite
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${log.proteinGrams}g P • ${log.carbsGrams}g C • ${log.fatsGrams}g G",
                                        fontSize = 12.sp,
                                        color = AlfaNeonLime
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(AlfaSurfaceDark)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${log.caloriesKcal} kcal",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AlfaTextWhite
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun MealAnalysisResultCard(result: MealNutrientResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(2.dp, AlfaNeonLime)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AlfaNeonLime,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Análise Concluída",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = AlfaNeonLime
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AlfaNeonLime)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${result.caloriesKcal} KCAL",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = AlfaBlack
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = result.foodSummary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = AlfaTextWhite
            )

            Spacer(modifier = Modifier.height(16.dp))

            // MACROS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MacroPill(label = "Proteínas", value = "${result.proteinGrams}g", color = AlfaNeonLime)
                MacroPill(label = "Carboidratos", value = "${result.carbsGrams}g", color = Color(0xFF38BDF8))
                MacroPill(label = "Gorduras", value = "${result.fatsGrams}g", color = Color(0xFFFFB300))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DISCLAIMER (MANDATORY)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(AlfaSurfaceDark)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = AlfaTextMuted,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = result.disclaimer,
                    fontSize = 11.sp,
                    color = AlfaTextGray
                )
            }
        }
    }
}

@Composable
fun MacroPill(label: String, value: String, color: Color) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AlfaSurfaceDark)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = AlfaTextMuted
        )
    }
}
