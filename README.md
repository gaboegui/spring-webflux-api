# Spring WebFlux REST API

A reactive RESTful API implementation using **Spring Boot 3** and **Spring WebFlux**. This project demonstrates two different programming models supported by WebFlux: the standard **Annotation-based** model and the **Functional** programming model.

The application manages **Products** and **Categories**, providing reactive CRUD operations and handling file uploads, backed by **MongoDB**.

## 🚀 Features

*   **Reactive Stack**: Fully non-blocking architecture using Reactor (Mono/Flux).
*   **Dual Implementation**:
    *   **Annotation-based**: Traditional `@RestController` approach (similar to Spring MVC).
    *   **Functional Endpoints**: Lambda-based routing using `RouterFunction` and `HandlerFunction`.
*   **MongoDB Integration**: Reactive data persistence with `ReactiveMongoRepository`.
*   **File Uploads**: Handling multipart requests for product images.
*   **Validation**: Bean validation for input data.
*   **Data Seeding**: Automatic population of sample data on startup.

## 🛠️ Tech Stack

*   **Java 17**
*   **Spring Boot 3.5.x**
*   **Spring WebFlux**
*   **Spring Data MongoDB (Reactive)**
*   **Maven**

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

*   **Java Development Kit (JDK) 17** or higher.
*   **Maven**.
*   **MongoDB**: An instance running locally on the default port `27017`.

## ⚙️ Configuration

The application configuration is located in `src/main/resources/application.properties`.

**Key Settings:**
*   **MongoDB URI**: `mongodb://localhost:27017/product_db`
*   **Upload Directory**: `config.upload.path=D://uploads//`
    *   *Note: Please update the `config.upload.path` to a valid directory on your machine before testing file uploads.*

## 🏃‍♂️ How to Run

1.  **Clone the repository**:
    ```bash
    git clone params...
    ```
2.  **Navigate to the project directory**.
3.  **Run the application** using Maven:
    ```bash
    mvn spring-boot:run
    ```

The server will start (default port is usually `8080`).

## 🔌 API Endpoints

The API exposes two sets of endpoints for the same functionality to demonstrate both coding styles.

### V1 - Annotation-Based (`/api/products`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/products` | List all products. |
| `GET` | `/api/products/{id}` | Get a product by ID. |
| `POST` | `/api/products` | Create a new product (JSON). |
| `PUT` | `/api/products/{id}` | Update a product. |
| `DELETE` | `/api/products/{id}` | Delete a product. |
| `POST` | `/api/products/upload/{id}`| Upload an image for a product. |
| `POST` | `/api/products/v1` | Create product with image (Multipart). |

### V2 - Functional Endpoints (`/api/v2/products`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v2/products` | List all products. |
| `GET` | `/api/v2/products/{id}` | Get a product by ID. |
| `POST` | `/api/v2/products` | Create a new product (JSON). |
| `PUT` | `/api/v2/products/{id}` | Update a product. |
| `DELETE` | `/api/v2/products/{id}` | Delete a product. |
| `POST` | `/api/v2/products/upload/{id}` | Upload an image for a product. |
| `POST` | `/api/v2/products/createWithPic` | Create product with image (Multipart). |



## 👨‍💻 Author

**Gabriel Eguiguren P.**

[![Gabriel Eguiguren P. X](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://x.com/GaBoEgui)
[![Gabriel Eguiguren P. Linkedin](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/gabrieleguiguren/)

### Donations or Support

If you found this project helpful, feel free to follow me or make a donation!

**ETH/Arbitrum/Optimism/Polygon/BSC/etc Address:** `0x2210C9bD79D0619C5d455523b260cc231f1C2F0D`
