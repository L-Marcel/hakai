import RoomGuard from "@components/Guards/RoomGuard";
import styles from "./index.module.scss";
import useGame from "@stores/useGame";
import useRoom from "@stores/useRoom";
import { FaArrowLeft, FaArrowRight } from "react-icons/fa";
import Button from "@components/Button";
import { close } from "../../services/room";
import { useEffect, useMemo, useState } from "react";
import QuestionView from "@components/Views/Question";
import StatusToast from "@components/Toast";
import { getGame } from "../../services/game";
import Loader from "@components/Loader";

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
  const setGame = useGame((state) => state.setGame);
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
      console.log("Detectado ID do jogo, buscando detalhes...");

      const fetchGameData = async () => {
        try {
          getGame(room.game); 
          console.log("Dados do jogo carregados!");

        } catch (error) {
          console.error("Falha ao buscar os detalhes do jogo:", error);
        }
      };

      fetchGameData();
    }
  }, [room, game, setGame]); 

  return (
    <>
      <StatusToast />
      <main className={styles.main}>
        <header className={styles.header}>
          <Button theme="full-purple" onClick={close}>
            <FaArrowLeft /> Sair
          </Button>
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
