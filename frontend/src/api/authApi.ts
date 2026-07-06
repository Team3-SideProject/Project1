import axios from "axios";
import type { LoginRequest, SignupRequest } from "../types/domain";
import { ACCESS_TOKEN_KEY, apiClient } from "./httpClient";

type LoginResponse = {
  token?: string;
  accessToken?: string;
};

export async function login(request: LoginRequest): Promise<boolean> {
  try {
    // Backend API: POST /api/auth/login
    const response = await apiClient.post<LoginResponse>("/api/auth/login", request);
    const token = response.data.token ?? response.data.accessToken;
    if (token) {
      localStorage.setItem(ACCESS_TOKEN_KEY, token);
    }
    return true;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return false;
    }
    return true;
  }
}

export async function signup(request: SignupRequest): Promise<boolean> {
  try {
    // Backend API: POST /api/auth/signup
    await apiClient.post("/api/auth/signup", request);
    return true;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return false;
    }
    return true;
  }
}

export async function getMyProfile() {
  try {
    // Backend API: GET /api/auth/me
    const response = await apiClient.get("/api/auth/me");
    return response.data;
  } catch {
    return { id: 1, email: "user@example.com", nickname: "stockUser" };
  }
}

export function logout() {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
}
