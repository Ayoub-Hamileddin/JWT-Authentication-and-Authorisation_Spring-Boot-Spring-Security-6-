import { baseApi } from "../../app/api/apiSlice";
import { AUTH_URL } from "../constantes";
export const authApiSlice = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.mutation({
      query: (credentials) => ({
        url: `${AUTH_URL}/login`,
        method: "POST",
        body: { ...credentials },
      }),
    }),
  }),
});

export const { useLoginMutation } = authApiSlice;
