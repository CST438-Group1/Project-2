import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API_URL from "../../api";
import "./Continent.css";
import NAtrack from "../../assets/NAtrack.jpg";
import EUtrack from "../../assets/EUtrack.jpg";
import AFtrack from "../../assets/AFtrack.jpg";
import SAtrack from "../../assets/SAtrack.jpg";
import APACtrack from "../../assets/APACtrack.jpg";
import AUtrack from "../../assets/AUtrack.jpg";

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

    const continentImages = {
        "North America": NAtrack,
        Europe: EUtrack,
        Africa: AFtrack,
        "South America": SAtrack,
        APAC: APACtrack,
        Australia: AUtrack,
        Asia: APACtrack,
        Oceania: AUtrack
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
                            <img src={continentImages[c.name]} alt={c.name} />

                            <div className="continent-card-content">
                                <h3>{c.name}</h3>
                                <p>{c.countryCount} Countries</p>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default Continent;