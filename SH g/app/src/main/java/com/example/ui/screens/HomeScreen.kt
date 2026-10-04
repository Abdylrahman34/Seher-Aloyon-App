package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.ProductItem
import com.example.ui.components.MaisonCategoryChip
import com.example.ui.components.MaisonProductCard
import com.example.ui.theme.EmeraldLuxe
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.OnyxBorder
import com.example.ui.theme.OnyxCard
import com.example.ui.theme.OnyxSurface
import com.example.ui.theme.RoseGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.viewmodel.MaisonViewModel

@Composable
fun HomeScreen(
    viewModel: MaisonViewModel,
    onNavigateToCatalog: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val products = SampleData.Products

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // Top Member Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "BIENVENUE",
                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        fontSize = 10.sp
                    ),
                    color = GoldLight
                )
                Text(
                    text = userProfile.displayName.ifEmpty { userProfile.username },
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextPrimaryDark
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(GoldPrimary.copy(alpha = 0.15f))
                    .border(0.5.dp, GoldPrimary, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userProfile.memberTier.take(15),
                        color = GoldLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Hero Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = OnyxCard),
            border = BorderStroke(1.dp, OnyxBorder),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable { onNavigateToCatalog() }
                .testTag("hero_banner_card")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF281E15), Color(0xFF16120F), ObsidianBlack)
                        )
                    )
                    .padding(22.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "MAISON ASTER & SEHER ALOYON",
                            color = GoldLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Haute Parfumerie &\nEye Couture Elegance",
                        style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,
                            lineHeight = 30.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = TextPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Immerse yourself in rare botanicals, royal Cambodian agarwood, and mesmerizing eye lashes.",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryDark,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(GoldPrimary)
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "EXPLORE COLLECTION",
                            color = ObsidianBlack,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = ObsidianBlack,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SampleData.Categories.forEach { category ->
                MaisonCategoryChip(
                    text = category,
                    isSelected = selectedCategory == category,
                    onClick = {
                        viewModel.selectCategory(category)
                        onNavigateToCatalog()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Featured Masterpieces Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Iconic Masterpieces",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp
                ),
                color = TextPrimaryDark
            )

            Text(
                text = "View All",
                color = GoldPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier
                    .clickable { onNavigateToCatalog() }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Horizontal Carousel of Masterpieces
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(products.take(4)) { product ->
                MaisonProductCard(
                    product = product,
                    isWishlisted = wishlistIds.contains(product.id),
                    onProductClick = { viewModel.selectProduct(product) },
                    onAddToCart = { viewModel.addToCart(product) },
                    onToggleWishlist = { viewModel.toggleWishlist(product.id) },
                    modifier = Modifier.width(220.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // VIP Salon & Lounge Reservation Teaser
        Card(
            colors = CardDefaults.cardColors(containerColor = OnyxCard),
            border = BorderStroke(1.dp, OnyxBorder),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable { onNavigateToAppointments() }
                .testTag("appointment_teaser_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary.copy(alpha = 0.15f))
                        .border(1.dp, GoldPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "VIP ATELIER & SALON",
                        color = GoldLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Book Private Consultation",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif
                        ),
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "Tailored fragrance creation and Seher Aloyon eye styling",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 11.sp
                        ),
                        color = TextSecondaryDark
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Seher Aloyon Heritage Note
        Card(
            colors = CardDefaults.cardColors(containerColor = OnyxSurface),
            border = BorderStroke(0.5.dp, OnyxBorder),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "سحر العيون — THE ART OF ALLURE",
                    color = GoldDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "\"True luxury is the harmony of an unforgettable scent and an enchanting gaze. Maison Aster and Seher Aloyon craft moments of timeless fascination.\"",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    ),
                    color = TextSecondaryDark,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
