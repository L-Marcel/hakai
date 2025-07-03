import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";
import Card from "@components/Card";
import { FaPlus, FaSignOutAlt } from "react-icons/fa";
import Button from "@components/Button";
import CheckRoomForm from "@components/Forms/CheckRoomForm";
import { logout } from "../../services/user";
import { useEffect, useState } from "react";
import { Game } from "@stores/useGame";
import { getAllGames } from "../../services/game";
import DashboardGuard from "@components/Guards/DashboardGuard";
import GameModal from "@components/Modal";

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

  const [showModal, setShowModal] = useState(false);
  useEffect(() => {
    getAllGames()
      .then((games) => {
        setGames(games);
      })
      .catch((error: HttpError) => {
        setError(error.message);
      });
  }, []);
  function handleGameCreated(newGame: Game) {
    setGames((prev) => [newGame, ...prev]);
  }

  return (
    <main className={styles.main}>
      <header className={styles.header}>
        <div>
          <Button
            theme="full-orange"
            onClick={() => setShowModal(true)}
          >
            <FaPlus />
          </Button>
          <CheckRoomForm />
        </div>
        <Button theme="full-orange" onClick={() => logout()}>
          <FaSignOutAlt />
        </Button>
      </header>

      <GameModal
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        onGameCreated={handleGameCreated}
      />

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
