import { rankingUsers } from "../mocks";
import type { RankingUser } from "../types/domain";

const API_BASE_URL = "http://localhost:8080";

export async function getRankings(): Promise<RankingUser[]> {
  try {
    // Backend API: GET /api/rankings/assets
    const response = await fetch(`${API_BASE_URL}/api/rankings/assets`);
    if (!response.ok) throw new Error("랭킹 조회 실패");
    return await response.json();
  } catch {
    return rankingUsers;
  }
}
