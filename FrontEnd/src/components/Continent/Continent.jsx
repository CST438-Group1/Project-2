import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API_URL from "../../api";
import "./Continent.css";

function Continent() {
  const navigate = useNavigate();
  const [continents, setContinents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function load() {
      try {
        setLoading(true);
        setError("");
        const res = await fetch(`${API_URL}/continents`);
        if (!res.ok) throw new Error(`API error: ${res.status}`);
        const data = await res.json();
        setContinents(Array.isArray(data) ? data : []);
      } catch (e) {
        setError(e.message || "Failed to load continents");
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  return (
    <div className="continent-wrapper">
      <div className="navbar">
        <div className="logo">
          MotoRYX<span className="dot">.</span>
        </div>
        <div className="nav-links">
          <span>Saved</span>
          <span>Profile</span>
          <button className="logout-btn" onClick={() => navigate("/")}>
            Log Out
          </button>
        </div>
      </div>

      <div className="header">
        <h2>Choose Your Continent</h2>
      </div>

      {loading && <p>Loading…</p>}
      {error && <p style={{ color: "#f4b400" }}>{error}</p>}

      {!loading && !error && (
        <div className="continent-grid">
          {continents.map((c) => (
            <div
              key={c.name}
              className="continent-card"
              onClick={() =>
                navigate(`/continents/${encodeURIComponent(c.name)}`)
              }
            >
              <h3>{c.name}</h3>
              <p>{c.countryCount} Countries</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default Continent;
