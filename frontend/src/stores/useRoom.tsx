import { UUID } from "crypto";
import { create } from "zustand";
import { Client } from "@stomp/stompjs";

export type Participant = {
  uuid: UUID;
  user?: UUID;
  nickname: string;
  score: number;
};

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
  setClient: (client?: Client) => void;
  setRoom: (room?: Room) => void;
  setParticipant: (participant?: Participant) => void;
};

const useRoom = create<RoomStore>((set) => ({
  setClient: (client?: Client) => set({ client }),
  setRoom: (room?: Room) => set({ room }),
  setParticipant: (participant?: Participant) => set({ participant })
}));

export default useRoom;
