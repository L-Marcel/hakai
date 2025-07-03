import useGame, { AnswersHistory } from "@stores/useGame";
import useRoom from "@stores/useRoom";
import { UUID } from "crypto";
import api from "./axios";

export async function saveParticipantAnswer(answers: string[], question: UUID) {
  const { participant } = useRoom.getState();
  
  if(participant && question) {
    return api
      .post("/answers", {
        answers,
        question,
        participant: participant?.uuid,
      });
  };
};

export async function getGameAnswers(uuid?: UUID): Promise<void> {
  const { setHistory } = useGame.getState();
  return await api.get<AnswersHistory[]>(`answers/game/${uuid}`).then((response) => {
    setHistory(response.data);
  });
};