# 📘 API Test Project – Expand Testing Notes API

This repository contains API tests for the [Expand Testing Notes API](https://practice.expandtesting.com/notes/api/api-docs) using Java and RestAssured.  
The project was created to practice REST API test automation, organize reusable test components, and demonstrate structured test design.

---

## 🛠 Technologies Used

- **Java** – JDK 23.0.2 (may work with JDK 11+, not verified)
- **RestAssured** – for HTTP requests and response validation
- **TestNG** – for test execution
- **Maven** – for dependency management and build

---

## 📁 Project Structure

src/
├── main/
│   └── java/
│       ├── helpers/         # API endpoints, token/auth helpers, test data
│       └── models/          # POJOs for API responses
├── test/
│   └── java/
│       ├── notes/           # Tests for CRUD operations on notes
│       └── users/           # Tests for user login and profile operations

## 🚀 Running the Tests

### Prerequisites:

* JDK 11 or higher (developed with JDK 23.0.2)
* Maven
* Internet connection (tests run against a live public API)

To run all tests, execute:

```mvn clean test```

### 🔐 Authentication

Tests that require a valid user session use a generated token that is cached during runtime to avoid redundant login requests.  
The token is generated using the `AuthHelper` class, which retrieves credentials from a `user_info.properties` file and sends a login request.

Credentials and the base URL are loaded using Java’s `ResourceBundle`, and sensitive information is not hardcoded in the test logic.

---

### 📝 Notes & Project Insights

#### 🔹 Purpose

This project was created to practice REST API test automation using Java and popular open-source tools.  
It helps me explore clean test design, modular structure, and reusable components for real-world readiness.

#### 🔹 Test Design Choices

- Each HTTP method (GET, POST, PATCH, PUT, DELETE) is covered by a separate test class for clarity and separation of concerns
- The `AuthHelper` handles login and token caching, reducing unnecessary requests during test execution
- Negative and edge case data is extracted into a helper class (`InvalidTestData`) for reusability and easier updates
- Response models (POJOs) are used to deserialize and validate responses more clearly