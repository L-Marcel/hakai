import { UUID } from "crypto";
import { create } from "zustand";
import { User } from "./useAuth";

export type Participant = {
  uuid: UUID;
  nickname: string;
  score: number;
  user?: User;
};

export type Room = {
  code: string;
  owner: string;
  participants: Participant[];
  socket?: WebSocket;
};

type RoomStore = {
  room?: Room;
  participant?: Participant;
  check: (code?: string) => Promise<Result>;
  connect: (room: Room) => Promise<void>;
  create: (game: UUID) => Promise<Result<string>>;
  join: (nickname: string, user?: User) => Promise<Result>;
};

const useRoom = create<RoomStore>((set, get) => ({
  participants: [],
  check: async (code?: string) => {
    const response = await fetch("http://localhost:8080/rooms/" + code, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      return {
        ok: true,
        value: code,
      };
    } else {
      const error = await response.json();
      return {
        ok: false,
        error,
      };
    }
  },
  connect: async (room: Room) => {
    room.socket = new WebSocket("http://localhost:8080/websocket");
    room.socket.onopen = () => {
      console.log("Connected!");
    };
    set({ room });
  },
  create: async (game: UUID) => {
    const response = await fetch("http://localhost:8080/rooms/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ uuid: game }),
    });

    if (response.ok) {
      const { code, owner } = await response.json();
      const room: Room = {
        code,
        owner,
        participants: [],
      };

      await get().connect(room);
      return {
        ok: true,
        value: code,
      };
    } else {
      const error = await response.json();
      return {
        ok: false,
        error,
      };
    }
  },
  join: async (nickname: string, user?: User) => {
    const code = get().room?.code;
    const response = await fetch("http://localhost:8080/rooms/" + code + "/join", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        nickname,
        user
      }),
    });

    if (response.ok) {
      const { uuid, nickname, score, room } = await response.json();
      const participant: Participant = {
        uuid,
        nickname,
        score,
        user
      };

      await get().connect(room);
      return {
        ok: true,
        value: participant,
      };
    } else {
      const error = await response.json();
      return {
        ok: false,
        error,
      };
    }
  },
}));

export default useRoom;
