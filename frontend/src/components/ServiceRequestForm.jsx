import { useState, useEffect } from "react";

// Reusable form for both creating and editing a service request.
// `initialValues` is null for create, or an existing request for edit.
export default function ServiceRequestForm({ initialValues, onSubmit, onCancel, submitting }) {
  const [form, setForm] = useState({ title: "", description: "", category: "" });
  const [error, setError] = useState("");

  useEffect(() => {
    if (initialValues) {
      setForm({
        title: initialValues.title || "",
        description: initialValues.description || "",
        category: initialValues.category || "",
      });
    } else {
      setForm({ title: "", description: "", category: "" });
    }
  }, [initialValues]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    if (!form.title.trim()) {
      setError("Title is required");
      return;
    }
    if (!form.category.trim()) {
      setError("Category is required");
      return;
    }

    onSubmit(form);
  };

  return (
    <form onSubmit={handleSubmit} className="service-request-form">
      <label>
        Title
        <input name="title" value={form.title} onChange={handleChange} />
      </label>
      <label>
        Description
        <textarea name="description" value={form.description} onChange={handleChange} />
      </label>
      <label>
        Category
        <input name="category" value={form.category} onChange={handleChange} />
      </label>

      {error && <p className="error-message">{error}</p>}

      <div className="form-actions">
        <button type="submit" disabled={submitting}>
          {initialValues ? "Save Changes" : "Create Request"}
        </button>
        {onCancel && (
          <button type="button" onClick={onCancel} disabled={submitting}>
            Cancel
          </button>
        )}
      </div>
    </form>
  );
}
