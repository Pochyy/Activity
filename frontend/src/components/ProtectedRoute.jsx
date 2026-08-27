import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

// Wraps a route element. If there's no valid session, redirects to /login
// instead of rendering the protected page. This is a UX convenience only -
// the real enforcement happens on the Spring Boot backend.
export default function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
}
