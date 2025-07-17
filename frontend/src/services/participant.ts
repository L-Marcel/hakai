import useRoom, { Participant } from "@stores/useRoom";
import api from "./axios";
import {getConcreteQuestionVariant, QuestionVariant } from "@stores/useGame";
import { connect } from "./socket";
import { saveParticipantAnswer } from "./answers";

export async function exit(): Promise<void> {
  const { setParticipant } = useRoom.getState();

  return await api.delete("participants").then(() => {
    setParticipant(undefined);
  });
}

export async function sendParticipantAnswer(answers: string[], current: QuestionVariant): Promise<void> {
  const { participant } = useRoom.getState();

  if (!current) {
    console.error("Tentou responder uma questão que não existe.");
    return;
  }

  const concreteQuestion = getConcreteQuestionVariant(current);

  return api
    .post("participants/answer", {
      answers,
      question: concreteQuestion.original,
           participant: participant!.uuid,
    })
    // .then(() => {
    //  saveParticipantAnswer(answers, concreteQuestion.original!);
    // })
    ;
}

export async function getParticipant(): Promise<void> {
  const { setParticipant } = useRoom.getState();

  return await api.get<Participant>("participants/me").then((response) => {
    connect(response.data.room, response.data.uuid,true);
    setParticipant(response.data);
  });
}
