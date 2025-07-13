import useGame, { Game } from "@stores/useGame";
import { UUID } from "crypto";
import api from "./axios";

export type ConcreteQuestionRequest = {
  type: "ConcreteQuestionRequest";
  question: string;
  answers: string[];
  contexts: string | string[];
};

export type BaseQuestionRequest = {
  type: string;
  wrappee: QuestionRequest;
};

export type QuestionWithFeedbackRequest = {
  type: "QuestionWithFeedbackRequest",
  wrappee: ConcreteQuestionRequest;
};


export type QuestionRequest = ConcreteQuestionRequest | BaseQuestionRequest | QuestionWithFeedbackRequest;

export interface GameRequest {
  title: string;
  questions: QuestionRequest[];
}

export async function getGame(uuid?: UUID): Promise<void> {
  const { setGame } = useGame.getState();

  return await api.get<Game>(`games/${uuid}`).then((response) => {
    setGame(response.data);
  });
}

export async function getAllGames(): Promise<Game[]> {
  return await api.get<Game[]>("games").then((response) => {
    return response.data;
  });
}
export async function createGame(payload: GameRequest): Promise<Game> {
  return api.post<Game>("games", payload).then((response) => response.data);
}
