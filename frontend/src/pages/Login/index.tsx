import { useState } from "react";
import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [formData, setFormData] = useState({
    email: "",
    password: ""
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async(e: React.FormEvent) => {
    e.preventDefault();

    const response = await fetch("http://localhost:8080/users/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(formData)
    });

   
    if(response.ok) {
      const token = await response.text();
      localStorage.setItem("token", token);
      navigate("/");
    } else {
      const data = await response.json();
      setError(data.message);
    };
  };

  return (
    <main className={styles.main}>
      <section className={styles.section}>
        <h2 className={styles.title}>Login</h2>
        <form className={styles.form} onSubmit={handleSubmit}>
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
          <button type="submit" className={styles.button}>Entrar</button>
        </form>    
        <p>Não tem login? <a href="/register">Registre-se</a></p>
      </section>
    </main>
  );
}
