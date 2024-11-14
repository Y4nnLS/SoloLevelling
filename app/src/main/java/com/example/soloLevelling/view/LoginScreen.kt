package com.example.soloLevelling.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soloLevelling.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(authViewModel: AuthViewModel, onLoginSuccess: () -> Unit, onBackToMenu: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginSuccess = authViewModel.loginSuccess.value
    val loginError = authViewModel.loginError.value

    if (loginSuccess) {
        onLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título da Tela
        Text(
            text = "Bem-vindo de Volta!",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo de Texto para Email
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Texto para Senha
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão de Login
        Button(
            onClick = { authViewModel.authenticateUser(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Login", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exibir Erro de Login
        if (loginError.isNotEmpty()) {
            Text(
                loginError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Voltar ao Menu Principal
        TextButton(
            onClick = onBackToMenu,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("Voltar ao Menu Principal")
        }
    }
}
