import { DetailedHTMLProps, HTMLAttributes, useState } from "react";
import styles from "./index.module.scss";
import Button from "@components/Button";
import Tag from "@components/Tag";
import { difficultyToString, Question, getConcreteQuestionVariant, QuestionVariant } from "@stores/useGame";
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
  className,
  ...props
}: Props) {
  const [selectedOptions, setSelectedOptions] = useState<string[]>([]); console.log("Opções selecionadas:", selectedOptions);
  if (variant) {
    const concreteVariant = getConcreteQuestionVariant(variant);
    const { difficulty, contexts, options, question, uuid } = concreteVariant;
    const classes = [styles.question, className];
    const finalClassName = classes.join(" ");
    const handleOptionToggle = (option: string) => {
      setSelectedOptions((currentSelected) => {
        if (currentSelected.includes(option)) {
          return currentSelected.filter((item) => item !== option);
        }
        return [...currentSelected, option];
      });
    }; const handleSubmit = () => {
      if (selectedOptions.length === 0) {
        alert("Por favor, selecione ao menos uma resposta.");
        return;
      }
      sendParticipantAnswer(selectedOptions);
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
        </header>
        <ol className={styles.options}>
          {options.map((option) => {
            const isSelected = selectedOptions.includes(option);
            const isHighlighted = highlight && highlight.includes(option);

            return (
              <Button
                disabled={!!highlight}
                onClick={() => sendParticipantAnswer([option])}
                id={id}
                theme="partial-purple"
                key={uuid + "-" + option}
              >
                {option}
              </Button>
            );
          })}
        </ol>  {!highlight && (
          <Button
            onClick={handleSubmit}
            theme="full-red"
            style={{ marginTop: '20px', width: '100%' }}
          >
            Enviar Resposta
          </Button>
        )}
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
          {answers.map((option) => {
            return (
              <Button
                disabled
                id="highlight"
                theme="partial-purple"
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