## 🙋‍♂️ Equipe de desenvolvimento

<table align='center'>
  <tr>
    <td align="center">
        <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/101208372?v=4" width="100px;" alt=""/><br /><sub><b><a href="https://github.com/Y4nnLS">Yann Lucas</a></b></sub></a><br />🤓☝</a></td>
    <td align="center">
        <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/60533993?v=4" width="100px;" alt=""/><br /><sub><b><a href="https://github.com/Ypsiloon">Felipe Franco</a></b></sub></a><br />👻</a></td>
  </table>

# Gerenciador de Tarefas

Este projeto é um aplicativo simples de **Gerenciamento de Tarefas** desenvolvido em Kotlin usando **Jetpack Compose** e **Room** como banco de dados local. O app permite que o usuário cadastre, visualize, defina a prioridade e exclua tarefas pessoais.

---

## Propósito do Aplicativo

Desenvolvimento de um aplicativo para o gerenciamento de tarefas, permitindo o cadastro de tarefas com descrição e prioridade, facilitando a visualização e exclusão das tarefas.

---

## Estrutura do Projeto

### Entidade


> [!NOTE]
> `Tarefa`  representa cada tarefa cadastrada pelo usuário.

```kotlin
@Entity(tableName = "tarefa")
data class Tarefa(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,        
    val descricao: String,    
    val prioridade: Int       // 0: Baixa, 1: Média, 2: Alta)
)
```

- **id**: PRIMARY KEY.
- **nome**: Nome da tarefa.
- **descricao**: Descrição da tarefa.
- **prioridade**: Prioridade da tarefa.

---

### DAO (Data Access Object)

> [!NOTE]
> `TarefaDao` é responsável por todas as operações de banco de dados relacionadas à entidade `Tarefa`, incluindo os métodos de criação, leitura e exclusão.


```kotlin
@Dao
interface TarefaDao {
    @Insert  
    suspend fun inserir(tarefa: Tarefa)       //Insere uma nova tarefa no banco de dados.

    @Query("SELECT * FROM tarefa")
    suspend fun buscarTodos(): List<Tarefa>   //Recupera e retorna as tarefas cadastradas no banco de dados.

    @Delete
    suspend fun deletarTarefa(tarefa: Tarefa) // Exclui uma tarefa específica do banco de dados com base em seu identificador (`id`).
}
```
## Funcionalidades

- **Cadastro de Tarefas**: Permite adicionar uma nova tarefa com nome, descrição e prioridade.
- **Visualização das Tarefas**: Exibe todas as tarefas cadastradas.
- **Exclusão de Tarefas**: Permite deletar uma tarefa específica da lista.

---
### Configuração do Banco de Dados com Room
> [!NOTE]
> A classe de banco de dados é configurada utilizando RoomDatabase com o padrão Singleton para garantir uma única instância.

```kotlin

@Database(entities = [Tarefa::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tarefaDao(): TarefaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```
---
### Fluxo de Operações
- Fluxo de Inserção:
  1. Interação do Usuário: O usuário acessa a tela de cadastro e preenche os
campos de nome, descrição e prioridade.
  2. Ação de Inserção: Ao clicar no botão "Salvar", o aplicativo valida os campos
obrigatórios.
  3. Inserção no Banco: Após a validação, a tarefa é salva no banco de dados pelo
método `inserir` do `TarefaDao`. A operação é executada em uma coroutine
para evitar o bloqueio da interface.
  4. Atualização da Interface: Após a inserção, o app atualiza a lista de exibição com
as tarefas mais recentes.
- Fluxo de Consulta:
  1. Inicialização do App: Quando o aplicativo inicia, ele carrega todas as tarefas do
banco de dados por meio do método `buscarTodos`.
  2. Exibição dos Dados: As tarefas são exibidas em uma lista na interface usando
`LazyColumn` no Jetpack Compose, apresentando cada tarefa com detalhes
de nome, descrição e prioridade.
- Fluxo de Exclusão:
  1. Interação do Usuário: O usuário seleciona uma tarefa e escolhe a opção
"Excluir".
  2. Ação de Exclusão: A tarefa é removida do banco de dados pelo método
`deletarTarefa`.
  3. Atualização da Interface: A lista de tarefas é atualizada para refletir a exclusão.

