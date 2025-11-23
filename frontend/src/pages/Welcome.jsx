import React from "react";
import { useSelector } from "react-redux";
import {
  selectCurrentToken,
  selectCurrentUser,
} from "../features/auth/authSlice";

const Welcome = () => {
  const user = useSelector(selectCurrentUser);
  const token = useSelector(selectCurrentToken);

  return (
    <div>
      sec{" "}
      <section className="welcome">
        <h1>Welcome {user}</h1>
        <p>Token: {token}</p>

        <p>Go to the Users List</p>
      </section>
    </div>
  );
};

export default Welcome;
