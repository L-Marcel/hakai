import useRoom, { Participant } from "@stores/useRoom";
import api from "./axios";
import { getConcreteQuestionVariant, QuestionVariant } from "@stores/useGame";
import { connect } from "./socket";
import { saveParticipantAnswer } from "./answers";

export async function exit(): Promise<void> {
  const { setParticipant } = useRoom.getState();

  return await api.delete("participants").then(() => {
    setParticipant(undefined);
  });
}

export async function sendParticipantAnswer(
  answers: string[],
  variant: QuestionVariant
): Promise<void> {
  const { participant } = useRoom.getState();
  //const { questions, setQuestions } = useGame.getState();

  const concreteQuestion = getConcreteQuestionVariant(variant);

  return api
    .post("participants/answer", {
      answers,
      question: concreteQuestion.original,
      participant: participant!.uuid,
    })
    .then(() => {
      saveParticipantAnswer(answers, concreteQuestion.original!);
    });
}

export async function getParticipant(): Promise<void> {
  const { setParticipant } = useRoom.getState();

  return await api.get<Participant>("participants/me").then((response) => {
    connect(response.data.room, response.data.uuid);
    setParticipant(response.data);
  });
}
