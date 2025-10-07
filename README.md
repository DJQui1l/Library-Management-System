# 📚 Library Management System (Java + MySQL)

A simple **Library Management System** built with **Java (Swing + JDBC)** and **MySQL**, allowing basic operations such as managing books, authors, categories, and issuing books to members.

---

## 🧩 Features
- ✅ Admin Login
- 📘 Manage Books
- 🧑‍💼 Manage Authors
- 🏷️ Manage Categories
- 🏢 Manage Publishers
- 📅 Issue and Return Books
- 💾 MySQL Database Integration via JDBC

---

## ⚙️ Prerequisites
Make sure you have the following installed:

| Component | Description |
|------------|-------------|
| **Java JDK** | Version 11 or later |
| **IDE** | Eclipse, IntelliJ IDEA, or NetBeans |
| **MySQL Server** | Version 8.0 or later |
| **MySQL Connector/J** | JDBC driver `.jar` file for database connection |

Download MySQL Connector/J here:
👉 [https://dev.mysql.com/downloads/connector/j/](https://dev.mysql.com/downloads/connector/j/)

---

## 🏗️ Project Setup

### 1. Create the Java Project
1. Open your IDE.
2. Create a new Java Project named **LibraryManagement**.
3. Inside the `src` folder, create a package named `library`.
4. Add the MySQL Connector/J `.jar` to your project classpath.
   *(In Eclipse: `Project → Properties → Java Build Path → Libraries → Add External JAR`)*

---

## 🗄️ Database Setup

### 1. Create Database
```sql
CREATE DATABASE library;
USE library;