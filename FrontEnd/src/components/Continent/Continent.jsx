import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API_URL from "../../api";
import "./Continent.css";

function Continent() {
    const navigate = useNavigate();

    const [continents, setContinents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // fetch continents
    useEffect(() => {
        async function load() {
            try {
                setLoading(true);
                setError("");

                const res = await fetch(`${API_URL}/continents`);
                if (!res.ok) throw new Error(`api error: ${res.status}`);

                const data = await res.json();
                setContinents(Array.isArray(data) ? data : []);
            } catch (e) {
                setError(e.message || "failed to load continents");
            } finally {
                setLoading(false);
            }
        }

        load();
    }, []);

    // navigate to country page
    const handleContinentClick = (continentName) => {
        navigate(`/continents/${encodeURIComponent(continentName)}`);
    };

    return (
        <div className="continent-wrapper">
            {/* navbar */}
            <div className="navbar">
                <div className="logo">
                    MotoRYX<span className="dot">.</span>
                </div>

                <div className="nav-links">
                    <span>Saved</span>
                    <span>Profile</span>
                    <button
                        className="logout-btn"
                        onClick={() => navigate("/")}
                    >
                        Log Out
                    </button>
                </div>
            </div>

            {/* header */}
            <div className="header">
                <p className="subtitle">Choose Your Continent</p>
            </div>

            {/* states */}
            {loading && <p>Loading…</p>}
            {error && <p style={{ color: "#f4b400" }}>{error}</p>}

            {/* continent grid */}
            {!loading && !error && (
                <div className="continent-grid">
                    {continents.map((c) => (
                        <div
                            key={c.name}
                            className="continent-card"
                            onClick={() => handleContinentClick(c.name)}
                        >
                            <h3>{c.name}</h3>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default Continent;