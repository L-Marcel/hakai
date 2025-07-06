import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";

import Button from "@components/Button";
import {
  FaArrowLeft,
} from "react-icons/fa";
import { useNavigate, useParams } from "react-router-dom";
import QuestionView from "@components/Views/Question";
import useGame from "@stores/useGame";
import { useEffect } from "react";
import { UUID } from "crypto";
import StatusToast from "@components/Toast";
import { getGame } from "../../services/game";
import { getGameAnswers } from "../../services/answers";

export default function Results() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

export function Page() {
  const navigate = useNavigate();
  const { uuid } = useParams();

  const game = useGame((state) => state.game);
  const history = useGame((state) => state.history);
  
  const onClose = () => {
    navigate("/dashboard");
  };
  
  useEffect(() => {
    getGame(uuid as UUID);
    getGameAnswers(uuid as UUID);
  }, [uuid]);

  return (
    <>
      <StatusToast />
      <main className={styles.main}>
        <section className={styles.panel}>
          <div className={styles.controllers}>
            <div className={styles.buttons}>
              <Button onClick={onClose} theme="light-orange">
                <FaArrowLeft />
                Voltar
              </Button>
            </div>
          </div>
          <div className={styles.informations}>
            <h1>{game?.title}</h1>
            <p>
              Questões: {" "}
              <span data-selectable className={styles.code}>
                {game?.questions.length}
              </span>
            </p>
          </div>
        </section>
        <section>
          {game?.questions.map((question) => {
            const answers = (history ?? [])
              .filter((answer) => answer.question === question.uuid);

            return (
              <div className={styles.history} key={question.uuid}>
                <QuestionView 
                  highlight={question?.answers} 
                  question={question}
                />
                {answers.map((answer) => {
                  const isCorrect = question?.answers.includes(answer.answer);

                  return (
                    <article className={styles.question} key={answer.uuid}>
                      <h4>{answer.nickname}</h4>
                      <ol className={styles.options}>
                        <Button
                          disabled
                          id={isCorrect? "highlight":""}
                          theme="partial-orange"
                        >
                          {answer.answer}
                        </Button>
                      </ol>
                    </article>
                  );
                })}
              </div>
            );
          })}
        </section>
      </main>
    </>
  );
}
