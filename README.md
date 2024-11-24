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
## 🗺️ Diagrama de Navegação

![Diagrama de Navegação](diagrama_navegacao.png)

O aplicativo possui dois principais fluxos:
1. **Tela de Login:** Permite autenticação do usuário.
2. **Tela de Gerenciamento de Tarefas:** Após o login, o usuário pode visualizar, adicionar, editar e excluir tarefas.

Usei uma representação visual para detalhar o fluxo entre as Activities, como mostrado acima.

---

## 📊 Diagrama de Estrutura do Banco de Dados
Explicação das Relações
- **User e Mission:** Muitos-para-Muitos através da tabela intermediária UserMission.
- **Mission:** Contém informações detalhadas sobre as tarefas atribuídas.
- **User:** Gerencia os dados de login e identidade dos usuários.

IMAGEM AQUI

---

## Estrutura do Projeto

### Entidade


> [!NOTE]
> Neste projeto, temos três entidades principais: Mission, User e UserMission. Elas foram desenvolvidas para estruturar e gerenciar os dados de forma eficiente utilizando o Room Database.

### Entidade: Mission
A entidade Mission representa as missões ou atividades que podem ser atribuídas aos usuários no sistema.
```kotlin
@Entity(tableName = "mission")
data class Mission(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Int, // 0: LOW, 1: MEDIUM, 2: HIGH
    val frequency: String // DAILY, WEEKLY, MONTHLY
)
```

- **id:** PRIMARY KEY gerada automaticamente para cada missão.
- **title:** Título da missão.
- **description**: Descrição detalhada da missão.
- **priority:** Prioridade da missão (0: Baixa, 1: Média, 2: Alta).
- **frequency:** Frequência da missão (DIÁRIA, SEMANAL, MENSAL).


### Entidade: User
A entidade User representa os usuários registrados no sistema.
```kotlin
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String
)
```

- **id:** PRIMARY KEY gerada automaticamente para cada usuário.
- **username:** Nome de usuário escolhido pelo participante.
- **email:** Endereço de e-mail registrado.
- **password:** Senha do usuário.


### Entidade: UserMission
A entidade UserMission conecta usuários às missões atribuídas, permitindo um relacionamento muitos-para-muitos entre User e Mission.
```kotlin
@Entity(tableName = "userMission")
data class UserMission(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Relaciona a missão ao usuário
    val missionId: Int // Relaciona à missão original
)
```

- **id:** PRIMARY KEY gerada automaticamente para cada relação.
- **userId:** FOREIGN KEY que referencia o ID de um usuário na tabela User.
- **missionId:** FOREIGN KEY que referencia o ID de uma missão na tabela Mission.

---
### Relações entre Entidades
- User ↔ UserMission

Um usuário pode estar relacionado a várias missões por meio da tabela UserMission.

- Mission ↔ UserMission

Uma missão pode ser atribuída a vários usuários, sendo registrada na tabela UserMission.
Essas relações permitem a criação de um sistema flexível e escalável para gerenciar atividades e atribuições personalizadas para cada usuário no aplicativo.

---

## DAO (Data Access Object)

### `MissionDao`

> [!NOTE]  
> `MissionDao` é responsável pelas operações de banco de dados relacionadas à entidade `Mission`, incluindo a recuperação de todas as missões e a inserção de novas missões.

```kotlin
@Dao
interface MissionDao {
    @Query("SELECT * FROM mission")
    suspend fun getAllMissions(): List<Mission> 
    // Recupera e retorna todas as missões cadastradas no banco de dados.

    @Insert
    suspend fun insertMissions(missions: List<Mission>) 
    // Insere uma lista de missões no banco de dados.
}
```

---

### `UserDao`

> [!NOTE]  
> `UserDao` é responsável pelas operações de banco de dados relacionadas à entidade `User`, incluindo a inserção de novos usuários e a busca de um usuário por e-mail.

```kotlin
@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User) 
    // Insere um novo usuário no banco de dados.

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User? 
    // Busca um único usuário pelo e-mail.
}
```

---

### `UserMissionDao`

> [!NOTE]  
> `UserMissionDao` gerencia as operações relacionadas à entidade `UserMission`, que conecta usuários a missões. Ele permite buscar missões associadas a um usuário e atribuir missões aleatórias.

