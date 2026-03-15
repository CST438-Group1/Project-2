import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import API_URL from "../../api";
import "./Country.css";
import japanFlag from "../../assets/japanFlag.png";
import singaporeFlag from "../../assets/singaporeFlag.png";
import indiaFlag from "../../assets/indiaFlag.png";
import uaeFlag from "../../assets/uaeFlag.png";
import bahrainFlag from "../../assets/bahrainFlag.png";
import malaysiaFlag from "../../assets/malaysiaFlag.jpg";
import saudiFlag from "../../assets/saudiArabiaFlag.png";

const flagMap = {
    Japan: japanFlag,
    Singapore: singaporeFlag,
    India: indiaFlag,
    "United Arab Emirates": uaeFlag,
    Bahrain: bahrainFlag,
    Malaysia: malaysiaFlag,
    "Saudi Arabia": saudiFlag
};

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
                        >
                            <img
                                src={flagMap[c.name]}
                                alt={c.name}
                                className="country-flag"
                            />

                            <h3>{c.name}</h3>

                            {c.trackCount !== undefined && (
                                <p>
                                    {c.trackCount} Track{c.trackCount !== 1 ? "s" : ""}
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