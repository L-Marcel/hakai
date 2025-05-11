import useRoom from "@stores/useRoom";
import { UUID } from "crypto";

export async function generate(original: UUID): Promise<Result> {
  const { room } = useRoom.getState();

  const response = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/rooms/${room?.code}/question/${original}/generate`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
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
}
