import { ReactNode, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useRoom from "@stores/useRoom";
import { check } from "../../services/roomService";

interface Props {
  children: ReactNode;
}

export default function RoomGuard({ children }: Props) {
  const { code } = useParams();

  const exists = useRoom((state) => state.exists);
  const navigate = useNavigate();

  useEffect(() => {
    if (!exists) {
      check(code).then((response: Result) => {
        if (!response.ok) navigate("/home");
      });
    }
  }, [exists, code, navigate]);

  return exists ? children : null;
}
