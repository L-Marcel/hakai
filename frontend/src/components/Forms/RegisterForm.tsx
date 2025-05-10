import Input from "@components/Input";
import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import Button from "@components/Button";
import useAuth, { RegisterUserData } from "@stores/useAuth";

export default function RegisterForm() {
  const navigate = useNavigate();
  const register = useAuth((state) => state.register);
  const [error, setError] = useState(""); const [confirmPassword, setConfirmPassword] = useState("");
  const [data, setData] = useState<RegisterUserData>({
    email: "",
    password: "",
    name: "",
  });

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {


    const { name, value } = e.target;

    if (name === "confirmPassword") {
      setConfirmPassword(value);


    } else {

      const updatedData = { ...data, [name]: value };
      setData(updatedData);

    }
    setError("");



  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!data.name || !data.email || !data.password || !confirmPassword) {
      setError("Preencha todos os campos.");
      return;
    }

    if (data.password.length < 6) {
      setError("A senha deve ter pelo menos 6 caracteres.");
      return;
    }

    if (data.password !== confirmPassword) {
      setError("As senhas não coincidem.");
      return;
    }

    const response = await register(data);
    if (response.ok) navigate("/login");
    else setError(response.error.message);
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
        type="confirmPassword"
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
