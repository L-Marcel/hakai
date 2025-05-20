import useRoom, { Participant, Room } from "@stores/useRoom";
import { connect, disconnect } from "./socket";
import { UUID } from "crypto";
import api from "./axios";

export async function join(nickname: string, code?: string): Promise<void> {
  const { setParticipant } = useRoom.getState();
  return await api
    .post<Participant>(`rooms/${code}/join`, {
      nickname,
    })
    .then((response) => {
      connect(code, response.data.uuid);
      setParticipant(response.data);
    });
}

export async function close(): Promise<void> {
  return await api.post("rooms/close").then(() => {
    disconnect();
  });
}

export async function create(game: UUID): Promise<string> {
  return await api
    .post<Room>("rooms/create", {
      game,
    })
    .then((response) => {
      connect(response.data.code);
      return response.data.code;
    });
}

export async function getRoom(code?: string): Promise<void> {
  const { setRoom } = useRoom.getState();

  return await api.get<Room>(`rooms/${code}`).then((response) => {
    setRoom(response.data);
  });
}

export async function getOpenRoom(): Promise<Room> {
  return await api.get<Room>("rooms").then((response) => {
    return response.data;
  });
}
