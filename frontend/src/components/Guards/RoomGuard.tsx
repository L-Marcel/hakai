import { ReactNode, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useRoom from "../../stores/useRoom";

interface Props {
  children: ReactNode;
}

export default function RoomGuard({ children }: Props) {
  const { code } = useParams();

  const [exists, setExists] = useState(false);
  const check = useRoom((state) => state.check);
  const navigate = useNavigate();

  useEffect(() => {
    check(code).then((response: Result) => {
      if (response.ok) setExists(true);
      else navigate("/home");
    });
  }, [code, check, navigate]);

  return exists ? children : null;
}
