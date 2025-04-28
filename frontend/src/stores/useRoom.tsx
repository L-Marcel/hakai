import { UUID } from "crypto";
import { create } from "zustand";

export type Participant = {
    uuid: UUID;
    nickname: string;
    score: number;
};

export type Room = {
    code: string;
    owner: string;
    participants: Participant[];
    socket?: WebSocket;
};

type RoomStore = {
    room?: Room;
    connect: (room: Room) => Promise<void>;
    create: (game: UUID) => Promise<Result<string>>;
    join: () => Promise<void>;
};

const useRoom = create<RoomStore>(
    (set, get) => ({
        code: "",
        participants: [],
        connect: async(room: Room) => {
            room.socket = new WebSocket("http://localhost:8080/websocket");
            room.socket.onopen = () => {
                console.log("Connected!");
            };
            set({ room });
        },
        create: async(game: UUID) => {
            const response = await fetch("http://localhost:8080/rooms/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ uuid: game }),
            });

            if(response.ok) {
                const { code, owner } = await response.json();
                const room: Room = {
                    code,
                    owner,
                    participants: []
                };

                await get().connect(room);
                return {
                    ok: true,
                    value: code
                };
            } else {
                const error = await response.json();
                return {
                    ok: false,
                    error,
                };
            };
        },
        join: async() => {
           
        },
    })
);

export default useRoom;