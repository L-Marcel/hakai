import Input from "@components/Input";
import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import Button from "@components/Button";
import { register, RegisterUserData } from "../../services/user";

export default function RegisterForm() {
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [data, setData] = useState<RegisterUserData>({
    email: "",
    password: "",
    name: "",
  });

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    if(name === "confirmPassword") {
      setConfirmPassword(value);
    } else {
      const updatedData = { ...data, [name]: value };
      setData(updatedData);
    }

    setError("");
  };

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if(data.password !== confirmPassword) {
      setError("As senhas não coincidem.");
      return;
    }

    register(data)
      .then(() => {
        navigate("/login");
      })
      .catch((error: HttpError) => {
        setError(error.message);
      });
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
      <Input
        autoComplete="off"
        type="password"
        name="confirmPassword"
        placeholder="Confirme a senha"
        onChange={onChange}
      />
      {error && <p className={styles.error}>{error}</p>}
      <Button theme="full-orange" type="submit">
        Registrar
      </Button>
    </form>
  );
}
