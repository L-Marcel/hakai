import { useState } from "react";
import styles from "./index.module.scss";
import background from "@assets/undraw_hiking.svg";
import useAuth, { LoginData } from "../../stores/useAuth";
import AuthGuard from "@components/Guards/AuthGuard";

export default function MainPage() {
  return (
    <AuthGuard onlyUnauthenticated>
      <Page/>
    </AuthGuard>
  );
};

function Page() {
  const login = useAuth((state) => state.login);
  const [error, setError] = useState("");
  const [data, setData] = useState<LoginData>({
    email: "",
    password: ""
  });

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setData({ ...data, [e.target.name]: e.target.value });
    setError("");
  };

  const onSubmit = async(e: React.FormEvent) => {
    e.preventDefault();
    const response = await login(data);
    if(!response.ok) setError(response.error.message);
  };

  return (
    <main className={styles.main}>
      <img className={styles.background} src={background} alt=""/>
      <section className={styles.section}>
        <div className={styles.title}>
          <h1>HAKAI</h1>
          <p>Aprendendo de maneiras diferentes</p>
        </div>
        <form className={styles.form} onSubmit={onSubmit}>
          <input
            autoComplete="off"
            type="email"
            name="email"
            placeholder="Email"
            className={styles.input}
            onChange={onChange}
          />
          <input
            autoComplete="off"
            type="password"
            name="password"
            placeholder="Senha"
            className={styles.input}
            onChange={onChange}
          />
          {error && <p className={styles.error}>{error}</p>}
          <button type="submit" className={styles.button}>Entrar</button>
        </form>
        <p>Não tem login? <a href="/register">Registre-se</a>.</p>
      </section>
    </main>
  );
}
