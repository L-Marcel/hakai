export {};

declare global {
  export type HttpError = {
    message: string;
    status: number;
  };

  export type ValidationMessage = {
    error: boolean;
    content: string;
  };

  export type ValidationError = {
    field: string;
    messages: ValidationMessage[];
  };

  export type ValidationErrors = {
    status: 400;
    errors: ValidationError[];
  };
}
