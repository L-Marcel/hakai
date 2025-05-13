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
export async function load({ setUser, token }: AuthStore): Promise<Result> {
  try {
    const response = await fetch(
      `${import.meta.env.VITE_BACKEND_URL}/users/me`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (response.ok) {
      const user = await response.json();
      setUser(user);
      return { ok: true };
    } else {
      logout();
      const error = await response.json();
      return {
        ok: false,
        error,
      };
    }
  } catch (err) {
    logout();
    return {
      ok: false,
      error: {
        message: "Erro de rede ao carregar o usuário.",
        status: 500,
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
