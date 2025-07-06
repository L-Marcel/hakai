import { Navigate, Route, Routes } from "react-router-dom";
import "@styles/global.scss";
import MainPage from "@pages/Main";
import RegisterPage from "@pages/Register";
import DashboardPage from "@pages/Dashboard";
import RoomPage from "@pages/Room";
import RoomPanelPage from "@pages/RoomPanel";
import { useEffect } from "react";
import Results from "@pages/Results";

function App() {
  useEffect(() => {
    const handleClick = (e: MouseEvent) => {
      const target = e.target as HTMLElement;
      if (!target.closest("[data-selectable]")) {
        window.getSelection()?.removeAllRanges();
      }
    };

    document.addEventListener("click", handleClick);

    return () => {
      document.removeEventListener("click", handleClick);
    };
  }, []);

  return (
    <>
      <Routes>
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/room/panel/:code" element={<RoomPanelPage />} />
        <Route path="/home" element={<MainPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/room/:code" element={<RoomPage />} />
        <Route path="/game-results/:uuid" element={<Results />} />
        <Route path="*" element={<Navigate to="/home" />} />
      </Routes>
    </>
  );
}

export default App;
