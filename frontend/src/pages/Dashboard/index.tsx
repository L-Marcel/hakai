import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";
import Card from "@components/Card";
import { FaPlus, FaSignOutAlt } from "react-icons/fa";
import useAuth from "../../stores/useAuth";
import Button from "@components/Button";
import CheckRoomForm from "@components/Forms/CheckRoomForm";

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
        <div>
          <Button theme="full-orange" disabled>
            <FaPlus />
          </Button>
          <CheckRoomForm />
        </div>
        <Button theme="full-orange" onClick={logout}>
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
