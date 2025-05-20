import { UUID } from "crypto";
import api from "./axios";

export async function generate(question: UUID): Promise<void> {
  return await api.post(`rooms/questions/${question}/generate`);
}
