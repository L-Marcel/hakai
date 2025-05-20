import { ReactNode, useEffect } from "react";
import useRoom from "@stores/useRoom";
import { useNavigate, useParams } from "react-router-dom";
import { connect } from "../../services/socket";
import useGame from "@stores/useGame";
import useAuth from "@stores/useAuth";
import { request } from "../../services/game";

interface Props {
  children: ReactNode;
}

export default function OwnerGuard({ children }: Props) {
  const { code } = useParams();
  const room = useRoom((state) => state.room);
  const game = useGame((state) => state.game);
  const user = useAuth((state) => state.user);
  const navigate = useNavigate();
  const isOwner = user?.uuid === game?.owner;

  useEffect(() => {
    if (!game?.owner && !isOwner && room) request(room?.game);
    else if (isOwner) connect(code, undefined, true);
    else if (room) navigate("/home");
  }, [isOwner, code, room, game, navigate, user]);

  return isOwner ? children : null;
}
