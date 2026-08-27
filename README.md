# Activity 1 — Authenticated Service Request Module

Spring Boot + Spring Security (JWT) backend, ReactJS + Vite frontend.

## Prerequisites

- Java 17+
- Maven
- Node.js ^20.19.0 or >=22.12.0, npm
- MariaDB running locally, with a database named `activity1`

## Backend Setup

1. Add the dependencies in `backend/pom-dependencies-to-add.xml` to your project's `pom.xml`.
2. Copy `backend/application.properties` into `src/main/resources/application.properties`
   (or merge the `jwt.secret` / `jwt.expiration-ms` lines into your existing file).
   Replace `jwt.secret` with your own generated value before deploying anywhere public.
3. Copy the Java source files from `backend/src/main/java/edu/cit/lariosa/activity1/`
   into your existing project at the same package path, overwriting `User.java` and
   `UserController.java`.
4. Run the backend:
   ```
   mvn spring-boot:run
   ```
   It starts on `http://localhost:8080`.

   Note: `spring.jpa.hibernate.ddl-auto=update` will automatically add the new
   `role` column to `users` and create the `service_requests` table.

## Frontend Setup

1. Copy the files from `frontend/src/` into your existing `frontend/src/` folder,
   overwriting `App.jsx`, `main.jsx`, and the `pages/` files listed.
2. Install React Router if not already installed:
   ```
   npm install react-router-dom
   ```
3. Run the frontend:
   ```
   npm run dev
   ```
   It starts on `http://localhost:5173`.

## Testing Ownership Enforcement

1. Register two accounts (User A, User B) via `/register`.
2. Log in as User A, create a Service Request, note its `id` from the response
   or from the My Service Requests page.
3. Log in as User B (different browser tab/profile or after logging out).
4. Try `GET /api/requests/{id}` for User A's request using User B's JWT
   (e.g. with curl or Postman) — expect `404 Not Found`.
5. Try `PUT` / `DELETE` on the same id as User B — expect `404 Not Found`.

See `API_DOCUMENTATION.md` for the full endpoint contract.
