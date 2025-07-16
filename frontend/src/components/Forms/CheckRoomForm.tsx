import Button from "@components/Button";
import Input from "@components/Input";
import { useState } from "react";
import { FaClone } from "react-icons/fa6";
import styles from "./index.module.scss";
import { create, getRoomForClone } from "../../services/room";
import { createGame, getGameForClone } from "../../services/game";
import useAuth from "@stores/useAuth";
import { Game } from "@stores/useGame";
import useGenerationStatus from "@stores/useStatus";

interface CheckRoomFormProps {
  onGameCreated: (newGame: Game) => void;
}

export default function CheckRoomForm({onGameCreated} : CheckRoomFormProps) {
  const [error, setError] = useState("");
  const [code, setCode] = useState("");
  const {setGenerationStatus} = useGenerationStatus.getState();

  const onChangeCode = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/[^\d]*/g, "");
    if (value !== e.target.value) setError("Use apenas dígitos!");
    else setError("");
    setCode(value);
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const room = await getRoomForClone(code);
      const game = await getGameForClone(room.game);

      const clonedGame = await createGame({
        title: game.title + " (Cópia)",
        questions: game.questions.map((q) => ({
          type: "ConcreteQuestionRequest",
          question: q.question,
          answers: q.answers,
          contexts: q.contexts,
        })),
      });
      onGameCreated(clonedGame);
      setGenerationStatus("Jogo clonado com sucesso!");
    }
    catch (error: any) {
      setError(error.message || "Erro ao clonar jogo.");
    }
   
  };

  return (
    <form name="join" className={styles.form} onSubmit={onSubmit}>
      <div>
        <Input
          autoComplete="off"
          name="code"
          inputMode="numeric"
          placeholder="Código de sala"
          onChange={onChangeCode}
          value={code}
        />
        <Button theme="full-purple" type="submit" tabIndex={-1}>
          <FaClone />
        </Button>
      </div>
      {error && <p className={styles.error}>{error}</p>}
    </form>
  );
}
