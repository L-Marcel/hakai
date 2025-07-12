import useRoom, { Participant } from "@stores/useRoom";
import api from "./axios";
import useGame, {getConcreteQuestionVariant } from "@stores/useGame";
import { connect } from "./socket";
import { saveParticipantAnswer } from "./answers";

export async function exit(): Promise<void> {
  const { setParticipant } = useRoom.getState();

  return await api.delete("participants").then(() => {
    setParticipant(undefined);
  });
}

export async function sendParticipantAnswer(answers: string[]): Promise<void> {
  const { participant } = useRoom.getState();
  const { question, setQuestion } = useGame.getState();

 
  if (!question) {
    console.error("Tentou responder uma questão que não existe.");
    return;
  }

  const concreteQuestion = getConcreteQuestionVariant(question);

  return api
    .post("participants/answer", {
      answers,
      question: concreteQuestion.original,
           participant: participant!.uuid,
    })
    .then(() => {
  
     saveParticipantAnswer(answers, concreteQuestion.original!);  setQuestion(undefined);
    });
}

export async function getParticipant(): Promise<void> {
  const { setParticipant } = useRoom.getState();

  return await api.get<Participant>("participants/me").then((response) => {
    connect(response.data.room, response.data.uuid);
    setParticipant(response.data);
  });
}
