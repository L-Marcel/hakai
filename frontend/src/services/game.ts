import useGame, { QuestionVariant, Game } from "@stores/useGame";
import useRoom from "@stores/useRoom";
import { UUID } from "crypto";
import api from "./axios";

export async function request(uuid?: UUID): Promise<void> {
  const { setGame } = useGame.getState();

  return await api.get<Game>(`games/${uuid}`).then((response) => {
    setGame(response.data);
  });
}

export function sendQuestion(variants: QuestionVariant[]): void {
  const client = useRoom.getState().client;
  const room = useRoom.getState().room;

  if (!room || !client) return;

  client.publish({
    destination: "/channel/events/rooms/" + room.code + "/question",
    body: JSON.stringify(variants),
  });
}

export async function requestAllGames(): Promise<Game[]> {
  return await api.get<Game[]>("games").then((response) => {
    return response.data;
  });
}
