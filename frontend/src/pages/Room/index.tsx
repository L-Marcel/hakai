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

export default function RoomPage() {
  return (
    <RoomGuard>
        <Page />
    </RoomGuard>
  );
}

function Page() {
  const game = useGame((state) => state.game);
  const currentQuestions = useGame((state) => state.currentQuestions);
  
  const [index, setIndex] = useState(0);
  const question = useMemo(() => {
    if (!currentQuestions || currentQuestions.length === 0) return undefined;
    return currentQuestions[index];
  }, [index, currentQuestions]);

  
  const toNextQuestion = () => setIndex((index) => ++index);
  const toPreviousQuestion = () => setIndex((index) => --index);


  return (
    <>
      <StatusToast />
    
    <main className={styles.main}>
      <header className={styles.header}>
        <Button theme="full-purple" onClick={close}>
          <FaArrowLeft /> Sair
        </Button>
      </header>
      <section>

        {question && (
          <QuestionView variant={question} />
        )}

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
              disabled={
                !currentQuestions || index >= currentQuestions.length - 1
              }
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
