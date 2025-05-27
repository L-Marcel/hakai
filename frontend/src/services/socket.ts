import { Client } from "@stomp/stompjs";
import useGame, { QuestionVariant } from "@stores/useGame";
import useRoom, { Room } from "@stores/useRoom";
import { UUID } from "crypto";
import { getRoom } from "./room";
import useGenerationStatus from "@stores/useStatus";

export function disconnect(): void {
  const { setRoom, setParticipant, setClient } = useRoom.getState();
  const { setGame, setQuestion } = useGame.getState();

  setClient(undefined);
  setRoom(undefined);
  setClient(undefined);
  setParticipant(undefined);
  setGame(undefined);
  setQuestion(undefined);
}

export function connect(
  code?: string,
  participant?: UUID,
  isOwner?: boolean
): void {
  const { room, setRoom, setClient, client: oldClient } = useRoom.getState();
  const { setVariants, setQuestion } = useGame.getState();

  if(oldClient && oldClient.connected) return;

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

      client.subscribe(
        `/channel/events/rooms/${code}/status`,
        (message) => {
          useGenerationStatus.getState().setGenerationStatus(message.body);
        }
      );

      if(participant && room) {
        client.subscribe(
          "/channel/events/rooms/" + code + "/participants/" + participant + "/question",
          (message) => {
            const variant: QuestionVariant = JSON.parse(message.body);
            setQuestion(variant);
          }
        );
      }

      if(participant) getRoom(code);

      if(isOwner) {
        client.subscribe(
          "/channel/events/rooms/" + code + "/" + room?.owner + "/variants",
          (message) => {
            const variants: QuestionVariant[] = JSON.parse(message.body);
            setVariants(variants);
          }
        );
      }
    },
  });

  client.activate();
  setClient(client);
}


