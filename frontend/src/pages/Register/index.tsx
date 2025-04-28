import { useState } from "react";
import styles from "@pages/Main/index.module.scss";
import { useNavigate } from "react-router-dom";
import background from "@assets/undraw_hiking.svg";
import AuthGuard from "@components/Guards/AuthGuard";

export default function RegisterPage() {
  return (
    <AuthGuard onlyUnauthenticated>
      <Page/>
    </AuthGuard>
  );
};

function Page() {
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    name: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async(e: React.FormEvent) => {
    e.preventDefault();
    const response = await fetch("http://localhost:8080/users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    if(response.ok) {
      navigate("/login");
    } else {
      const data = await response.json();
      setError(data.message);
    };
  };

  return (
    <main className={styles.main}>
      <img className={styles.background} src={background} alt=""/>
      <section className={styles.section}>
        <div className={styles.title}>
          <h1>HAKAI</h1>
          <p>Aprendendo de maneiras diferentes</p>
        </div>
        <form className={styles.form} onSubmit={handleSubmit}>
          <input
            autoComplete="off"
            type="text"
            name="name"
            placeholder="Nome"
            className={styles.input}
            onChange={handleChange}
          />
          <input
            autoComplete="off"
            type="email"
            name="email"
            placeholder="Email"
            className={styles.input}
            onChange={handleChange}
          />
          <input
            autoComplete="off"
            type="password"
            name="password"
            placeholder="Senha"
            className={styles.input}
            onChange={handleChange}
          />
          {error && <p className={styles.error}>{error}</p>}
          <button type="submit" className={styles.button}>
            Registrar
          </button>
        </form> 
        <p>Já tem conta? <a href="/login">Entre</a>.</p>
      </section>
    </main>
  );
}
