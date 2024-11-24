
# Bank Application

A Spring Boot application for managing user accounts and exchanging currency between PLN and USD. This application provides RESTful APIs for account creation, retrieval, and currency exchange.

---

## Table of Contents
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [Endpoints](#endpoints)
  - [GET /getAccount/{id}](#get-getaccountid)
  - [POST /createAccount](#post-createaccount)
  - [POST /exchangeFromPln](#post-exchangefrompln)
  - [POST /exchangeFromUsd](#post-exchangefromusd)

---

## Technologies Used
- **Java**
- **Spring Boot**
- **Maven**
- **H2 Database**

---

## Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/boczarpiotr/bankApp.git
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. The application will be accessible at `http://localhost:8081`.

---

## Endpoints

### 1. GET `/getAccount/{id}`
Retrieve details of an account by its ID.

- **Request**:
  - **Method**: `GET`
  - **Path Variable**: 
    - `id` (Long) - The ID of the account to retrieve.
  - **Produces**: `application/json`
  
- **Response**:
  - **200 OK**: Returns account details.
  - **404 Not Found**: If no account is found for the given ID.
  
- **Example**:
  ```bash
  curl -X GET http://localhost:8081/getAccount/1
  ```

---

### 2. POST `/createAccount`
Create a new account.

- **Request**:
  - **Method**: `POST`
  - **Consumes**: `application/json`
  - **Produces**: `application/json`
  - **Body**:
    ```json
    {
      "name": "Piotr",
      "surname": "Kowalski",
      "initialBalancePln": 100
    }
    ```
  
- **Response**:
  - **201 Created**: Account successfully created.
  - **400 Bad Request**: If the request data is invalid.

- **Example**:
  ```bash
  curl -X POST http://localhost:8081/createAccount        -H "Content-Type: application/json"        -d '{"name":"Piotr","surname":"Kowalski","initialBalancePln":100}'
  ```

---

### 3. POST `/exchangeFromPln`
Exchange a specified amount of PLN to USD.

- **Request**:
  - **Method**: `POST`
  - **Consumes**: `application/json`
  - **Produces**: `application/json`
  - **Body**:
    ```json
    {
      "id": 1,
      "amountForExchange": 50.0
    }
    ```
  
- **Response**:
  - **200 OK**: Exchange successful.
  - **400 Bad Request**: If the request data is invalid or insufficient funds.

- **Example**:
  ```bash
  curl -X POST http://localhost:8081/exchangeFromPln        -H "Content-Type: application/json"        -d '{"id":1,"amountForExchange":50.0}'
  ```

---

### 4. POST `/exchangeFromUsd`
Exchange a specified amount of USD to PLN.

- **Request**:
  - **Method**: `POST`
  - **Consumes**: `application/json`
  - **Produces**: `application/json`
  - **Body**:
    ```json
    {
      "id": 1,
      "amountForExchange": 20.0
    }
    ```
  
- **Response**:
  - **200 OK**: Exchange successful.
  - **400 Bad Request**: If the request data is invalid or insufficient funds.

- **Example**:
  ```bash
  curl -X POST http://localhost:8081/exchangeFromUsd        -H "Content-Type: application/json"        -d '{"id":1,"amountForExchange":20.0}'
  ```

---

## Notes
- Ensure the application is connected to the database correctly.
- Default port: `8081`. Change it in `application.properties` if needed.

---

## Author
Piotr Boczar
