import { useState } from "react";
import { DetailedHTMLProps, HTMLAttributes } from "react";
import styles from "./index.module.scss";
import Button from "@components/Button";
import Tag from "@components/Tag";
import {
  difficultyToString,
  getConcreteQuestionVariant,
  Question,
  QuestionVariant,
} from "@stores/useGame";
import { sendParticipantAnswer } from "../../../services/participant";

interface Props
  extends DetailedHTMLProps<HTMLAttributes<HTMLDivElement>, HTMLDivElement> {
  question?: Question;
  variant?: QuestionVariant;
  highlight?: string[];
  editable?: boolean;
}

export default function QuestionView({
  highlight,
  question,
  variant,
  editable,
  className,
  ...props
}: Props) {
  // Estados controlados por pergunta (uuid)
  const [answeredMap, setAnsweredMap] = useState<Record<string, boolean>>({});
  const [selectedMap, setSelectedMap] = useState<Record<string, string | null>>({});

  if (variant) {
    const concreteVariant = getConcreteQuestionVariant(variant);
    const { difficulty, contexts, options, question, uuid } = concreteVariant;
    const classes = [styles.question, className];
    const finalClassName = classes.join(" ");

    const selected = selectedMap[uuid] || null;
    const answered = answeredMap[uuid] || false;

    let points = 0;
    switch (difficulty) {
      case "EASY":
        points = 100;
        break;
      case "NORMAL":
        points = 200;
        break;
      case "HARD":
        points = 300;
        break;
    }

    const handleSelect = (option: string) => {
      if (answered) return;

      setSelectedMap((prev) => ({ ...prev, [uuid]: option }));
      setAnsweredMap((prev) => ({ ...prev, [uuid]: true }));
      sendParticipantAnswer([option], variant, difficulty);
    };

    return (
      <article className={finalClassName} {...props}>
        <header className={styles.header}>
          <p className={styles.tags}>
            <Tag theme="full-purple" value={difficultyToString[difficulty]} />
            {(contexts || []).map((value) => (
              <Tag
                theme="light-purple"
                key={uuid + "-" + value}
                value={value}
              />
            ))}
          </p>
          <h1>{question}</h1>
          <p id="values">{points} pontos</p>
        </header>

        <ol className={styles.options}>
          {options.map((option) => {
            const isSelected = selected === option;

            return (
              <Button
                disabled={answered || editable}
                onClick={() => handleSelect(option)}
                id={isSelected && answered ? "highlight" : "option"}
                theme="partial-purple"
                key={uuid + "-" + option}
              >
                {option}
              </Button>
            );
          })}
        </ol>

        {"feedback" in variant && answered && (
          <footer className={styles.feedback}>
            <h4>Feedback:</h4>
            <p>{variant.feedback}</p>
          </footer>
        )}
      </article>
    );
  }

  else if (question) {
    const { answers, question: content, uuid } = question;
    const classes = [styles.question, className];
    const finalClassName = classes.join(" ");

    return (
      <article className={finalClassName} {...props}>
        <header className={styles.header}>
          <p className={styles.tags}>
            <Tag theme="full-purple" value="base" />
            {(question.contexts || []).map((value) => (
              <Tag
                theme="light-purple"
                key={uuid + "-" + value}
                value={value}
              />
            ))}
          </p>
          <h1>{content}</h1>
        </header>

        <ol className={styles.options}>
          {answers.map((option) => (
            <Button
              disabled
              id="highlight"
              theme="partial-purple"
              key={uuid + "-" + option + "-answer"}
            >
              {option}
            </Button>
          ))}
        </ol>
      </article>
    );
  }

  return null;
}