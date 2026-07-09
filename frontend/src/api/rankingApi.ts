import type { RankingApiResponse, RankingRow } from "../types/domain";
import { apiClient } from "./httpClient";

function toRankingRow(response: RankingApiResponse): RankingRow {
  return {
    userId: Number(response.userId),
    nickname: response.nickname,
    totalAsset: Number(response.totalAsset),
    rank: Number(response.ranking)
  };
}

export async function getRankings(): Promise<RankingRow[]> {
  try {
    // Backend API: GET /api/rankings
    const response = await apiClient.get<RankingApiResponse[]>("/api/rankings");
    return response.data.map(toRankingRow);
  } catch {
    return [];
  }
}
