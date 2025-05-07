import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";
import RoomGuard from "@components/Guards/RoomGuard";
import ParticipantsMansoryGrid from "@components/Grid/ParticipantsGrid";
import { Participant, QuestionVariant } from "@stores/useRoom";
import Button from "@components/Button";
import {
  FaArrowLeft,
  FaArrowRight,
  FaBomb,
  FaPlay,
  FaSignOutAlt,
  FaSync,
} from "react-icons/fa";
import { useParams } from "react-router-dom";
import { FaUserGroup } from "react-icons/fa6";
import QuestionVariantsCarousel from "@components/Carousel";
import QuestionView from "@components/Views/Question";

export default function RoomPanelPage() {
  return (
    <AuthGuard>
      <RoomGuard>
        <Page />
      </RoomGuard>
    </AuthGuard>
  );
}

function Page() {
  const { code } = useParams();

  const mockedQuestionVariants: QuestionVariant[] = [
    {
      uuid: "b8b6d0e3-5a13-4f5c-9d38-81f79d44e530",
      level: 0,
      context: ["Docker", "Container", "Configurações"],
      question:
        "Qual parâmetro do Docker impede que um container reinicie automaticamente?",
      options: ["--restart=always", "--restart=no", "--restart=on-failure"],
    },
    {
      uuid: "62a7f3c0-68d4-4604-8733-fcaa3e8d0f4c",
      level: 0,
      context: ["Docker", "Container", "Configurações"],
      question:
        "Ao criar um container Docker, qual opção desativa o reinício automático?",
      options: [
        "--restart=unless-stopped",
        "--restart=always",
        "--restart=no",
        "--restart=on-failure",
      ],
    },
    {
      uuid: "48f76efb-0e7c-4649-b627-59bc6599a397",
      level: 1,
      context: ["Docker", "Container", "Configurações"],
      question:
        "Qual das seguintes políticas de --restart garante que o container nunca será reiniciado automaticamente em nenhuma situação?",
      options: [
        "--restart=unless-stopped",
        "--restart=on-failure:3",
        "--restart=always",
        "--restart=on-failure",
        "--restart=no",
      ],
    },
    {
      uuid: "3e7f3ae5-ef08-4b74-a9ae-62aa6204f6c0",
      level: 2,
      context: ["Docker", "Container", "Configurações"],
      question:
        "Um engenheiro DevOps precisa garantir que um container só inicie quando explicitamente ordenado, sem reinício automático após falhas ou reboot do host. Qual política --restart se aplica?",
      options: [
        "--restart=always",
        "--restart=on-failure",
        "--restart=unless-stopped",
        "--restart=no",
        "--restart=on-failure:5",
      ],
    },
    {
      uuid: "a6e173f7-f57b-4bd1-8a7f-eaa4c7730c5d",
      level: 2,
      context: ["Docker", "Container", "Configurações"],
      question:
        "Considerando as seguintes opções de --restart do Docker, qual garante que um container nunca será reiniciado automaticamente, mesmo após uma falha ou reboot do sistema?",
      options: [
        "--restart=unless-stopped",
        "--restart=no",
        "--restart=manual",
        "--restart=on-error",
        "--restart=on-failure:0",
        "--restart=always",
      ],
    },
  ];

  const mockedParticipants: Participant[] = [
    {
      nickname: "Lucas Marcel Silva de Brito",
      score: 100,
      uuid: "5e5b2e43-1b9b-4c07-9e2a-7eeb8eae18c1",
    },
    {
      nickname: "Gina Marcele de Sousa Silva",
      score: 190,
      uuid: "0c863b92-d985-4d40-9212-8753dc2e0e79",
    },
    {
      nickname: "Marcela Silva Batista",
      score: 180,
      uuid: "134f99f1-3df6-4976-b47e-4a6bfe676816",
    },
    {
      nickname: "Maria Clara Fernandes",
      score: 200,
      uuid: "df5a6b86-1e9d-4f98-8026-fd1adff1b1d4",
    },
    {
      nickname: "Marcelo Inventado",
      score: 170,
      uuid: "4a169122-4a2f-43fa-8474-4d23dbfc5850",
    },
  ];

  const hardestQuestionVariant =
    mockedQuestionVariants.length == 0
      ? ""
      : mockedQuestionVariants[mockedQuestionVariants.length - 1].uuid;

  return (
    <main className={styles.main}>
      <section className={styles.panel}>
        <div>
          <h1>Sala de jogo</h1>
          <p>Código: {code}</p>
        </div>
        <div className={styles.buttons}>
          <Button theme="full-orange">
            <FaPlay />
            Lançar
          </Button>
          <Button theme="light-orange">
            <FaSync />
            Gerar
          </Button>
          <Button disabled theme="light-orange">
            <FaArrowLeft />
            Anterior
          </Button>
          <Button theme="light-orange">
            <FaArrowRight />
            Próxima
          </Button>
          <Button theme="light-orange">
            <FaSignOutAlt />
            Finalizar
          </Button>
        </div>
      </section>
      <section>
        <h1></h1>
        <QuestionVariantsCarousel
          items={mockedQuestionVariants}
          start={hardestQuestionVariant}
          render={(selected, item) => {
            return (
              <QuestionView
                variant={item}
                hidden={selected !== item.uuid}
                key={item.uuid}
              />
            );
          }}
        />
      </section>
      <section className={styles.participants}>
        <h4>
          <FaBomb /> Perguntas: 4<span>/</span>
          <FaUserGroup /> Participantes: {mockedParticipants.length}
        </h4>
        <ParticipantsMansoryGrid participants={mockedParticipants} />
      </section>
    </main>
  );
}
