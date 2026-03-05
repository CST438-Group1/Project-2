import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { vi } from "vitest";
import Login from "./Login";

const renderLogin = () =>
  render(
    <MemoryRouter>
      <Login />
    </MemoryRouter>,
  );

describe("Login Component", () => {
  test("renders Welcome Back text", () => {
    renderLogin();
    expect(screen.getByText("Welcome Back")).toBeInTheDocument();
  });

  test("renders username input", () => {
    renderLogin();
    expect(screen.getByPlaceholderText("Username")).toBeInTheDocument();
  });

  test("renders password input", () => {
    renderLogin();
    expect(screen.getByPlaceholderText("Password")).toBeInTheDocument();
  });

  test("renders Log In button", () => {
    renderLogin();
    expect(screen.getByText("Log In")).toBeInTheDocument();
  });

  test("submits form with entered values", () => {
    const consoleSpy = vi.spyOn(console, "log").mockImplementation(() => {});

    renderLogin();

    const username = screen.getByPlaceholderText("Username");
    const password = screen.getByPlaceholderText("Password");
    const button = screen.getByText("Log In");

    fireEvent.change(username, { target: { value: "Anthony" } });
    fireEvent.change(password, { target: { value: "1234" } });
    fireEvent.click(button);

    expect(consoleSpy).toHaveBeenCalled();
    consoleSpy.mockRestore();
  });
});
