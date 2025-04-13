import { useState } from "react";
import styles from "./index.module.scss";
import Register from "@pages/Register";

export default function Login() {
  const [formData, setFormData] = useState({
    email: "",
    senha: ""
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const formParams = new URLSearchParams();
    formParams.append("email", formData.email);
    formParams.append("senha", formData.senha);

    try {
      const response = await fetch("http://localhost:8080/users/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: formParams.toString()
      });

      if (response.ok) {
        const data = await response.json();
        const mensagem = data.mensagem;
        const token = data.token;
        localStorage.setItem("token", token);

        alert(`Login realizado com sucesso!\n${mensagem}`);


      } else {
        alert("Email ou senha inválidos.");
      }
    } catch (error) {
      console.error("Erro ao fazer login:", error);
      alert("Erro ao tentar fazer login.");
    }
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
            name="senha"
            placeholder="Senha"
            className={styles.input}
            onChange={handleChange}
          />

          <button type="submit" className={styles.button}>Entrar</button>



        </form>    <p>Não tem login? <a href="/register">Registre-se</a></p>
      </section>
    </main>
  );
}
