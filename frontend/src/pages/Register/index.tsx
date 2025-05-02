import styles from "@pages/Main/index.module.scss";
import background from "@assets/undraw_hiking.svg";
import AuthGuard from "@components/Guards/AuthGuard";
import RegisterForm from "@components/Forms/RegisterForm";

export default function RegisterPage() {
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
        <RegisterForm />
        <p>
          Já tem conta? <a href="/login">Entre</a>.
        </p>
      </section>
    </main>
  );
}
