import { UUID } from "crypto";
import api from "./axios";
import useGame, { QuestionVariant } from "@stores/useGame";
import useRoom from "@stores/useRoom";

export async function generateVariants(question: UUID): Promise<void> {
  return await api.post(`rooms/questions/${question}/generate`);
}

export function sendQuestion(variants: QuestionVariant[]): void {
  const { room, client } = useRoom.getState();

  if (!room || !client) return;

  client.publish({
    destination: "/channel/events/rooms/" + room.code + "/question",
    body: JSON.stringify(variants),
  });
}

export async function sendQuestionAnswer(answer: string): Promise<void> {
  const { room, participant } = useRoom.getState();
  const { question, setQuestion } = useGame.getState();

  return api
    .post(`rooms/${room?.code}/questions/${question?.original}/answer`, {
      answer,
      participant: participant?.uuid,
    })
    .then(() => {
      setQuestion(undefined);
    });
}
