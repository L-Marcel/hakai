import styles from "./index.module.scss";
import Card from "@components/Card";

export default function GamesPage() {
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
}
