import { UUID } from "crypto";
import { create } from "zustand";

export enum Difficulty {
  Easy = 1,
  Medium = 2,
  Hard = 3,
}

export const difficultyToString: Record<Difficulty, string> = {
  [Difficulty.Easy]: "Fácil",
  [Difficulty.Medium]: "Média",
  [Difficulty.Hard]: "Difícil",
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
  answer: string;
  variants?: QuestionVariant[];
  context: string[];
};

export type QuestionVariant = {
  uuid: UUID;
  difficulty: Difficulty;
  context: string[];
  question: string;
  options: string[];
  original: UUID;
};

type GameStore = {
  question?: QuestionVariant;
  game?: Game;
  setQuestion: (current?: QuestionVariant) => void;
  setGame: (game?: Game) => void;
  setVariants: (variants: QuestionVariant[]) => void;
};

const useGame = create<GameStore>((set) => ({
  setQuestion: (question?: QuestionVariant) => set({ question }),
  setGame: (game?: Game) => set({ game }),
  setVariants: (variants: QuestionVariant[]) =>
    set((state) => {
      if (variants.length === 0 || !state.game) return state;

      const questions = state.game.questions.map((question) => {
        if (question.uuid === variants[0].original) {
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
}));

export default useGame;
