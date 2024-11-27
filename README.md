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

![Diagrama1](https://github.com/user-attachments/assets/916fed0c-c843-4a0b-a9f2-f3c30a2d7491)


O aplicativo possui dois principais fluxos:
1. **Tela de Login:** Permite autenticação do usuário.
2. **Tela de Gerenciamento de Tarefas:** Após o login, o usuário pode visualizar, adicionar, editar e excluir tarefas.

Usei uma representação visual para detalhar o fluxo entre as Activities, como mostrado acima.

---

## 📊 Diagrama de Estrutura do Banco de Dados

![Diagrama2](https://github.com/user-attachments/assets/534ccb78-4030-42d2-8a33-874a470e90a5)


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
@Entity(
    tableName = "userMission",
    foreignKeys = [
        ForeignKey(
            entity = Mission::class,
            parentColumns = ["id"],
            childColumns = ["missionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["missionId"]),
        Index(value = ["userId"])
    ]
)
data class UserMission(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "missionId") val missionId: Int
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMission(mission: Mission): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissions(missions: List<Mission>)

    @Delete
    suspend fun deleteMission(mission: Mission)

    @Update
    suspend fun updateMission(mission: Mission)
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserMission(userMission: UserMission)

    @Query("DELETE FROM userMission WHERE id = :missionId")
    suspend fun deleteUserMissionById(missionId: Int)

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

@Database(entities = [User::class, Mission::class, UserMission::class], version = 1, exportSchema = true)
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
class MissionViewModel(private val repository: MissionRepository) : ViewModel() {

    val missions = mutableStateListOf<Mission>()

    init {
        viewModelScope.launch {
            missions.addAll(repository.getAllMissions())
        }
    }

    fun assignMissionsToUser(userId: Int?, mission: Mission) {
        if (userId == null) {
            throw IllegalStateException("Erro: userId não está definido ao criar uma missão.")
        }
        viewModelScope.launch {
            // Adiciona a missão ao banco e à lista global
            repository.insertMissionAndAssignToUser(userId, mission)
            if (missions.none { it.id == mission.id }) {
                missions.add(mission) // Apenas adiciona a missão se ela ainda não estiver na lista
            }
        }
    }



    suspend fun getUserMissions(userId: Int): List<UserMission> {
        return repository.getUserMissions(userId)
    }


    fun deleteMission(mission: Mission) {
        viewModelScope.launch {
            repository.deleteMission(mission)
            repository.deleteUserMissionById(missionId = mission.id)
            missions.remove(mission)
        }
    }

    fun updateMission(mission: Mission) {
        viewModelScope.launch {
            repository.updateMission(mission)
            val index = missions.indexOfFirst { it.id == mission.id }
            if (index != -1) {
                missions[index] = mission
            }
        }
    }
}
```
---
### Fluxo de Operações

- **Fluxo de Inserção de Missões**:  
  1. **Interação do Usuário**: O usuário acessa o formulário de cadastro de missões e preenche os campos, como nome, descrição, prioridade e frequencia da missão.  
  2. **Ação de Cadastro**: Após preencher os campos obrigatórios, o usuário clica no botão "Cadastrar". O aplicativo valida os campos para garantir que todas as informações necessárias foram fornecidas.  
  3. **Inserção no Banco de Dados**: Após a validação, a missão é salva no banco de dados de missões por meio do método `insert` do DAO correspondente. Simultaneamente, o aplicativo obtém o ID do usuário que adicionou a missão e o ID da missão recém-criada. Esses dados são salvos no banco de dados `UserMission` por meio de uma operação separada. Todas essas ações são executadas em coroutines para evitar o bloqueio da interface do usuário.  
  4. **Atualização da Interface**: Após as operações bem-sucedidas, o aplicativo atualiza as listas relacionadas, garantindo que as novas missões sejam exibidas para o usuário imediatamente.

- Fluxo de edição:
  1. Interação do Usuário: O usuário clica no botão de "editar" do lado de uma tarefa, os dados da missao são apresentados nos inputs.
  2. Ação de Edição: O usuário pode editar a tarefa desejada, alterando no banco de dados de missao.
  3. Atualização da Interface: A lista de tarefas é atualizada para refletir a edição.

- Fluxo de Exclusão:
  1. Interação do Usuário: O usuário clica no botão de "excluir" do lado de uma tarefa.
  2. Ação de Exclusão: A tarefa é removida do banco de dados de missao e do banco de usermission
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
organização do código para garantir o padrão de projeto MVVM, essencial em
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
