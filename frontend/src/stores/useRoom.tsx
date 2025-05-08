import { UUID } from "crypto";
import { create } from "zustand";
import { Client } from "@stomp/stompjs";

enum Difficult {
  Easy,
  Medium,
  Hard,
}

export const difficultToString: Record<Difficult, string> = {
  [Difficult.Easy]: "Fácil",
  [Difficult.Medium]: "Média",
  [Difficult.Hard]: "Difícil",
};

export type QuestionVariant = {
  uuid: UUID;
  level: Difficult;
  context: string[];
  question: string;
  options: string[];
};

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
  check: (code?: string) => Promise<Result>;
  disconnect: () => void;
  connect: (code?: string, participant?: UUID) => Promise<void>;
  create: (game: UUID) => Promise<Result<string>>;
  close: (code: string) => Promise<Result>;
  join: (nickname: string, code?: string) => Promise<Result>;
};

const useRoom = create<RoomStore>((set, get) => ({
  participants: [],
  exists: false,
  check: async (code?: string) => {
    const response = await fetch(
      `${import.meta.env.VITE_BACKEND_URL}/rooms/${code}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (response.ok) {
      set({ exists: true });
      return {
        ok: true,
      };
    } else {
      const error = await response.json();
      return {
        ok: false,
        error,
      };
    }
  },
  disconnect: () => {
    get().client?.deactivate();
    set((state) => ({
      ...state,
      exists: false,
      client: undefined,
      room: undefined,
      participant: undefined,
    }));
  },
  connect: async (code?: string, participant?: UUID) => {
    const client: Client = new Client({
      brokerURL: `${import.meta.env.VITE_WEBSOCKET_URL}/websocket`,
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("abc");
        client.subscribe(
          "/channel/events/rooms/" + code + "/closed",
          (message) => {
            console.log(message);
            get().disconnect();
          }
        );

        client.subscribe(
          "/channel/events/rooms/" + code + "/participants/entered",
          (message) => {
            const room: Room = JSON.parse(message.body);
            set({ room });
          }
        );

        if (participant) {
          const subscription = client.subscribe(
            "/channel/events/rooms/" + code + "/" + participant + "/entered",
            (message) => {
              const room: Room = JSON.parse(message.body);
              set({ room });
              subscription.unsubscribe();
            }
          );

          client.publish({
            destination: "/channel/triggers/rooms/" + code + "/" + participant,
          });
        }
      },
      onDisconnect: () => get().disconnect(),
      onWebSocketError: () => get().disconnect(),
    });

    client.activate();
    set({ client });
  },
  create: async (game: UUID) => {
    const response = await fetch(
      `${import.meta.env.VITE_BACKEND_URL}/rooms/create`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ game }),
      }
    );

    if (response.ok) {
      const room: Room = await response.json();
      await get().connect(room.code);
      return {
        ok: true,
        value: room.code,
      };
    } else {
      const error = await response.json();
      return {
        ok: false,
        error,
      };
    }
  },
  close: async (code: string) => {
    const response = await fetch(
      `${import.meta.env.VITE_BACKEND_URL}/rooms/${code}/close`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (response.ok) {
      get().disconnect();
      return {
        ok: true,
      };
    } else {
      const error = await response.json();
      return {
        ok: false,
        error,
      };
    }
  },
  join: async (nickname: string, code?: string) => {
    const response = await fetch(
      `${import.meta.env.VITE_BACKEND_URL}/rooms/${code}/join`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          nickname,
        }),
      }
    );

    if (response.ok) {
      const participant: Participant = await response.json();
      await get().connect(code, participant.uuid);
      set({ participant });
      return {
        ok: true,
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
