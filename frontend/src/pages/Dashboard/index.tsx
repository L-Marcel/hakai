import { useParams } from "react-router-dom";
import styles from "./index.module.scss";
import Card from "@components/Card";

export default function Dashboard() {
  const { id } = useParams();

  return (
    <main className={styles.main}>
      <header className={styles.header}>
        <h1>User ID: {id}</h1>
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
