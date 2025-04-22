import { useState } from "react";
import styles from "@pages/Login/index.module.scss";
import { useNavigate } from "react-router-dom";

export default function Register() {
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
      <section className={styles.section}>
        <h2 className={styles.title}>Registre-se</h2>
        <form className={styles.form} onSubmit={handleSubmit}>
          <input
            type="text"
            name="name"
            placeholder="Nome"
            className={styles.input}
            onChange={handleChange}
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            className={styles.input}
            onChange={handleChange}
          />
          <input
            type="password"
            name="password"
            placeholder="Senha"
            className={styles.input}
            onChange={handleChange}
          />
          {error && <p>{error}</p>}
          <button type="submit" className={styles.button}>
            Registrar
          </button>
        </form> 
        <p>Já tem conta? <a href="/login">Log in</a></p>
      </section>
    </main>
  );
}
