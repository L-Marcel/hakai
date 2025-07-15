import { UUID } from "crypto";
import api from "./axios";
import useGame, {
  getConcreteQuestionVariant,
  QuestionVariant,
  transformQuestionVariantToRequest,
} from "@stores/useGame";
import useRoom from "@stores/useRoom";
import useGenerationStatus from "@stores/useStatus";

export async function generateVariants(question: UUID): Promise<void> {
  return await api.post(`questions/${question}/generate`);
}
export async function generateAllVariants(): Promise<void> {
  const { game } = useGame.getState();
  if (!game) return;

  const response = await api.post(`questions/generate-all`);
  const { variants } = response.data;

  if (variants && Array.isArray(variants)) {
    useGame.getState().setVariants(variants);
  }
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
export async function sendAllQuestions(): Promise<void> {
  const { room } = useRoom.getState();
  const { game } = useGame.getState();
  const { setGenerationStatus } = useGenerationStatus.getState();

  if (!room || !game || game.questions.length === 0) {
    return;
  }

  const questionsPayload = game.questions
    .map((question) => {
      if (question.variants && question.variants.length > 0) {
        return {
          original: question.uuid,
          variants: question.variants.map(transformQuestionVariantToRequest),
        };
      };

      return null;
    }).filter((q) => q !== null);

  if (questionsPayload.length === 0) {
    setGenerationStatus("Nenhuma variante para ser enviada!")
    return;
  } else {
    setGenerationStatus("Enviando as " + questionsPayload.length + " variantes!");
  };

  return await api.post(`questions/send-all`, {
    code: room.code,
    questions: questionsPayload,
  });
}
