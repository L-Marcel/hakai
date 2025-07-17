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

export type QuestionWithFeedbackVariant = {
  type: string;
  uuid: UUID;
  question: string;
  difficulty: Difficulty;
  options: string[];
  contexts?: string[];
  answers?: string[];
  original?: UUID;
  feedback: string;
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

export type QuestionVariant = BaseQuestionVariant | ConcreteQuestionVariant | QuestionWithFeedbackVariant;

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

export function alredyRecivedAllVariants(): boolean {
  const questions = useGame.getState().game?.questions;

  if (!questions || questions.length === 0) return false;

  const expectedTotalVariants = questions.length * 3;
  const actualTotalVariants = questions.reduce((count, question) => {
    return count + (question.variants?.length || 0);
  }, 0);

  return actualTotalVariants === expectedTotalVariants;
}


export type AnswersHistory = {
  uuid: UUID;
  question: UUID;
  nickname: string;
  answer: string;
};

type GameStore = {
  currentQuestions?: QuestionVariant[];
  game?: Game;
  history?: AnswersHistory[];
  setQuestions: (currentQuestions?: QuestionVariant[]) => void;
  setGame: (game?: Game) => void;
  setVariants: (variants: QuestionVariant[]) => void;
  setHistory: (answers: AnswersHistory[]) => void; updateQuestion: (questionId: UUID, field: 'correctValue' | 'wrongValue', value: number) => void;
};

const useGame = create<GameStore>((set) => ({
  setQuestions: (currentQuestions?: QuestionVariant[]) => set({ currentQuestions }),
  setGame: (game?: Game) => set({ game }),
  setVariants: (newVariants: QuestionVariant[]) =>
    set((state) => {
      if (newVariants.length === 0 || !state.game) return state;

      const groupedByOriginal: Record<string, QuestionVariant[]> = {};

      newVariants.forEach((variant) => {
        const originalId = getConcreteQuestionVariant(variant).original;
        if (!originalId) return;
        if (!groupedByOriginal[originalId]) {
          groupedByOriginal[originalId] = [];
        }
        groupedByOriginal[originalId].push(variant);
      });

      const questions = state.game.questions.map((question) => {
        const additionalVariants = groupedByOriginal[question.uuid];
        if (!additionalVariants) return question;

        const existingVariants = question.variants || [];

        // Evitar duplicatas comparando UUIDs
        const allVariants = [
          ...existingVariants,
          ...additionalVariants.filter(
            (v) =>
              !existingVariants.some(
                (existing) => getConcreteQuestionVariant(existing).uuid === getConcreteQuestionVariant(v).uuid
              )
          ),
        ];

        return {
          ...question,
          variants: allVariants,
        };
      });

      return {
        game: {
          ...state.game,
          questions,
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
            [field]: value, // Atualiza 'correctValue' ou 'wrongValue'
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
