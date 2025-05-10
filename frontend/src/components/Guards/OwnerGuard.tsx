import { ReactNode, useEffect, useState } from "react";
import useRoom from "@stores/useRoom";
import { useNavigate, useParams } from "react-router-dom";
import { connect } from "../../services/socketService";

interface Props {
  children: ReactNode;
}

export default function OwnerGuard({ children }: Props) {
  const { code } = useParams();
  const room = useRoom((state) => state.room);;
  const navigate = useNavigate();
  const [isOwner, setIsOwner] = useState(true);

  //[TODO] Fazer verificar de verdade

  useEffect(() => {
    connect(code);
  }, [code, room, setIsOwner, navigate]);

  return isOwner ? children : null;
}
