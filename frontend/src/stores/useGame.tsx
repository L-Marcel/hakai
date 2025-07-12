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
  setHistory: (answers: AnswersHistory[]) => void;
};

const useGame = create<GameStore>((set) => ({
  setQuestion: (question?: QuestionVariant) => set({ question }),
  setGame: (game?: Game) => set({ game }),
  setVariants: (variants: QuestionVariant[]) =>
    set((state) => {
      if (variants.length === 0 || !state.game) return state;
      const firstVariantData = getConcreteQuestionVariant(variants[0]);

      const questions = state.game.questions.map((question) => {
        if (question.uuid === firstVariantData.original) {
          return {
            ...question,
            variants,
          };
        }
        return question;
      });

      return {
        game: {
          ...state.game,
          questions,
        },
      };
    }),
  setHistory: (history: AnswersHistory[]) => set({ history }),
}));

export default useGame;
