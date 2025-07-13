import { DetailedHTMLProps, HTMLAttributes } from "react";
import styles from "./index.module.scss";

interface Props
  extends DetailedHTMLProps<HTMLAttributes<HTMLSpanElement>, HTMLSpanElement> {
  value: string;
  theme?: "default" | "light-purple" | "full-purple";
}

export default function Tag({
  value,
  theme = "default",
  className,
  ...props
}: Props) {
  const classes = [styles.tag, className];

  switch (theme) {
    case "light-purple":
      classes.push(styles.lightOrange);
      break;
    case "full-purple":
      classes.push(styles.fullOrange);
      break;
    default:
      break;
  }

  const finalClassName = classes.join(" ");
  return (
    <span className={finalClassName} {...props}>
      {value}
    </span>
  );
}
