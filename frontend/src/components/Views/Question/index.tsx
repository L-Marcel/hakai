import { DetailedHTMLProps, HTMLAttributes } from "react";
import styles from "./index.module.scss";
import Button from "@components/Button";
import Tag from "@components/Tag";
import { difficultToString, QuestionVariant } from "@stores/useQuestions";

interface Props
  extends DetailedHTMLProps<HTMLAttributes<HTMLDivElement>, HTMLDivElement> {
  variant: QuestionVariant;
  highlight?: string;
}

export default function QuestionView({
  highlight,
  variant,
  className,
  ...props
}: Props) {
  const { level, context, options, question, uuid } = variant;
  const classes = [styles.question, className];
  const finalClassName = classes.join(" ");
  return (
    <article className={finalClassName} {...props}>
      <header className={styles.header}>
        <p className={styles.tags}>
          <Tag theme="full-orange" value={difficultToString[level]} />
          {context.map((value) => {
            return (
              <Tag
                theme="light-orange"
                key={uuid + "-" + value}
                value={value}
              />
            );
          })}
        </p>
        <h1>{question}</h1>
      </header>
      <ol className={styles.options}>
        {options.map((option) => {
          const id = highlight && option === highlight ? "highlight" : "option";
          return (
            <Button
              disabled={!!highlight}
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
}
