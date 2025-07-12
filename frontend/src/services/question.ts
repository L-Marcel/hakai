import { UUID } from "crypto";
import api from "./axios";
import { getConcreteQuestionVariant, QuestionVariant, transformQuestionVariantToRequest } from "@stores/useGame";
import useRoom from "@stores/useRoom";

export async function generateVariants(question: UUID): Promise<void> {
  return await api.post(`questions/${question}/generate`);
}

export async function sendQuestion(variants: QuestionVariant[]): Promise<void> {
  const { room } = useRoom.getState();
  if (!room) return;

  const _variants = [...variants];
  const code = room.code;
  const original = getConcreteQuestionVariant(_variants[0]).original;

  return await api.post(`questions/send`, {
    code,
    original,
    variants: [..._variants].map(transformQuestionVariantToRequest),
  });
}
