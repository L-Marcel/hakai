import RoomGuard from "@components/Guards/RoomGuard";
import styles from "./index.module.scss";
import useGame from "@stores/useGame";
import useRoom from "@stores/useRoom";
import { FaArrowLeft } from "react-icons/fa";
import Button from "@components/Button";
import { close } from "../../services/room";

export default function RoomPage() {
  return (
    <RoomGuard>
        <Page />
    </RoomGuard>
  );
}

function Page() {
  const room = useRoom((state) => state.room);
  const question = useGame((state) => state.question);
  const game = useGame.getState().game;

  return (
    <main className={styles.main}>
      <header className={styles.header}>
        <Button theme="full-purple" onClick={close}>
          <FaArrowLeft /> Sair
        </Button>
      </header>
      <section>

        {
          //<QuestionView variant={game.questions} />
        }
        Nº Questões {}
      </section>
    </main>
  );
}
