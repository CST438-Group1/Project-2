import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import API_URL from "../../api";
import "./Vehicle.css";

const brands = [
    "All Brands",
    "Alfa Romeo",
    "AlphaTauri",
    "Alpine",
    "Aston Martin",
    "Ferrari",
    "Force India",
    "Haas",
    "Manor",
    "McLaren",
    "Mercedes-Benz",
    "Racing Bulls",
    "Racing Point",
    "Red Bull",
    "Renault",
    "Sauber",
    "Toro Rosso",
    "Williams"
];

function Vehicle() {
    const { continentName, countryName, trackId } = useParams();
    const navigate = useNavigate();

    const [laps, setLaps] = useState([]);
    const [trackName, setTrackName] = useState("");
    const [search, setSearch] = useState("");
    const [selectedBrand, setSelectedBrand] = useState("All Brands");

    // fetch track name and lap times
    useEffect(() => {
        fetch(`${API_URL}/tracks`)
            .then((res) => res.json())
            .then((data) => {
                const track = data.find(
                    (t) => t.trackId.toString() === trackId
                );

                if (track) setTrackName(track.name);
            })
            .catch((err) =>
                console.error("error fetching track:", err)
            );

        fetch(`${API_URL}/laps/track/${trackId}`)
            .then((res) => res.json())
            .then((data) => setLaps(data))
            .catch((err) =>
                console.error("error fetching laps:", err)
            );
    }, [trackId]);

    // filter vehicles
    const filteredLaps = laps
        .filter((lap) =>
            lap.vehicle.name.toLowerCase().includes(search.toLowerCase())
        )
        .filter((lap) =>
            selectedBrand === "All Brands"
                ? true
                : lap.vehicle.name.includes(selectedBrand)
        );

    return (
        <div className="vehicle-wrapper">

            {/* navbar */}
            <div className="navbar">
                <div className="logo-row">
                    <div className="logo">
                        MotoRYX<span className="dot">.</span>
                    </div>

                    <div className="nav-location">
                        {decodeURIComponent(countryName)}, {trackName}
                    </div>
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
            <div className="vehicle-header">
                <p className="subtitle">Choose Your Vehicle</p>

                <button
                    className="back-btn"
                    onClick={() =>
                        navigate(
                            `/continents/${continentName}/${countryName}`
                        )
                    }
                >
                    ← Return To Track
                </button>
            </div>

            {/* search */}
            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Search For Vehicle"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
            </div>

            {/* main layout */}
            <div className="vehicle-content">

                {/* brand sidebar */}
                <div className="brand-section">

                    <h3 className="brand-title">Filter By Brand</h3>

                    <div className="brand-sidebar">
                        {brands.map((brand) => (
                            <div
                                key={brand}
                                className={`brand-item ${
                                    selectedBrand === brand ? "active" : ""
                                }`}
                                onClick={() => setSelectedBrand(brand)}
                            >
                                {brand}
                            </div>
                        ))}
                    </div>

                </div>

                {/* vehicle lap list */}
                <div className="vehicle-scroll">
                    {filteredLaps.map((lap) => (
                        <div
                            key={lap.lapId}
                            className="vehicle-card"
                            onClick={() =>
                                navigate(
                                    `/continents/${continentName}/${countryName}/${trackId}/vehicle/${lap.vehicle.vehicleId}`
                                )
                            }
                            style={{ cursor: "pointer" }}
                        >
                            <div className="vehicle-info">
                                <h3>{lap.vehicle.name}</h3>
                                <p>{lap.driver}</p>
                            </div>

                            <div className="lap-time">
                                {lap.lapTime}
                            </div>
                        </div>
                    ))}
                </div>

            </div>
        </div>
    );
}

export default Vehicle;