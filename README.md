# 📚 Library Management System - Backend

Spring Boot REST API for library operations with JWT security and MySQL integration

## 🎯 Backend Responsibilities
- RESTful API for frontend interactions
- JWT Authentication & Authorization (Librarian/Member roles)
- Business Logic Enforcement:
  - Lending rules (max books, renewals, fines)
  - Membership validity checks
- Database Operations via Spring Data JPA

## 🚀 Tech Stack
| Layer       | Technology               |
|-------------|--------------------------|
| Framework   | Spring Boot 3.x          |
| Security    | Spring Security + JWT    |
| Database    | MySQL 8                  |
| ORM         | Hibernate/JPA            |
| Build Tool  | Maven                    |

## 📂 API Endpoints (Key Examples)
### 🔐 Auth Controller
- `POST /api/auth/register` → Register (Librarian/Member)
- `POST /api/auth/login` → JWT Token generation
- `POST /api/auth/reset-password` → Password reset

### 📖 Book Controller
- `GET /api/books?search={query}&status={available}` → Search/filter books
- `POST /api/books` → Add new book (Librarian only)
- `PUT /api/books/{id}/reserve` → Reserve book (Member)

### 📦 Lending Controller
- `POST /api/loans/borrow` → Borrow book (with due date calc)
- `POST /api/loans/return` → Return book (+ fine if overdue)
- `GET /api/loans/member/{id}` → Member's borrowing history

## ⚙️ Setup & Configuration
### 1. Database Setup
```sql
CREATE DATABASE lms_db;

### 2. Configure `application.properties`

### 3. Run the Backend in Eclipse

Right-click your main application class (the one with @SpringBootApplication)

Select Run As → Spring Boot App

