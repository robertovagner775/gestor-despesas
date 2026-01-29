# Sistema de Gerenciamento de Despesas

Sistema para gerenciamento de finanças pessoais, com foco no controle de despesas, receitas e orçamentos, oferecendo um dashboard que apresenta a saúde financeira do usuário.

## Tecnologias

### Já Utilizadas
- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Docker

### Planejadas
- Angular
- AWS (EC2, RDS, SMS)
- Login social (OAuth2)

## Principais Funcionalidades

- Gerenciamento de orçamentos (budgets)
- Controle de despesas com alertas (e-mail, SMS) em caso de ultrapassar valores planejados
- Controle de receitas
- Orçamentos separados por categorias
- Login com plataformas externas (OAuth2)

## Task List

### BACK-END

- [x] Configuração inicial do projeto (migrations, Swagger e Docker)
- [x] Autenticação com JWT (login e logout)
- [x] CRUD de Orçamentos (Budgets)
- [ ] CRUD de Despesas (Expenses)
- [ ] CRUD de Receitas (Incomes)
- [ ] Dashboard financeiro
- [ ] Envio de alertas (e-mail / SMS)
- [ ] Testes unitários

### FRONT-END

- [ ] Prototipação das telas
- [ ] Desenvolvimento das telas de Login e Registro
- [ ] Gerenciamento de Orçamentos, Despesas e Receitas
- [ ] Dashboard financeiro (gráficos)
