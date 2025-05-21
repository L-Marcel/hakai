import useRoom, { Participant } from "@stores/useRoom";
import api from "./axios";
import useGame from "@stores/useGame";
import { connect } from "./socket";

export async function sendParticipantAnswer(answer: string): Promise<void> {
  const { participant } = useRoom.getState();
  const { question, setQuestion } = useGame.getState();

  return api
    .post("participants/answer", {
      answer,
      question: question?.original,
      participant: participant?.uuid,
    })
    .then(() => {
      setQuestion(undefined);
    });
}

export async function getParticipant(): Promise<void> {
  const { setParticipant } = useRoom.getState();

  return await api.get<Participant>("participants/me").then((response) => {
    console.log(response);
    connect(response.data.room, response.data.uuid);
    setParticipant(response.data);
  });
}
