import RoomGuard from "@components/Guards/RoomGuard";
import styles from "./index.module.scss";

export default function RoomPage() {
  return (
    <RoomGuard>
      <Page />
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
