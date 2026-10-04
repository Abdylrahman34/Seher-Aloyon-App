package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.ProductItem
import com.example.ui.components.MaisonCategoryChip
import com.example.ui.components.MaisonPrimaryButton
import com.example.ui.components.MaisonProductCard
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: MaisonViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.filteredProducts.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 90.dp)
    ) {
        // Title Bar
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(
                text = "HAUTE SÉLECTION",
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontSize = 10.sp
                ),
                color = GoldLight
            )
            Text(
                text = "The Collections",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp
                ),
                color = TextPrimaryDark
            )
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            placeholder = { Text("Search by name, notes, or ingredients...", fontSize = 13.sp, color = TextMuted) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = GoldPrimary
                )
            },
            trailingIcon = if (searchQuery.isNotEmpty()) {
                {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                    }
                }
            } else null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = OnyxBorder,
                focusedTextColor = TextPrimaryDark,
                unfocusedTextColor = TextPrimaryDark,
                focusedContainerColor = OnyxSurface,
                unfocusedContainerColor = OnyxSurface,
                cursorColor = GoldPrimary
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .testTag("catalog_search_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Categories Row
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
                    onClick = { viewModel.selectCategory(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grid of Products
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No Creations Found",
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        color = GoldLight
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try adjusting your search criteria or category filter.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products, key = { it.id }) { product ->
                    MaisonProductCard(
                        product = product,
                        isWishlisted = wishlistIds.contains(product.id),
                        onProductClick = { viewModel.selectProduct(product) },
                        onAddToCart = { viewModel.addToCart(product) },
                        onToggleWishlist = { viewModel.toggleWishlist(product.id) }
                    )
                }
            }
        }
    }

    // Product Detail Bottom Sheet
    selectedProduct?.let { product ->
        ProductDetailSheet(
            product = product,
            isWishlisted = wishlistIds.contains(product.id),
            onDismiss = { viewModel.selectProduct(null) },
            onAddToCart = { qty ->
                viewModel.addToCart(product, qty)
                viewModel.selectProduct(null)
            },
            onToggleWishlist = { viewModel.toggleWishlist(product.id) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailSheet(
    product: ProductItem,
    isWishlisted: Boolean,
    onDismiss: () -> Unit,
    onAddToCart: (Int) -> Unit,
    onToggleWishlist: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = OnyxSurface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(GoldDark)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Top Row: Category and Wishlist
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.category.uppercase(),
                    color = GoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                IconButton(onClick = onToggleWishlist) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) RoseGold else TextMuted
                    )
                }
            }

            Text(
                text = product.name,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp
                ),
                color = TextPrimaryDark
            )

            Text(
                text = product.subtitle,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = TextSecondaryDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Price & Volume Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(OnyxCard)
                    .border(1.dp, OnyxBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "PRICE", fontSize = 9.sp, color = TextMuted, letterSpacing = 1.sp)
                    Text(
                        text = "$${"%.0f".format(product.price)}",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = GoldPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "VOLUME / FORMAT", fontSize = 9.sp, color = TextMuted, letterSpacing = 1.sp)
                    Text(
                        text = product.volume,
                        fontSize = 13.sp,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "ARTISAN DESCRIPTION",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.description,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = TextSecondaryDark,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Olfactory / Notes Breakdown
            Card(
                colors = CardDefaults.cardColors(containerColor = OnyxCard),
                border = BorderStroke(1.dp, OnyxBorder),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (product.iconType == "perfume") "OLFACTORY PYRAMID" else "SIGNATURE SPECIFICATIONS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NoteItem(label = if (product.iconType == "perfume") "Top Notes" else "Key Material", value = product.topNotes)
                    NoteItem(label = if (product.iconType == "perfume") "Heart Notes" else "Structure", value = product.heartNotes)
                    NoteItem(label = if (product.iconType == "perfume") "Base Notes" else "Longevity / Finish", value = product.baseNotes)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quantity selector and Add to Bag
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity Controls
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(OnyxCard)
                        .border(1.dp, OnyxBorder, RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .clickable { if (quantity > 1) quantity-- },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("-", color = GoldLight, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = "$quantity",
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .clickable { if (quantity < 10) quantity++ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", color = GoldLight, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                MaisonPrimaryButton(
                    text = "Add to Bag • $${"%.0f".format(product.price * quantity)}",
                    onClick = { onAddToCart(quantity) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun NoteItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text(
            text = "$label: ",
            color = GoldLight,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            color = TextSecondaryDark,
            fontSize = 11.sp
        )
    }
}
