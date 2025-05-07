import { DetailedHTMLProps, LiHTMLAttributes } from "react";
import { QuestionVariant } from "@stores/useRoom";

interface Props
  extends DetailedHTMLProps<LiHTMLAttributes<HTMLLIElement>, HTMLLIElement> {
  variant: QuestionVariant;
}

export default function QuestionView({ variant, ...props }: Props) {
  const { level, options, question, uuid } = variant;
  return (
    <li {...props}>
      <p>{level}</p>
      <h1>{question}</h1>
      <ul>
        {options.map((option) => {
          return <li key={uuid + "-" + option}>{option}</li>;
        })}
      </ul>
    </li>
  );
}
