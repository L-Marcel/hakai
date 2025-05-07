import { DetailedHTMLProps, HTMLAttributes } from "react";
import styles from "./index.module.scss";

interface Props
  extends DetailedHTMLProps<HTMLAttributes<HTMLSpanElement>, HTMLSpanElement> {
  value: string;
  theme?: "default" | "light-orange" | "full-orange";
}

export default function Tag({
  value,
  theme = "default",
  className,
  ...props
}: Props) {
  const classes = [styles.tag, className];

  switch (theme) {
    case "light-orange":
      classes.push(styles.lightOrange);
      break;
    case "full-orange":
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
