package com.example.soloLevelling.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soloLevelling.viewmodel.AuthViewModel

import android.util.Log

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

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            android.util.Log.wtf("RegisterScreen", "Registro bem-sucedido, redirecionando para HomeScreen.")
            Toast.makeText(context, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
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
            onClick = {
                android.util.Log.wtf("RegisterScreen", "Tentando registrar usuário. Dados: username=$username, email=$email")
                authViewModel.registerUser(username, email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        if (registrationError.isNotEmpty()) {
            android.util.Log.e("RegisterScreen", "Erro de registro: $registrationError")
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
