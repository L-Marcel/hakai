import useAuth, { AuthStore } from "@stores/useAuth";
import { UUID } from "crypto";

export type LoginData = {
  email: string;
  password: string;
};

export type RegisterUserData = LoginData & {
  name: string;
};

export function logout(): void {
  const { setUser, setToken } = useAuth.getState();

  setUser(undefined);
  setToken(undefined);
}

export async function login(data: LoginData): Promise<Result> {
  const { setToken } = useAuth.getState();

  const response = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/users/login`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }
  );

  if (response.ok) {
    const token = await response.text();
    setToken(token);
    return await load(useAuth.getState());
  } else {
    const error = await response.json();
    return {
      ok: false,
      error,
    };
  }
}

export async function load({ setUser }: AuthStore): Promise<Result> {
  // [TODO] Fazer a requisição aqui quando tiver a rota...
  // eslint-disable-next-line no-constant-condition
  if (true) {
    const user = {
      uuid: "08b89edf-6ce1-4c41-8b98-6daff49146c5" as UUID,
      name: "Administrador",
    };

    setUser(user);
    return {
      ok: true,
    };
  } else {
    logout();
    return {
      ok: false,
      error: {
        message: "",
        status: 400,
      },
    };
  }
}

export async function register(data: RegisterUserData): Promise<Result> {
  const response = await fetch(`${import.meta.env.VITE_BACKEND_URL}/users`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (response.ok) {
    return {
      ok: true,
    };
  } else {
    const error = await response.json();
    return {
      ok: false,
      error,
    };
  }
}
