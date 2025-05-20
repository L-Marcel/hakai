import { ReactNode, useEffect, useState } from "react";
import JoinPage from "@pages/Join";
import useRoom from "@stores/useRoom";
import { getParticipant } from "../../services/room";

interface Props {
  children: ReactNode;
}

export default function ParticipantGuard({ children }: Props) {
  const room = useRoom((state) => state.room);
  const participant = useRoom((state) => state.participant);
  const [checked, setChecked] = useState(false);

  useEffect(() => {
    getParticipant(room?.code).finally(() => setChecked(true));
  }, [room, setChecked]);

  return participant ? children : checked ? <JoinPage /> : null;
}
