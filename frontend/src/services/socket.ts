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
  const { room, setRoom, setClient, client: oldClient } = useRoom.getState();
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
        "/channel/events/rooms/" + code + "/updated",
        (message) => {
          const room: Room = JSON.parse(message.body);
          setRoom(room);
        }
      );

      client.subscribe(
        "/channel/events/rooms/" + code + "/question",
        (message) => {
          const variants: QuestionVariant[] = JSON.parse(message.body);
          if (variants.length > 0) {
            const current = useRoom.getState().participant?.currentDifficulty;

            let currentNum: number | undefined;
            if(current === "EASY"){
              currentNum = 1;
            } else if (current === "NORMAL") {
              currentNum = 2;
            } else if (current === "HARD") {
              currentNum = 3;
            }
            const selected = variants.find(
              (v) => v.difficulty === currentNum
            );

            if (selected) {
              setQuestion(selected);
            }
          }
        }
      );

      if (participant) getRoom(code);

      if (isOwner) {
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
