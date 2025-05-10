import useRoom, { Participant, Room } from "@stores/useRoom";
import { connect, disconnect } from "./socketService";
import { UUID } from "crypto";

export async function join(nickname: string, code?: string): Promise<Result> {
  const { setParticipant } = useRoom.getState();
  const response = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/rooms/${code}/join`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        nickname,
      }),
    }
  );

  if (response.ok) {
    const participant: Participant = await response.json();
    connect(code, participant.uuid);
    setParticipant(participant);
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

export async function close(code: string): Promise<Result> {
  const response = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/rooms/${code}/close`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (response.ok) {
    disconnect();
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

export async function create(game: UUID): Promise<Result<string>> {
  const response = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/rooms/create`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ game }),
    }
  );

  if (response.ok) {
    const room: Room = await response.json();
    connect(room.code);
    return {
      ok: true,
      value: room.code,
    };
  } else {
    const error = await response.json();
    return {
      ok: false,
      error,
    };
  }
}

export async function check(code?: string): Promise<Result> {
  const { setExists } = useRoom.getState();
  const response = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/rooms/${code}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (response.ok) {
    setExists(true);
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
