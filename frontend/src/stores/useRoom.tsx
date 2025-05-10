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
  participants: Participant[];
  ready: boolean;
};

type RoomStore = {
  client?: Client;
  room?: Room;
  participant?: Participant;
  exists: boolean;
  setClient: (client?: Client) => void;
  setRoom: (room?: Room) => void;
  setParticipant: (participant?: Participant) => void;
  setExists: (exists: boolean) => void;
};

const useRoom = create<RoomStore>((set) => ({
  exists: false,
  setClient: (client?: Client) => set({ client }),
  setRoom: (room?: Room) => set({ room }),
  setParticipant: (participant?: Participant) => set({ participant }),
  setExists: (exists: boolean) => set({ exists }),
}));

export default useRoom;
