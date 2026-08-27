import { useState, useEffect, useCallback } from "react";
import { Link } from "react-router-dom";
import { serviceRequestApi } from "../services/api";
import ServiceRequestForm from "../components/ServiceRequestForm";

export default function ServiceRequests() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingRequest, setEditingRequest] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  const loadRequests = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const data = await serviceRequestApi.list();
      setRequests(data);
    } catch (err) {
      setError(err.message || "Failed to load service requests");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadRequests();
  }, [loadRequests]);

  const handleCreate = async (form) => {
    setSubmitting(true);
    setError("");
    try {
      await serviceRequestApi.create(form);
      setSuccessMessage("Service request created successfully.");
      setShowCreateForm(false);
      await loadRequests();
    } catch (err) {
      setError(err.message || "Failed to create service request");
    } finally {
      setSubmitting(false);
    }
  };

  const handleUpdate = async (form) => {
    setSubmitting(true);
    setError("");
    try {
      await serviceRequestApi.update(editingRequest.id, form);
      setSuccessMessage("Service request updated successfully.");
      setEditingRequest(null);
      await loadRequests();
    } catch (err) {
      setError(err.message || "Failed to update service request");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this service request?")) return;
    setError("");
    try {
      await serviceRequestApi.remove(id);
      setSuccessMessage("Service request deleted.");
      await loadRequests();
    } catch (err) {
      setError(err.message || "Failed to delete service request");
    }
  };

  return (
    <div className="service-requests-page">
      <header>
        <h1>My Service Requests</h1>
        <Link to="/dashboard">Back to Dashboard</Link>
      </header>

      {error && <p className="error-message">{error}</p>}
      {successMessage && <p className="success-message">{successMessage}</p>}

      {!showCreateForm && !editingRequest && (
        <button onClick={() => setShowCreateForm(true)}>+ New Service Request</button>
      )}

      {showCreateForm && (
        <ServiceRequestForm
          onSubmit={handleCreate}
          onCancel={() => setShowCreateForm(false)}
          submitting={submitting}
        />
      )}

      {editingRequest && (
        <ServiceRequestForm
          initialValues={editingRequest}
          onSubmit={handleUpdate}
          onCancel={() => setEditingRequest(null)}
          submitting={submitting}
        />
      )}

      {loading ? (
        <p>Loading...</p>
      ) : requests.length === 0 ? (
        <p>No service requests yet.</p>
      ) : (
        <ul className="service-request-list">
          {requests.map((r) => (
            <li key={r.id} className="service-request-item">
              <h3>{r.title}</h3>
              <p>{r.description}</p>
              <p>
                <strong>Category:</strong> {r.category}
              </p>
              <p>
                <strong>Created:</strong> {new Date(r.dateCreated).toLocaleString()}
              </p>
              <div className="item-actions">
                <button onClick={() => setEditingRequest(r)}>Edit</button>
                <button onClick={() => handleDelete(r.id)}>Delete</button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
