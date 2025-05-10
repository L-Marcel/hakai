import { Client } from "@stomp/stompjs";
import useQuestions, { QuestionVariant } from "@stores/useQuestions";
import useRoom, { Room } from "@stores/useRoom";
import { UUID } from "crypto";

export function disconnect(): void {
  const { client, setRoom, setParticipant, setClient, setExists } =
    useRoom.getState();

  if (client) client.deactivate();
  setRoom(undefined);
  setClient(undefined);
  setExists(false);
  setParticipant(undefined);
}

export function connect(
  code?: string,
  participant?: UUID,
  isOwner?: boolean
): void {
  const { room, setRoom, setClient } = useRoom.getState();
  const { setVariants } = useQuestions.getState();
  const client: Client = new Client({
    brokerURL: `${import.meta.env.VITE_WEBSOCKET_URL}/websocket`,
    reconnectDelay: 5000,
    onConnect: () => {
      console.log("abc");
      client.subscribe(
        "/channel/events/rooms/" + code + "/closed",
        (message) => {
          console.log(message);
          disconnect();
        }
      );

      client.subscribe(
        "/channel/events/rooms/" + code + "/participants/entered",
        (message) => {
          const room: Room = JSON.parse(message.body);
          setRoom(room);
        }
      );

      if (participant) {
        const subscription = client.subscribe(
          "/channel/events/rooms/" + code + "/" + participant + "/entered",
          (message) => {
            const room: Room = JSON.parse(message.body);
            setRoom(room);
            subscription.unsubscribe();
          }
        );

        client.publish({
          destination: "/channel/triggers/rooms/" + code + "/" + participant,
        });
      }

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
    onDisconnect: disconnect,
    onWebSocketError: disconnect,
  });

  client.activate();
  setClient(client);
}
