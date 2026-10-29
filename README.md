# CondoGestor — Sistema de Gestão de Condomínios

O **CondoGestor** é um sistema web desenvolvido para auxiliar síndicos e administradores no gerenciamento de condomínios, centralizando informações de moradores, unidades, ocorrências e reservas de áreas comuns.

O projeto foi desenvolvido na disciplina de **Laboratório de Programação**, do curso de Sistemas de Informação.

## Tecnologias utilizadas

- **Backend:** Java 21, Spring Boot 4.1.2 e Spring MVC
- **Frontend:** HTML5, CSS3, JavaScript e Thymeleaf
- **Persistência:** Spring Data JPA e Hibernate
- **Banco de dados:** PostgreSQL hospedado no Neon
- **Autenticação:** sessões HTTP e senhas protegidas com BCrypt
- **Gerenciamento de dependências:** Maven

## Funcionalidades

- **Autenticação:** login e logout de usuários, com acesso aos dados do condomínio associado.
- **Dashboard:** visão geral com indicadores de moradores, unidades, ocorrências e reservas.
- **Moradores:** cadastro, consulta, edição e exclusão, com validações de CPF, e-mail e campos obrigatórios.
- **Unidades:** listagem e acompanhamento das unidades do condomínio.
- **Ocorrências:** registro, consulta e atualização de status.
- **Reservas:** agendamento de áreas comuns, alteração de status e verificação de conflitos.

## Estrutura do projeto

O código Java está organizado em camadas:

- `controller/` — Recebimento de requisições e gerenciamento das rotas.
- `service/` — Regras de negócio e operações da aplicação.
- `repository/` — Acesso ao banco de dados por meio do Spring Data JPA.
- `model/` — Entidades e mapeamentos JPA.
- `config/` — Configurações da aplicação e de segurança.

Os arquivos HTML estão em `src/main/resources/templates/`, enquanto CSS e JavaScript ficam em `src/main/resources/static/`.

## Como executar

### Pré-requisitos

- Java JDK 21 ou superior
- Apache Maven 3.6 ou superior, para execução pelo terminal
- Acesso ao banco PostgreSQL utilizado pelo projeto

### Configuração do banco de dados

A conexão com o PostgreSQL é definida em `src/main/resources/application.properties`. 
As variáveis ficam no arquivo .env.example

A aplicação oferece suporte às seguintes variáveis de ambiente para configurar a conexão:

- `DB_URL` — URL JDBC do PostgreSQL
- `DB_USER` — Usuário do banco
- `DB_PASSWORD` — Senha do banco

Não inclua credenciais pessoais em commits ou documentação.

### Execução

Com o Maven disponível, acesse a pasta do projeto e execute:

`mvn spring-boot:run`

Alternativamente, abra o projeto no VS Code com as extensões Java e Spring Boot e execute a classe `CondominioApplication.java`.

Após a inicialização, acesse:

**http://localhost:8080/login**

Para entrar no sistema, utilize uma conta previamente cadastrada no banco de dados.

## Desenvolvedores

- Carlos Phelipe M. de Albuquerque
- Vinicius Metri

## Finalidade

Projeto acadêmico desenvolvido para fins educacionais, com foco na aplicação prática de Java, Spring Boot, desenvolvimento web e persistência de dados.
