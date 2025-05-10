import { UUID } from "crypto";
import { create } from "zustand";

enum Difficult {
  Easy = 1,
  Medium = 2,
  Hard = 3,
}

export const difficultToString: Record<Difficult, string> = {
  [Difficult.Easy]: "Fácil",
  [Difficult.Medium]: "Média",
  [Difficult.Hard]: "Difícil",
};

export type Question = {
  uuid: UUID;
  question: string;
  answer: string;
  variants: QuestionVariant[];
};

export type QuestionVariant = {
  uuid: UUID;
  level: Difficult;
  context: string[];
  question: string;
  options: string[];
  original: UUID;
};

type QuestionVariantsStore = {
  current?: QuestionVariant;
  questions: Question[];
  setCurrent: (current: QuestionVariant) => void;
  setQuestions: (questions: Question[]) => void;
  setVariants: (variants: QuestionVariant[]) => void;
};

const useQuestions = create<QuestionVariantsStore>((set) => ({
  questions: [],
  setCurrent: (current: QuestionVariant) => set({ current }),
  setQuestions: (questions: Question[]) => set({ questions }),
  setVariants: (variants: QuestionVariant[]) =>
    set((state) => {
      if (variants.length === 0) return state;

      const questions = state.questions.map((question) => {
        if (question.uuid === variants[0].original) {
          return {
            ...question,
            variants,
          };
        }
        return question;
      });

      return { questions };
    }),
}));

export default useQuestions;
