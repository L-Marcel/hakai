import PlayIcon from "@components/Icons/PlayIcon";
import styles from "./index.module.scss";
import { EditIcon } from "@components/Icons/EditIcon";
import { EraserIcon } from "@components/Icons/EraserIcon";

export default function Card() {
  const createRoom = async() => {
    const response = await fetch("http://localhost:8080/rooms/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        "uuid": "4ad9e19e-8f9b-4b93-a7a1-17ea469bd455"
      })
    });

    if(response.ok) {
      console.log(await response.json());
    } else {
      console.log(response.status);
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
        <button onClick={createRoom}>
          <PlayIcon />
          Iniciar
        </button>
      </div>
    </li>
  );
}
