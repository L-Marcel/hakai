import Input from "@components/Input";
import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import Button from "@components/Button";

export default function RegisterForm() {
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    name: "",
  });

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError("");
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const response = await fetch("http://localhost:8080/users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    if (response.ok) {
      navigate("/login");
    } else {
      const data = await response.json();
      setError(data.message);
    }
  };

  return (
    <form className={styles.form} onSubmit={onSubmit}>
      <Input
        autoComplete="off"
        type="text"
        name="name"
        placeholder="Nome"
        onChange={onChange}
      />
      <Input
        autoComplete="off"
        type="email"
        name="email"
        placeholder="Email"
        onChange={onChange}
      />
      <Input
        autoComplete="off"
        type="password"
        name="password"
        placeholder="Senha"
        onChange={onChange}
      />
      {error && <p className={styles.error}>{error}</p>}
      <Button theme="full-orange" type="submit">
        Registrar
      </Button>
    </form>
  );
}
