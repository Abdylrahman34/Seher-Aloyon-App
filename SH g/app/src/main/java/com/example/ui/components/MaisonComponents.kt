package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ProductItem
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

val GoldGradientBrush = Brush.linearGradient(
    colors = listOf(GoldLight, GoldPrimary, GoldDark)
)

val DarkObsidianBrush = Brush.verticalGradient(
    colors = listOf(OnyxSurface, ObsidianBlack)
)

@Composable
fun MaisonLogo(
    modifier: Modifier = Modifier,
    size: Int = 44
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF2E2419), OnyxSurface)
                )
            )
            .border(1.dp, GoldPrimary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = "Maison Aster & Seher Aloyon Insignia",
            tint = GoldPrimary,
            modifier = Modifier.size((size * 0.55).dp)
        )
    }
}

@Composable
fun MaisonBrandHeader(
    modifier: Modifier = Modifier,
    title: String = "MAISON ASTER",
    subtitle: String = "SEHER ALOYON HAUTE PARFUMERIE & COUTURE"
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MaisonLogo(size = 54)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = title,
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                fontFamily = FontFamily.Serif,
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Light
            ),
            color = GoldPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = subtitle,
            style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 2.sp,
                fontSize = 9.sp
            ),
            color = TextSecondaryDark,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MaisonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: (() -> Unit)? = null,
    testTag: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 13.sp) },
        leadingIcon = leadingIcon?.let {
            { Icon(imageVector = it, contentDescription = null, tint = GoldPrimary) }
        },
        trailingIcon = if (isPassword && onTogglePasswordVisibility != null) {
            {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle password visibility",
                        tint = GoldLight
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GoldPrimary,
            unfocusedBorderColor = OnyxBorder,
            focusedLabelColor = GoldLight,
            unfocusedLabelColor = TextSecondaryDark,
            focusedTextColor = TextPrimaryDark,
            unfocusedTextColor = TextPrimaryDark,
            cursorColor = GoldPrimary,
            focusedContainerColor = OnyxSurface,
            unfocusedContainerColor = OnyxSurface
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier)
    )
}

@Composable
fun MaisonPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    testTag: String = ""
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = GoldPrimary,
            contentColor = ObsidianBlack,
            disabledContainerColor = GoldDark.copy(alpha = 0.5f),
            disabledContentColor = ObsidianBlack.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp), spotColor = GoldPrimary)
            .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = ObsidianBlack,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun MaisonOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    testTag: String = ""
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(1.dp, GoldPrimary),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = GoldLight
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .height(48.dp)
            .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                style = androidx.compose.material3.MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 1.sp,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
fun MaisonCategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) GoldPrimary else OnyxCard,
        border = BorderStroke(1.dp, if (isSelected) GoldPrimary else OnyxBorder),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = if (isSelected) ObsidianBlack else TextSecondaryDark,
            style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp
            ),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun MaisonProductCard(
    product: ProductItem,
    isWishlisted: Boolean,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit,
    onToggleWishlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = OnyxCard),
        border = BorderStroke(1.dp, OnyxBorder),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top row with badge and wishlist heart
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (product.badge != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GoldPrimary.copy(alpha = 0.15f))
                            .border(0.5.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = product.badge,
                            color = GoldLight,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(4.dp))
                }

                IconButton(
                    onClick = onToggleWishlist,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) RoseGold else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Icon / Motif Art Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF2A2118), OnyxSurface)
                        )
                    )
                    .border(0.5.dp, OnyxBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val iconVector = when (product.iconType) {
                        "eye" -> Icons.Default.RemoveRedEye
                        "skincare" -> Icons.Default.Spa
                        "treatment" -> Icons.Default.AutoAwesome
                        else -> Icons.Default.LocalMall
                    }
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(42.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.volume,
                        color = TextMuted,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Product Name and Subtitle
            Text(
                text = product.name,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontSize = 15.sp,
                    lineHeight = 18.sp
                ),
                color = TextPrimaryDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = product.subtitle,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 11.sp
                ),
                color = TextSecondaryDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "${product.rating}",
                    color = GoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " (${product.reviewCount})",
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Price and Add button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${"%.0f".format(product.price)}",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = GoldPrimary
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldPrimary)
                        .clickable(onClick = onAddToCart)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "ADD",
                        color = ObsidianBlack,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
