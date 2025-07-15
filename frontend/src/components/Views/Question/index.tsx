import { DetailedHTMLProps, HTMLAttributes } from "react";
import styles from "./index.module.scss";
import Button from "@components/Button";
import Tag from "@components/Tag";
import {
  difficultyToString,
  Question,
  getConcreteQuestionVariant,
  QuestionVariant,
} from "@stores/useGame";

interface Props
  extends DetailedHTMLProps<HTMLAttributes<HTMLDivElement>, HTMLDivElement> {
  question?: Question;
  variant?: QuestionVariant;
  highlight?: string[];
  editable?: boolean;
  selectedOptions: string[];
  onOptionToggle: (option: string) => void;
}

export default function QuestionView({
  highlight,
  question,
  variant,
  className,
  selectedOptions,
  onOptionToggle,
  ...props
}: Props) {
  if (variant) {
    const concrete = getConcreteQuestionVariant(variant);

    const { difficulty, contexts, options, question, uuid, wrongValue, correctValue } = concrete;
    const classes = [styles.question, className];
    const finalClassName = classes.join(" ");

    return (
      <article className={finalClassName} {...props}>
        <header className={styles.header}>
          <p className={styles.tags}>
            <Tag theme="full-red" value={difficultyToString[difficulty]} />
            {(contexts || []).map((value) => (
              <Tag theme="light-red" key={uuid + "-" + value} value={value} />
            ))}
          </p>
          <h1>{question}</h1>
          {!!correctValue && !wrongValue && <p id="values">Você ganha {correctValue} pontos por acerto.</p>}
          {!correctValue && !!wrongValue && <p id="values">Você perde {wrongValue} pontos por erro.</p>}
          {!!correctValue && !!wrongValue && <p id="values">Você ganha {correctValue} pontos por acerto e perde {wrongValue} por erro.</p>}
        </header>
        <ol className={styles.options}>
          {options.map((option) => {
            const isSelected = selectedOptions.includes(option);
            const isHighlighted = highlight && highlight.includes(option);

            return (
              <Button
                disabled={!!highlight}
                onClick={() => onOptionToggle(option)}
                theme={isSelected ? "light-red" : "partial-red"}
                id={isHighlighted ? "highlight" : "option"}
                key={uuid + "-" + option}
              >
                {option}
              </Button>
            );
          })}
        </ol>
      </article>
    );
  } else if (question) {
    const { answers, question: content, uuid } = question;

    const classes = [styles.question, className];
    const finalClassName = classes.join(" ");
    return (
      <article className={finalClassName} {...props}>
        <header className={styles.header}>
          <p className={styles.tags}>
            <Tag theme="full-red" value="base" />
            {(question.contexts || []).map((value) => (
              <Tag theme="light-red" key={uuid + "-" + value} value={value} />
            ))}
          </p>
          <h1>{content}</h1>
        </header>
        <ol className={styles.options}>
          {answers.map((option) => {
            return (
              <Button
                disabled
                id="highlight"
                theme="partial-red"
                key={uuid + "-" + option + "-answer"}
              >
                {option}
              </Button>
            );
          })}
        </ol>
      </article>
    );
  } else return null;
}
