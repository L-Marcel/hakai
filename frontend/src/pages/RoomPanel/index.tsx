import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";
import RoomGuard from "@components/Guards/RoomGuard";
import ParticipantsMansoryGrid from "@components/Grid/ParticipantsGrid";
import { Participant } from "../../stores/useRoom";
import Button from "@components/Button";
import { FaArrowLeft, FaArrowRight, FaBomb, FaPlay, FaSignOutAlt } from "react-icons/fa";
import { useParams } from "react-router-dom";
import { FaUserGroup } from "react-icons/fa6";

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
  const mockedParticipants: Participant[] = [
    {
      nickname: "Lucas Marcel Silva de Brito",
      score: 100,
      uuid: "5e5b2e43-1b9b-4c07-9e2a-7eeb8eae18c1"
    },
    {
      nickname: "Gina Marcele de Sousa Silva",
      score: 190,
      uuid: "0c863b92-d985-4d40-9212-8753dc2e0e79"
    },
    {
      nickname: "Marcela Silva Batista",
      score: 180,
      uuid: "134f99f1-3df6-4976-b47e-4a6bfe676816"
    },
    {
      nickname: "Maria Clara Fernandes",
      score: 200,
      uuid: "df5a6b86-1e9d-4f98-8026-fd1adff1b1d4"
    },
    {
      nickname: "Marcelo Inventado",
      score: 170,
      uuid: "4a169122-4a2f-43fa-8474-4d23dbfc5850"
    }
  ];

  return (
    <main className={styles.main}>
      <section className={styles.panel}>
        <div>
          <h1>Sala de jogo</h1>
          <p>Código: {code}</p>
        </div>
        <div className={styles.buttons}>
          <Button theme="full-orange">
            <FaPlay />Lançar
          </Button>
          <Button disabled theme="light-orange">
            <FaArrowLeft />Anterior
          </Button>
          <Button theme="light-orange">
            <FaArrowRight />Próxima
          </Button>
          <Button theme="light-orange">
            <FaSignOutAlt />Finalizar
          </Button>
        </div>
      </section>
      <section className={styles.participants}>
        <h4>
          <FaBomb/> Perguntas: 4
          <span>/</span>
          <FaUserGroup/> Participantes: {mockedParticipants.length}
        </h4>
        <ParticipantsMansoryGrid participants={mockedParticipants}/>
      </section>
    </main>
  );
}
