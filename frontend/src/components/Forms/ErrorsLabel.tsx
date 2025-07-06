import styles from "./index.module.scss";

interface Props {
  field: string;
  errors?: ValidationError[];
}

export default function ErrorLabel({ field, errors = [] }: Props) {
  const all = errors.filter((error) => error.field === field);
  const first = all.length > 0 ? all[0] : undefined;

  if (!first) return null;

  return (
    <div className={styles.errors}>
      {first.messages
        .filter((message) => message.error)
        .map((message) => (
          <p key={message.content} className={styles.error}>
            {message.content}
          </p>
        ))}
    </div>
  );
}
