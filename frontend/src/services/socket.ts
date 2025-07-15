import { Client } from "@stomp/stompjs";
import useGame, {
  getConcreteQuestionVariant,
  putScoreValueInConcreteQuestionVariant,
  QuestionVariant,
  transformQuestionVariantFromResponse,
} from "@stores/useGame";
import useRoom, { Room } from "@stores/useRoom";
import { UUID } from "crypto";
import { getRoom } from "./room";
import useGenerationStatus from "@stores/useStatus";
import api from "./axios";

export function disconnect(): void {
  const { setRoom, setParticipant, setClient } = useRoom.getState();
  const { setGame, setQuestions } = useGame.getState();

  setClient(undefined);
  setRoom(undefined);
  setClient(undefined);
  setParticipant(undefined);
  setGame(undefined);
  setQuestions([]);
}
type ScoringResponse = {
  question: string;
  correctValue: number;
  wrongValue: number;
};

async function fetchQuestionScoring(uuid: string): Promise<ScoringResponse> {
  return api.get<ScoringResponse>(`questions/${uuid}/scoring`).then((response) => response.data);
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

                  if (originalUuid) {
                    try {
                      const scoring = await fetchQuestionScoring(originalUuid);
                      variant = putScoreValueInConcreteQuestionVariant(
                        variant,
                        scoring.correctValue,
                        scoring.wrongValue
                      );
                    } catch (error) {
                      console.warn(
                        "Erro ao buscar pontuação para",
                        originalUuid,
                        error
                      );
                    }
                  }

                  return {
                    ...variant,
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
