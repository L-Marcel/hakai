import { Client } from "@stomp/stompjs";
import useGame, { getConcreteQuestionVariant, Question, QuestionVariant, transformQuestionVariantFromResponse } from "@stores/useGame";
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
          "/channel/events/rooms/" +
            code +
            "/participants/" +
            participant +
            "/question",
          (message) => {
            console.log("Recebida lista de questões do jogo!", message.body);
            const payload: any = JSON.parse(message.body);

            if (Array.isArray(payload)) {
  console.log("[DEBUG 1] Payload recebido e é um array. Tamanho:", payload.length);

  const variantsReceived: QuestionVariant[] = payload.map(item => transformQuestionVariantFromResponse(item.wrappee));
  console.log("[DEBUG 2] Variantes extraídas e formatadas:", variantsReceived);

  const { game, setGame } = useGame.getState(); 

  const playableQuestions: Question[] = variantsReceived.map((variant, index) => {
    console.log(`[DEBUG 3.${index}] Mapeando variante:`, variant);
    
    const concreteVariant = getConcreteQuestionVariant(variant);
    const originalUuid = concreteVariant.original;
    console.log(`[DEBUG 3.${index}] UUID Original da variante:`, originalUuid);
    const originalQuestion = game?.questions.find(q => q.uuid === originalUuid);
    console.log(`[DEBUG 3.${index}] Questão Original encontrada no estado?`, originalQuestion);

    return {
      uuid: concreteVariant.uuid,
      question: concreteVariant.question,
      answers: concreteVariant.options,
      contexts: concreteVariant.contexts ?? [],
      wrongValue: originalQuestion?.wrongValue ?? 0,
      correctValue: originalQuestion?.correctValue ?? 0,
      variants: [variant],
    };
  });

  console.log("[DEBUG 4] Array final 'playableQuestions' pronto para ser salvo:", playableQuestions);

  if (game) {
    setGame({
      ...game,
      questions: playableQuestions,
    });
    console.log("[DEBUG 5] setGame FOI CHAMADO! O estado deveria ter sido atualizado.");
  } else {
    console.error("[DEBUG ERRO] O objeto 'game' não foi encontrado no estado na hora de salvar as questões!");
  }
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
            const formattedVariants = variants.map(transformQuestionVariantFromResponse);
            setVariants(formattedVariants);
          }
        );
      }
    },
  });

  client.activate();
  setClient(client);
}
