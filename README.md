# 📚 Library System

Sistema de gerenciamento de biblioteca, com backend em Spring Boot e frontend em React.

---

## 🚀 Tecnologias

**Backend**

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL

**Frontend**

* React
* TypeScript
* Material UI
* Axios

**Testes**

* JUnit
* H2 Database

---

## 📁 Estrutura

```
elotech/
 ├── library/        # Backend
 └── library-front/  # Frontend
```

---

## ⚙️ Como rodar

### 🔐 1. Configure as variáveis de ambiente

```
DB_NAME=library
DB_USER=postgres
DB_PASSWORD=sua_senha
```

---

### 🗄️ 2. Crie o banco no PostgreSQL

```sql
CREATE DATABASE library;
```

---

### 🔧 3. Backend

```
cd library
./mvnw spring-boot:run
```

API disponível em:
http://localhost:8080

---

### 💻 4. Frontend

```
cd library-front
npm install
npm start
```

Aplicação disponível em:
http://localhost:3000

---

## 🔗 Funcionalidades

* CRUD de usuários
* CRUD de livros
* Controle de empréstimos
* Sistema de recomendação por categoria

---

## 🧪 Testes

```
cd library
./mvnw test
```

---

## 📌 Regras de negócio

* Todos os campos obrigatórios
* Email válido
* Datas não podem ser futuras
* Um livro só pode ter um empréstimo ativo

---

## 👨‍💻 Autor

Matheus Martins
