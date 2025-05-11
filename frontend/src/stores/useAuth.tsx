import { UUID } from "crypto";
import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import { load } from "../services/authService";

export type User = {
  uuid: UUID;
  name: string;
};

export type AuthStore = {
  token?: string;
  user?: User;
  setToken: (token?: string) => void;
  setUser: (user?: User) => void;
};

const useAuth = create<AuthStore>()(
  persist(
    (set) => ({
      setToken: (token?: string) => set({ token }),
      setUser: (user?: User) => set({ user }),
    }),
    {
      name: "hakai@auth",
      storage: createJSONStorage(() => localStorage),
      partialize: (state: AuthStore) => ({ token: state.token }),
      onRehydrateStorage: () => {
        return (state?: AuthStore, error?: unknown) => {
          if (state?.token && !error) load(state);
        };
      },
    }
  )
);

export default useAuth;
