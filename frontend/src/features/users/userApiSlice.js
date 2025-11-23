import { baseApi } from "../../app/api/apiSlice";
import { USER_URL } from "../constantes";

const userApiSlice = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getAllUsers: builder.query({
      query: () => ({
        url: `${USER_URL}`,
      }),
    }),
  }),
});

export const { useGetAllUsersQuery } = userApiSlice;
