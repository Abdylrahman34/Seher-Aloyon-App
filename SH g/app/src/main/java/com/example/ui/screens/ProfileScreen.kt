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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.components.MaisonOutlinedButton
import com.example.ui.theme.CrimsonVelvet
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
import com.example.ui.theme.TextSecondaryDark
import com.example.viewmodel.MaisonViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    viewModel: MaisonViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val firebaseStatus by viewModel.firebaseStatus.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var showFirebaseGuide by remember { mutableStateOf(false) }

    val memberSinceStr = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        .format(Date(userProfile.memberSince))

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // Title Header
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(
                text = "ESPACE PRIVILÈGE",
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontSize = 10.sp
                ),
                color = GoldLight
            )
            Text(
                text = "Member Atelier & Account",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp
                ),
                color = TextPrimaryDark
            )
        }

        // Member Identity Card
        Card(
            colors = CardDefaults.cardColors(containerColor = OnyxCard),
            border = BorderStroke(1.dp, OnyxBorder),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF2E2419), OnyxSurface)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.2f))
                                    .border(1.5.dp, GoldPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = userProfile.displayName.ifEmpty { userProfile.username },
                                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    ),
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = "@${userProfile.username}",
                                    color = GoldLight,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = GoldPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Tier & Points Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Tier
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(OnyxSurface)
                                .border(0.5.dp, OnyxBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Diamond,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "STATUS",
                                        color = TextMuted,
                                        fontSize = 9.sp,
                                        letterSpacing = 1.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = userProfile.memberTier,
                                    color = GoldLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Points
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(OnyxSurface)
                                .border(0.5.dp, OnyxBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "VIP CREDITS",
                                        color = TextMuted,
                                        fontSize = 9.sp,
                                        letterSpacing = 1.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${userProfile.loyaltyPoints} Aster Pts",
                                    color = GoldLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Member Since: $memberSinceStr",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    if (userProfile.favoriteNotes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Olfactory Signature: ${userProfile.favoriteNotes}",
                            color = TextSecondaryDark,
                            fontSize = 11.sp
                        )
                    }

                    if (userProfile.vipBio.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${userProfile.vipBio}\"",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Firebase Backend Status Card
        Card(
            colors = CardDefaults.cardColors(containerColor = OnyxCard),
            border = BorderStroke(1.dp, OnyxBorder),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Firebase Backend Status",
                            style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Serif
                            ),
                            color = TextPrimaryDark
                        )
                    }

                    IconButton(
                        onClick = { viewModel.refreshFirebaseStatus() },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = GoldLight,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Status chip
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (firebaseStatus.isLiveConnected) EmeraldLuxe.copy(alpha = 0.2f)
                                else GoldDark.copy(alpha = 0.2f)
                            )
                            .border(
                                0.5.dp,
                                if (firebaseStatus.isLiveConnected) EmeraldLuxe else GoldPrimary,
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (firebaseStatus.isLiveConnected) "FIREBASE LIVE CONNECTED" else "LOCAL PERSISTENCE MODE",
                            color = if (firebaseStatus.isLiveConnected) EmeraldLuxe else GoldLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = firebaseStatus.message,
                    color = TextSecondaryDark,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (showFirebaseGuide) "Hide Architecture & Setup Details ▲" else "View Firebase Architecture & Setup Details ▼",
                    color = GoldPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { showFirebaseGuide = !showFirebaseGuide }
                )

                AnimatedVisibility(visible = showFirebaseGuide) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(OnyxSurface)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "FIREBASE SETUP INSTRUCTIONS:",
                            color = GoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "1. In Firebase Console, create a project and add an Android app with applicationId: 'com.aistudio.maisonaster.kpxqza'.\n" +
                                   "2. Enable Authentication > Email/Password provider.\n" +
                                   "3. Enable Cloud Firestore in production or test mode.\n" +
                                   "4. Download google-services.json and place it in the app/ directory.\n" +
                                   "5. Rebuild: The Google Services Gradle plugin will link live credentials automatically.",
                            color = TextSecondaryDark,
                            fontSize = 10.sp,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "FIRESTORE SCHEMA:",
                            color = GoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• usernames/{username} -> { username, uid, createdAt } (Ensures uniqueness constraint)\n" +
                                   "• users/{uid} -> { uid, username, displayName, memberTier, loyaltyPoints, ... }\n" +
                                   "• appointments/{id} -> { userId, serviceName, specialistName, date, timeSlot, ... }\n" +
                                   "• orders/{id} -> { userId, items, subtotal, total, status, trackingCode, ... }",
                            color = TextMuted,
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Out Button
        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            MaisonOutlinedButton(
                text = "Sign Out of Atelier",
                onClick = onLogout,
                icon = Icons.Default.Logout,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Edit Profile Modal
    if (showEditDialog) {
        var tempName by remember { mutableStateOf(userProfile.displayName) }
        var tempNotes by remember { mutableStateOf(userProfile.favoriteNotes) }
        var tempBio by remember { mutableStateOf(userProfile.vipBio) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            containerColor = OnyxCard,
            title = {
                Text(
                    text = "Edit Privilège Profile",
                    fontFamily = FontFamily.Serif,
                    color = GoldLight,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Display Name / Title") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = OnyxBorder,
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = tempNotes,
                        onValueChange = { tempNotes = it },
                        label = { Text("Olfactory Signature Notes") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = OnyxBorder,
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = tempBio,
                        onValueChange = { tempBio = it },
                        label = { Text("VIP Bio") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = OnyxBorder,
                            focusedTextColor = TextPrimaryDark,
                            unfocusedTextColor = TextPrimaryDark
                        ),
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(tempName, tempNotes, tempBio)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack)
                ) {
                    Text("SAVE", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("CANCEL", color = TextMuted)
                }
            }
        )
    }
}
