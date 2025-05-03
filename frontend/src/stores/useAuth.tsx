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
      token: undefined,
      user: undefined,
      register: async (data: RegisterUserData) => {
        const response = await fetch("http://localhost:8080/users", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(data),
        });
        
        if (response.ok) {
          return {
            ok: true
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
            uuid: "ac87ba139-8903-4934-8f5d-baf74e291600" as UUID,
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
        const response = await fetch("http://localhost:8080/users/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(data),
        });

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
        set({
          token: undefined,
          user: undefined,
        });
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
