import React from "react";
import { useGetAllUsersQuery } from "../features/users/userApiSlice";
import { useSelector } from "react-redux";
import { selectCurrentUser } from "../features/auth/authSlice";
const UsersList = () => {
  const {
    data: users,
    isLoading,
    isError,
    isSuccess,
    error,
  } = useGetAllUsersQuery();

  const connectedUser = useSelector(selectCurrentUser);
  let content;
  if (isLoading) {
    content = <p>"Loading..."</p>;
  } else if (isSuccess) {
    content = (
      <section className="users">
        <h1>Welcome {connectedUser}</h1>
        <h3>Users List</h3>
        <ul>
          {users.map((user) => {
            return <li key={user.id}>{user.firstName}</li>;
          })}
        </ul>
        <Link to="/welcome">Back to Welcome</Link>
      </section>
    );
  } else if (isError) {
    content = <p>{JSON.stringify(error)}</p>;
  }
  return content;
};

export default UsersList;
