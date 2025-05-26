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

export default function GameModal({ isOpen, onClose, onGameCreated }: GameModalProps) {
    const [title, setTitle] = useState("");
    const [questions, setQuestions] = useState<QuestionRequest[]>([
        { question: "", answer: "", context: [] },
    ]);
    const [error, setError] = useState<string | null>(null);

    function handleAddQuestion() {
        setQuestions([...questions, { question: "", answer: "", context: [] }]);
    } function handleRemoveQuestion(idx: number) {
        const updated = [...questions];
        updated.splice(idx, 1);
        setQuestions(updated);
    }

    function handleQuestionChange(idx: number, field: keyof QuestionRequest, value: string) {
        const copy = [...questions];
        if (field === "context") {
            copy[idx][field] = value.split(",").map((c) => c.trim());
        } else {
            (copy[idx] as any)[field] = value;
        }
        setQuestions(copy);
    }

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setError(null);
        const payload: GameRequest = { title, questions };
        try {
            const created = await createGame(payload);
            onGameCreated(created);
            onClose();
            setTitle("");
            setQuestions([{ question: "", answer: "", context: [] }]);
        } catch (err: any) {
            setError(err.message || "Erro ao criar o jogo");
        }
    }

    if (!isOpen) return null;
    return (
        <div className={styles.backdrop}>
            <div className={styles.modal} onClick={e => e.stopPropagation()}>
                <header className={styles.header}>
                    <h2>Novo Jogo</h2>
                    <button onClick={onClose} className={styles.closeBtn}>×</button>
                </header>
                <form className={styles.form} onSubmit={handleSubmit}>
                    <label>
                        Título
                        <input
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            required
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
                                    onChange={(e) => handleQuestionChange(i, "question", e.target.value)}
                                    required
                                />
                            </label>

                            <label>
                                Resposta
                                <input
                                    type="text"
                                    value={q.answer}
                                    onChange={(e) => handleQuestionChange(i, "answer", e.target.value)}
                                    required
                                />
                            </label>

                            <label>
                                Contextos (vírgula-separados)
                                <input
                                    type="text"
                                    value={q.context.join(", ")}
                                    onChange={(e) => handleQuestionChange(i, "context", e.target.value)}
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
                    

                    <button type="button" onClick={handleAddQuestion} className={styles.addQuestionBtn}>
                        + Adicionar Pergunta
                    </button>

                    {error && <p className={styles.error}>{error}</p>}

                    <div className={styles.actions}>
                        <button type="button" onClick={onClose}>Cancelar</button>
                        <button type="submit" className={styles.submitBtn}>Criar</button>
                    </div>
                </form>
            </div>
        </div>
    );
}
