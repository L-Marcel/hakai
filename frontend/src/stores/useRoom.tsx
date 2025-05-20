import { UUID } from "crypto";
import { create } from "zustand";
import { Client } from "@stomp/stompjs";
import { Difficult } from "@stores/useGame";

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
  difficult: Difficult;
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
  getNextDifficult: () => Difficult;
};

const useRoom = create<RoomStore>((set, get) => ({
  setClient: (client?: Client) => set({ client }),
  setRoom: (room?: Room) => set({ room }),
  setParticipant: (participant?: Participant) => set({ participant }),
  history: [],
  setHistory: (attempt) => set((state) =>({
    history: [...state.history, attempt]
  })),
  getNextDifficult: () => {
    const history = get().history;
    if (history.length === 0) return Difficult.Medium;

    const last = history[history.length - 1];
    
    return last.correct
      ? Math.min(Difficult.Hard, last.difficult + 1)
      : Math.max(Difficult.Easy, last.difficult - 1);
  },
}));

export default useRoom;
