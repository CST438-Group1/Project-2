import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./components/Login/Login";
import SignUp from "./components/SignUp/SignUp";
import Continent from "./components/Continent/Continent";
import Country from "./components/Country/Country";
import Track from "./components/Track/Track";
import Vehicle from "./components/Vehicle/Vehicle";
import Specs from "./components/Specs/Specs";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/signup" element={<SignUp />} />

                <Route path="/continents" element={<Continent />} />
                <Route path="/continents/:continentName" element={<Country />} />
                <Route path="/continents/:continentName/:countryName/:trackId/vehicle/:vehicleId" element={<Specs />} />

                {/* NEW ROUTES */}
                <Route
                    path="/continents/:continentName/:countryName"
                    element={<Track />}
                />
                <Route
                    path="/continents/:continentName/:countryName/:trackId"
                    element={<Vehicle />}
                />

                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;