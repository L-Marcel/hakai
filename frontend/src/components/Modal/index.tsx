// src/components/GameModal.tsx
import { useState } from "react";
import styles from "./index.module.scss";
import { Game } from "@stores/useGame";
import { createGame, GameRequest, QuestionRequest } from "../../services/game";

interface GameModalProps {
  isOpen: boolean;
  onClose: () => void;
  onGameCreated: (newGame: Game) => void;
}

export default function GameModal({
  isOpen,
  onClose,
  onGameCreated,
}: GameModalProps) {
  const [title, setTitle] = useState("");
  const [questions, setQuestions] = useState<QuestionRequest[]>([
    { question: "", answers: [""], contexts: [] },
  ]);
  const [error, setError] = useState<string | null>(null);

  function handleAddQuestion() {
    setQuestions([...questions, { question: "", answers: [""], contexts: [] }]);
  }
  function handleRemoveQuestion(idx: number) {
    const updated = [...questions];
    updated.splice(idx, 1);
    setQuestions(updated);
  }

  function handleQuestionChange(
    idx: number,
    field: keyof QuestionRequest,
    value: string
  ) {
    const _questions = [...questions];

    if (field === "contexts") {
      _questions[idx][field] = value.split(",").map((c) => c.trim());
    } else if (field === "answers") {
      _questions[idx][field][0] = value;
    } else {
      _questions[idx] = {
        ..._questions[idx],
        [field]: value,
      };
    }

    setQuestions(_questions);
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);

    const questionsWithType = questions.map((q) => ({
      type: q.type ?? "ConcreteQuestionRequest",
      question: q.question,
      answers: q.answers,
      contexts: q.contexts,
    }));

    const payload: GameRequest = { title, questions: questionsWithType };

    try {
      const created = await createGame(payload);
      onGameCreated(created);
      onClose();
      setTitle("");
      setQuestions([{ question: "", answers: [""], contexts: [] }]);
    } catch (error) {
      const _error = error as Error;
      setError(_error.message ?? "Erro ao criar o jogo");
    }
  }

  if (!isOpen) return null;
  return (
    <div className={styles.backdrop}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <header className={styles.header}>
          <h2>Novo Jogo</h2>
          <button onClick={onClose} className={styles.closeBtn}>
            ×
          </button>
        </header>
        <form className={styles.form} onSubmit={handleSubmit}>
          <label>
            Título
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </label>

          {questions.map((q, i) => (
            <fieldset key={i} className={styles.questionBlock}>
              <legend>Pergunta {i + 1}</legend>

              <label>
                Pergunta
                <input
                  type="text"
                  value={q.question}
                  onChange={(e) =>
                    handleQuestionChange(i, "question", e.target.value)
                  }
                />
              </label>

              <label>
                Resposta
                <input
                  type="text"
                  value={q.answers[0]}
                  onChange={(e) =>
                    handleQuestionChange(i, "answers", e.target.value)
                  }
                />
              </label>

              <label>
                Contextos (vírgula-separados)
                <input
                  type="text"
                  value={q.contexts.join(", ")}
                  onChange={(e) =>
                    handleQuestionChange(i, "contexts", e.target.value)
                  }
                />
              </label>

              {questions.length > 1 && (
                <button
                  type="button"
                  onClick={() => handleRemoveQuestion(i)}
                  className={styles.removeQuestionBtn}
                >
                  Remover Pergunta
                </button>
              )}
            </fieldset>
          ))}

          <button
            type="button"
            onClick={handleAddQuestion}
            className={styles.addQuestionBtn}
          >
            + Adicionar Pergunta
          </button>

          {error && <p className={styles.error}>{error}</p>}

          <div className={styles.actions}>
            <button type="button" onClick={onClose}>
              Cancelar
            </button>
            <button type="submit" className={styles.submitBtn}>
              Criar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
