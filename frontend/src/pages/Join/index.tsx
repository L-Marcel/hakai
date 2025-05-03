import styles from "@pages/Main/index.module.scss";
import background from "@assets/undraw_hiking.svg";
import JoinForm from "@components/Forms/JoinForm";

export default function JoinPage() {
    return (
      <main className={styles.main}>
        <img className={styles.background} src={background} alt="" />
        <section className={styles.section}>
          <div className={styles.title}>
            <h1>HAKAI</h1>
            <p>Aprendendo de maneiras diferentes</p>
          </div>
          <JoinForm />
        </section>
      </main>
    );
};