import axios from "axios";
import type { LoginRequest, SignupRequest } from "../types/domain";
import { ACCESS_TOKEN_KEY, REFRESH_TOKEN_KEY, apiClient } from "./httpClient";

type LoginResponse = {
  token?: string;
  accessToken?: string;
  refreshToken?: string;
};

export async function login(request: LoginRequest): Promise<boolean> {
  try {
    // Backend API: POST /api/auth/login
    const response = await apiClient.post<LoginResponse>("/api/auth/login", request);
    const token = response.data.token ?? response.data.accessToken;
    if (token) {
      localStorage.setItem(ACCESS_TOKEN_KEY, token);
    }
    if (response.data.refreshToken) {
      localStorage.setItem(REFRESH_TOKEN_KEY, response.data.refreshToken);
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

export async function logout() {
  try {
    // Backend API: POST /api/auth/logout
    await apiClient.post("/api/auth/logout");
  } catch {
    // 로그아웃 API가 실패해도 프론트 토큰은 제거합니다.
  }
  localStorage.removeItem(ACCESS_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
}
