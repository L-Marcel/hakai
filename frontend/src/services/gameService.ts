import useAuth from "@stores/useAuth";
import useGame from "@stores/useGame";
import { Game } from "@stores/useGame";
import { UUID } from "crypto";

export async function request(uuid?: UUID): Promise<Result> {
  const { token } = useAuth.getState();
  const { setGame } = useGame.getState();

  const response = await fetch(
    `${import.meta.env.VITE_BACKEND_URL}/games/${uuid}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    }
  );

  if (response.ok) {
    const game: Game = await response.json();
    setGame(game);
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

export async function requestAllGames(): Promise<Result<Game[]>> {
  const { token } = useAuth.getState();

  const response = await fetch(`${import.meta.env.VITE_BACKEND_URL}/games`, {
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
  });

  if (response.ok) {
    const games: Game[] = await response.json();
    return {
      ok: true,
      value: games,
    };
  } else {
    const error = await response.json();
    return {
      ok: false,
      error,
    };
  }
}
