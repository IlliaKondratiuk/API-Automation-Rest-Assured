# 📘 API Test Project – Expand Testing Notes API

This repository contains API tests for the [Expand Testing Notes API](https://practice.expandtesting.com/notes/api/api-docs) using Java and RestAssured.  
The project was created to practice REST API test automation, organize reusable test components, and demonstrate structured test design.

---

## 🛠 Technologies Used

- **Java** – JDK 23.0.2 (may work with JDK 11+, not verified)
- **Maven** – for dependency management and building
- **RestAssured** – for HTTP requests and response validation
- **TestNG** – for test execution(including parallel execution)
- **SLF4J** - test logging
- **Allure** - reporting

---

## 📁 Project Structure

```text
src/
├── main/
│   └── java/
│       ├── helpers/         # Token generation, endpoint constants, and test data
│       │   ├── ApiEndpoints.java
│       │   ├── AuthHelper.java
│       │   └── InvalidTestData.java
│       └── models/          # POJOs for deserializing API responses
│           ├── Note.java
│           └── GetNotesResponse.java
├── test/
│   └── java/
│       ├── notes/           # CRUD tests for notes
│       │   ├── GetNotesTest.java
│       │   ├── PostNoteTest.java
│       │   ├── PatchNoteTest.java
│       │   ├── PutNoteTest.java
│       │   └── DeleteNoteTest.java
│       └── users/           # User-related tests
│           ├── LoginTest.java
│           └── ProfileTests.java
```

### 📄 Folder Descriptions

- `helpers/` – Contains utility classes for handling authentication, API route management, and invalid test input data
- `models/` – Contains plain Java objects (POJOs) used to deserialize JSON responses into Java objects
- `notes/` – Each class tests a specific HTTP operation (GET, POST, PUT, PATCH, DELETE) on the Notes resource. I did not overload the project with all possible operations as they were not different from each other and would not bring anything new in terms of learning/demonstation.
- `users/` – Includes tests for login and profile-related endpoints


## 🚀 Running the Tests

### Prerequisites:

* JDK 11 or higher (developed with JDK 23.0.2)
* Maven
* Allure CLI
* Internet connection (tests run against a live public API)

To run all tests, execute:

```mvn clean test```

To see the generated Allure reports, execute:

```allure serve```

### 🔐 Authentication

* Tests that require a valid user session use a generated token that is cached during runtime to avoid redundant login requests.  
* The token is generated using the `AuthHelper` class, which retrieves credentials from a `user_info.properties` file and sends a login request.
* Credentials and the base URL are loaded using Java’s `ResourceBundle`, and sensitive information is not hardcoded in the test logic.

---

### 📝 Notes & Project Insights

#### 🔹 Purpose

This project was created to practice REST API test automation using Java and popular open-source tools.  
It helps me explore clean test design, modular structure, and reusable components for learning and demonstration purposes. 

#### 🔹 Test Design Choices

- Each HTTP method (GET, POST, PATCH, PUT, DELETE) is covered by a separate test class for clarity and separation of concerns
- The `AuthHelper` handles login and token caching, reducing unnecessary requests during test execution
- Negative and edge case data is extracted into a helper class (`InvalidTestData`) for reusability and easier updates
- Response models (POJOs) are used to deserialize and validate responses more clearly