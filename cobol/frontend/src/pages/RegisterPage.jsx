import React, { useState } from "react";
import api from "../lib/api";
import { useNavigate } from "react-router-dom";

export default function RegisterPage() {
  const [empid, setEmpid] = useState("");
  const [password, setPassword] = useState("");
  const [deptid, setDeptid] = useState("");
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    try {
      const response = await api.post("/api/auth/register", {
        empid,
        password,
        deptid: parseInt(deptid),
      });
      setSuccess("Registration successful! Redirecting...");
      console.log("Registered:", response.data);

      setTimeout(() => navigate("/login"), 2000);
    } catch (err) {
      console.error("Register error:", err);
      setError(err.response?.data?.error || "Registration failed.");
    }
  };

  return (
    <div style={{ fontFamily: "monospace", padding: 24 }}>
      <h2>COBOL Airlines — Register</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>USERIDI</label>
          <input
            name="empid"
            value={empid}
            onChange={(e) => setEmpid(e.target.value)}
            required
          />
        </div>
        <div>
          <label>PASSWI</label>
          <input
            name="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <div>
          <label>DEPTID</label>
          <input
            name="deptid"
            value={deptid}
            onChange={(e) => setDeptid(e.target.value)}
            required
          />
        </div>
        <button type="submit">Register</button>

        {error && <div style={{ color: "red", marginTop: 8 }}>{error}</div>}
        {success && <div style={{ color: "green", marginTop: 8 }}>{success}</div>}
      </form>
    </div>
  );
}
