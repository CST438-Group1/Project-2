import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import API_URL from "../../api";
import "./Specs.css";

function Specs() {
    const { continentName, countryName, trackId, vehicleId } = useParams();
    const navigate = useNavigate();

    const [vehicle, setVehicle] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch(`${API_URL}/vehicles/${vehicleId}`)
            .then((res) => {
                if (!res.ok) {
                    throw new Error("Vehicle not found");
                }
                return res.json();
            })
            .then((data) => {
                console.log("Vehicle data:", data); // Debug
                setVehicle(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Error fetching vehicle:", err);
                setError("Failed to load vehicle specs.");
                setLoading(false);
            });
    }, [vehicleId]);

    if (loading) {
        return <div className="specs-loading">Loading vehicle specs...</div>;
    }

    if (error) {
        return <div className="specs-error">{error}</div>;
    }

    return (
        <div className="specs-wrapper">
            {/* Navbar */}
            <div className="specs-navbar">
                <div className="logo">
                    MotoRYX<span className="dot">.</span>
                </div>

                <button
                    className="back-btn"
                    onClick={() =>
                        navigate(
                            `/continents/${continentName}/${countryName}/${trackId}`
                        )
                    }
                >
                    ← Return to Vehicles
                </button>
            </div>

            {/* Vehicle Header */}
            <div className="specs-header">
                <h1 className="vehicle-title">
                    {vehicle.name || "Unknown Vehicle"}
                </h1>

                <p className="vehicle-subtitle">
                    {vehicle.year || "—"} • {vehicle.country || "—"}
                </p>
            </div>

            {/* Specs Grid */}
            <div className="specs-grid">
                <div className="spec-item">
                    <span>Engine Type</span>
                    <strong>{vehicle.engineType ?? "N/A"}</strong>
                </div>

                <div className="spec-item">
                    <span>Displacement</span>
                    <strong>{vehicle.displacement ?? "N/A"}</strong>
                </div>

                <div className="spec-item">
                    <span>Power</span>
                    <strong>{vehicle.power ?? "N/A"}</strong>
                </div>

                <div className="spec-item">
                    <span>Torque</span>
                    <strong>{vehicle.torque ?? "N/A"}</strong>
                </div>

                <div className="spec-item">
                    <span>Power / Weight</span>
                    <strong>{vehicle.powerWeight ?? "N/A"}</strong>
                </div>
            </div>

            {/* Optional Image Section */}
            {vehicle.imageUrl && (
                <div className="specs-image-container">
                    <img
                        src={vehicle.imageUrl}
                        alt={vehicle.name}
                        className="vehicle-image"
                    />
                </div>
            )}
        </div>
    );
}

export default Specs;