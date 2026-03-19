# Sistema de Gerenciamento de Despesas


![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=flat&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=flat&logo=angular&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-FF9900?style=flat&logo=amazonaws&logoColor=white)

Sistema para gerenciamento de finanças pessoais, com foco no controle de despesas, receitas e orçamentos, oferecendo um dashboard que apresenta de forma clara a saúde financeira do usuário.

## Tecnologias

### Já Utilizadas
- Java 21
- Spring Boot
- Spring Security 
- Spring Data JPA
- PostgreSQL
- Docker

### Planejadas
- Angular
- AWS (EC2, RDS, SNS)
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
- [x] CRUD de Orçamentos 
- [x] CRUD de Despesas 
- [x] CRUD de Receitas 
- [ ] Dashboard financeiro com métricas e indicadores
- [ ] Envio de alertas (e-mail / SMS)
- [ ] Testes unitários

### FRONT-END

- [ ] Prototipação das telas
- [ ] Desenvolvimento das telas de Login e Registro
- [ ] Gerenciamento de Orçamentos, Despesas e Receitas
- [ ] Dashboard financeiro