---
## Melhorias Criativas
- **Validação de Campos Obrigatórios com Toast's**
  - Antes de salvar uma tarefa, o aplicativo fara a verificação e exibirá um Toast caso nome ou descrição estejam vazios. Informando o usuário que ele deve preencher os campos obrigatórios.
  - Essa validação evita que tarefas incompletas/mal descritas sejam salvas, melhorando a experiência do usuário na organização de suas tarefas.

- **Adição de Data de Conclusão e Notificação de Lembrete**
  - O usuário poderá definir uma data de conclusão para cada tarefa e selecionar um número de dias antes do prazo para ser notificado. Essa notificação seria enviada com antecedência, alertando o usuário sobre a aproximação do prazo de uma tarefa.
  - Essa funcionalidade ajuda os usuários a se organizarem melhor, permitindo que eles recebam lembretes antecipados para evitar atrasos.

- **Organização das Tarefas por Projetos**
  - Cada tarefa poderá ser associada a um projeto específico. Os projetos funcionariam como agrupadores, permitindo que o usuário organize as tarefas de forma hierárquica. Cada projeto conteria suas próprias tarefas, que podem ser visualizadas separadamente.
  - Essa organização hierárquica facilita a gestão de tarefas relacionadas a um objetivo ou projeto maior, dando mais clareza ao usuário e possibilitando o gerenciamento de atividades mais complexas.

- **Ordenação e Filtros**
  - Permitir que o usuário filtre as tarefas por prioridade (ex.: Alta, Média, Baixa). Assim, o usuário pode visualizar apenas as tarefas mais importantes, o que ajuda na organização e foco nas tarefas de maior prioridade.
  - Adicionar uma opção para ordenar as tarefas por diferentes critérios, como ordem alfabética, ordem de prioridade, por data de conclusão e por projeto.
  - Essas ordenações e filtragens proporcionam um maior controle e visibilidade das tarefas, melhorando a produtividade e ajudando o usuário a visualizar suas tarefas do jeito que preferir.
---

### Conclusão
- Durante o desenvolvimento deste aplicativo de Gerenciamento de Tarefas, foram
aprendidos conceitos importantes sobre o uso do Room para persistência de dados,
integração com o Jetpack Compose para uma interface dinâmica e reativa, além da
organização do código para garantir o padrão de projeto Singleton, essencial em
aplicativos Android.
- ### Principais Aprendizados:
  - Como estruturar uma base de dados utilizando o Room e trabalhar com
entidades e DAOs para gerenciar os dados.
  - O uso de validações e notificações para melhorar a experiência do usuário e
garantir integridade dos dados.
  - A importância de filtros e ordenação para facilitar o acesso rápido às
informações mais relevantes.
Desafios Enfrentados:
Um dos principais desafios foi planejar o fluxo de dados para que todas as operações
de CRUD fossem realizadas de forma eficiente, além de assegurar que a aplicação
mantivesse uma estrutura organizada e de fácil manutenção.

---

### Prompt para o ChatGPT
```
"Preciso de ajuda para desenvolver um aplicativo de Gerenciamento de Tarefas em Kotlin, utilizando Jetpack Compose e o banco de dados Room. 
Preciso de:

1. Definição da entidade Tarefa com campos para nome, descrição e prioridade.
2. Criação do DAO TarefaDao com métodos para inserir, buscar e deletar tarefas.
3. Configuração de uma classe AppDatabase com o padrão Singleton.
4. Orientação sobre os fluxos de operação de inserção, consulta e exclusão para o aplicativo.
5. Sugestões de melhorias criativas, como validação de campos e notificações.

Poderia também gerar exemplos de código para essas funcionalidades?"
```

---

## Tecnologias Utilizadas
- [**Kotlin**](https://kotlinlang.org/docs/home.html) 
- [**Jetpack Compose**](https://developer.android.com/jetpack/compose/documentation) 
- [**Room**](https://developer.android.com/training/data-storage/room) 
- [**Coroutines**](https://kotlinlang.org/docs/coroutines-overview.html) 

---

## Como Executar o Projeto

1. Clone o repositório.
2. Abra o projeto no Android Studio.
3. Compile e execute no emulador ou em um dispositivo físico.

---

## Contribuições

Contribuições são bem-vindas! Se você tiver sugestões de melhorias ou novas funcionalidades, fique à vontade para abrir um *pull request*.

--- 

## Licença

Este projeto é de uso livre para fins educacionais e pessoais.

--- 
