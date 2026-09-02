package com.example.ui

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.screens.AnalyzeScreen
import com.example.ui.screens.BarberGuideScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.ResultsScreen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanBright
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyleVisionApp(
    viewModel: StyleVisionViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.copiedPromptMessage) {
        state.copiedPromptMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearCopiedPromptNotice()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { err ->
            snackbarHostState.showSnackbar(
                message = err,
                duration = SnackbarDuration.Long
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkObsidian,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(GoldPrimary, GoldDark)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCut,
                                contentDescription = "StyleVision AI Logo",
                                tint = DarkObsidian,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "STYLEVISION",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.5.sp,
                                        color = TextPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CyanAccent)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "AI",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DarkObsidian,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                            Text(
                                text = "Virtual Master Barber & Facial Geometry",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondary,
                                    fontSize = 10.5.sp
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 8.dp
            ) {
                // Tab 1: Scan / Analyze
                NavigationBarItem(
                    selected = state.activeTab == MainTab.ANALYZE,
                    onClick = { viewModel.setTab(MainTab.ANALYZE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CenterFocusStrong,
                            contentDescription = "Scan & Analyze"
                        )
                    },
                    label = {
                        Text(
                            text = "Scan",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkObsidian,
                        selectedTextColor = GoldLight,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )

                // Tab 2: Recommendations & Prompts
                NavigationBarItem(
                    selected = state.activeTab == MainTab.RESULTS,
                    onClick = { viewModel.setTab(MainTab.RESULTS) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ContentCut,
                            contentDescription = "Haircut Recommendations"
                        )
                    },
                    label = {
                        Text(
                            text = "Styles & Prompts",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkObsidian,
                        selectedTextColor = GoldLight,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )

                // Tab 3: Saved History
                NavigationBarItem(
                    selected = state.activeTab == MainTab.HISTORY,
                    onClick = { viewModel.setTab(MainTab.HISTORY) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Saved Scans"
                        )
                    },
                    label = {
                        Text(
                            text = "Saved (${state.savedAnalyses.size})",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkObsidian,
                        selectedTextColor = GoldLight,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )

                // Tab 4: Barber Guide
                NavigationBarItem(
                    selected = state.activeTab == MainTab.BARBER_GUIDE,
                    onClick = { viewModel.setTab(MainTab.BARBER_GUIDE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Barber Guide"
                        )
                    },
                    label = {
                        Text(
                            text = "Barber Guide",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkObsidian,
                        selectedTextColor = GoldLight,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.activeTab) {
                MainTab.ANALYZE -> {
                    AnalyzeScreen(
                        state = state,
                        onImageSelected = { uri -> viewModel.onImageSelected(uri, viewModel.getApplication()) },
                        onSampleSelected = { resId, label -> viewModel.loadSampleModel(resId, label) },
                        onGenderChange = { viewModel.setGenderPreference(it) },
                        onHairTextureChange = { viewModel.setHairTexture(it) },
                        onNotesChange = { viewModel.setUserNotes(it) },
                        onToggleOverlay = { viewModel.toggleGeometryOverlay() },
                        onOverlayTypeChange = { viewModel.setGeometryOverlayType(it) },
                        onAnalyze = { viewModel.analyzeCurrentPhoto() }
                    )
                }

                MainTab.RESULTS -> {
                    ResultsScreen(
                        state = state,
                        onSaveAnalysis = { viewModel.saveCurrentAnalysis() },
                        onCopyPrompt = { viewModel.setCopiedPromptNotice(it) },
                        onRescan = { viewModel.setTab(MainTab.ANALYZE) }
                    )
                }

                MainTab.HISTORY -> {
                    HistoryScreen(
                        savedAnalyses = state.savedAnalyses,
                        onSelectAnalysis = { viewModel.loadSavedAnalysis(it) },
                        onDeleteAnalysis = { viewModel.deleteSavedAnalysis(it) }
                    )
                }

                MainTab.BARBER_GUIDE -> {
                    BarberGuideScreen()
                }
            }
        }
    }
}
