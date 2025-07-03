"use client";
import { useEffect, useState } from "react";
import Button from "@components/Button";
import styles from "./index.module.scss";
import { FaArrowLeft, FaChevronDown, FaChevronUp } from "react-icons/fa";
import { groupAnswers } from "@components/Views/Answers";
import useGame from "@stores/useGame";
import AuthGuard from "@components/Guards/AuthGuard";
import { getGameAnswers } from "../../services/answers";
import { getGame } from "../../services/game";
import { useNavigate, useParams } from "react-router-dom";
import { UUID } from "crypto";

export default function Results() {
  return (
    <AuthGuard>
        <Page />
    </AuthGuard>
  );
}

export function Page() {
    const navigate = useNavigate();
    const { uuid } = useParams();

    const history = useGame((state) => state.history);

    const [showByPerson, setShowByPerson] = useState<Record<string, boolean>>({});
    const groupedResults = groupAnswers(history);

    const toggleShowByPerson = (questionId: string) => {
        setShowByPerson((prev) => ({
            ...prev,
            [questionId]: !prev[questionId],
        }));
    };

    useEffect(() => {
        getGame(uuid as UUID);
        getGameAnswers(uuid as UUID);
    }, [uuid]);

    return (
        <main className={styles.main}>
            <div className={styles.header}>
                <Button theme="full-orange" onClick={() => navigate("/dashboard")}>
                    <FaArrowLeft /> Voltar
                </Button>
            </div>
            <div className={styles.section}>

                <h2>Resultados</h2>

                <table className={styles.resultsTable}>
                    <thead>
                        <tr>
                            <th>Questão</th>
                            <th>Total de Respostas</th>
                            <th>Detalhes</th>
                        </tr>
                    </thead>
                    <tbody>
                        {Object.entries(groupedResults).map(
                            ([questionId, questionData]) => (
                                <tr key={questionId} className={styles.questionRow}>
                                    <td>
                                        <strong>{questionId}</strong>{" "}{}
                                    </td>
                                    <td>{questionData.answers.length}</td>
                                    <td>
                                        <Button
                                            theme="partial-orange"
                                            onClick={() => toggleShowByPerson(questionId)}
                                            className={styles.toggleButton}
                                        >
                                            {showByPerson[questionId] ? (
                                                <>
                                                    <FaChevronUp /> Esconder por Pessoa
                                                </>
                                            ) : (
                                                <>
                                                    <FaChevronDown /> Ver por Pessoa
                                                </>
                                            )}
                                        </Button>
                                    </td>
                                    {showByPerson[questionId] && (
                                        <td colSpan={3} className={styles.answersByPerson}>
                                            <h3>Respostas por Pessoa:</h3>
                                            <ul>
                                                {Object.entries(questionData.groupedByNickname).map(
                                                    ([nickname, answers]) => (
                                                        <li key={nickname}>
                                                            <strong>{nickname}:</strong>
                                                            <ul>
                                                                {answers.map((answer) => (
                                                                    <li key={answer.uuid}>
                                                                        {answer.answers}
                                                                    </li>
                                                                ))}
                                                            </ul>
                                                        </li>
                                                    )
                                                )}
                                            </ul>
                                        </td>
                                    )}
                                </tr>
                            )
                        )}
                    </tbody>
                </table>
            </div>
        </main>
    );
}