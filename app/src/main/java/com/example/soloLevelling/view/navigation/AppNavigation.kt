package com.example.soloLevelling.view.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.*
import com.example.soloLevelling.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBackToMenu: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val registrationError = authViewModel.registrationError.value
    val loginSuccess = authViewModel.loginSuccess.value

    // Observa o estado de sucesso no registro
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            Toast.makeText(context, "Conta criada com sucesso!", Toast.LENGTH_LONG).show()
            onRegisterSuccess() // Redireciona para a tela desejada
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crie sua Conta", fontSize = 28.sp, modifier = Modifier.padding(bottom = 24.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nome de Usuário") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { authViewModel.registerUser(username, email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        // Mostra erro se o e-mail já estiver registrado
        if (registrationError.isNotEmpty()) {
            Text(
                text = registrationError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBackToMenu) {
            Text("Voltar ao Menu Principal")
        }
    }
}
