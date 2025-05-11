import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";
import { FaEdit, FaEraser, FaPlay } from "react-icons/fa";
import Button from "@components/Button";
import { create } from "../../services/roomService";

export default function Card() {
  const navigate = useNavigate();

  const onStart = async () => {
    const response = await create("4ad9e19e-8f9b-4b93-a7a1-17ea469bd455");
    // [TODO] Vai ter que criar um guard depois para redirecionar se já tiver sala de jogo
    if (response.ok) navigate("/room/panel/" + response.value);
  };

  return (
    <li className={styles.card}>
      <div className={styles.content}>
        <h1>Processos de software</h1>
        <p>5 perguntas contextualizadas</p>
      </div>
      <div className={styles.buttons}>
        <Button rounded="full" disabled>
          <FaEraser />
        </Button>
        <Button rounded="full" disabled>
          <FaEdit />
        </Button>
        <Button theme="light-orange" onClick={onStart}>
          <FaPlay />
          Iniciar
        </Button>
      </div>
    </li>
  );
}
