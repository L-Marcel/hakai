import { ReactNode } from "react";
import { Navigate } from "react-router-dom";

interface PrivateProps {
    children: ReactNode
};

export default function Private(props: PrivateProps) {
    const token = localStorage.getItem("token");
    return token ? props.children : <Navigate to="/login" />;
};