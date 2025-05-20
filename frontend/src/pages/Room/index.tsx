import RoomGuard from "@components/Guards/RoomGuard";
import styles from "./index.module.scss";
import ParticipantGuard from "@components/Guards/ParticipantGuard";
import useGame from "@stores/useGame";
import QuestionView from "@components/Views/Question";
import { FaUserGroup } from "react-icons/fa6";
import useRoom from "@stores/useRoom";
import ParticipantsMansoryGrid from "@components/Grid/ParticipantsGrid";

export default function RoomPage() {
  return (
    <RoomGuard>
      <ParticipantGuard>
        <Page />
      </ParticipantGuard>
    </RoomGuard>
  );
}

function Page() {
  const room = useRoom((state) => state.room);
  const question = useGame((state) => state.question);

  return (
    <main className={styles.main}>
      {question ? (
        <section>
          <QuestionView variant={question} />
        </section>
      ) : (
        <section className={styles.participants}>
          <h1>Aguardando dono da sala...</h1>
          <h4>
            <FaUserGroup /> Participantes: {room?.participants.length ?? 0}
          </h4>
          <ParticipantsMansoryGrid
            ranked
            participants={room?.participants ?? []}
          />
        </section>
      )}
    </main>
  );
}
