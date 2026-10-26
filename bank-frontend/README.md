# Bank Account Management Application

Full-stack banking application built with **Spring Boot + React + JWT + Bootstrap**.
The system supports customer management, account management, authentication, role-based access, pagination, and frontend testing with coverage.

---

## Project Overview

This application allows users to manage customers and bank accounts.

Features include:

* Create / Update / View / Delete customers
* Create / Update / View / Delete accounts
* Savings & Checking accounts
* Search accounts by customer id / name / balance
* Role-based UI (ADMIN / USER)
* JWT authentication
* Pagination in UI
* Frontend unit testing with coverage

---

## Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring Data JPA
* H2 / MySQL
* Spring Security
* JWT Authentication
* REST API

### Auth Service

* Spring Boot
* Spring Security
* JWT
* BCrypt password encoder

### Frontend

* React
* Vite
* React Router
* Axios
* Bootstrap
* Bootstrap Icons

### Testing

* Vitest
* React Testing Library
* jsdom
* Coverage (v8)

---

## Architecture

```
Frontend (React)
   |
   |-----> Auth Service (JWT)
   |
   |-----> Account / Customer API (Spring Boot)
```

Auth service generates token
Frontend sends token in header
Backend validates token

```
Authorization: Bearer <token>
```

---

## Roles

| Role          | Permissions            |
| ------------- | ---------------------- |
| ADMIN         | create, update, delete |
| USER          | read only              |
| Not logged in | no sidebar             |

Role stored in JWT:

```
ROLE_ADMIN
ROLE_USER
```

---

## Features

### Customers

* View all customers
* Add customer
* Update customer
* Delete customer
* Find by id

### Accounts

* View all accounts
* Add savings account
* Add checking account
* Update account
* Delete account
* Find by balance
* Find by customer id
* Find by customer name

### UI Features

* Sidebar navigation
* Bootstrap styling
* Carousel on home page
* Fixed sidebar layout
* Alerts for errors
* Pagination (5 per page)

---

## Pagination

Implemented in frontend.

Only 5 records per page.

```
Previous | Page 1 of 3 | Next
```

No backend changes required.

---

## Authentication Flow

1. Login page calls auth service

```
POST /api/v1/auth/login
```

Response:

```
{
  token,
  username,
  role
}
```

Token stored in localStorage

```
localStorage.token
localStorage.role
```

Axios sends token automatically.

---

## Role Based UI

Sidebar uses:

```
useAuth()
```

Admin only:

* Add
* Update
* Delete

User:

* View only

Not logged in:

* No sidebar

---

## Testing

Frontend tests created using:

```
Vitest
React Testing Library
```

Test files:

```
src/test/
```

Tests include:

* Login
* CustomerList
* AccountList
* Sidebar
* ProtectedRoute
* AdminRoute
* FindById
* FindByBalance
* AddCustomer
* UpdateCustomer
* CustomerDetails
* AccountDetails
* FindAccountsByCustomer
* FindAccountsByCustomerName

Run tests:

```
npm run test
```

Run coverage:

```
npm run coverage
```

Coverage ~50%

---

## How to Run Project

### 1. Start Auth Service

```
cd auth-service
mvn spring-boot:run
```

Runs on:

```
http://localhost:9090
```

---

### 2. Start Backend API

```
cd backend
mvn spring-boot:run
```

Runs on:

```
http://localhost:8080
```

---

### 3. Start Frontend

```
cd bank-frontend
npm install
npm run dev
```

Runs on:

```
http://localhost:5173
```

---

## Login Example

```
username: tarunjeet91
password: *********
```

---

## Optional Features Implemented

* Pagination
* Role-based sidebar
* Alerts
* JWT auth service
* Protected routes
* Admin routes
* Bootstrap carousel
* Test coverage

---

## Author

Tarunjeet Singh
Spring Boot + React Full Stack Project
