package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FacialAnalysisData
import com.example.ui.StyleVisionUiState
import com.example.ui.components.AnalysisDashboard
import com.example.ui.components.HaircutRecommendationCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanBright
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ResultsScreen(
    state: StyleVisionUiState,
    onSaveAnalysis: () -> Unit,
    onCopyPrompt: (String) -> Unit,
    onRescan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val data = state.analysisResult

    if (data == null) {
        // Empty state
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCut,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = "No Analysis Yet",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Upload a frontal selfie and tap 'Analyze Facial Geometry' to view facial dimensions and tailored haircut recommendations.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        lineHeight = 22.sp
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onRescan,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = DarkObsidian
                    )
                ) {
                    Text("Go to Scan Screen", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Top Action Bar (Save Scan + Share Consultation + Rescan)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Save button
            OutlinedButton(
                onClick = onSaveAnalysis,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    1.dp,
                    if (state.isCurrentAnalysisSaved) SuccessGreen else GoldPrimary
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (state.isCurrentAnalysisSaved) SuccessGreen else GoldLight
                ),
                modifier = Modifier.testTag("save_analysis_button")
            ) {
                Icon(
                    imageVector = if (state.isCurrentAnalysisSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (state.isCurrentAnalysisSaved) "Saved in History" else "Save Analysis",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Share button
                IconButton(
                    onClick = {
                        val shareText = buildString {
                            append("StyleVision AI - Facial Analysis & Barber Specs\n\n")
                            append("Face Shape: ${data.faceShape}\n")
                            append("Hairline Pattern: ${data.hairlinePattern}\n")
                            append("Facial Dimensions: ${data.proportions.facialRatio}, Jawline: ${data.proportions.jawline}\n\n")
                            append("Tailored Haircut Recommendations:\n")
                            data.recommendations.forEachIndexed { i, rec ->
                                append("${i + 1}. ${rec.styleName} (${rec.category})\n")
                                append("   Why: ${rec.whyItWorks}\n")
                                append("   Barber Specs: ${rec.barberInstructions}\n\n")
                            }
                        }
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "StyleVision AI Haircut Consultation")
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Haircut Plan"))
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF1E212E))
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Consultation",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Rescan button
                IconButton(
                    onClick = onRescan,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF1E212E))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "New Scan",
                        tint = GoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // 1. Full Facial Structure Analysis Section
        AnalysisDashboard(data = data)

        // 2. HAIRCUT RECOMMENDATIONS SECTION (3 Distinct Styles)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCut,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "3 TAILORED HAIRCUT STYLES",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = GoldLight
                    )
                )
            }

            Text(
                text = "Each style is scientifically engineered to balance your ${data.faceShape} face shape and ${data.hairlinePattern} hairline.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )

            // Render all 3 haircut recommendations
            data.recommendations.forEachIndexed { index, recommendation ->
                HaircutRecommendationCard(
                    recommendation = recommendation,
                    index = index,
                    isSelected = index == 0,
                    onCopyPrompt = onCopyPrompt
                )
            }
        }

        // 3. Master Barber Consultation Summary Note
        if (data.barberConsultationNotes.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1726)),
                border = BorderStroke(1.dp, GoldDark.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "Master Barber Pro Tip",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = data.barberConsultationNotes,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
