import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Dashboard() {
  const { username, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="dashboard-page">
      <h1>Welcome, {username}</h1>
      <nav>
        <Link to="/requests">My Service Requests</Link>
      </nav>
      <button onClick={handleLogout}>Logout</button>
    </div>
  );
}
