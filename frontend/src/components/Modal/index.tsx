// src/components/GameModal.tsx
import { useState } from "react";
import styles from "./index.module.scss";
import { Game } from "@stores/useGame";
import { createGame, GameRequest, QuestionRequest } from "../../services/game";
import Input from "@components/Input";
import ErrorLabel from "@components/Forms/ErrorsLabel";
import { FaX } from "react-icons/fa6";
import Button from "@components/Button";

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
    { question: "", answers: [""], contexts: "" },
  ]);

  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<ValidationError[]>([]);

  function handleAddQuestion() {
    setQuestions([...questions, { question: "", answers: [""], contexts: "" }]);
  }

  function handleRemoveQuestion(idx: number) {
    const updated = [...questions];
    updated.splice(idx, 1);
    setQuestions(updated);
    setErrors([]);
  }

  function handleQuestionChange(
    idx: number,
    field: keyof QuestionRequest,
    value: string
  ) {
    const _questions = [...questions];

    if (field === "answers") {
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
    setError("");
    setErrors([]);

    const questionsWithType = questions.map((q) => ({
      type: q.type ?? "ConcreteQuestionRequest",
      question: q.question,
      answers: q.answers,
      contexts: (q.contexts as string)
        .split(",")
        .map((c) => c.trim().toLowerCase())
        .filter((c) => c),
    }));

    const payload: GameRequest = { title, questions: questionsWithType };

    createGame(payload)
      .then((createdGame) => {
        onGameCreated(createdGame);
        onClose();
        setTitle("");
        setQuestions([{ question: "", answers: [""], contexts: [] }]);
      })
      .catch((error: HttpError | ValidationErrors) => {
        if (error.status === 400 && "errors" in error) {
          setErrors((error as ValidationErrors).errors);
        } else {
          setError((error as HttpError).message);
        }
      });
  }

  if (!isOpen) return null;

  return (
    <div className={styles.backdrop}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <header className={styles.header}>
          <h2>Novo Jogo</h2>
          <button
            type="button"
            onClick={() => {
              onClose();
              setErrors([]);
              setError("");
            }}
            className={styles.closeBtn}
          >
            <FaX />
          </button>
        </header>
        <form className={styles.form} onSubmit={handleSubmit}>
          <Input
            autoComplete="off"
            type="text"
            value={title}
            placeholder="Título"
            onChange={(e) => setTitle(e.target.value)}
          />
          <ErrorLabel field="title" errors={errors} />
          {questions.map((q, i) => (
            <fieldset key={i} className={styles.questionBlock}>
              <legend>Pergunta {i + 1}</legend>
              <Input
                autoComplete="off"
                type="text"
                placeholder="Pergunta"
                value={q.question}
                onChange={(e) =>
                  handleQuestionChange(i, "question", e.target.value)
                }
              />
              <ErrorLabel field={`questions.${i}.question`} errors={errors} />
              <Input
                autoComplete="off"
                type="text"
                placeholder="Resposta"
                value={q.answers[0]}
                onChange={(e) =>
                  handleQuestionChange(i, "answers", e.target.value)
                }
              />
              <ErrorLabel field={`questions.${i}.answers.0`} errors={errors} />
              <Input
                autoComplete="off"
                type="text"
                placeholder="Contextos separados por vírgula"
                value={q.contexts}
                onChange={(e) =>
                  handleQuestionChange(i, "contexts", e.target.value)
                }
              />
              <ErrorLabel field={`questions.${i}.contexts`} errors={errors} />

              {questions.length > 1 && (
                <Button
                  type="button"
                  theme="partial-purple"
                  onClick={() => handleRemoveQuestion(i)}
                >
                  Remover
                </Button>
              )}
            </fieldset>
          ))}
          <Button
            type="button"
            theme="light-purple"
            onClick={handleAddQuestion}
          >
            Adicionar pergunta
          </Button>

          {error && <p className={styles.error}>{error}</p>}

          <div className={styles.actions}>
            <Button theme="partial-purple" type="button" onClick={onClose}>
              Cancelar
            </Button>
            <Button theme="full-purple" type="submit">
              Criar
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
