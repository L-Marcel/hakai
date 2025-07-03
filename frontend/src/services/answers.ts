import useGame, { AnswersHistory } from "@stores/useGame";
import useRoom from "@stores/useRoom";
import { UUID } from "crypto";
import api from "./axios";

export async function saveParticipantAnswer(answers: string[], question: UUID) {
  const { participant } = useRoom.getState();
  const { setHistory} = useGame.getState();
  
  console.log("SaveParticipant disparado!");
  
  if(participant && question) {
    return api
      .post("/answers", {
        answers,
        question,
        participant: participant?.uuid,
      })
      .then(() => {
        
          const history: AnswersHistory = {
            uuid: crypto.randomUUID(),
            question: question as UUID,
            nickname: participant.nickname,
            answer: answers,
          }
          setHistory(history);
        }
      );
  }
}


export async function getGameAnswers(code: UUID): Promise<void> {
    const { setHistory } = useGame.getState();
    return await api.get<AnswersHistory>(`answers/game/${code}`).then((response) => {
        console.log(response);
        setHistory(response.data);
    });
}