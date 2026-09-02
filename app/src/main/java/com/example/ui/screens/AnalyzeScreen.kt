package com.example.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.GeometryOverlayType
import com.example.ui.StyleVisionUiState
import com.example.ui.components.FacialGeometryOverlay
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanBright
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnalyzeScreen(
    state: StyleVisionUiState,
    onImageSelected: (Uri) -> Unit,
    onSampleSelected: (Int, String) -> Unit,
    onGenderChange: (String) -> Unit,
    onHairTextureChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onToggleOverlay: () -> Unit,
    onOverlayTypeChange: (GeometryOverlayType) -> Unit,
    onAnalyze: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Photo Picker launcher compliant with Play Policy
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onImageSelected(uri)
            }
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Selfie Visualizer Card with Interactive Geometric Overlay
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            border = BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header inside card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CenterFocusStrong,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Frontal Selfie & Facial Grid",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    // Overlay toggle button
                    OutlinedButton(
                        onClick = onToggleOverlay,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, if (state.showGeometryOverlay) CyanAccent else DarkCardBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (state.showGeometryOverlay) CyanBright else TextMuted
                        ),
                        contentPadding = ButtonDefaults.TextButtonContentPadding,
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(
                            imageVector = if (state.showGeometryOverlay) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Grid",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (state.showGeometryOverlay) "Grid On" else "Grid Off",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp)
                        )
                    }
                }

                // Image Container with Canvas Overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF0F1017))
                        .border(1.dp, Color(0xFF262938), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.selectedBitmap != null) {
                        Image(
                            bitmap = state.selectedBitmap.asImageBitmap(),
                            contentDescription = "Uploaded Frontal Selfie",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Geometric Overlay Layer
                        if (state.showGeometryOverlay) {
                            FacialGeometryOverlay(
                                isScanning = state.isAnalyzing,
                                overlayType = state.geometryOverlayType,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        // Empty placeholder
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "Select or upload a frontal selfie",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                            )
                        }
                    }

                    // Alignment helper badge at bottom of picture
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xCC0D0E12))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (state.isAnalyzing) "Scanning Facial Geometry & Symmetry..." else "Frontal Portrait • Neutral Expression",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (state.isAnalyzing) CyanBright else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                // Grid Layer Selector (when overlay active)
                if (state.showGeometryOverlay) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GeometryOverlayType.values().forEach { type ->
                            val isSelected = state.geometryOverlayType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) Color(0xFF1E2D38) else Color(0xFF13151D))
                                    .border(
                                        1.dp,
                                        if (isSelected) CyanAccent else Color(0xFF232635),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable { onOverlayTypeChange(type) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (type) {
                                        GeometryOverlayType.ALL -> "Full Grid"
                                        GeometryOverlayType.THIRDS -> "Thirds"
                                        GeometryOverlayType.JAWLINE_AXIS -> "Jawline"
                                        GeometryOverlayType.HAIRLINE_GUIDE -> "Hairline"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) CyanBright else TextMuted,
                                        fontSize = 10.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }

                // Upload Button & Quick Sample Presets Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("upload_selfie_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF222636),
                            contentColor = GoldLight
                        ),
                        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Choose Photo",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Sample 1 button
                    OutlinedButton(
                        onClick = { onSampleSelected(R.drawable.sample_model_1, "Model 1") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (state.selectedSampleResId == R.drawable.sample_model_1) GoldPrimary else DarkCardBorder
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (state.selectedSampleResId == R.drawable.sample_model_1) Color(0xFF252115) else Color.Transparent,
                            contentColor = TextPrimary
                        )
                    ) {
                        Text(
                            text = "Model 1",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp)
                        )
                    }

                    // Sample 2 button
                    OutlinedButton(
                        onClick = { onSampleSelected(R.drawable.sample_model_2, "Model 2") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (state.selectedSampleResId == R.drawable.sample_model_2) GoldPrimary else DarkCardBorder
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (state.selectedSampleResId == R.drawable.sample_model_2) Color(0xFF252115) else Color.Transparent,
                            contentColor = TextPrimary
                        )
                    ) {
                        Text(
                            text = "Model 2",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp)
                        )
                    }
                }
            }
        }

        // 2. Styling Preferences & Hair Texture Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            border = BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Styling Context & Preferences",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }

                // Styling Target Category
                Text(
                    text = "Style Category Target",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
                val genderOptions = listOf(
                    "Masculine / Barber",
                    "Feminine / Salon",
                    "Universal / Modern Flow"
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    genderOptions.forEach { opt ->
                        val isSelected = state.genderPreference == opt
                        FilterChip(
                            selected = isSelected,
                            onClick = { onGenderChange(opt) },
                            label = { Text(opt, fontSize = 11.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldDark.copy(alpha = 0.35f),
                                selectedLabelColor = GoldLight,
                                containerColor = Color(0xFF161822),
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = DarkCardBorder,
                                selectedBorderColor = GoldPrimary
                            )
                        )
                    }
                }

                // Hair Texture
                Text(
                    text = "Hair Texture & Density",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
                val textureOptions = listOf(
                    "Straight",
                    "Wavy",
                    "Curly",
                    "Coily / Afro",
                    "Fine / Thinning"
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    textureOptions.forEach { tex ->
                        val isSelected = state.hairTexture == tex
                        FilterChip(
                            selected = isSelected,
                            onClick = { onHairTextureChange(tex) },
                            label = { Text(tex, fontSize = 11.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanAccent.copy(alpha = 0.25f),
                                selectedLabelColor = CyanBright,
                                containerColor = Color(0xFF161822),
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = DarkCardBorder,
                                selectedBorderColor = CyanAccent
                            )
                        )
                    }
                }

                // Optional Barber Notes
                OutlinedTextField(
                    value = state.userNotes,
                    onValueChange = onNotesChange,
                    label = { Text("Specific Goals / Barber Notes (Optional)") },
                    placeholder = { Text("e.g., Hide high forehead, keep beard balance, low maintenance...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = GoldLight,
                        unfocusedLabelColor = TextMuted
                    ),
                    maxLines = 2
                )
            }
        }

        // 3. High-Impact "ANALYZE FACIAL GEOMETRY" Action Button
        Button(
            onClick = onAnalyze,
            enabled = !state.isAnalyzing && state.selectedBitmap != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("analyze_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = DarkObsidian,
                disabledContainerColor = Color(0xFF2B2D3A),
                disabledContentColor = TextMuted
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            if (state.isAnalyzing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = DarkObsidian,
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Analyzing Facial Geometry...",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ANALYZE FACIAL GEOMETRY",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
