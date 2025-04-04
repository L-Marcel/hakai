import { Route, Routes } from "react-router-dom";
import "@styles/global.scss";
import Login from "@pages/Login";
import Register from "@pages/Register";
import Dashboard from "@pages/Dashboard";

function App() {
  return (
    <>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/:id" element={<Dashboard />} />
      </Routes>
    </>
  );
}

export default App;
