import type { LoginRequest, SignupRequest } from "../types/domain";

const API_BASE_URL = "http://localhost:8080";

export async function login(request: LoginRequest): Promise<void> {
  try {
    // Backend API: POST /api/auth/login
    await fetch(`${API_BASE_URL}/api/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(request)
    });
  } catch {
    return;
  }
}

export async function signup(request: SignupRequest): Promise<void> {
  try {
    // Backend API: POST /api/auth/signup
    await fetch(`${API_BASE_URL}/api/auth/signup`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(request)
    });
  } catch {
    return;
  }
}

export async function getMyProfile() {
  try {
    // Backend API: GET /api/auth/me
    const response = await fetch(`${API_BASE_URL}/api/auth/me`);
    if (!response.ok) throw new Error("내 정보 조회 실패");
    return await response.json();
  } catch {
    return { id: 1, email: "user@example.com", nickname: "stockUser" };
  }
}
