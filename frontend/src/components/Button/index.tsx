import { ButtonHTMLAttributes, DetailedHTMLProps } from "react";
import styles from "./index.module.scss";

interface Props
  extends DetailedHTMLProps<
    ButtonHTMLAttributes<HTMLButtonElement>,
    HTMLButtonElement
  > {
  rounded?: "default" | "full";
  theme?: "default" | "light-orange" | "full-orange" | "partial-orange";
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
    case "light-orange":
      classes.push(styles.lightOrange);
      break;
    case "full-orange":
      classes.push(styles.fullOrange);
      break;
    case "partial-orange":
      classes.push(styles.partialOrange);
      break;
    default:
      break;
  }

  const finalClassName = classes.join(" ");
  return <button className={finalClassName} {...props} />;
}
