# Encurtador de URLs

Fiz esse projeto para aplicar meus estudos em Java e Spring na prática, especialmente desenvolvimento Web, definição de regras de negócio e integração com banco de dados.

Esse é um aplicativo web para encurtamento de URLs, desenvolvido com Java e Spring Boot. Os usuários podem encurtar qualquer URL, definir datas de expiração, marcar links como privados e visualizar a contagem de cliques.

## Funcionalidades

- **Encurtamento de URLs** — gera uma chave alfanumérica exclusiva de 6 caracteres para qualquer URL válida
- **Rastreamento de cliques** — contabiliza quantas vezes cada link encurtado foi acessado
- **Datas de expiração** — os links podem ser configurados para expirar após um número configurável de dias
- **Links públicos e privados** — links privados são acessíveis apenas pelo proprietário
- **Autenticação de usuário** — registro e login com criptografia de senha BCrypt
- **Controle de acesso baseado em funções** — funções `ROLE_USER` e `ROLE_ADMIN` com hierarquia via Spring Security
- **Painel de administração** — administradores podem visualizar e gerenciar todas as URLs encurtadas de todos os usuários
- **Listagens paginadas** — URLs públicas são listadas na página inicial com paginação no servidor
- **Validação de URL** — valida se a URL original realmente existe antes do encurtamento
- **Páginas de erro personalizadas** — páginas de erro 404 e 500 dedicadas
- **Migrações de banco de dados** — esquema gerenciado com Flyway

## Stack

- Java 17  
- Spring Boot 4.0   
- Spring MVC + Thymeleaf
- Bootstrap 5.3  
- Spring Security 6  
- Spring Data JPA + Hibernate  
- PostgreSQL 17  
- Flyway  
- Docker + Docker Compose  
- Maven

## Como Executar 

### Pré-requisitos

- Docker e Docker Compose

### Executando com Docker Compose

1\. Clone o repositório:

```bash

git clone https://github.com/pedrocf01/url-shortener.git

cd url-shortener
```

2\. Crie a imagem Docker:

```bash

docker build -t url-shortener:latest .

```

3\. Inicie a aplicação e o banco de dados:

```bash

docker compose up

```

4\. Abra [http://localhost:8080](http://localhost:8080) no navegador.

O arquivo `compose.yaml` inicia um contêiner PostgreSQL junto com a aplicação. O Flyway executa automaticamente as migrações do banco de dados na inicialização.


## Application Properties

O comportamento da aplicação é controlado pelo arquivo `application.properties` usando o prefixo `app.*`:

| Propriedade | Padrão | Descrição |
|---|---|---|
| `app.base-url` | `http://localhost:8080` | URL base  |
| `app.default-expiry-in-days` | `30` | Expiração para envios de URLs anônimos |
| `app.validate-original-url` | `true` | Indica se a URL original deve ser validada antes de ser salva |
| `app.page-size` | `10` | Número de itens por página nas listagens |

## Como funciona

1. Um usuário envia uma URL através do formulário da página inicial.

2. O `ShortUrlService` valida opcionalmente a URL enviando uma requisição HTTP HEAD.

3. Uma chave única de 6 caracteres é gerada usando o `SecureRandom` e verificada no banco de dados para evitar conflitos.

4. A URL encurtada é armazenada no PostgreSQL com metadados: proprietário, data de criação, expiração, sinalizador de privacidade e contagem de cliques.

5. Quando alguém acessa `/s/{shortKey}`, o serviço consulta a chave, verifica a expiração e as regras de privacidade, incrementa o contador de cliques e redireciona para a URL original.

## Páginas

| Rota | Acesso | Descrição |
|---|---|---|
| `/` | Público | Página inicial com formulário para encurtar URLs e lista de links públicos |
| `/s/{shortKey}` | Público | Redirecionar para a URL original |
| `/short-urls` | Público | Navegar por todas as URLs encurtadas públicas |
| `/my-urls` | Autenticado | Gerenciar suas próprias URLs encurtadas |
| `/register` | Público | Criar uma conta |
| `/login` | Público | Entrar |
| `/admin/dashboard` | Administradores | Visualizar e gerenciar todas as URLs |

## Esquema do Banco de Dados

Gerenciado por Flyway migrações em `src/main/resources/db/migration/`:

### **`short_urls`**

| Coluna | Tipo | Descrição |
| --- | --- | --- |
| `id` | bigint | Chave primária |
| `short_key` | varchar(10) | Identificador curto único |
| `original_url` | text | URL de destino |
| `is_private` | boolean | Indica se apenas o proprietário pode acessar |
| `expires_at` | timestamptz | Timestamp de expiração (permite valores nulos) |
| `created_by` | bigint (FK) | Referência a `users.id` |
| `click_count` | bigint | Número de acessos |
| `created_at` | timestamptz | Timestamp de criação |   


### **`users`**

| Coluna | Tipo | Descrição |
|---|---|---|
| `id` | bigint | Chave primária |
| `email` | varchar(100) | Identificador de login único |
| `password` | varchar(100) | Senha criptografada com BCrypt |
| `name` | varchar(100) | Nome de exibição |
| `role` | varchar(20) | `ROLE_USER` ou `ROLE_ADMIN` |
| `created_at` | timestamptz | Timestamp de registro |

## Segurança

- As senhas são criptografadas com BCrypt através do `PasswordEncoder` do Spring Security.

- Hierarquia de funções: `ROLE_ADMIN > ROLE_USER` — administradores herdam todas as permissões de usuário.

- A segurança em nível de método está habilitada (`@EnableMethodSecurity`) para controle de acesso.

- URLs privadas impõem acesso somente ao proprietário na camada de serviço.
