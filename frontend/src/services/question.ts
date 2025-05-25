import { UUID } from "crypto";
import api from "./axios";
import { QuestionVariant } from "@stores/useGame";
import useRoom from "@stores/useRoom";

export async function generateVariants(question: UUID): Promise<void> {
  return await api.post(`questions/${question}/generate`);
}

export async function sendQuestion(variants: QuestionVariant[]): Promise<void> {
  const { room } = useRoom.getState();
  if (!room) return;

  const code = room.code;
  const original = variants[0].original;

  return await api.post(
    `questions/send`,
    {
      code,
      original,
      variants
    }
  );
}

