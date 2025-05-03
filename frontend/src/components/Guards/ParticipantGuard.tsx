import { ReactNode } from "react";
import JoinPage from "@pages/Join";
import useRoom from "../../stores/useRoom";

interface Props {
  children: ReactNode;
}

export default function ParticipantGuard({ children }: Props) {
  const participant = useRoom((state) => state.participant);
  return participant ? children : <JoinPage />;
}
