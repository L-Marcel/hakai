import "dotenv/config";
import ngrok from "@ngrok/ngrok";
import { spawn } from "node:child_process";

(async function () {
  const backendListener = await ngrok.forward({
    addr: 8080,
    authtoken_from_env: true,
  });

  const frontendListener = await ngrok.forward({
    addr: 5173,
    authtoken_from_env: true,
  });

  console.log(`Backend: ${backendListener.url()}\n`);
  console.log(`Frontend: ${frontendListener.url()}`);

  const vite = spawn("pnpm", ["vite"], {
    stdio: "inherit",
    shell: true,
    env: {
      ...process.env,
      VITE_BACKEND_URL: backendListener.url() as string,
      VITE_WEBSOCKET_URL: backendListener
        .url()
        ?.replace("https://", "wss://") as string,
    },
  });

  vite.on("exit", (code: string) => {
    console.log(`Vite finalizou com código ${code}`);
    process.exit(code ?? 0);
  });
})();

process.stdin.resume();
