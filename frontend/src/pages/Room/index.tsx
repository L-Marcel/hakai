import RoomGuard from "@components/Guards/RoomGuard";
import styles from "./index.module.scss";
import ParticipantGuard from "@components/Guards/ParticipantGuard";
import useGame, { getConcreteQuestionVariant } from "@stores/useGame";
import QuestionView from "@components/Views/Question";
import { FaUserGroup } from "react-icons/fa6";
import useRoom from "@stores/useRoom";
import ParticipantsMansoryGrid from "@components/Grid/ParticipantsGrid";
import { FaArrowLeft } from "react-icons/fa";
import Button from "@components/Button";
import { exit, sendParticipantAnswer } from "../../services/participant";
import { useState } from "react";

export default function RoomPage() {
  return (
    <RoomGuard>
      <ParticipantGuard>
        <Page />
      </ParticipantGuard>
    </RoomGuard>
  );
}

type AllAnswers = Record<string, string[]>;
function Page() {
  const room = useRoom((state) => state.room);
  const questions = useGame((state) => state.questions);

  const [allAnswers, setAllAnswers] = useState<AllAnswers>({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleAnswerChange = (questionUuid: string, option: string) => {
    setAllAnswers((currentAnswers) => {
      const currentOptions = currentAnswers[questionUuid] || [];
      const newOptions = currentOptions.includes(option)
        ? currentOptions.filter((item) => item !== option)
        : [...currentOptions, option];
      return {
        ...currentAnswers,
        [questionUuid]: newOptions,
      };
    });
  };

  const handleSubmitAllAnswers = async () => {
    setIsSubmitting(true);
    const submissionPromises = Object.entries(allAnswers).map(
      ([uuid, selectedOptions]) => {
        if (selectedOptions.length > 0) {
          const questionVariant = questions.find(
            (q) => getConcreteQuestionVariant(q).uuid === uuid
          );
          if (questionVariant) {
            return sendParticipantAnswer(selectedOptions, questionVariant);
          }
        }
        return Promise.resolve(null);
      }
    );
    try {
      await Promise.all(submissionPromises);
      alert("Respostas enviadas com sucesso!");
    } catch {
      alert("Ocorreu um erro ao enviar suas respostas. Tente novamente.");
    } finally {
      setIsSubmitting(false);
    }
  };
  return (
    <main className={styles.main}>
      <header className={styles.header}>
        <Button theme="full-red" onClick={() => exit()}>
          <FaArrowLeft /> Sair
        </Button>
        <div className={styles.participants}>
          <h3>Aguardando dono da sala...</h3>
          <h4>
            <FaUserGroup /> Participantes: {room?.participants.length ?? 0}
          </h4>
        </div>
      </header>
      {questions.length >= 0 ? (
        <section>
          {questions.map((question) => {
            const concrete = getConcreteQuestionVariant(question);
            
            return (
              <QuestionView
                key={concrete.original ?? concrete.uuid}
                variant={question}
                selectedOptions={allAnswers[concrete.uuid] || []}
                onOptionToggle={(option) =>
                  handleAnswerChange(concrete.uuid, option)
                }
              />
            );
          })}
          <Button
            onClick={handleSubmitAllAnswers}
            theme="full-red"
            disabled={isSubmitting}
            style={{
              marginTop: "30px",
              width: "100%",
              fontSize: "1.2rem",
              padding: "15px",
            }}
          >
            {isSubmitting ? "Enviando..." : "Enviar Todas as Respostas"}
          </Button>
        </section>
      ) : (
        <section>
          <ParticipantsMansoryGrid
            ranked
            participants={room?.participants ?? []}
          />
        </section>
      )}
    </main>
  );
}
