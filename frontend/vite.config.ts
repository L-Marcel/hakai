import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "node:path";

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@styles": path.join(__dirname, "src", "styles"),
      "@pages": path.join(__dirname, "src", "pages"),
      "@components": path.join(__dirname, "src", "components"),
      "@assets": path.join(__dirname, "src", "assets"),
    },
  },
  css: {
    preprocessorOptions: {
      scss: {
        api: "legacy",
        silenceDeprecations: ["legacy-js-api", "import"],
        additionalData: '@import "@styles/variables.scss";\n'
      }
    }
  }
});
