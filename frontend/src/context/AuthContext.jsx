import { createContext, useContext, useState, useCallback } from "react";

const AuthContext = createContext(null);

// Holds only non-sensitive session data client-side: the JWT, userId, and
// username. The password is never stored here or anywhere in the browser.
export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem("token"));
  const [username, setUsername] = useState(() => localStorage.getItem("username"));
  const [userId, setUserId] = useState(() => localStorage.getItem("userId"));

  const login = useCallback((authResponse) => {
    localStorage.setItem("token", authResponse.token);
    localStorage.setItem("username", authResponse.username);
    localStorage.setItem("userId", authResponse.userId);
    setToken(authResponse.token);
    setUsername(authResponse.username);
    setUserId(authResponse.userId);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("userId");
    setToken(null);
    setUsername(null);
    setUserId(null);
  }, []);

  const value = {
    token,
    username,
    userId,
    isAuthenticated: !!token,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within an AuthProvider");
  return ctx;
}
