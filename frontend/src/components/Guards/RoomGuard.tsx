import { ReactNode, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useRoom from "@stores/useRoom";

interface Props {
  children: ReactNode;
}

export default function RoomGuard({ children }: Props) {
  const { code } = useParams();

  const exists = useRoom((state) => state.exists);
  const check = useRoom((state) => state.check);
  const navigate = useNavigate();

  useEffect(() => {
    if(!exists) {
      check(code).then((response: Result) => {
        if (!response.ok) navigate("/home");
      });
    };
  }, [exists, code, check, navigate]);

  return exists ? children : null;
}
