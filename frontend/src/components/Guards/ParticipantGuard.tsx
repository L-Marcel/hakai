import { ReactNode, useEffect, useState } from "react";
import JoinPage from "@pages/Join";
import useRoom from "@stores/useRoom";
import { getParticipant } from "../../services/participant";

interface Props {
  children: ReactNode;
}

export default function ParticipantGuard({ children }: Props) {
  const participant = useRoom((state) => state.participant);
  const [checked, setChecked] = useState(false);

  useEffect(() => {
    getParticipant().finally(() => setChecked(true));
  }, [setChecked]);

  return participant ? children : checked ? <JoinPage /> : null;
}
