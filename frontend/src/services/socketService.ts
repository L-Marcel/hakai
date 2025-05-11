import { Client } from "@stomp/stompjs";
import useGame, { QuestionVariant } from "@stores/useGame";
import useRoom, { Room } from "@stores/useRoom";
import { UUID } from "crypto";

export function disconnect(): void {
  const { client, setRoom, setParticipant, setClient } =
    useRoom.getState();

  if (client) client.deactivate();
  setRoom(undefined);
  setClient(undefined);
  setParticipant(undefined);
}

export function connect(
  code?: string,
  participant?: UUID,
  isOwner?: boolean
): void {
  const { room, setRoom, setClient } = useRoom.getState();
  const { setVariants } = useGame.getState();
  const client: Client = new Client({
    brokerURL: `${import.meta.env.VITE_WEBSOCKET_URL}/websocket`,
    reconnectDelay: 5000,
    onConnect: () => {
      client.subscribe(
        "/channel/events/rooms/" + code + "/closed",
        (message) => {
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

      client.subscribe(
        "/channel/events/rooms/" + code + "/question",
        (message) => {
          const variants: QuestionVariant[] = JSON.parse(message.body);
          if (variants.length > 0) {
            useGame.getState().setCurrent(variants[0]);
          }
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
