import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";

export default function RoomPanelPage() {
  return (
    <AuthGuard>
      <Page/>
    </AuthGuard>
  );
};

function Page() {
  // const token = localStorage.getItem("token");

  return (
    <main className={styles.main}>
        <h1>RoomPanelPage</h1>
    </main>
  );
};
