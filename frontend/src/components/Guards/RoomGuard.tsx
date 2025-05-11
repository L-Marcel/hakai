import { ReactNode, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useRoom from "@stores/useRoom";
import { check } from "../../services/roomService";

interface Props {
  children: ReactNode;
}

export default function RoomGuard({ children }: Props) {
  const { code } = useParams();

  const room = useRoom((state) => state.room);
  const navigate = useNavigate();

  useEffect(() => {
    if (!room) {
      check(code).then((response: Result) => {
        if (!response.ok) navigate("/home");
      });
    }
  }, [room, code, navigate]);

  return room ? children : null;
}
