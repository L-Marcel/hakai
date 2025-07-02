import { DetailedHTMLProps, HTMLAttributes } from "react";
import styles from "./index.module.scss";
import Button from "@components/Button";
import Tag from "@components/Tag";
import { difficultyToString, Question, QuestionVariant } from "@stores/useGame";
import { sendParticipantAnswer } from "../../../services/participant";

interface Props
  extends DetailedHTMLProps<HTMLAttributes<HTMLDivElement>, HTMLDivElement> {
  question?: Question;
  variant?: QuestionVariant;
  highlight?: string;
  editable?: boolean;
}

export default function QuestionView({
  highlight,
  question,
  variant,
  className,
  ...props
}: Props) {
  if(variant) {
    const { difficulty, context, options, question, uuid } = variant;
    const classes = [styles.question, className];
    const finalClassName = classes.join(" ");

    return (
      <article className={finalClassName} {...props}>
        <header className={styles.header}>
          <p className={styles.tags}>
            <Tag theme="full-orange" value={difficultyToString[difficulty]} />
            {(context || []).map((value) => (
              <Tag
                theme="light-orange"
                key={uuid + "-" + value}
                value={value}
              />
            ))}
          </p>
          <h1>{question}</h1>
        </header>
        <ol className={styles.options}>
          {options.map((option) => {
            const id =
              highlight && option === highlight ? "highlight" : "option";

            return (
              <Button
                disabled={!!highlight}
                onClick={() => sendParticipantAnswer(option)}
                id={id}
                theme="partial-orange"
                key={uuid + "-" + option}
              >
                {option}
              </Button>
            );
          })}
        </ol>
      </article>
    );
  } else if(question) {
    const { answer, question: content, uuid } = question;
    const classes = [styles.question, className];
    const finalClassName = classes.join(" ");
    return (
      <article className={finalClassName} {...props}>
        <header className={styles.header}>
          <p className={styles.tags}>
            <Tag theme="full-orange" value="base" />
              {(question.context || []).map((value) => (
              <Tag
                theme="light-orange"
                key={uuid + "-" + value}
                value={value}
              />
            ))}
          </p>
          <h1>{content}</h1>
        </header>
        <ol className={styles.options}>
          <Button disabled id="highlight" theme="partial-orange">
            {answer}
          </Button>
        </ol>
      </article>
    );
  } else return null;
}
