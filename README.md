# ApiFactory

This is a RESTful API built with Spring Boot that manages clients and their contracts. It supports different types of clients (Person, Company), contract creation, updates, filtering, and summary endpoints with validation and ISO 8601-compliant date formats.

---

## Features

- Create, read, update, and delete clients

- Create contracts for clients (with optional start/end dates)

- Automatic update of contract end date when client is deleted

- Automatic management of updateDate on contract cost changes

- Filter active contracts by update date

- Return total active contract cost per client (optimized)

##  Technologies

- JDK 21
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database (file-based)
- Lombok (optional)
- Bean Validation (JSR-380)
- Maven

---

###  Prerequisites

- JDK 21
- Maven
- IDE (VS Code, IntelliJ, Eclipse)
- Postman or curl (for testing)

---
### Running the Application

Clone the project and run:

```bash
git clone https://github.com/Farinha2023/ApiFactory.git
cd contract-api
./mvnw spring-boot:run
```

Once started, the application will be accessible at:

```bash
http://localhost:8080/clients (may be empty at first)

```
---
### Architecture & Design Explanation

The application follows a layered architecture to promote separation of concerns and maintainability:

- domain: contains core business entities such as Client and Contract.

- dto: defines data transfer objects used to expose or receive only necessary data in API requests/responses.

- mapper: handles conversion between domain models and DTOs to maintain decoupling between internal logic and external representation.

- repository: defines interfaces for data access using Spring Data JPA.

- service: encapsulates business logic, such as validations and domain rules.

- controller: exposes RESTful endpoints and handles incoming HTTP requests.

An H2 database is used in file mode to enable lightweight, persistent data storage suitable for local development and testing. It requires no installation or additional configuration for users cloning the project. The database files (`ApiDB.mv.db`, `ApiDB.lock.db`) are stored in the project directory. Deleting these files resets the stored data.

---

## 📬 Postman Collection

The file `Postman_Collection.postman_collection.json` included in this repository contains a full set of HTTP requests corresponding to all API operations. It can be imported into Postman to facilitate testing and exploration of the API.

---


## ✅ API Reliability and Proof of Functionality

This API was designed following standard RESTful principles and layered architecture, ensuring clear separation of concerns and maintainability. It has been tested using unit and integration tests, as well as manual testing with tools like Postman and curl.

Data validation is enforced at multiple layers, including input DTO validation and business rules in the service layer. The use of Spring Data JPA and the H2 database ensures reliable data persistence.

Additionally, the automatic handling of contract end dates upon client deletion and the internal update of contract modification dates demonstrate the API’s adherence to the specified business logic.

Collectively, these factors confirm that the API functions correctly according to the requirements.

### ✅ Added `ApiFactoryApplicationTests` for End-to-End API Validation

This test class (`ApiFactoryApplicationTests`) was added to perform **comprehensive integration testing** of the REST API using Spring Boot’s `MockMvc`. It simulates real client-server interactions without starting an external server, ensuring that all endpoints behave correctly and data flows properly between layers.

The tests follow a logical sequence (using `@TestMethodOrder`) to replicate a real-world usage scenario:
1. **Create Clients** – Tests both `PERSON` and `COMPANY` creation with validation of required fields.
2. **Update Clients** – Verifies that immutable fields (`birthDate`, `companyIdentifier`) are not overwritten.
3. **Create and Manage Contracts** – Tests contract creation, updates to cost (with automatic update date refresh), and filtering of active vs. expired contracts.
4. **Aggregate Endpoint** – Ensures the performance endpoint correctly sums active contract costs.
5. **Validation Checks** – Includes negative test cases for invalid phone numbers, emails, negative costs, and missing fields.
6. **Delete Clients** – Confirms that deleting a client automatically ends all their contracts and resets related totals.

By adding this class, we can automatically verify that the **domain rules, validation constraints, and business logic** are correctly enforced at the API level. This approach improves reliability, prevents regression, and provides confidence before deployment.
