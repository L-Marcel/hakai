import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";
import RoomGuard from "@components/Guards/RoomGuard";

export default function RoomPanelPage() {
  return (
    <AuthGuard>
      <RoomGuard>
        <Page />
      </RoomGuard>
    </AuthGuard>
  );
}

function Page() {
  // const token = localStorage.getItem("token");

  return (
    <main className={styles.main}>
      <h1>RoomPanelPage</h1>
    </main>
  );
}
