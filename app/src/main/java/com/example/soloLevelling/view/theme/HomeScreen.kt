package com.example.soloLevelling.view.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.soloLevelling.viewmodel.AuthViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel) {
    Column {
        Text("Bem-vindo à Tela Principal")
        Button(onClick = {
            authViewModel.logout()
        }) {
            Text("Logout")
        }
    }
}
