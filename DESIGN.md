# Expense Tracker — Overview & Design

A personal expense tracking application built incrementally as a Spring Boot learning project.

## Goal

Build a functional expense tracker while learning Spring Boot concepts hands-on — starting simple and layering in technologies like Kafka, security, caching, etc. over time.

## Tech Stack (Evolving)

| Phase | Technology                                 | Purpose                                         |
| ----- | ------------------------------------------ | ----------------------------------------------- |
| 1     | Spring Boot 3.x, H2, Spring Data JPA       | Core CRUD + persistence                         |
| 2     | Bean Validation, Global Exception Handling | Input validation + error handling               |
| 3     | Apache Kafka                               | Event streaming (expense events, budget alerts) |
| 4     | Spring Security                            | Authentication & authorization                  |
| 5     | PostgreSQL                                 | Production-grade database                       |
| 6+    | Redis, Scheduling, Reporting               | Caching, automated summaries                    |

## Domain Model

### Expense

- `id` — auto-generated primary key
- `description` — what the expense was for
- `amount` — decimal value (BigDecimal)
- `category` — enum (FOOD, TRANSPORT, HOUSING, UTILITIES, ENTERTAINMENT, HEALTH, SHOPPING, EDUCATION, TRAVEL, OTHER)
- `expenseDate` — when the expense occurred
- `notes` — optional free-text
- `createdAt` / `updatedAt` — audit timestamps

## API Design

### Core CRUD

| Method | Endpoint             | Description       |
| ------ | -------------------- | ----------------- |
| POST   | `/api/expenses`      | Create an expense |
| GET    | `/api/expenses`      | List all expenses |
| GET    | `/api/expenses/{id}` | Get expense by ID |
| PUT    | `/api/expenses/{id}` | Update an expense |
| DELETE | `/api/expenses/{id}` | Delete an expense |

### Filtering & Aggregation

| Method | Endpoint                                           | Description          |
| ------ | -------------------------------------------------- | -------------------- |
| GET    | `/api/expenses/category/{category}`                | Filter by category   |
| GET    | `/api/expenses/date-range?start=...&end=...`       | Filter by date range |
| GET    | `/api/expenses/total/category/{category}`          | Sum by category      |
| GET    | `/api/expenses/total/date-range?start=...&end=...` | Sum by date range    |

## Project Structure

```
expense-tracker/
├── pom.xml
├── src/main/java/com/example/expensetracker/
│   ├── ExpenseTrackerApplication.java
│   ├── model/
│   │   ├── Expense.java
│   │   └── Category.java          (enum)
│   ├── dto/
│   │   ├── ExpenseRequest.java     (input validation)
│   │   └── ExpenseResponse.java
│   ├── repository/
│   │   └── ExpenseRepository.java  (Spring Data JPA)
│   ├── service/
│   │   └── ExpenseService.java     (business logic)
│   ├── controller/
│   │   ├── ExpenseController.java  (REST endpoints)
│   │   └── GlobalExceptionHandler.java
│   └── exception/
│       └── ResourceNotFoundException.java
├── src/main/resources/
│   └── application.yml
└── src/test/
```

## Phase Details

### Phase 1 — Core CRUD

Learn: `@RestController`, `@Service`, `@Repository`, `@Entity`, Spring Data JPA, H2 console, `application.yml` configuration.

### Phase 1 Controller Implementation Guide (Do It Yourself)

Goal: implement `ExpenseController` yourself and understand the responsibility split between Controller, Service, and Repository.

#### 1) What the controller should and should not do

- Should:
  - expose REST endpoints (`/api/expenses`)
  - validate incoming request payloads (`@Valid`)
  - convert HTTP inputs (path/query/body) into service calls
  - return proper HTTP status codes
- Should not:
  - contain database queries
  - contain complex business rules
  - directly use JPA in controller methods

Keep controller thin: request in -> service call -> response out.

#### 2) Build order (recommended)

1. Create `ExpenseController` with `@RestController` and `@RequestMapping("/api/expenses")`.
2. Inject `ExpenseService` through constructor injection.
3. Add one endpoint at a time in this order:
   - `POST /api/expenses`
   - `GET /api/expenses`
   - `GET /api/expenses/{id}`
   - `PUT /api/expenses/{id}`
   - `DELETE /api/expenses/{id}`
4. Use `@Valid` on request DTO input and return DTOs (not entity objects directly).
5. Verify each endpoint using Postman or curl before moving to the next one.

#### 3) Endpoint checklist

For each endpoint, confirm:
- mapping annotation is correct (`@PostMapping`, `@GetMapping`, etc.)
- request body/path variable annotations are present
- expected status code is returned:
  - create -> `201 Created`
  - fetch/list/update -> `200 OK`
  - delete -> `204 No Content`
- error path is covered (invalid input, ID not found)

#### 4) Practice flow (hands-on learning loop)

For each endpoint:
1. Write method signature in controller.
2. Call a service method that does not exist yet.
3. Implement that service method.
4. Run app and test endpoint.
5. Refactor naming/response structure if needed.

This loop helps you learn by creating small compile-time and runtime feedback cycles.

#### 5) Testing strategy while learning

- Start with manual API testing (Postman/curl) to understand request/response behavior.
- Then add `MockMvc` tests for controller:
  - one success test per endpoint
  - one validation failure test
  - one resource-not-found test

#### 6) Suggested resources

- Spring REST Controllers (official guide): https://spring.io/guides/gs/rest-service/
- Building a RESTful Web Service with Spring Boot: https://spring.io/guides/tutorials/rest/
- Spring Web annotations reference (`@RestController`, `@RequestMapping`, etc.): https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html
- Validation in Spring (`@Valid`, Bean Validation): https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html
- ResponseEntity usage (status + body control): https://www.baeldung.com/spring-response-entity
- MockMvc testing reference: https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-framework.html

#### 7) Definition of done for your controller

- All 5 CRUD endpoints are implemented and manually tested.
- Validation errors return meaningful messages.
- Not-found IDs return correct error status.
- Controller has no repository usage (service-only dependency).
- Basic controller tests pass.

### Phase 2 — Validation & Error Handling

Learn: Bean Validation (`@Valid`, `@NotNull`, `@DecimalMin`), `@RestControllerAdvice`, custom exceptions, proper HTTP status codes.

### Phase 3 — Kafka Integration

Learn: `spring-kafka`, `KafkaTemplate`, `@KafkaListener`, event-driven architecture. Publish expense-created/updated/deleted events. Build a consumer that checks budget thresholds and triggers alerts.

### Phase 4 — Spring Security

Learn: Authentication, authorization, JWT tokens, securing endpoints, user-specific expense data.

### Phase 5 — PostgreSQL

Learn: Database migration (Flyway or Liquibase), Spring profiles for dev vs prod datasources, connection pooling (HikariCP).

### Phase 6+ — Advanced Features

- Redis caching for summaries (`@Cacheable`)
- `@Scheduled` monthly reports
- Pagination & sorting
- Docker Compose for local dev environment
