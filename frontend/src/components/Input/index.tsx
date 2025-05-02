import { DetailedHTMLProps, InputHTMLAttributes, useState } from "react";
import styles from "./index.module.scss";
import Button from "@components/Button";
import { FaRegEye, FaRegEyeSlash } from "react-icons/fa";

interface Props
  extends DetailedHTMLProps<
    InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > {}

export default function Input({ className, type, ...props }: Props) {
  const [show, setShow] = useState(false);
  const classes = [styles.input, className];
  const isPassword = type === "password";
  const finalType = isPassword && show ? "text" : type;
  if (isPassword) classes.push(styles.passwordInput);
  const finalClassName = classes.join(" ");

  const onClickShowButton = () => {
    setShow((show) => !show);
  };

  return (
    <div className={styles.wrapper}>
      <input className={finalClassName} type={finalType} {...props} />
      {isPassword && (
        <Button
          type="button"
          onClick={onClickShowButton}
          className={styles.passwordButton}
        >
          {show ? <FaRegEye /> : <FaRegEyeSlash />}
        </Button>
      )}
    </div>
  );
}
