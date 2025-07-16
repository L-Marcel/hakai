import useRoom, { Participant, Room } from "@stores/useRoom";
import { connect, disconnect } from "./socket";
import { UUID } from "crypto";
import api from "./axios";
import useAuth from "@stores/useAuth";

export async function join(nickname: string, code?: string): Promise<void> {
  const { setParticipant } = useRoom.getState();

  return await api
    .post<Participant>(`rooms/${code}/join`, {
      nickname,
    })
    .then((response) => {
      connect(response.data.room, response.data.uuid, true);
      setParticipant(response.data);
    });
}

export async function kick(participant: UUID): Promise<void> {
  return await api.delete(`rooms/participants/${participant}`);
}

export async function close(): Promise<void> {
  return await api.delete("rooms").then(() => {
    disconnect();
  });
}

export async function create(game: UUID): Promise<string> {
  const {user} = useAuth.getState();
  return await api.post<Room>("rooms", { game }).then((response) => {
    join(user!.name, response.data.code);
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

export async function getRoomForClone(code?: string): Promise<Room> {
  return await api.get<Room>(`rooms/${code}`).then((response) => {
    return response.data;
  });
}