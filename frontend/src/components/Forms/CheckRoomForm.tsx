import Button from "@components/Button";
import Input from "@components/Input";
import { useState } from "react";
import { FaArrowRight } from "react-icons/fa";
import styles from "./index.module.scss";
import useRoom from "../../stores/useRoom";
import { useNavigate } from "react-router-dom";

export default function JoinForm() {
  const navigation = useNavigate();
  const check = useRoom((state) => state.check);
  const [error, setError] = useState("");
  const [code, setCode] = useState("");

  const onChangeCode = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/[^\d]*/g, "");
    if (value !== e.target.value) setError("Use apenas dígitos!");
    else setError("");
    setCode(value);
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const response = await check(code);
    if (response.ok) navigation("/room/" + code);
    else setError(response.error.message);
  };

  return (
    <form name="join" className={styles.form} onSubmit={onSubmit}>
      <div>
        <Input
          autoComplete="off"
          name="code"
          inputMode="numeric"
          placeholder="Código de sala"
          onChange={onChangeCode}
          value={code}
        />
        <Button theme="full-orange" type="submit" tabIndex={-1}>
          <FaArrowRight />
        </Button>
      </div>
      {error && <p className={styles.error}>{error}</p>}
    </form>
  );
}
