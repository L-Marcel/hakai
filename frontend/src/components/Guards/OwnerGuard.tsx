import { ReactNode, useEffect, useState } from "react";
import useRoom from "@stores/useRoom";
import { useNavigate, useParams } from "react-router-dom";

interface Props {
  children: ReactNode;
}

export default function OwnerGuard({ children }: Props) {
  const { code } = useParams();
  const room = useRoom((state) => state.room);
  const connect = useRoom((state) => state.connect);
  const navigate = useNavigate();
  const [isOwner, setIsOwner] = useState(true);

  //[TODO] Fazer verificar de verdade

  useEffect(() => {
    connect(code);
  }, [code, room, setIsOwner, navigate, connect]);

  return isOwner ? children : null;
}
