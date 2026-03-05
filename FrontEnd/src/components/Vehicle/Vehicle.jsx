import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "./Vehicle.css";

function Vehicle() {
    const { continentName, countryName, trackId } = useParams();
    const navigate = useNavigate();

    const [laps, setLaps] = useState([]);
    const [trackName, setTrackName] = useState("");
    const [search, setSearch] = useState("");

    // fetch track name and laps
    useEffect(() => {
        fetch("http://localhost:8080/tracks")
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

        fetch(`http://localhost:8080/laps/track/${trackId}`)
            .then((res) => res.json())
            .then((data) => setLaps(data))
            .catch((err) =>
                console.error("error fetching laps:", err)
            );
    }, [trackId]);

    // filter vehicles by search
    const filteredLaps = laps.filter((lap) =>
        lap.vehicle.name
            .toLowerCase()
            .includes(search.toLowerCase())
    );

    return (
        <div className="vehicle-wrapper">
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
                <p className="subtitle">
                    Choose Your Vehicle
                </p>

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
                    placeholder="Search Vehicle"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
            </div>

            {/* vehicle list */}
            <div className="vehicle-list">
                {filteredLaps.map((lap) => (
                    <div
                        key={lap.lapId}
                        className="vehicle-card"
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
    );
}

export default Vehicle;