```kotlin
@Dao
interface UserMissionDao {
    @Query("SELECT * FROM userMission WHERE userId = :userId")
    suspend fun getUserMissions(userId: Int): List<UserMission> 
    // Recupera todas as missões associadas a um usuário específico.

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserMissions(userMissions: List<UserMission>) 
    // Insere ou atualiza as missões atribuídas a um usuário.

    suspend fun assignRandomMissionsToUser(userId: Int, database: AppDatabase) {
        val missionDao = database.missionDao()
        val userMissionDao = database.userMissionDao()

        // Obtém todas as missões disponíveis
        val allMissions = missionDao.getAllMissions()

        // Seleciona 3 missões aleatórias
        val randomMissions = allMissions.shuffled().take(3)

        // Cria relações para o usuário
        val userMissions = randomMissions.map { mission ->
            UserMission(userId = userId, missionId = mission.id)
        }

        // Insere as missões atribuídas ao banco
        userMissionDao.insertUserMissions(userMissions)
    }
}
```

- **`assignRandomMissionsToUser`**: Método adicional que atribui três missões aleatórias de todas as missões disponíveis para um usuário específico. 

### Resumo  
Essas DAOs fornecem abstração sobre as operações CRUD no banco de dados Room, garantindo separação de responsabilidades e mantendo o código organizado e eficiente.
---
## Funcionalidades

- **Cadastro de Tarefas**: Permite adicionar uma nova tarefa com nome, descrição e prioridade.
- **Edição de Tarefas**: Permite editar uma tarefa com nome, descrição e prioridade.
- **Visualização das Tarefas**: Exibe todas as tarefas cadastradas.
- **Exclusão de Tarefas**: Permite deletar uma tarefa específica da lista.

---
### Configuração do Banco de Dados com Room
> [!NOTE]
> A classe de banco de dados é configurada utilizando RoomDatabase com o padrão Singleton para garantir uma única instância.

```kotlin

@Database(entities = [Tarefa::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun missionDao(): MissionDao
    abstract fun userMissionDao(): UserMissionDao


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
## 🔄 Padrão MVVM (Model-View-ViewModel)

Este projeto segue o padrão arquitetural MVVM:
- **Model:** Gerencia os dados e operações (Room Database e DAOs).
- **ViewModel:** Fornece os dados processados para a View e gerencia a lógica de interface.
- **View:** Composta pelas Activities e Jetpack Compose, que exibem os dados fornecidos pelo ViewModel.

### Exemplo: Fluxo de Inserção
1. O usuário preenche os campos na View (Compose).
2. O ViewModel valida os dados e chama o DAO correspondente para inserir no banco.
3. A View é atualizada com os dados mais recentes.

```kotlin
// Exemplo de interação ViewModel ↔ DAO
class MissionViewModel(private val missionDao: MissionDao) : ViewModel() {
    val allMissions: LiveData<List<Mission>> = liveData {
        emit(missionDao.getAllMissions())
    }

    fun addMission(mission: Mission) {
        viewModelScope.launch {
            missionDao.insert(mission)
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
## 🛠️ Melhorias Criativas

### Testes Realizados
- **Fluxo de Inserção:** Testado para garantir que tarefas são adicionadas corretamente.
- **Fluxo de Exclusão:** Verificado se tarefas são removidas e a interface atualizada.
- **Validação de Campos:** Certificado que campos obrigatórios não aceitam valores vazios.

### Melhorias Criativas
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

### 👥 Responsabilidades dos Membros

- **Felipe Franco Pinheiro:**
  - Desenvolvimento da interface de usuário com Jetpack Compose.
  - Implementação da navegação entre Activities.
- **Yann Lucas:**
  - Estruturação do banco de dados Room.
  - Implementação das DAOs e operações CRUD.
  - Design do padrão MVVM e integração das camadas.

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


## Tecnologias Utilizadas
- [**Kotlin**](https://kotlinlang.org/docs/home.html) 
- [**Jetpack Compose**](https://developer.android.com/jetpack/compose/documentation) 
- [**Room**](https://developer.android.com/training/data-storage/room) 
- [**Coroutines**](https://kotlinlang.org/docs/coroutines-overview.html) 



---

## ✅ Requisitos Funcionais Atendidos

| Requisito                                      | Status         |
|-----------------------------------------------|----------------|
| Navegação entre pelo menos duas Activities     | ✅ Implementado |
| Banco de dados com Room e operações CRUD       | ✅ Implementado |
| Relacionamento entre pelo menos duas entidades | ✅ Implementado |
| Interface responsiva com Jetpack Compose       | ✅ Implementado |
| Padrão arquitetural MVVM                       | ✅ Implementado |

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
