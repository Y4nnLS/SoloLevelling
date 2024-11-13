package com.example.soloLevelling.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.soloLevelling.model.Validacao
import com.example.soloLevelling.model.entity.Filme

class FilmeViewModel: ViewModel() {

    var listaFilmes = mutableStateOf(listOf<Filme>())
        private set

    fun salvarFilme(titulo: String, diretor: String) : String {

        // se algum dos dados estiver em branco:
        if (Validacao.haCamposEmBranco(titulo, diretor)) {
            return "Preencha todos os campos!"
        }

        // cria objeto do tipo Filme
        var filme = Filme(
            Validacao.getId(),
            titulo,
            diretor
        )

        // adicionar este filme na lista de filmes cadastradados
        listaFilmes.value += filme

        return "Filme salvo com sucesso!"

    }

    fun excluirFilme(id: Int) {

        listaFilmes.value = listaFilmes.value.filter { it.id != id }

    }

    fun atualizarFilme(id: Int, titulo: String,
                       diretor: String) : String {

        if (Validacao.haCamposEmBranco(titulo, diretor)) {
            return ("Ao editar, preencha todos os dados do filme!")
        }

        var filme = listaFilmes.value.find { it.id == id } ?: return "Erro ao atualizar filme"


       val filmesAtualizados = listaFilmes.value.map { filme ->
            if (filme.id == id) {
                filme.copy(titulo = titulo, diretor = diretor)
            } else {
                filme

            }
        }

        listaFilmes.value = filmesAtualizados

        return "Filme atualizado!"
    }

}