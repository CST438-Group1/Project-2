import "./Continent.css";

function Continent(){
    const continents = [
        {name: "North America", countries: 23},
        {name: "Europe", countries: 44},
        {name: "South America", countries: 54},
        {name: "Asian Pacific", countries: 12},
        {name: "Australia", countries: 49}
    ];

    return (
        <div className="contient-wrapper">

            {/* navbar */}
            <div className="navbar">
                <div className="logo">
                    MotoRYX<span className="dot">.</span>
                </div>

                <div className="nav-links">
                    <span>Saved</span>
                    <span>Profile</span>
                    <button className="logout-btn">Log Out</button>
                </div>
            </div>

            {/* header */}
            <div className="header">
                <h2>Choose Your Continent</h2>
            </div>

            {/* grid */}
            <div className="continent-grid">
                {continents.map((continent, index) => (
                    <div key={index} className="continent-card">
                        <h3>{continent.name}</h3>
                        <p>{continent.countries} Countries</p>
                    </div>
                ))}
            </div>

        </div>
    );
}

export default Continent