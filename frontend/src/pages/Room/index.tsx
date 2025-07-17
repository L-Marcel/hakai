import RoomGuard from "@components/Guards/RoomGuard";
import styles from "./index.module.scss";
import useGame from "@stores/useGame";
import useRoom from "@stores/useRoom";
import { FaArrowLeft, FaArrowRight, FaPlay, FaSync } from "react-icons/fa";
import Button from "@components/Button";
import { close, join } from "../../services/room";
import { useEffect, useMemo, useState } from "react";
import QuestionView from "@components/Views/Question";
import StatusToast from "@components/Toast";
import { getGame } from "../../services/game";
import Loader from "@components/Loader";
import { generateAllVariants, sendAllQuestions } from "../../services/question";
import useAuth from "@stores/useAuth";
import { connect } from "../../services/socket";

export default function RoomPage() {
  return (
    <RoomGuard>
        <Page />
    </RoomGuard>
  );
}

function Page() {
  const room = useRoom((state) => state.room);
  const game = useGame((state) => state.game);
  const user = useAuth((state) => state.user);
  const currentQuestions = useGame((state) => state.currentQuestions);
  const score = useRoom((state) => state.participant?.score ?? 0);
  
  const [index, setIndex] = useState(0);
  const questions = useMemo(() => game?.questions ?? [], [game]);
  const question = useMemo(() => {
    if (!currentQuestions || currentQuestions.length === 0) return undefined;
    const questionIndex = index % currentQuestions.length;
    return currentQuestions[questionIndex];
  }, [index, currentQuestions]);

  const toNextQuestion = () => setIndex((index) => ++index);
  const toPreviousQuestion = () => setIndex((index) => --index);

  useEffect(() => {
    if (room?.game && !game) {
      getGame(room.game).then(() => {
        if(room.participants.length <= 0)
          join(user!.name, room.code);
        else connect(room?.code, room.participants[0].uuid);
      })
    }
  }, [room, game, user]); 

  return (
    <>
      <StatusToast />
      <main className={styles.main}>
        <header className={styles.header}>
          <Button theme="full-purple" onClick={close}>
            <FaArrowLeft /> Sair
          </Button>

          <div className="flex flex-row gap-2 items-center">
            <Button
              onClick={() => sendAllQuestions()}
              theme="full-purple"
            >
              <FaPlay />
              Lançar
            </Button>
          </div>

          <div className={styles.final_points}>
            <h3>Pontos totais: {score}</h3>
          </div>
        </header>
        <section>
              <QuestionView variant={question} />
              <div className={styles.controllers}>
                <div className={styles.buttons}>
                  <Button
                    disabled={index <= 0}
                    onClick={toPreviousQuestion}
                    theme="light-purple"
                  >
                    <FaArrowLeft />
                    Anterior
                  </Button>
                  
                  <span>
                    Questão {index+1}/{game?.questions.length}
                  </span>

                  <Button
                    disabled={index >= questions.length - 1}
                    onClick={toNextQuestion}
                    theme="light-purple"
                  >
                    <FaArrowRight />
                    Próxima
                  </Button>
                </div>
              </div>
          
        </section>
      </main>
    </>
  );
}
