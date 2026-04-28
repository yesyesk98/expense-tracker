# Expense Tracker — Overview & Design

A personal expense tracking application built incrementally as a Spring Boot learning project.


## Urgent Next Do

These are immediate next actions (not long-term milestones):

1. Add a steering document for low-token, high-signal prompting.
2. Add Postman collection/environment for quick API testing.
3. Practice Java Streams API and useful collections in service layer logic.
4. Start Kafka integration spike for expense event publishing.


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
| 6     | React (Frontend)                           | UI for managing expenses                        |
| 7     | Python, FastAPI, LangChain                 | AI chatbot for natural language expense mgmt    |
| 8+    | Redis, Scheduling, Reporting               | Caching, automated summaries                    |

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

##### Quick curl test (current implemented endpoint)

Use this for the currently implemented `POST /api/expenses` endpoint:

```bash
curl -i -X POST "http://localhost:8080/api/expenses" \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Lunch",
    "amount": 12.50,
    "category": "FOOD",
    "expenseDate": "2026-04-27",
    "notes": "Team lunch"
  }'
```

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

### Phase 6 — React Frontend

Learn: React fundamentals, component-based UI, `fetch`/`axios` for API calls, React Router, state management. Build a single-page app that talks to the Spring Boot REST API.

#### Pages

- Dashboard — overview of spending with totals by category
- Expense List — table of all expenses with filtering by category and date range
- Add/Edit Expense — form to create or update an expense

#### Tech

- React 18+ with functional components and hooks
- Vite for project scaffolding and dev server
- Axios or fetch for HTTP calls to `http://localhost:8080/api/expenses`
- Spring Boot will need CORS configuration to allow requests from the React dev server (typically `http://localhost:5173`)

#### Project Structure

```
expense-tracker-ui/        (separate project, sibling to expense-tracker/)
├── package.json
├── src/
│   ├── App.jsx
│   ├── components/
│   │   ├── ExpenseList.jsx
│   │   ├── ExpenseForm.jsx
│   │   └── Dashboard.jsx
│   └── services/
│       └── expenseApi.js  (API call functions)
└── index.html
```

### Phase 7 — AI Chatbot (Python Microservice)

Learn: Python, FastAPI, LangChain, LLM function calling, microservice communication.

Build a separate Python service that lets users manage expenses through natural language conversation.

#### Architecture

```
React chat UI → Python chatbot service (FastAPI) → Spring Boot REST API → Database
```

#### Tech Stack

- **FastAPI** — Python web framework for the chatbot API (`/chat` endpoint)
- **LangChain** — LLM orchestration framework (prompt management, tool/function calling, conversation memory)
- **OpenAI Python SDK** or **Ollama** — LLM provider (Ollama for free local inference)
- **httpx** — HTTP client for calling Spring Boot REST APIs from Python

#### Project Structure

```
expense-tracker-chatbot/
├── requirements.txt
├── main.py              (FastAPI app)
├── chat/
│   ├── agent.py         (LangChain agent with tools)
│   └── tools.py         (tool definitions that call Spring Boot APIs)
└── config.py            (API URLs, LLM settings)
```

#### How It Works

1. User sends a chat message (e.g., "I spent $30 on lunch today")
2. FastAPI receives the message, passes it to LangChain agent
3. LangChain + LLM decides which tool to call (create_expense, get_expenses, get_total, etc.)
4. The tool makes an HTTP request to the Spring Boot REST API
5. LangChain formats the LLM response and returns it to the user

#### Capabilities

- Create expenses via natural language ("Spent $45 on groceries yesterday")
- Query spending ("How much did I spend on food this month?")
- List and filter expenses ("Show me all transport expenses from last week")
- Conversation memory for follow-up questions ("What about entertainment?")

### Phase 8+ — Advanced Features

- Redis caching for summaries (`@Cacheable`)
- `@Scheduled` monthly reports
- Pagination & sorting
- Docker Compose for local dev environment


