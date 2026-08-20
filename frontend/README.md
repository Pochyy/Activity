# Activity 1 — React Frontend (Registration / Login / Dashboard)

A ReactJS + Vite frontend that connects to the existing Spring Boot backend
(`edu.cit.lariosa.activity1`).

## Why you don't need `npm create vite`

This folder is already a complete Vite + React project (this is what
`npm create vite` would have generated). You hit an npm error running
`create-vite` from a mapped network drive (`Z:\...`), which is a common
Windows/npm issue — file system operations like symlinks/temp writes often
fail on network shares. To avoid it entirely, just use this folder directly.

If you ever do need to run `create-vite` yourself, do it from a local drive
(e.g. `C:\Users\<you>\Desktop\...`), not `Z:\`.

## Setup

1. Copy this `frontend` folder to your local machine (not the `Z:` drive —
   use `C:\Users\l23y19w23\Documents\...` or similar).
2. Open a terminal in the `frontend` folder.
3. Install dependencies:
   ```
   npm install
   ```
4. (Optional) Copy `.env.example` to `.env` if your backend runs on a
   different host/port than `http://localhost:8080`:
   ```
   copy .env.example .env
   ```
5. Start the dev server:
   ```
   npm run dev
   ```
6. Open the printed URL (default `http://localhost:5173`).

Make sure your Spring Boot backend is running (default `http://localhost:8080`)
before testing the forms.

## IMPORTANT: Enable CORS on the backend

Your `UserController` currently has no CORS configuration. Since the React
dev server runs on a different port (`5173`) than Spring Boot (`8080`), the
browser will block requests with a CORS error unless you allow it.

Add this to your controller (quickest fix):

```java
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    ...
}
```

Or, for a project-wide config, add a `WebMvcConfigurer` bean:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
```

## Project structure

```
frontend/
├── src/
│   ├── api/
│   │   ├── authApi.js      # All backend calls live here (axios)
│   │   └── session.js      # Stores logged-in user in sessionStorage (no password)
│   ├── components/
│   │   └── Navbar.jsx      # Navigation between Login / Register / Dashboard
│   ├── pages/
│   │   ├── Register.jsx
│   │   ├── Login.jsx
│   │   └── Dashboard.jsx
│   ├── App.jsx              # Routes
│   ├── main.jsx
│   └── index.css
├── index.html
├── vite.config.js
└── package.json
```

## Security notes

- Passwords are never stored in `localStorage`/`sessionStorage`. Only
  `{ id, username }` is kept in `sessionStorage` after login/registration —
  the password field is stripped from every API response before it touches
  app state (see `sanitizeUser` in `src/api/authApi.js`).
- **Backend note:** your current `User` entity stores passwords in plain
  text in the database and returns them in the JSON response body. That's a
  backend-side issue outside the scope of this frontend — if you want to
  harden it later, hash passwords with `BCryptPasswordEncoder` in the
  controller/service before saving, and exclude the password field from
  the response (e.g. with `@JsonIgnore` or a separate response DTO).

## Suggested commit sequence

```
git init
git add .env.example .gitignore package.json vite.config.js index.html src/main.jsx src/index.css
git commit -m "chore: scaffold Vite React project"

git add src/api/authApi.js src/api/session.js
git commit -m "feat: API integration layer for register/login endpoints"

git add src/pages/Register.jsx
git commit -m "feat: registration page with client-side validation"

git add src/pages/Login.jsx
git commit -m "feat: login page with client-side validation"

git add src/pages/Dashboard.jsx
git commit -m "feat: dashboard page for authenticated users"

git add src/components/Navbar.jsx src/App.jsx
git commit -m "feat: routing and navigation between login/register/dashboard"
```
