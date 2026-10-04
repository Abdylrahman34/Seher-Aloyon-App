package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.model.Order
import com.example.ui.components.MaisonOutlinedButton
import com.example.ui.components.MaisonPrimaryButton
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BagScreen(
    viewModel: MaisonViewModel,
    onNavigateToCatalog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val isGiftPackaging by viewModel.giftPackaging.collectAsState()
    val promoDiscount by viewModel.promoDiscount.collectAsState()
    val subtotal by viewModel.subtotal.collectAsState()
    val total by viewModel.grandTotal.collectAsState()
    val isPlacingOrder by viewModel.isPlacingOrder.collectAsState()
    val orders by viewModel.userOrders.collectAsState()

    var promoInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // Title Header
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(
                text = "VOTRE COMMANDE",
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontSize = 10.sp
                ),
                color = GoldLight
            )
            Text(
                text = "Luxury Bag & Checkout",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp
                ),
                color = TextPrimaryDark
            )
        }

        if (cartItems.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = OnyxCard),
                border = BorderStroke(1.dp, OnyxBorder),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(OnyxSurface)
                            .border(1.dp, OnyxBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = GoldDark,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Your Luxury Bag is Empty",
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        color = GoldLight
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Curate your signature fragrance flacons and Seher Aloyon eye couture lashes.",
                        fontSize = 12.sp,
                        color = TextSecondaryDark,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    MaisonOutlinedButton(
                        text = "Explore Collections",
                        onClick = onNavigateToCatalog
                    )
                }
            }
        } else {
            // Cart Items List
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                cartItems.forEach { item ->
                    CartItemRow(
                        cartItem = item,
                        onQuantityChange = { delta -> viewModel.updateCartQuantity(item.product.id, delta) },
                        onRemove = { viewModel.removeFromCart(item.product.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gift Packaging Card
            Card(
                colors = CardDefaults.cardColors(containerColor = OnyxCard),
                border = BorderStroke(1.dp, OnyxBorder),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Complimentary Luxury Gift Box",
                            color = TextPrimaryDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Signature black lacquer box, silk ribbon, and personalized calligraphy card.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = isGiftPackaging,
                        onCheckedChange = { viewModel.toggleGiftPackaging() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ObsidianBlack,
                            checkedTrackColor = GoldPrimary,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = OnyxSurface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Promo Code Card
            Card(
                colors = CardDefaults.cardColors(containerColor = OnyxCard),
                border = BorderStroke(1.dp, OnyxBorder),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = promoInput,
                        onValueChange = { promoInput = it },
                        placeholder = { Text("Promo Code (e.g. VIPSEHER)", fontSize = 12.sp, color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = OnyxBorder,
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark,
                            focusedContainerColor = OnyxSurface,
                            unfocusedContainerColor = OnyxSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(GoldPrimary)
                            .clickable { viewModel.applyPromoCode(promoInput) }
                            .padding(horizontal = 14.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = "APPLY",
                            color = ObsidianBlack,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Order Breakdown Card
            Card(
                colors = CardDefaults.cardColors(containerColor = OnyxCard),
                border = BorderStroke(1.dp, OnyxBorder),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "ORDER SUMMARY",
                        color = GoldLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    SummaryRow(label = "Items Subtotal", amount = "$${"%.2f".format(subtotal)}")
                    if (promoDiscount > 0.0) {
                        SummaryRow(
                            label = "Privilège VIP Discount (10%)",
                            amount = "-$${"%.2f".format(promoDiscount)}",
                            amountColor = EmeraldLuxe
                        )
                    }
                    SummaryRow(
                        label = "White Glove Courier Shipping",
                        amount = "COMPLIMENTARY",
                        amountColor = GoldLight
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(OnyxBorder)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Investment",
                            fontFamily = FontFamily.Serif,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = "$${"%.2f".format(total)}",
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    MaisonPrimaryButton(
                        text = "Place Luxury Order",
                        onClick = { viewModel.placeOrder() },
                        isLoading = isPlacingOrder,
                        icon = Icons.Default.LocalShipping,
                        testTag = "place_order_button"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Order History Section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Order History & Atelier Dispatches",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp
                ),
                color = TextPrimaryDark
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (orders.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = OnyxSurface),
                    border = BorderStroke(0.5.dp, OnyxBorder),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No past orders yet. Orders placed will appear here with live tracking.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                orders.forEach { order ->
                    OrderHistoryCard(order = order)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = OnyxCard),
        border = BorderStroke(1.dp, OnyxBorder),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.product.name,
                    color = TextPrimaryDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${cartItem.product.volume} • $${"%.0f".format(cartItem.product.price)} each",
                    color = TextSecondaryDark,
                    fontSize = 11.sp
                )
            }

            // Quantity adjusters
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(OnyxSurface)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .clickable { onQuantityChange(-1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("-", color = GoldLight, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "${cartItem.quantity}",
                    color = TextPrimaryDark,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .clickable { onQuantityChange(1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = GoldLight, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Remove",
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun OrderHistoryCard(order: Order) {
    val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(order.createdAt))

    Card(
        colors = CardDefaults.cardColors(containerColor = OnyxCard),
        border = BorderStroke(1.dp, OnyxBorder),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tracking: ${order.trackingCode}",
                    color = GoldLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(GoldPrimary.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = order.status,
                        color = GoldPrimary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            order.items.forEach { item ->
                Text(
                    text = "• ${item.quantity}x ${item.productName} ($${"%.0f".format(item.unitPrice)})",
                    color = TextSecondaryDark,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = dateStr, color = TextMuted, fontSize = 10.sp)
                Text(
                    text = "Total: $${"%.2f".format(order.total)}",
                    color = GoldPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    amount: String,
    amountColor: Color = TextPrimaryDark
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondaryDark, fontSize = 12.sp)
        Text(text = amount, color = amountColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
