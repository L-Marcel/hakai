import Button from "@components/Button";
import Input from "@components/Input";
import { useState } from "react";
import { FaArrowRight } from "react-icons/fa";
import styles from "./index.module.scss";
import { useParams } from "react-router-dom";
import { join } from "../../services/room";

export default function JoinForm() {
  const { code } = useParams();
  const [error, setError] = useState("");
  const [nickname, setNickname] = useState("");

  const onChangeNickname = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
    setError("");
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const response = await join(nickname, code);
    if (!response.ok) setError(response.error.message);
  };

  return (
    <form name="join" className={styles.form} onSubmit={onSubmit}>
      <div>
        <Input
          autoComplete="off"
          name="nickname"
          placeholder="Apelido"
          onChange={onChangeNickname}
          value={nickname}
        />
        <Button theme="full-orange" type="submit" tabIndex={-1}>
          <FaArrowRight />
        </Button>
      </div>
      {error && <p className={styles.error}>{error}</p>}
    </form>
  );
}
