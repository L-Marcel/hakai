import { useState } from "react";
import styles from "./index.module.scss";
import { Game } from "@stores/useGame";
import { createGame, QuestionRequest } from "../../services/game";
import Input from "@components/Input";
import ErrorLabel from "@components/Forms/ErrorsLabel";
import { FaX } from "react-icons/fa6";
import Button from "@components/Button";

interface GameModalProps {
  isOpen: boolean;
  onClose: () => void;
  onGameCreated: (newGame: Game) => void;
}
type QuestionType = "base" | "multiple-choice";
interface QuestionFormData {
  question: string;
  answers: string[];
  contexts: string;
  type: QuestionType;
  language: string;
}
export default function GameModal({
  isOpen,
  onClose,
  onGameCreated,
}: GameModalProps) {
  const [title, setTitle] = useState("");
  const [questions, setQuestions] = useState<QuestionFormData[]>([
    { question: "", answers: [""], contexts: "", type: "base", language: "" },
  ]);
  const [error, setError] = useState<string>("");
  const [errors, setErrors] = useState<ValidationError[]>([]);
  const [exitingAnswers, setExitingAnswers] = useState<{ id: number, text: string }[]>([]);

  function handleAddQuestion() {
    setQuestions([
      ...questions,
      { question: "", answers: [""], contexts: "", type: "base", language: "" }
    ]);
  }
  function handleTypeChange(idx: number, newType: QuestionType) {
    const updated = questions.map((q, i) => {
      if (i === idx) {
        return { ...q, type: newType, answers: [""] };
      }
      return q;
    });
    setQuestions(updated);
  }
  function handleAnswerChange(questionIdx: number, answerIdx: number, value: string) {
    setQuestions(prevQuestions =>
      prevQuestions.map((question, qIdx) => {
        if (qIdx === questionIdx) {
          const newAnswers = [...question.answers];
          newAnswers[answerIdx] = value;
          return { ...question, answers: newAnswers };
        } return question;
      })
    );
  }
  function handleAddAnswer(questionIdx: number) {
    setQuestions(prevQuestions =>
      prevQuestions.map((question, qIdx) => {
        if (qIdx === questionIdx) {
          return {
            ...question,
            answers: [...question.answers, ""],
          };
        } return question;
      })
    );
  }
  function handleRemoveAnswer(questionIdx: number, answerIdx: number) {
    const ANIMATION_DURATION = 350;

    const answerToRemove = questions[questionIdx].answers[answerIdx];
    const exitingItem = { id: Date.now(), text: answerToRemove };
    setExitingAnswers(prev => [...prev, exitingItem]);
    setQuestions(prevQuestions =>
      prevQuestions.map((q, i) => {
        if (i === questionIdx) {
          return { ...q, answers: q.answers.filter((_, i) => i !== answerIdx) };
        }
        return q;
      })
    );
    setTimeout(() => {
      setExitingAnswers(prev => prev.filter(item => item.id !== exitingItem.id));
    }, ANIMATION_DURATION);
  }
  function handleRemoveQuestion(idx: number) {
    const updated = [...questions];
    updated.splice(idx, 1);
    setQuestions(updated);
    setErrors([]);
  }

  function handleQuestionChange(
    idx: number,
    field: keyof QuestionFormData,
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

    const questionsForPayload = questions.map((q) => {

      let request: QuestionRequest = {
        type: "ConcreteQuestionRequest",
        question: q.question,
        answers: q.answers.filter((a) => a.trim()),
        contexts: (q.contexts as string)
          .split(",")
          .map((c) => c.trim().toLowerCase())
          .filter((c) => c),
      };

      if (q.type === "multiple-choice") {
        request = {
          type: "MultipleChoiceQuestionRequest",
          wrappee: request,
        };
      }

      if (q.language && q.language.trim() !== "") {
        request = {
          type: "LanguageQuestionRequest",
          language: q.language.trim(),
          wrappee: request,
        };
      }

      return request;
    });

    const payload = { title, questions: questionsForPayload };

    try {
      const createdGame = await createGame(payload);
      onGameCreated(createdGame);
      onClose();
    } catch (error: unknown) {
      const _error = error as HttpError | ValidationErrors;
      if (_error.status === 400 && "errors" in _error) {
        setErrors(_error.errors);
      } else if("message" in _error) {
        setError(_error.message || "Ocorreu um erro desconhecido.");
      }
    }
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
              <select
                value={q.type}
                onChange={(e) => handleTypeChange(i, e.target.value as QuestionType)}
                className={styles.select}
              >
                <option value="simple">Simples</option>
                <option value="multiple-choice">Múltipla Escolha</option>
              </select>
              <Input
                autoComplete="off"
                type="text"
                placeholder="Pergunta"
                value={q.question}
                onChange={(e) => handleQuestionChange(i, "question", e.target.value)}
              />
              <ErrorLabel field={`questions.${i}.question`} errors={errors} />
              <Input
                autoComplete="off"
                type="text"
                placeholder="Linguagem (opcional, ex: Inglês, Alemão, Espanhol)"
                value={q.language}
                onChange={(e) => handleQuestionChange(i, "language", e.target.value)}
              />
              <ErrorLabel field={`questions.${i}.language`} errors={errors} />

              {q.answers.map((answer, ansIdx) => (
                <div key={`answer-${ansIdx}`} className={styles.answerRow}>
                  <Input
                    autoComplete="off"
                    type="text"
                    placeholder={`Resposta ${ansIdx + 1}`}
                    value={answer}
                    onChange={(e) => handleAnswerChange(i, ansIdx, e.target.value)}
                  />
                  {q.answers.length > 1 && (
                    <Button
                      type="button"
                      theme="full-red"
                      className={styles.deleteButton}
                      onClick={() => handleRemoveAnswer(i, ansIdx)}
                    >
                      <FaX size={12} />
                    </Button>
                  )}
                </div>
              ))}
              {exitingAnswers.map((exitingItem) => (
                <div
                  key={exitingItem.id}
                  className={`${styles.answerRow} ${styles.answerRowExiting}`}
                >
                  <Input
                    autoComplete="off"
                    type="text"
                    value={exitingItem.text}
                    readOnly
                  />
                </div>
              ))}
              <ErrorLabel field={`questions.${i}.answers`} errors={errors} />
              <div className={
                q.type === "multiple-choice"
                  ? `${styles.collapsible} ${styles.expanded}`
                  : styles.collapsible
              }>
                <Button
                  type="button"
                  theme="partial-red"
                  onClick={() => handleAddAnswer(i)}
                >
                  Adicionar Resposta
                </Button>
              </div>
              <Input
                autoComplete="off"
                type="text"
                placeholder="Contextos separados por vírgula"
                value={q.contexts}
                onChange={(e) => handleQuestionChange(i, "contexts", e.target.value)}
              />
              <ErrorLabel field={`questions.${i}.contexts`} errors={errors} />
              {questions.length > 1 && (
                <Button
                  type="button"
                  theme="partial-red"
                  onClick={() => handleRemoveQuestion(i)}
                  style={{ 'marginTop': '16px' }}
                >
                  Remover Pergunta
                </Button>
              )}
            </fieldset>
          ))}
          <Button
            type="button"
            theme="full-red"
            onClick={handleAddQuestion}
          >
            Adicionar Pergunta
          </Button>
          {error && <p className={styles.error}>{error}</p>}
          <div className={styles.actions}>
            <Button theme="partial-red" type="button" onClick={onClose}>
              Cancelar
            </Button>
            <Button theme="full-red" type="submit">
              Criar
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
