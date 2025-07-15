import { UUID } from "crypto";
import { create } from "zustand";

export enum Difficulty {
  EASY = "EASY",
  NORMAL = "NORMAL",
  HARD = "HARD",
}

export const difficultyToString: Record<Difficulty, string> = {
  [Difficulty.EASY]: "fácil",
  [Difficulty.NORMAL]: "média",
  [Difficulty.HARD]: "difícil",
};

export type Game = {
  uuid: UUID;
  owner: UUID;
  title: string;
  questions: Question[];
};

export type Question = {
  uuid: UUID;
  question: string;
  answers: string[];
  wrongValue: number;
  correctValue: number;
  variants?: QuestionVariant[];
  contexts: string[];
};

export type ConcreteQuestionVariant = {
  type: string;
  uuid: UUID;
  question: string;
  difficulty: Difficulty;
  options: string[];
  contexts?: string[];
  answers?: string[];
  original?: UUID;
};

export type BaseQuestionVariant = {
  type: string;
  wrappee: QuestionVariant;
};

export type QuestionVariant = BaseQuestionVariant | ConcreteQuestionVariant;

export function getConcreteQuestionVariant(variant: QuestionVariant): ConcreteQuestionVariant {
  if ('wrappee' in variant && variant.wrappee) {
    return getConcreteQuestionVariant(variant.wrappee);
  };

  return variant as ConcreteQuestionVariant;
};

export function transformQuestionVariantFromResponse(variant: QuestionVariant): QuestionVariant {
  variant.type = variant.type.replace("Response", "");

  if ('wrappee' in variant && variant.wrappee) {
    variant.wrappee = transformQuestionVariantFromResponse(variant.wrappee);
  };

  return variant;
};

export function transformQuestionVariantToRequest(variant: QuestionVariant): QuestionVariant {
  variant.type = variant.type.replace("Response", "");

  if ('wrappee' in variant && variant.wrappee) {
    variant.wrappee = transformQuestionVariantToRequest(variant.wrappee);
    return variant;
  };

  return {
    ...variant,
    answers: undefined,
    contexts: undefined,
    original: undefined
  };
};

export type AnswersHistory = {
  uuid: UUID;
  question: UUID;
  nickname: string;
  answer: string;
};

type GameStore = {
  question?: QuestionVariant;
  game?: Game;
  history?: AnswersHistory[];
  setQuestion: (current?: QuestionVariant) => void;
  setGame: (game?: Game) => void;
  setVariants: (variants: QuestionVariant[]) => void;
  setHistory: (answers: AnswersHistory[]) => void; updateQuestion: (questionId: UUID, field: 'correctValue' | 'wrongValue', value: number) => void;
};

const useGame = create<GameStore>((set) => ({
  setQuestion: (question?: QuestionVariant) => set({ question }),
  setGame: (game?: Game) => set({ game }),
  setVariants: (variants: QuestionVariant[]) =>
    set((state) => {
      console.groupCollapsed("[useGame] Executando setVariants"); // Agrupa os logs
      console.log("[setVariants] 1. Estado ANTES da atualização:", JSON.parse(JSON.stringify(state.game?.questions)));
      console.log("[setVariants] 2. Variantes RECEBIDAS:", variants);

      if (variants.length === 0 || !state.game) {
        console.warn("[setVariants] Abortado: Sem variantes ou sem jogo no estado.");
        console.groupEnd();
        return state;
      }

      const variantsByOriginal = variants.reduce((acc, variant) => {
        const originalId = getConcreteQuestionVariant(variant).original;
        if (originalId) {
          if (!acc[originalId]) acc[originalId] = [];
          acc[originalId].push(variant);
        }
        return acc;
      }, {} as Record<UUID, QuestionVariant[]>);

      console.log("[setVariants] 3. Variantes AGRUPADAS por questão:", variantsByOriginal);

      const updatedQuestions = state.game.questions.map((question) => {
        if (variantsByOriginal[question.uuid]) {
          return { ...question, variants: variantsByOriginal[question.uuid] };
        }
        return question;
      });

      console.log("[setVariants] 4. Questões ATUALIZADAS (prontas para salvar):", updatedQuestions);
      console.groupEnd();

      return {
        game: {
          ...state.game,
          questions: updatedQuestions,
        },
      };
    }),

  setHistory: (history: AnswersHistory[]) => set({ history }),

  updateQuestion: (questionId, field, value) =>
    set((state) => {
      if (!state.game) {
        return state;
      }

      const updatedQuestions = state.game.questions.map((q) => {
        if (q.uuid === questionId) {
          return {
            ...q,
            [field]: value,
          };
        }
        return q;
      });

      return {
        game: {
          ...state.game,
          questions: updatedQuestions,
        },
      };
    }),
}));

export default useGame;
