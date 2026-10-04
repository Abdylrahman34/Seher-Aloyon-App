package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocalMall
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MaisonLogo
import com.example.ui.theme.EmeraldLuxe
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.OnyxBorder
import com.example.ui.theme.OnyxCard
import com.example.ui.theme.OnyxSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.viewmodel.MaisonViewModel
import kotlinx.coroutines.delay

@Composable
fun MainAppScreen(
    viewModel: MaisonViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val cartItems by viewModel.cartItems.collectAsState()
    val totalCartCount = cartItems.sumOf { it.quantity }
    val userFeedback by viewModel.userFeedback.collectAsState()

    // Handle back button to return to Home screen
    BackHandler(enabled = selectedTab != 0) {
        selectedTab = 0
    }

    LaunchedEffect(userFeedback) {
        if (userFeedback != null) {
            delay(3500)
            viewModel.dismissFeedback()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        Scaffold(
            topBar = {
                // Luxury Top App Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(OnyxSurface)
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MaisonLogo(size = 36)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "MAISON ASTER",
                            fontFamily = FontFamily.Serif,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "SEHER ALOYON ATELIER",
                            fontSize = 8.sp,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                    }

                    // Bag Shortcut Button
                    IconButton(
                        onClick = { selectedTab = 3 },
                        modifier = Modifier.testTag("top_bar_bag_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (totalCartCount > 0) {
                                    Badge(
                                        containerColor = GoldPrimary,
                                        contentColor = ObsidianBlack
                                    ) {
                                        Text("$totalCartCount", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (selectedTab == 3) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                                contentDescription = "Luxury Bag",
                                tint = if (selectedTab == 3) GoldPrimary else TextPrimaryDark
                            )
                        }
                    }
                }
            },
            bottomBar = {
                // Bottom Navigation
                NavigationBar(
                    containerColor = OnyxSurface,
                    contentColor = TextPrimaryDark,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .border(1.dp, OnyxBorder)
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 0) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                                contentDescription = "Showcase"
                            )
                        },
                        label = { Text("Atelier", fontSize = 10.sp, letterSpacing = 0.5.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_showcase")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 1) Icons.Filled.LocalMall else Icons.Outlined.LocalMall,
                                contentDescription = "Catalog"
                            )
                        },
                        label = { Text("Collection", fontSize = 10.sp, letterSpacing = 0.5.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_collection")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 2) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                                contentDescription = "Appointments"
                            )
                        },
                        label = { Text("Salon", fontSize = 10.sp, letterSpacing = 0.5.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_salon")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (totalCartCount > 0) {
                                        Badge(
                                            containerColor = GoldPrimary,
                                            contentColor = ObsidianBlack
                                        ) {
                                            Text("$totalCartCount", fontWeight = FontWeight.Bold, fontSize = 9.sp)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (selectedTab == 3) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                                    contentDescription = "Bag"
                                )
                            }
                        },
                        label = { Text("Bag", fontSize = 10.sp, letterSpacing = 0.5.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_bag")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 4) Icons.Filled.Person else Icons.Outlined.Person,
                                contentDescription = "Profile"
                            )
                        },
                        label = { Text("Privilège", fontSize = 10.sp, letterSpacing = 0.5.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ObsidianBlack,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_profile")
                    )
                }
            },
            containerColor = ObsidianBlack
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTab) {
                    0 -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToCatalog = { selectedTab = 1 },
                        onNavigateToAppointments = { selectedTab = 2 }
                    )
                    1 -> CatalogScreen(viewModel = viewModel)
                    2 -> AppointmentsScreen(viewModel = viewModel)
                    3 -> BagScreen(
                        viewModel = viewModel,
                        onNavigateToCatalog = { selectedTab = 1 }
                    )
                    4 -> ProfileScreen(
                        viewModel = viewModel,
                        onLogout = onLogout
                    )
                }
            }
        }

        // Floating Luxury Notification Pill
        AnimatedVisibility(
            visible = userFeedback != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 64.dp, start = 20.dp, end = 20.dp)
        ) {
            userFeedback?.let { feedback ->
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(OnyxCard)
                        .border(1.dp, GoldPrimary, RoundedCornerShape(24.dp))
                        .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = GoldPrimary)
                        .clickable { viewModel.dismissFeedback() }
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feedback,
                        color = TextPrimaryDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
