import { Client } from "@stomp/stompjs";
import useGame, { alredyRecivedAllVariants, QuestionVariant, transformQuestionVariantFromResponse } from "@stores/useGame";
import useRoom, { Room } from "@stores/useRoom";
import { UUID } from "crypto";
import { getRoom } from "./room";
import useGenerationStatus from "@stores/useStatus";
import { sendAllQuestions } from "./question";

export function disconnect(): void {
  const { setRoom, setParticipant, setClient } = useRoom.getState();
  const { setGame, setQuestions } = useGame.getState();

  setClient(undefined);
  setRoom(undefined);
  setClient(undefined);
  setParticipant(undefined);
  setGame(undefined);
  setQuestions(undefined);
}

export function connect(
  code?: string,
  participant?: UUID,
  isOwner?: boolean
): void {
  const { room, setRoom, setClient, client: oldClient } = useRoom.getState();
  const { setVariants, setQuestions } = useGame.getState();

  if (oldClient && oldClient.connected) return;

  console.log("Usuário conectado ao socket!");

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
          (message) => {
            const playload: QuestionVariant[] = JSON.parse(message.body);
            const formattedVariantsList: QuestionVariant[] = [];
            playload.forEach(variant => {
              const formattedVariant = transformQuestionVariantFromResponse(variant)
              formattedVariantsList.push(formattedVariant);
            });
            setQuestions(formattedVariantsList);
            console.log("CURRENT QUESTIONS POPULADAS", formattedVariantsList);
          }
        );
      }
      
      if (participant) getRoom(code);

      if (isOwner) {
        client.subscribe(
          "/channel/events/rooms/" + code + "/" + room?.owner + "/variants",
          (message) => {
            const variants: QuestionVariant[] = JSON.parse(message.body);
            const formattedVariants = variants.map(transformQuestionVariantFromResponse);
            setVariants(formattedVariants);
            setTimeout(() => {
              if(alredyRecivedAllVariants()) sendAllQuestions();
            }, 100);
          }
        );
      }
    },
  });

  client.activate();
  setClient(client);
}