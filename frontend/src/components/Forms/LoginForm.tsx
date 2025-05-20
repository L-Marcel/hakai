import { useState } from "react";
import styles from "./index.module.scss";
import Input from "@components/Input";
import Button from "@components/Button";
import { login, LoginData } from "../../services/user";

export default function LoginForm() {
  const [error, setError] = useState("");
  const [data, setData] = useState<LoginData>({
    email: "",
    password: "",
  });

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setData({ ...data, [e.target.name]: e.target.value });
    setError("");
  };

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    login(data).catch((error: HttpError) => {
      setError(error.message);
    });
  };

  return (
    <form className={styles.form} onSubmit={onSubmit}>
      <Input
        autoComplete="off"
        type="email"
        name="email"
        placeholder="Email"
        onChange={onChange}
        value={data.email}
      />
      <Input
        autoComplete="off"
        type="password"
        name="password"
        placeholder="Senha"
        onChange={onChange}
        value={data.password}
      />
      {error && <p className={styles.error}>{error}</p>}
      <Button theme="full-orange" type="submit">
        Entrar
      </Button>
    </form>
  );
}
