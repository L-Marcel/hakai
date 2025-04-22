import { Navigate, Route, Routes } from "react-router-dom";
import "@styles/global.scss";
import Login from "@pages/Login";
import Register from "@pages/Register";
import Dashboard from "@pages/Dashboard";
import Private from "@components/Private";

function App() {
  return (
    <>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Private>
          <Dashboard />
        </Private>} />
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </>
  );
}

export default App;
