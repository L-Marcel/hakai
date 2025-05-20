import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";
import Card from "@components/Card";
import { FaPlus, FaSignOutAlt } from "react-icons/fa";
import Button from "@components/Button";
import CheckRoomForm from "@components/Forms/CheckRoomForm";
import { logout } from "../../services/user";
import { useEffect, useState } from "react";
import { Game } from "@stores/useGame";
import { requestAllGames } from "../../services/game";
import DashboardGuard from "@components/Guards/DashboardGuard";

export default function DashboardPage() {
  return (
    <AuthGuard>
      <DashboardGuard>
        <Page />
      </DashboardGuard>
    </AuthGuard>
  );
}
function Page() {
  const [error, setError] = useState<string | null>(null);
  const [games, setGames] = useState<Game[]>([]);

  useEffect(() => {
    requestAllGames()
      .then((games) => {
        setGames(games);
      })
      .catch((error: HttpError) => {
        setError(error.message);
      });
  }, []);

  return (
    <main className={styles.main}>
      <header className={styles.header}>
        <div>
          <Button theme="full-orange" disabled>
            <FaPlus />
          </Button>
          <CheckRoomForm />
        </div>
        <Button theme="full-orange" onClick={() => logout()}>
          <FaSignOutAlt />
        </Button>
      </header>
      <section className={styles.dashboard}>
        <ul>
          {games.map((game) => (
            <Card key={game.uuid} game={game} />
          ))}
        </ul>
        {error && <p className={styles.error}>{error}</p>}
      </section>
    </main>
  );
}
