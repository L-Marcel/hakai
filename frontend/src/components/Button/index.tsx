import { ButtonHTMLAttributes, DetailedHTMLProps } from "react";
import styles from "./index.module.scss";

interface Props
  extends DetailedHTMLProps<
    ButtonHTMLAttributes<HTMLButtonElement>,
    HTMLButtonElement
  > {
  rounded?: "default" | "full";
  theme?: "default" | "light-purple" | "full-purple" | "partial-purple";
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
    case "light-purple":
      classes.push(styles.lightPurple);
      break;
    case "full-purple":
      classes.push(styles.fullPurple);
      break;
    case "partial-purple":
      classes.push(styles.partialPurple);
      break;
    default:
      break;
  }

  const finalClassName = classes.join(" ");
  return <button className={finalClassName} {...props} />;
}
