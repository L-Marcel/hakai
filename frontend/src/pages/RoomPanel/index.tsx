import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";
import RoomGuard from "@components/Guards/RoomGuard";
import ParticipantsMansoryGrid from "@components/Grid/ParticipantsGrid";
import Button from "@components/Button";
import {
  FaArrowLeft,
  FaArrowRight,
  FaBomb,
  FaPlay,
  FaSignOutAlt,
  FaSync,
} from "react-icons/fa";
import { useParams } from "react-router-dom";
import { FaUserGroup } from "react-icons/fa6";
import QuestionVariantsCarousel from "@components/Carousel";
import QuestionView from "@components/Views/Question";
import { close } from "../../services/room";
import useRoom from "@stores/useRoom";
import useGame, {
  getConcreteQuestionVariant,
  QuestionVariant,
} from "@stores/useGame";
import OwnerGuard from "@components/Guards/OwnerGuard";
import { useMemo, useState } from "react";
import { UUID } from "crypto";
import { generateAllVariants, sendAllQuestions } from "../../services/question";
import StatusToast from "@components/Toast";

export default function RoomPanelPage() {
  return (
    <AuthGuard>
      <RoomGuard>
        <OwnerGuard>
          <Page />
        </OwnerGuard>
      </RoomGuard>
    </AuthGuard>
  );
}

function Page() {
  const { code } = useParams();
  const [index, setIndex] = useState(0);

  const game = useGame((state) => state.game);
  const room = useRoom((state) => state.room);
  const [duration, setDuration] = useState(30);
  const questions = useMemo(() => game?.questions ?? [], [game]);
  const question = useMemo(() => {
    if (questions.length === 0) return undefined;
    const questionIndex = index % questions.length;
    return questions[questionIndex];
  }, [index, questions]);

  const variants: QuestionVariant[] = useMemo(
    () => question?.variants ?? [],
    [question]
  );

  const hasAnyVariant = useMemo(
    () => questions.some((q) => q.variants && q.variants.length > 0),
    [questions]
  );

  const hardestVariant = useMemo(() => {
    if (variants.length === 0) return "";
    const lastVariant = variants[variants.length - 1];
    return getConcreteQuestionVariant(lastVariant).uuid;
  }, [variants]);

  const toNextQuestion = () => setIndex((index) => ++index);
  const toPreviousQuestion = () => setIndex((index) => --index);

  return (
    <>
      <StatusToast />
      <main className={styles.main}>
        <section className={styles.panel}>
          <div className={styles.informations}>
            <h1>Sala de jogo</h1>
            <p>
              Código:{" "}
              <span data-selectable className={styles.code}>
                {code}
              </span>
            </p>
          </div>
          <div className={styles.controllers}>
            <div className={styles.buttons}>
              <Button
                disabled={!hasAnyVariant}
                onClick={sendAllQuestions}
                theme="full-red"
              >
                <FaPlay />
                Lançar
              </Button>
              <Button
                disabled={questions.length === 0}
                onClick={generateAllVariants}
                theme="light-red"
              >
                <FaSync />
                Gerar
              </Button>
              <Button
                disabled={index <= 0}
                onClick={toPreviousQuestion}
                theme="light-red"
              >
                <FaArrowLeft />
                Anterior
              </Button>
              <Button
                disabled={index >= questions.length - 1}
                onClick={toNextQuestion}
                theme="light-red"
              >
                <FaArrowRight />
                Próxima
              </Button>
              <Button onClick={close} theme="light-red">
                <FaSignOutAlt />
                Finalizar
              </Button>
            </div>
          </div>
        </section>
        <section>
          <QuestionView highlight={question?.answers} question={question} selectedOptions={[]}
            onOptionToggle={() => { }} />
          {variants && variants.length > 0 && (
            <QuestionVariantsCarousel
              items={variants}
              start={hardestVariant}
              identifier={(item) => getConcreteQuestionVariant(item).uuid}
              render={(item) => {
                return (
                  <li key={getConcreteQuestionVariant(item).uuid}>
                    <QuestionView
                      highlight={question?.answers}
                      variant={item} selectedOptions={[]}
                      onOptionToggle={() => { }}
                    />
                  </li>
                );
              }}
            />
          )}
        </section>
        <section className={styles.participants}>
          <h4>
            <FaBomb /> Perguntas: 4<span>/</span>
            <FaUserGroup /> Participantes: {room?.participants.length ?? 0}
          </h4>
          <ParticipantsMansoryGrid
            ranked
            userIsRoomOwner
            participants={room?.participants ?? []}
          />
        </section>
      </main>
    </>
  );
}
