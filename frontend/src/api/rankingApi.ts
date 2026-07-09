import { rankingUsers } from "../mocks";
import type { RankingUser } from "../types/domain";
import { apiClient } from "./httpClient";

export async function getRankings(): Promise<RankingUser[]> {
  try {
    // Backend API: GET /api/rankings/assets
    const response = await apiClient.get<RankingUser[]>("/api/rankings/assets");
    return response.data;
  } catch {
    return rankingUsers;
  }
}
