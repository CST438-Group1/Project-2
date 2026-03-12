import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import API_URL from "../../api";
import "./Country.css";

function Country() {
    const { continentName } = useParams();
    const navigate = useNavigate();

    const [countries, setCountries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // fetch countries for continent
    useEffect(() => {
        async function load() {
            try {
                setLoading(true);
                setError("");

                const res = await fetch(
                    `${API_URL}/continents/${encodeURIComponent(continentName)}/countries`
                );

                if (!res.ok) throw new Error(`api error: ${res.status}`);

                const data = await res.json();
                setCountries(Array.isArray(data) ? data : []);
            } catch (e) {
                setError(e.message || "failed to load countries");
            } finally {
                setLoading(false);
            }
        }

        load();
    }, [continentName]);

    // navigate to track page
    const handleCountryClick = (countryName) => {
        navigate(
            `/continents/${continentName}/${encodeURIComponent(countryName)}`
        );
    };

    return (
        <div className="country-wrapper">
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
                <header>Choose Your Country</header>
                <button
                    className="back-btn"
                    onClick={() => navigate("/continents")}
                >
                    ← Back
                </button>
            </div>

            {/* states */}
            {loading && <p>Loading…</p>}
            {error && <p style={{ color: "#f4b400" }}>{error}</p>}

            {!loading && !error && countries.length === 0 && (
                <p style={{ color: "#aaa" }}>
                    No countries found for this continent.
                </p>
            )}

            {/* country grid */}
            {!loading && !error && (
                <div className="country-grid">
                    {countries.map((c) => (
                        <div
                            key={c.name}
                            className="country-card"
                            onClick={() => handleCountryClick(c.name)}
                            style={{ cursor: "pointer" }}
                        >
                            <h3>{c.name}</h3>

                            {c.trackCount !== undefined && (
                                <p>
                                    {c.trackCount} Track
                                    {c.trackCount !== 1 ? "s" : ""}
                                </p>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default Country;