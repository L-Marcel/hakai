import useAuth from "@stores/useAuth";
import axios, { AxiosError } from "axios";
import { logout } from "./user";

const api = axios.create({
  baseURL: `${import.meta.env.VITE_BACKEND_URL}`,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((request) => {
  const token = useAuth.getState().token;

  if (token) {
    request.headers["Authorization"] = "Bearer " + token;
  } else {
    const storage = localStorage.getItem("hakai@auth");
    if (storage) {
      const parsed = JSON.parse(storage);
      const storedToken = parsed?.state?.token;
      if (storedToken) {
        useAuth.getState().setToken(storedToken);
        request.headers["Authorization"] = "Bearer " + storedToken;
      }
    }
  }

  return request;
});

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) logout();

    if (error.response?.data) {
      const apiError = error.response?.data as HttpError;
      return Promise.reject(apiError);
    }

    return Promise.reject({
      status: error.response?.status,
      data: error.response?.data,
      message: "Erro inesperado!",
    } as HttpError);
  }
);

export default api;
