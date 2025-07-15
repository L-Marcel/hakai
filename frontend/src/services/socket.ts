import { Client } from "@stomp/stompjs";
import useGame, {
  getConcreteQuestionVariant,
  QuestionVariant,
  transformQuestionVariantFromResponse,
} from "@stores/useGame";
import useRoom, { Room } from "@stores/useRoom";
import { UUID } from "crypto";
import { getRoom } from "./room";
import useGenerationStatus from "@stores/useStatus";

export function disconnect(): void {
  const { setRoom, setParticipant, setClient } = useRoom.getState();
  const { setGame, setQuestions } = useGame.getState();

  setClient(undefined);
  setRoom(undefined);
  setClient(undefined);
  setParticipant(undefined);
  setGame(undefined);
  setQuestions([]);
}type ScoringResponse = {
  question: string;
  correctValue: number;
  wrongValue: number;
};

async function fetchQuestionScoring(uuid: string): Promise<ScoringResponse> {
  const response = await fetch(`/api/questions/${uuid}/scoring`);
  if (!response.ok) {
    throw new Error("Erro ao buscar pontuação da questão");
  }
  return await response.json();
}

export function connect(
  code?: string,
  participant?: UUID,
  isOwner?: boolean
): void {
  const { room, setRoom, setClient, client: oldClient } = useRoom.getState();
  const { setVariants, setQuestions } = useGame.getState();

  if (oldClient && oldClient.connected) return;

  const client: Client = new Client({
    brokerURL: `${import.meta.env.VITE_WEBSOCKET_URL}/websocket`,
    reconnectDelay: 5000,
    onConnect: () => {
      client.subscribe("/channel/events/rooms/" + code + "/closed", () =>
        disconnect()
      );

      client.subscribe(
        "/channel/events/rooms/" + code + "/updated",
        (message) => {
          const room: Room = JSON.parse(message.body);
          setRoom(room);
        }
      );

      client.subscribe(`/channel/events/rooms/${code}/status`, (message) => {
        useGenerationStatus.getState().setGenerationStatus(message.body);
      });

if (participant && room) {
  client.subscribe(
    `/channel/events/rooms/${code}/participants/${participant}/question`,
    async (message) => {
      const payload: QuestionVariant[] = JSON.parse(message.body);

      if (Array.isArray(payload)) {
        const variants: QuestionVariant[] = payload.map((item) =>
          transformQuestionVariantFromResponse(item)
        );
        const formattedVariants: QuestionVariant[] = await Promise.all(
          variants.map(async (variant) => {
            const concreteVariant = getConcreteQuestionVariant(variant);
            const originalUuid = concreteVariant.original;

            let correctValue = 0;
            let wrongValue = 0;

            if (originalUuid) {
              try {
                const scoring = await fetchQuestionScoring(originalUuid);
                correctValue = scoring.correctValue;
                wrongValue = scoring.wrongValue;
              } catch (error) {
                console.warn("Erro ao buscar pontuação para", originalUuid, error);
              }
            }

            return {
              ...variant,
              correctValue,
              wrongValue,
            };
          })
        );

        setQuestions(formattedVariants);
      }
    }
  );
}

      if (participant) getRoom(code);

      if (isOwner) {
        client.subscribe(
          "/channel/events/rooms/" + code + "/" + room?.owner + "/variants",
          (message) => {
            const variants: QuestionVariant[] = JSON.parse(message.body);
            const formattedVariants = variants.map(
              transformQuestionVariantFromResponse
            );
            setVariants(formattedVariants);
          }
        );
      }
    },
  });

  client.activate();
  setClient(client);
}
