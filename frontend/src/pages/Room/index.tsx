import RoomGuard from "@components/Guards/RoomGuard";
import styles from "./index.module.scss";
import ParticipantGuard from "@components/Guards/ParticipantGuard";
import useGame from "@stores/useGame";
import QuestionView from "@components/Views/Question";

export default function RoomPage() {
  return (
    <RoomGuard>
      <ParticipantGuard>
        <main className={styles.main}>
          <div>
            <Page />
          </div>
        </main>
      </ParticipantGuard>
    </RoomGuard>
  );
}

function Page() {
  const current  = useGame().current;
  if (!current) return (
    <>
      <h1>RoomPage</h1>
      <p>Aguardando pergunta...</p>
    </>
  );

  return (
    <>
      <QuestionView variant={current} />
    </>
  );
}