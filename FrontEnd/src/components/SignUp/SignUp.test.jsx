import { render, screen, fireEvent } from "@testing-library/react";
import SignUp from "./SignUp";

describe("SignUp Component", () => {

    test("renders Create An Account text", () => {
        render(<SignUp />);
        expect(screen.getByText("Create An Account")).toBeInTheDocument();
    });

    test("renders username input", () => {
        render(<SignUp />);
        expect(screen.getByPlaceholderText("Username")).toBeInTheDocument();
    });

    test("renders password input", () => {
        render(<SignUp />);
        expect(screen.getByPlaceholderText("Password")).toBeInTheDocument();
    });

    test("shows alert when passwords do not match", () => {
        render(<SignUp />);

        const password = screen.getByPlaceholderText("Password");
        const confirm = screen.getByPlaceholderText("Confirm Password");
        const button = screen.getByText("Sign Up");

        fireEvent.change(password, { target: { value: "1234" } });
        fireEvent.change(confirm, { target: { value: "5678" } });
        fireEvent.click(button);

        expect(window.alert).toBeCalled;
    });

});