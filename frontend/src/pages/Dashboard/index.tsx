import AuthGuard from "@components/Guards/AuthGuard";
import styles from "./index.module.scss";
import Card from "@components/Card";

export default function DashboardPage() {
  return (
    <AuthGuard>
      <Page/>
    </AuthGuard>
  );
};

function Page() {
  // const token = localStorage.getItem("token");

  return (
    <main className={styles.main}>
      <header className={styles.header}>
      </header>
      <section className={styles.dashboard}>
        <ul>
          <Card />
          <Card />
          <Card />
          <Card />
          <Card />
        </ul>
      </section>
    </main>
  );
};