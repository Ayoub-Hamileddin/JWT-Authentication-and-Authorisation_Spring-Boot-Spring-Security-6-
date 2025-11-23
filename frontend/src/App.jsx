import React from "react";
import { Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import Welcome from "./pages/Welcome";
import Login from "./pages/Login";
import Public from "./components/Public";
import RequiredAuth from "./components/RequiredAuth";

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Layout />} />
      {/* public Route */}
      <Route index element={<Public />} />
      <Route path="/login" element={<Login />} />
      {/* protected Route */}
      <Route element={<RequiredAuth />}>
        <Route path="/welcome" element={<Welcome />} />
      </Route>                
    </Routes>
  );
};

export default App;
