import styles from "./index.module.scss";

export default function Login() {
  return (
    <div className={styles.container}>
      <h2 className={styles.title}>Login</h2>
      <form className={styles.form}>
        <input type="email" placeholder="Email" className={styles.input} />
        <input type="password" placeholder="Senha" className={styles.input} />
        <button type="submit" className={styles.button}>
          Entrar
        </button>
      </form>
    </div>
  );
}
