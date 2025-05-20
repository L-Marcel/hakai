import useAuth, { AuthStore, User } from "@stores/useAuth";
import api from "./axios";
import axios from "axios";

export type LoginData = {
  email: string;
  password: string;
};

export type RegisterUserData = LoginData & {
  name: string;
};

export function logout(store: AuthStore = useAuth.getState()): void {
  const { setUser, setToken } = store;

  setUser(undefined);
  setToken(undefined);
}

export async function login(data: LoginData): Promise<void> {
  const { setToken } = useAuth.getState();

  return await api.post<string>("users/login", data).then(async (res) => {
    setToken(res.data);
    return await load(useAuth.getState());
  });
}

export async function load(
  store: AuthStore = useAuth.getState()
): Promise<void> {
  const { setUser, token } = store;

  return await axios
    .get<User>("http://localhost:8080/users/me", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
    .then((response) => setUser(response.data))
    .catch((error: HttpError) => {
      if (error.status === 401) logout(store);
      throw error;
    });
}

export async function register(data: RegisterUserData): Promise<void> {
  return await api.post("users", data);
}
