export {};

declare global {
  export type HttpError = {
    message: string;
    status: number;
  };
}
