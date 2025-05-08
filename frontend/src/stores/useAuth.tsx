import { UUID } from "crypto";
import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";

export type LoginData = {
  email: string;
  password: string;
};

export type RegisterUserData = LoginData & {
  name: string;
};

export type User = {
  uuid: UUID;
  name: string;
};

type AuthStore = {
  token?: string;
  user?: User;
  register: (data: RegisterUserData) => Promise<Result>;
  fetch: () => Promise<Result>;
  login: (data: LoginData) => Promise<Result>;
  logout: () => void;
};

const useAuth = create<AuthStore>()(
  persist(
    (set, get) => ({
      register: async (data: RegisterUserData) => {
        const response = await fetch(
          `${import.meta.env.VITE_BACKEND_URL}/users`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
          }
        );

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
      },
      fetch: async () => {
        // [TODO] Fazer a requisição aqui quando tiver a rota...
        // eslint-disable-next-line no-constant-condition
        if (true) {
          const user = {
            uuid: "f4b8ef6e-581e-4a42-bcef-3c9c4a98008f" as UUID,
            name: "Administrador",
          };

          set({ user });
          return {
            ok: true,
          };
        } else {
          get().logout();
          return {
            ok: false,
            error: {
              message: "",
              status: 400,
            },
          };
        }
      },
      login: async (data: LoginData) => {
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
          set({ token });
          return await get().fetch();
        } else {
          const error = await response.json();
          return {
            ok: false,
            error,
          };
        }
      },
      logout: () => {
        set((state) => ({
          ...state,
          token: undefined,
          user: undefined,
        }));
      },
    }),
    {
      name: "hakai@auth",
      storage: createJSONStorage(() => localStorage),
      partialize: (state: AuthStore) => ({ token: state.token }),
      onRehydrateStorage: () => {
        return (state?: AuthStore, error?: unknown) => {
          if (state?.token && !error) state.fetch();
        };
      },
    }
  )
);

export default useAuth;
