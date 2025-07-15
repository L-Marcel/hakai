import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";
import { FaEdit, FaEraser, FaPlay } from "react-icons/fa";
import Button from "@components/Button";
import { create } from "../../services/room";
import { Game } from "@stores/useGame";
import { FaArrowUpRightDots } from "react-icons/fa6";
import { useState } from "react";
import DurationModal from "@components/Modal/Duration";
interface CardProps {
  game: Game;
}

export default function Card({ game }: CardProps) {
  const navigate = useNavigate();



  const seeResults = () => {
    navigate("/game-results/" + game.uuid);
  };
  const [isModalOpen, setIsModalOpen] = useState(false); // 3. Estado para controlar o modal

  // 4. Esta função será chamada pelo modal com a duração escolhida
  const handleCreateRoom = (durationInMinutes: number) => {
    create(game.uuid, durationInMinutes).then((code) => {
      setIsModalOpen(false);
      navigate("/room/panel/" + code);

    });
  };

  return (<>
    <li className={styles.card}>
      <div className={styles.content}>
        <h1>{game.title}</h1>
        <p>{game.questions.length} perguntas contextualizadas</p>
      </div>
      <div className={styles.buttons}>
        <Button rounded="full" disabled>
          <FaEraser />
        </Button>
        <Button rounded="full" disabled>
          <FaEdit />
        </Button>
        <Button rounded="full" onClick={seeResults}>
          <FaArrowUpRightDots />
        </Button>
        <Button theme="light-red" onClick={() => setIsModalOpen(true)}>
          <FaPlay />
          Iniciar
        </Button>
      </div>
    </li>{isModalOpen && (
      <DurationModal
        onClose={() => setIsModalOpen(false)}
        onSelectDuration={handleCreateRoom}
      />
    )}
  </>);
}
