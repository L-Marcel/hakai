export {};

declare global {
  export type HttpError = {
    message: string;
    status: number;
  };

  export type Result<T = undefined> =
    | (T extends undefined
        ? {
            ok: true;
          }
        : {
            ok: true;
            value: T;
          })
    | {
        ok: false;
        error: HttpError;
      };
}
