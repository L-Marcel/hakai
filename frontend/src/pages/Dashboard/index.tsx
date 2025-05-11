import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";
import Card from "@components/Card";
import { FaPlus, FaSignOutAlt } from "react-icons/fa";
import Button from "@components/Button";
import CheckRoomForm from "@components/Forms/CheckRoomForm";
import { logout } from "../../services/authService";
import { useEffect, useState } from "react";
import { Game } from "@stores/useGame";
import { requestAllGames } from "../../services/gameService";

export default function DashboardPage() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}
function Page() {
  const [error, setError] = useState<string | null>(null);
  const [games, setGames] = useState<Game[]>([]);

  useEffect(() => {
    requestAllGames().then((result) => {
      if (result.ok) {
        setGames(result.value);
      } else {
        console.error("Erro ao carregar jogos:", result.error);
        setError(result.error.message);
      }
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
        <Button theme="full-orange" onClick={logout}>
          <FaSignOutAlt />
        </Button>
      </header>
      <section className={styles.dashboard}>
        <ul>
          {games.map((game) => (
            <Card key={game.uuid} game={game} />
          ))}
        </ul>{error && <p className={styles.error}>{error}</p>}

      </section>
    </main>
  );
}
