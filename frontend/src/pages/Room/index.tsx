import RoomGuard from "@components/Guards/RoomGuard";
import styles from "./index.module.scss";
import ParticipantGuard from "@components/Guards/ParticipantGuard";

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
  return (
    <main className={styles.main}>
      <h1>RoomPage</h1>
    </main>
  );
}
