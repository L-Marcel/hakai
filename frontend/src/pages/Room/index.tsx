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
          <div
            style={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              height: "100vh",
            }}
          >
            <Page />
          </div>
        </main>
      </ParticipantGuard>
    </RoomGuard>
  );
}

function Page() {
  const gameName = useGame().game?.title;
  const current = useGame().current;

  if (!current)
    return (
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <h1>ROOM: {gameName}</h1>
        <h2>Aguardando pergunta...</h2>
      </div>
    );

  return (
    <>
      <QuestionView variant={current} />
    </>
  );
}
