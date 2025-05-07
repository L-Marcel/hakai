import { useState } from "react";
import useAuth, { LoginData } from "@stores/useAuth";
import styles from "./index.module.scss";
import Input from "@components/Input";
import Button from "@components/Button";

export default function LoginForm() {
  const login = useAuth((state) => state.login);
  const [error, setError] = useState("");
  const [data, setData] = useState<LoginData>({
    email: "",
    password: "",
  });

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setData({ ...data, [e.target.name]: e.target.value });
    setError("");
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const response = await login(data);
    if (!response.ok) setError(response.error.message);
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
