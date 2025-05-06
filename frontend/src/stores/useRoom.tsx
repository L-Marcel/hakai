import { UUID } from "crypto";
import { create } from "zustand";
import { Client } from "@stomp/stompjs";

export type QuestionVariant = {
  uuid: UUID,
  level: number;
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
  check: (code?: string) => Promise<Result>;
  connect: (code?: string, participant?: UUID) => Promise<void>;
  create: (game: UUID) => Promise<Result<string>>;
  join: (nickname: string, code?: string) => Promise<Result>;
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
  connect: async (code?: string, participant?: UUID) => {
    const client: Client = new Client({
      brokerURL: "ws://localhost:8080/websocket",
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe(
          "/channel/events/rooms/" + code + "/participants/entered", 
          (message) => {
            const room: Room = JSON.parse(message.body);
            set({ room });
          }
        );

        if(participant) {
          const subscription = client.subscribe(
            "/channel/events/rooms/" + code + "/" + participant + "/entered", 
            (message) => {
              const room: Room = JSON.parse(message.body);
              set({ room });
              subscription.unsubscribe();
            },
          );

          client.publish({
            destination: "/channel/triggers/rooms/" + code + "/" + participant,
          });
        };
      },
      onDisconnect: () => {
        set({ 
          client: undefined,
          room: undefined,
          participant: undefined
        });
      }
    });

    client.activate();
    set({ client });
  },
  create: async (game: UUID) => {
    const response = await fetch("http://localhost:8080/rooms/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ game }),
    });

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
  join: async (nickname: string, code?: string) => {
    const response = await fetch("http://localhost:8080/rooms/" + code + "/join", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        nickname
      }),
    });

    if (response.ok) {
      const participant: Participant = await response.json();
      await get().connect(code, participant.uuid);
      set({ participant });
      return {
        ok: true
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
