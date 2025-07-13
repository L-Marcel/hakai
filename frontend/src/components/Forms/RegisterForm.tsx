import Input from "@components/Input";
import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import Button from "@components/Button";
import { register, RegisterUserData } from "../../services/user";
import ErrorLabel from "./ErrorsLabel";

export default function RegisterForm() {
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [errors, setErrors] = useState<ValidationError[]>([]);
  const [data, setData] = useState<RegisterUserData>({
    email: "",
    password: "",
    confirmPassword: "",
    name: "",
  });

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    const updatedData = { ...data, [name]: value };
    setData(updatedData);
  };

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setErrors([]);
    register(data)
      .then(() => {
        navigate("/login");
      })
      .catch((error: HttpError | ValidationErrors) => {
        if (error.status === 400 && "errors" in error) {
          setErrors((error as ValidationErrors).errors);
        } else {
          setError((error as HttpError).message);
        }
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
      <ErrorLabel field="name" errors={errors} />
      <Input
        autoComplete="off"
        name="email"
        placeholder="Email"
        onChange={onChange}
      />
      <ErrorLabel field="email" errors={errors} />
      <Input
        autoComplete="off"
        type="password"
        name="password"
        placeholder="Senha"
        onChange={onChange}
      />
      <ErrorLabel field="password" errors={errors} />
      <Input
        autoComplete="off"
        type="password"
        name="confirmPassword"
        placeholder="Confirme a senha"
        onChange={onChange}
      />
      <ErrorLabel field="confirmPassword" errors={errors} />
      {error && <p className={styles.error}>{error}</p>}
      <Button theme="full-purple" type="submit">
        Registrar
      </Button>
    </form>
  );
}
