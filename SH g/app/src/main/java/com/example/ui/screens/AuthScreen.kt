package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DarkObsidianBrush
import com.example.ui.components.MaisonBrandHeader
import com.example.ui.components.MaisonPrimaryButton
import com.example.ui.components.MaisonTextField
import com.example.ui.theme.CrimsonVelvet
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
import com.example.viewmodel.AuthUiState
import com.example.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by authViewModel.uiState.collectAsState()
    val username by authViewModel.username.collectAsState()
    val password by authViewModel.password.collectAsState()
    val displayName by authViewModel.displayName.collectAsState()
    val confirmPassword by authViewModel.confirmPassword.collectAsState()
    val isLoginMode by authViewModel.isLoginMode.collectAsState()
    val isPasswordVisible by authViewModel.isPasswordVisible.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    val isLoading = uiState is AuthUiState.Loading

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkObsidianBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Brand Header with Emblem
            MaisonBrandHeader()

            Spacer(modifier = Modifier.height(28.dp))

            // Auth Card
            Card(
                colors = CardDefaults.cardColors(containerColor = OnyxCard),
                border = BorderStroke(1.dp, OnyxBorder),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Mode Selector Tabs (Sign In vs Register)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(OnyxSurface)
                            .padding(4.dp)
                    ) {
                        Surface(
                            onClick = { if (!isLoginMode) authViewModel.toggleLoginMode() },
                            color = if (isLoginMode) GoldPrimary else Color.Transparent,
                            shape = RoundedCornerShape(9.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("tab_login")
                        ) {
                            Text(
                                text = "SIGN IN",
                                color = if (isLoginMode) ObsidianBlack else TextSecondaryDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }

                        Surface(
                            onClick = { if (isLoginMode) authViewModel.toggleLoginMode() },
                            color = if (!isLoginMode) GoldPrimary else Color.Transparent,
                            shape = RoundedCornerShape(9.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("tab_register")
                        ) {
                            Text(
                                text = "REGISTER",
                                color = if (!isLoginMode) ObsidianBlack else TextSecondaryDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = if (isLoginMode) "Member Authentication" else "Create Privilège Account",
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "Strictly username and password protected",
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                    )

                    // Error Message
                    AnimatedVisibility(visible = errorMessage != null) {
                        if (errorMessage != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CrimsonVelvet.copy(alpha = 0.2f))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = CrimsonVelvet,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = errorMessage!!,
                                    color = Color(0xFFFFB4AB),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }

                    // Fields
                    if (!isLoginMode) {
                        MaisonTextField(
                            value = displayName,
                            onValueChange = { authViewModel.onDisplayNameChanged(it) },
                            label = "Full Name or Title",
                            leadingIcon = Icons.Default.Badge,
                            testTag = "display_name_input"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    MaisonTextField(
                        value = username,
                        onValueChange = { authViewModel.onUsernameChanged(it) },
                        label = "Username (3-20 chars)",
                        leadingIcon = Icons.Default.Person,
                        testTag = "username_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    MaisonTextField(
                        value = password,
                        onValueChange = { authViewModel.onPasswordChanged(it) },
                        label = "Password (min. 6 chars)",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        isPasswordVisible = isPasswordVisible,
                        onTogglePasswordVisibility = { authViewModel.togglePasswordVisibility() },
                        testTag = "password_input"
                    )

                    if (!isLoginMode) {
                        Spacer(modifier = Modifier.height(14.dp))
                        MaisonTextField(
                            value = confirmPassword,
                            onValueChange = { authViewModel.onConfirmPasswordChanged(it) },
                            label = "Confirm Password",
                            leadingIcon = Icons.Default.Security,
                            isPassword = true,
                            isPasswordVisible = isPasswordVisible,
                            onTogglePasswordVisibility = { authViewModel.togglePasswordVisibility() },
                            testTag = "confirm_password_input"
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Primary Submit Button
                    MaisonPrimaryButton(
                        text = if (isLoginMode) "Access Atelier" else "Complete Registration",
                        onClick = { authViewModel.submitAuth() },
                        isLoading = isLoading,
                        testTag = "submit_auth_button"
                    )

                    // Quick Demo Credentials Fill
                    if (isLoginMode) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(OnyxSurface)
                                .clickable { authViewModel.fillDemoCredentials() }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Fill Demo VIP Credentials",
                                color = GoldDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Security note footer
            Text(
                text = "Secured with Firebase Authentication & Cloud Firestore",
                fontSize = 11.sp,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )
        }
    }
}
