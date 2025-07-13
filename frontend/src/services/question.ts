import { UUID } from "crypto";
import api from "./axios";
import { Question, QuestionVariant } from "@stores/useGame";
import useRoom from "@stores/useRoom";

export async function generateVariants(questionsList: Question[]): Promise<void> {
  const promises = questionsList.map(question =>
    api.post(`questions/${question.uuid as UUID}/generate`)
  );
  await Promise.all(promises);
}

export async function sendQuestion(variants: QuestionVariant[]): Promise<void> {
  const { room } = useRoom.getState();
  if (!room) return;

  const code = room.code;
  const original = variants[0];

  return await api.post(`questions/send`, {
    code,
    original,
    variants: variants.map((variant) => ({
      ...variant,
      answers: undefined,
      contexts: undefined,
      original: undefined,
    })),
  });
}
