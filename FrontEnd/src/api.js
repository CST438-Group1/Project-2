/**
 * API configuration for MotoRYX frontend.
 * Uses the VITE_API_URL environment variable when deployed on Render.
 * Falls back to localhost:8080 for local development.
 *
 * Usage:
 *   import API_URL from "./api";
 *   fetch(`${API_URL}/tracks`)
 */
const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

export default API_URL;
