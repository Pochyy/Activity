# API Data Contract — React ↔ Spring Boot

Base URL: `http://localhost:8080/api`

All request/response bodies are JSON. Protected endpoints require:

```
Authorization: Bearer <jwt>
```

---

## POST /api/register

**Purpose:** Create a new user account.

**Auth required:** No

**Request headers:** `Content-Type: application/json`

**Request body:**

| Field    | Type   | Required |
|----------|--------|----------|
| username | string | yes |
| password | string | yes |

**Success response:** `200 OK`
```json
{ "id": 1, "username": "vinzent" }
```

**Error responses:**
- `400 Bad Request` — `{ "message": "Username already exists" }`
- `400 Bad Request` — `{ "message": "Username and password are required" }`

**Sample request:**
```json
POST /api/register
{ "username": "vinzent", "password": "password123" }
```

---

## POST /api/login

**Purpose:** Authenticate a user and issue a JWT.

**Auth required:** No

**Request headers:** `Content-Type: application/json`

**Request body:**

| Field    | Type   | Required |
|----------|--------|----------|
| username | string | yes |
| password | string | yes |

**Success response:** `200 OK`
```json
{ "token": "eyJhbGciOi...", "userId": 1, "username": "vinzent" }
```

**Error responses:**
- `401 Unauthorized` — `{ "message": "Invalid username or password" }`

---

## POST /api/requests

**Purpose:** Create a service request owned by the authenticated user.

**Auth required:** Yes

**Request headers:** `Content-Type: application/json`, `Authorization: Bearer <jwt>`

**Request body:**

| Field       | Type   | Required |
|-------------|--------|----------|
| title       | string | yes |
| description | string | no |
| category    | string | yes |

Note: no `userId`/`createdBy` field — ownership is derived from the JWT on the backend.

**Success response:** `200 OK`
```json
{
  "id": 10,
  "title": "Broken faucet",
  "description": "Leaking under the sink",
  "category": "Maintenance",
  "dateCreated": "2026-08-27T10:15:00",
  "createdBy": "vinzent"
}
```

**Error responses:**
- `400 Bad Request` — `{ "message": "Title is required" }`
- `401 Unauthorized` — missing/invalid/expired JWT

---

## GET /api/requests

**Purpose:** List all service requests belonging to the authenticated user.

**Auth required:** Yes

**Request headers:** `Authorization: Bearer <jwt>`

**Success response:** `200 OK`
```json
[
  {
    "id": 10,
    "title": "Broken faucet",
    "description": "Leaking under the sink",
    "category": "Maintenance",
    "dateCreated": "2026-08-27T10:15:00",
    "createdBy": "vinzent"
  }
]
```

**Error responses:**
- `401 Unauthorized` — missing/invalid/expired JWT

---

## GET /api/requests/{id}

**Purpose:** Get one service request by id — only if it belongs to the authenticated user.

**Auth required:** Yes

**Request headers:** `Authorization: Bearer <jwt>`

**Success response:** `200 OK` — same shape as above (single object)

**Error responses:**
- `401 Unauthorized` — missing/invalid/expired JWT
- `404 Not Found` — `{ "message": "Service request not found" }` — returned both
  when the id doesn't exist **and** when it belongs to another user, so a
  caller cannot distinguish the two cases.

---

## PUT /api/requests/{id}

**Purpose:** Update a service request — only if it belongs to the authenticated user.

**Auth required:** Yes

**Request headers:** `Content-Type: application/json`, `Authorization: Bearer <jwt>`

**Request body:** same shape as `POST /api/requests`

**Success response:** `200 OK` — updated object

**Error responses:**
- `401 Unauthorized` — missing/invalid/expired JWT
- `404 Not Found` — `{ "message": "Service request not found" }`

---

## DELETE /api/requests/{id}

**Purpose:** Delete a service request — only if it belongs to the authenticated user.

**Auth required:** Yes

**Request headers:** `Authorization: Bearer <jwt>`

**Success response:** `204 No Content`

**Error responses:**
- `401 Unauthorized` — missing/invalid/expired JWT
- `404 Not Found` — `{ "message": "Service request not found" }`
