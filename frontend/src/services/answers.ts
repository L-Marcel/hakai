import useGame, { AnswersHistory } from "@stores/useGame";
import { UUID } from "crypto";
import api from "./axios";

export async function getGameAnswers(uuid?: UUID): Promise<void> {
  const { setHistory } = useGame.getState();
  return await api
    .get<AnswersHistory[]>(`answers/game/${uuid}`)
    .then((response) => {
      setHistory(response.data);
    });
}
