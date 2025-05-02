import styles from "./index.module.scss";
import background from "@assets/undraw_hiking.svg";
import AuthGuard from "@components/Guards/AuthGuard";
import CheckRoomForm from "@components/Forms/CheckRoomForm";
import LoginForm from "@components/Forms/LoginForm";

export default function MainPage() {
  return (
    <AuthGuard onlyUnauthenticated>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  return (
    <main className={styles.main}>
      <img className={styles.background} src={background} alt="" />
      <section className={styles.section}>
        <div className={styles.title}>
          <h1>HAKAI</h1>
          <p>Aprendendo de maneiras diferentes</p>
        </div>
        <CheckRoomForm />
        <hr />
        <LoginForm />
        <p>
          Não tem login? <a href="/register">Registre-se</a>.
        </p>
      </section>
    </main>
  );
}
