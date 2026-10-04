package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.FirebaseManager
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.MainAppScreen
import com.example.ui.theme.MaisonAsterTheme
import com.example.ui.theme.ObsidianBlack
import com.example.viewmodel.AuthUiState
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.MaisonViewModel

class MainActivity : ComponentActivity() {

    private lateinit var firebaseManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        firebaseManager = FirebaseManager(applicationContext)

        setContent {
            val authViewModel = remember { AuthViewModel(firebaseManager) }
            val authState by authViewModel.uiState.collectAsState()

            MaisonAsterTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ObsidianBlack
                ) {
                    Crossfade(
                        targetState = authState,
                        label = "AuthCrossfade"
                    ) { state ->
                        when (state) {
                            is AuthUiState.Authenticated -> {
                                val maisonViewModel = remember(state.profile.uid) {
                                    MaisonViewModel(firebaseManager, state.profile)
                                }
                                MainAppScreen(
                                    viewModel = maisonViewModel,
                                    onLogout = { authViewModel.logout() }
                                )
                            }
                            else -> {
                                AuthScreen(authViewModel = authViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
