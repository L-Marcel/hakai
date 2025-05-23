import { UUID } from "crypto";
import { create } from "zustand";
import { Client } from "@stomp/stompjs";
import { Difficulty } from "@stores/useGame";
import { disconnect } from "../services/socket";

export type Participant = {
  uuid: UUID;
  room: string;
  user?: UUID;
  nickname: string;
  currentDifficulty: String;
  score: number;
};

export type Room = {
  code: string;
  owner: UUID;
  game: UUID;
  participants: Participant[];
};

type RoomStore = {
  client?: Client;
  room?: Room;
  participant?: Participant;
  setClient: (client?: Client) => void;
  setRoom: (room?: Room) => void;
  setParticipant: (participant?: Participant) => void;
};

const useRoom = create<RoomStore>((set, get) => ({
  setClient: (client?: Client) => set({ client }),
  setRoom: (room?: Room) => {
    set({ room });
    
    const { participant } = get();
    if (participant) {
      const updated = room?.participants.find(p => p.uuid === participant.uuid);
      if (updated) {
        set({ participant: updated }); // <- aqui atualiza a dificuldade
      } else {
        disconnect(); // participante não existe mais na sala
      }
    }
  },
  setParticipant: (participant?: Participant) => set({ participant }),
}));

export default useRoom;
