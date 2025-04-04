import { useState } from "react";
import styles from "./Login.module.scss";

export default function Register() {
  const [formData, setFormData] = useState({
    email: "",
    senha: "",
    nome: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const response = await fetch("http://localhost:8080/users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });
    if (response.ok) {
      alert("Usuário registrado com sucesso!");
    } else {
      alert("Erro ao registrar usuário.");
    }
  };
  return (
    <div className={styles.container}>
      <h2 className={styles.title}>Registro</h2>
      <form className={styles.form} onSubmit={handleSubmit}>
        <input
          type="text"
          name="nome"
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
          name="senha"
          placeholder="Senha"
          className={styles.input}
          onChange={handleChange}
        />
        <button type="submit" className={styles.button}>
          Registrar
        </button>
      </form>
    </div>
  );
}
