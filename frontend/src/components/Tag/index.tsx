import { DetailedHTMLProps, HTMLAttributes } from "react";
import styles from "./index.module.scss";

interface Props
  extends DetailedHTMLProps<HTMLAttributes<HTMLSpanElement>, HTMLSpanElement> {
  value: string;
  theme?: "default" | "light-red" | "full-red";
}

export default function Tag({
  value,
  theme = "default",
  className,
  ...props
}: Props) {
  const classes = [styles.tag, className];

  switch (theme) {
    case "light-red":
      classes.push(styles.lightRed);
      break;
    case "full-red":
      classes.push(styles.fullRed);
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
