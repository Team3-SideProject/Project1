import { initialHoldings } from "../mocks";
import type { Holding } from "../types/domain";

const API_BASE_URL = "http://localhost:8080";

export async function getHoldings(): Promise<Holding[]> {
  try {
    // Backend API: GET /api/portfolio/me
    const response = await fetch(`${API_BASE_URL}/api/portfolio/me`);
    if (!response.ok) throw new Error("포트폴리오 조회 실패");
    const data = await response.json();
    return data.holdings ?? data.rows ?? data;
  } catch {
    return initialHoldings;
  }
}
