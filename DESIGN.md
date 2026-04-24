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
