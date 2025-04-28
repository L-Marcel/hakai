import { ReactNode } from "react";
import { useParams } from "react-router-dom";

interface Props {
    children: ReactNode;
};

export default function RoomGuard({ children }: Props) {
    const { code } = useParams();
    
    return children;
};