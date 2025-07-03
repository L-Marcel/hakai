import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";
import { FaEdit, FaEraser, FaPlay } from "react-icons/fa";
import Button from "@components/Button";
import { create } from "../../services/room";
import { Game } from "@stores/useGame";
import { FaArrowUpRightDots } from "react-icons/fa6";
interface CardProps {
  game: Game;
}

export default function Card({ game }: CardProps) {
  const navigate = useNavigate();

  const onStart = () => {
    create(game.uuid).then((code) => {
      navigate("/room/panel/" + code);
    });
  };
  
  const seeResults = () => {
    navigate("/game-results/" + game.uuid);
  };

  return (
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
        <Button theme="light-orange" onClick={onStart}>
          <FaPlay />
          Iniciar
        </Button>
      </div>
    </li>
  );
}
