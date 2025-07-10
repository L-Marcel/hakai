import { ButtonHTMLAttributes, DetailedHTMLProps } from "react";
import styles from "./index.module.scss";

interface Props
  extends DetailedHTMLProps<
    ButtonHTMLAttributes<HTMLButtonElement>,
    HTMLButtonElement
  > {
  rounded?: "default" | "full";
  theme?: "default" | "light-red" | "full-red" | "partial-red";
}

export default function Button({
  rounded = "default",
  theme = "default",
  className,
  ...props
}: Props) {
  const classes = [styles.button, className];
  if (rounded === "full") classes.push(styles.fullRounded);

  switch (theme) {
    case "light-red":
      classes.push(styles.lightRed);
      break;
    case "full-red":
      classes.push(styles.fullRed);
      break;
    case "partial-red":
      classes.push(styles.partialRed);
      break;
    default:
      break;
  }

  const finalClassName = classes.join(" ");
  return <button className={finalClassName} {...props} />;
}
