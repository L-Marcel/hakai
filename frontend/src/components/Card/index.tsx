import PlayIcon from "@components/Icons/PlayIcon";
import styles from "./index.module.scss";
import { EditIcon } from "@components/Icons/EditIcon";
import { EraserIcon } from "@components/Icons/EraserIcon";

export default function Card() {
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
        <button>
          <PlayIcon />
        </button>
      </div>
    </li>
  );
}
