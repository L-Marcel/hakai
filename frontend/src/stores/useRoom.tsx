import { UUID } from "crypto";
import { create } from "zustand";
import { Client } from "@stomp/stompjs";
import { Difficulty } from "@stores/useGame";

export type Participant = {
  uuid: UUID;
  user?: UUID;
  nickname: string;
  score: number;
};

export type QuestionAttempt = {
  question: UUID;
  chosenOption: string;
  correct: boolean;
  difficulty: Difficulty;
}

export type Room = {
  code: string;
  owner: UUID;
  game: UUID;
  participants: Participant[];
  ready: boolean;
};

type RoomStore = {
  client?: Client;
  room?: Room;
  participant?: Participant;
  history: QuestionAttempt[]; 
  setClient: (client?: Client) => void;
  setRoom: (room?: Room) => void;
  setParticipant: (participant?: Participant) => void;
  setHistory: (attempt: QuestionAttempt) => void;
  getNextDifficulty: () => Difficulty;
};

const useRoom = create<RoomStore>((set, get) => ({
  setClient: (client?: Client) => set({ client }),
  setRoom: (room?: Room) => set({ room }),
  setParticipant: (participant?: Participant) => set({ participant }),
  history: [],
  setHistory: (attempt) => set((state) =>({
    history: [...state.history, attempt]
  })),
  getNextDifficulty: () => {
    const history = get().history;
    if (history.length === 0) return Difficulty.Medium;

    const last = history[history.length - 1];

    return last.correct
      ? Math.min(Difficulty.Hard, last.difficulty + 1)
      : Math.max(Difficulty.Easy, last.difficulty - 1);
  },
}));

export default useRoom;
