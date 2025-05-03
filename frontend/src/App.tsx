import { Navigate, Route, Routes } from "react-router-dom";
import "@styles/global.scss";
import MainPage from "@pages/Main";
import RegisterPage from "@pages/Register";
import DashboardPage from "@pages/Dashboard";
import RoomPage from "@pages/Room";
import RoomPanelPage from "@pages/RoomPanel";

function App() {
  return (
    <>
      <Routes>
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/room/panel/:code" element={<RoomPanelPage />} />
        <Route path="/home" element={<MainPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/room/:code" element={<RoomPage />} />
        <Route path="*" element={<Navigate to="/home" />} />
      </Routes>
    </>
  );
}

export default App;
