import { describe, test, expect, beforeEach, vi } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Specs from "./Specs";

// Mock react-router-dom
vi.mock("react-router-dom", async () => {
    const actual = await vi.importActual("react-router-dom");
    return {
        ...actual,
        useParams: () => ({
            continentName: "APAC",
            countryName: "Japan",
            trackId: "1",
            vehicleId: "3",
        }),
        useNavigate: () => vi.fn(),
    };
});

// Mock fetch
global.fetch = vi.fn();

const mockVehicle = {
    name: "Mercedes-Benz W16",
    year: 2023,
    country: "Germany",
    engineType: "1.6L V6 Turbo",
    displacement: "1600cc",
    power: "1000hp",
    torque: "900Nm",
    powerWeight: "0.75 hp/kg",
    imageUrl: "test.jpg",
};

describe("Specs Component", () => {

    beforeEach(() => {
        fetch.mockReset();
    });

    test("renders loading state initially", () => {
        fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockVehicle,
        });

        render(
            <MemoryRouter>
                <Specs />
            </MemoryRouter>
        );

        expect(
            screen.getByText(/loading vehicle specs/i)
        ).toBeInTheDocument();
    });

    test("renders vehicle data on successful fetch", async () => {
        fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockVehicle,
        });

        render(
            <MemoryRouter>
                <Specs />
            </MemoryRouter>
        );

        await waitFor(() =>
            expect(
                screen.getByText("Mercedes-Benz W16")
            ).toBeInTheDocument()
        );

        expect(screen.getByText("1000hp")).toBeInTheDocument();
        expect(screen.getByText("900Nm")).toBeInTheDocument();
    });

    test("renders error state on failed fetch", async () => {
        fetch.mockResolvedValueOnce({
            ok: false,
        });

        render(
            <MemoryRouter>
                <Specs />
            </MemoryRouter>
        );

        await waitFor(() =>
            expect(
                screen.getByText(/failed to load vehicle specs/i)
            ).toBeInTheDocument()
        );
    });
});