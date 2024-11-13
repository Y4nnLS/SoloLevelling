package com.example.soloLevelling.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soloLevelling.viewmodel.FilmeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    var titulo by remember { mutableStateOf("") }
    var diretor by remember { mutableStateOf("") }
    var id by remember { mutableStateOf(0) }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    var filmeViewModel : FilmeViewModel = viewModel()
    var listaFilmes by filmeViewModel.listaFilmes
    val context = LocalContext.current
    var focusManager = LocalFocusManager.current

    // variavel de estado para exibir ou ocultar caiza de dialogo
    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    if (mostrarCaixaDialogo) {
        ExcluirFilme(onConfirm = {
            filmeViewModel.excluirFilme(id)
            mostrarCaixaDialogo = false
        }
            ,
            onDismiss = { mostrarCaixaDialogo = false}
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)) {

        Text(text = "Lista de Filmes", modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp)

        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = titulo,
            onValueChange = { titulo = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Título do filme")})

        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = diretor,
            onValueChange = { diretor = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Diretor do filme")})

        Spacer(modifier = Modifier.height(15.dp))

        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {

                var retorno = ""

                if (modoEditar) { // verificar se vamos editar o filme

                    retorno = filmeViewModel.atualizarFilme(id, titulo, diretor)
                    modoEditar = false
                    textoBotao = "Salvar"

                } else { // senão, vamos então salvar o filme

                    retorno = filmeViewModel.salvarFilme(titulo, diretor)
                }

                Toast.makeText(
                    context,
                    retorno,
                    Toast.LENGTH_LONG
                ).show()

                // limpar os campos de form
                titulo = ""
                diretor = ""
                // limpar o foco do form
                focusManager.clearFocus()

            }) {
            Text(text = textoBotao)
        }

        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn {

            items(listaFilmes) {

                filme ->

                Text(text = "${filme.titulo} (${filme.diretor})",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp)

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Button(onClick = {
                        id = filme.id
                        mostrarCaixaDialogo = true
                    }) {
                        Text(text = "Excluir")
                    }

                    Button(onClick = {

                        // ativamos modo editar:
                        modoEditar = true
                        // copiamos dados do filme atual:
                        id = filme.id
                        titulo = filme.titulo
                        diretor = filme.diretor
                        // mudamos o texto do botão:
                        textoBotao = "Atualizar"

                    }) {
                        Text(text = "Atualizar")
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                }
            }

        }

    }

}

@Composable
fun ExcluirFilme(onConfirm: () -> Unit, onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmar exclusão")},
        text = { Text(text = "Tem certeza que deseja excluir este filme?")},
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Sim, excluir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Não, cancelar")
            }
        })

}