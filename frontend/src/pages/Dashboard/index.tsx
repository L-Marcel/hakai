import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";
import Card from "@components/Card";
import { FaSignOutAlt } from "react-icons/fa";
import useAuth from "../../stores/useAuth";
import Button from "@components/Button";

export default function DashboardPage() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  const logout = useAuth((state) => state.logout);
  // const token = localStorage.getItem("token");

  return (
    <main className={styles.main}>
      <header className={styles.header}>
        <Button onClick={logout}>
          <FaSignOutAlt />
        </Button>
      </header>
      <section className={styles.dashboard}>
        <ul>
          <Card />
          <Card />
          <Card />
          <Card />
          <Card />
        </ul>
      </section>
    </main>
  );
}
