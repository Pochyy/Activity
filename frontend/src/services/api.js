const BASE_URL = "http://localhost:8080/api";

// Central fetch wrapper. Attaches the JWT via the Authorization: Bearer
// header on every request when a token exists. On a 401 (expired/invalid
// token) it clears the stored session so the app can redirect to /login.
async function request(path, { method = "GET", body, auth = true } = {}) {
  const headers = { "Content-Type": "application/json" };

  if (auth) {
    const token = localStorage.getItem("token");
    if (token) {
      headers["Authorization"] = `Bearer ${token}`;
    }
  }

  const response = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });

  if (response.status === 401) {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("userId");
  }

  let data = null;
  const text = await response.text();
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = text;
    }
  }

  if (!response.ok) {
    const message =
      (data && data.message) ||
      (typeof data === "string" ? data : null) ||
      `Request failed (${response.status})`;
    throw new Error(message);
  }

  return data;
}

export const authApi = {
  register: (username, password) =>
    request("/register", { method: "POST", body: { username, password }, auth: false }),
  login: (username, password) =>
    request("/login", { method: "POST", body: { username, password }, auth: false }),
};

export const serviceRequestApi = {
  list: () => request("/requests"),
  get: (id) => request(`/requests/${id}`),
  create: (payload) => request("/requests", { method: "POST", body: payload }),
  update: (id, payload) => request(`/requests/${id}`, { method: "PUT", body: payload }),
  remove: (id) => request(`/requests/${id}`, { method: "DELETE" }),
};
