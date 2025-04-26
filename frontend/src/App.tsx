import { Navigate, Route, Routes } from "react-router-dom";
import "@styles/global.scss";
import MainPage from "@pages/Main";
import RegisterPage from "@pages/Register";
import GamesPage from "@pages/Games";
import Private from "@components/Private";
import JoinPage from "@pages/Join";
import RoomPage from "@pages/Room";
import RoomPanelPage from "@pages/RoomPanel";

function App() {
  return (
    <>
      <Routes>
        <Route index element={<MainPage />} />
        <Route path="register" element={<RegisterPage />} />
        <Route path="join" element={<JoinPage />}/>
        <Route element={<Private />}>
          <Route path="games" element={<GamesPage />} />
          <Route path="room/panel/:code" element={<RoomPanelPage />}/>
        </Route>
        <Route path="room/:code" element={<RoomPage />}/>
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </>
  );
}

export default App;
