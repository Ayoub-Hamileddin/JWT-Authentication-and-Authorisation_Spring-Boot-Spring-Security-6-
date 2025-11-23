import React, { useState } from "react";
import { useLoginMutation } from "../features/auth/authApiSlice";
import { setCredentials } from "../features/auth/authSlice";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
const Login = () => {
  const [email, setUser] = useState();
  const [password, setPassword] = useState();

  const [login, { isLoading }] = useLoginMutation();

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const redirecte = location.state?.path;
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const userData = await login({ email, password }).unwrap();
      dispatch(setCredentials({ ...userData, email }));

      navigate("/welcome");
    } catch (error) {
      console.error(error);
    }
  };

  if (isLoading) {
    return <p>loading ...</p>;
  }

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <label>
          email:
          <input type="text" onChange={(e) => setUser(e.target.value)} />
        </label>
        <br />
        <label>
          password :
          <input
            type="password"
            onChange={(e) => setPassword(e.target.value)}
          />
        </label>
        <br />
        <button>Sign In</button>
      </form>
    </div>
  );
};

export default Login;
