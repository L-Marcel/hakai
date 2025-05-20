import { Client } from "@stomp/stompjs";
import useGame, { QuestionVariant } from "@stores/useGame";
import useRoom, { Room } from "@stores/useRoom";
import { UUID } from "crypto";
import { getRoom } from "./room";

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
  const { room, setRoom, setClient, getNextDifficulty, client: oldClient } = useRoom.getState();
  const { setVariants, setQuestion } = useGame.getState();

  if (oldClient && oldClient.connected) return;

  const client: Client = new Client({
    brokerURL: `${import.meta.env.VITE_WEBSOCKET_URL}/websocket`,
    reconnectDelay: 5000,
    onConnect: () => {
      client.subscribe("/channel/events/rooms/" + code + "/closed", () =>
        disconnect()
      );

      client.subscribe(
        "/channel/events/rooms/" + code + "/participants/entered",
        (message) => {
          const room: Room = JSON.parse(message.body);
          setRoom(room);
        }
      );


      if (participant) {
        getRoom(code);

        client.subscribe(
          "/channel/events/rooms/" + code + "/question",
          (message) => {
            const variants: QuestionVariant[] = JSON.parse(message.body);
            if (variants.length > 0) {
              const nextDifficulty = getNextDifficulty();
              const selected = variants.find(
                (variant) => variant.difficulty === nextDifficulty
              );

              setQuestion(selected);
            };
          }
        );
      };

      if (isOwner) {
        client.subscribe(
          "/channel/events/rooms/" + code + "/" + room?.owner,
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