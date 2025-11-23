import React from "react";
import { useSelector } from "react-redux";
import {
  selectCurrentToken,
  selectCurrentUser,
} from "../features/auth/authSlice";
import { Navigate, Outlet, useLocation } from "react-router-dom";
const RequiredAuth = () => {
  const user = useSelector(selectCurrentUser);
  const token = useSelector(selectCurrentToken);
  const location = useLocation();

  if (!(token && user)) {
    return <Navigate to="/login" state={{ path: location.pathname }} />;
  }
  return <Outlet />;
};

export default RequiredAuth;
