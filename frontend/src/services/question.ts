import { UUID } from "crypto";
import api from "./axios";
import { QuestionVariant } from "@stores/useGame";
import useRoom from "@stores/useRoom";

export async function generateVariants(question: UUID): Promise<void> {
  return await api.post(`questions/${question}/generate`);
}

export function sendQuestion(variants: QuestionVariant[]): void {
  const { room, client } = useRoom.getState();

  if (!room || !client) return;

  client.publish({
    destination: "/channel/events/rooms/" + room.code + "/question",
    body: JSON.stringify(variants),
  });
}
