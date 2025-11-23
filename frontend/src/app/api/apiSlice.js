import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { logout, setCredentials } from "../../features/auth/authSlice";
import { AUTH_URL } from "../../features/constantes";

const baseQuery = fetchBaseQuery({
  baseUrl: "http://localhost:5000",
  credentials: "include",
  prepareHeaders: (headers, { getState }) => {
    const token = getState().auth.accessToken;
    if (token) {
      headers.set("Authorization", `Bearer ${token}`);
    }
    return headers;
  },
});

const baseQueryWithReauth = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);
  if (result?.error?.status === 403) {
    let refreshResult = await baseQuery(
      {
        method: "POST",
        url: `${AUTH_URL}/refresh`,
      },
      api,
      extraOptions
    );
    if (refreshResult?.data) {
      console.log("refreshResult?.data");
      const user = api.getState().auth.user;
      api.dispatch(setCredentials({ ...refreshResult.data, user }));
      result = await baseQuery(args, api, extraOptions);
    } else {
      api.dispatch(logout());
    }
  }
  return result;
};

export const baseApi = createApi({
  reducerPath: "api",
  baseQuery: baseQueryWithReauth,
  endpoints: () => ({}),
});
