import PlayIcon from "@components/Icons/PlayIcon";
import styles from "./index.module.scss";
import { EditIcon } from "@components/Icons/EditIcon";
import { EraserIcon } from "@components/Icons/EraserIcon";
import useRoom from "../../stores/useRoom";
import { useNavigate } from "react-router-dom";

export default function Card() {
  const createRoom = useRoom((state) => state.create);
  const navigate = useNavigate();

  const onStart = async() => {
    const response = await createRoom("4ad9e19e-8f9b-4b93-a7a1-17ea469bd455");
    // [TODO] Vai ter que criar um guard depois para redirecionar se já tiver sala de jogo
    if(response.ok) {
      navigate("/room/panel/" + response.value);
    };
  };

  return (
    <li className={styles.card}>
      <div className={styles.content}>
        <h1>Processos de software</h1>
        <p>5 perguntas contextualizadas</p>
      </div>
      <div className={styles.buttons}>
        <button disabled>
          <EraserIcon />
        </button>
        <button disabled>
          <EditIcon />
        </button>
        <button onClick={onStart}>
          <PlayIcon />
          Iniciar
        </button>
      </div>
    </li>
  );
}
