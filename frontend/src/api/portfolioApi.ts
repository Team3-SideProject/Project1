import { initialHoldings } from "../mocks";
import type { Holding, PortfolioApiResponse } from "../types/domain";
import { apiClient } from "./httpClient";

export async function getPortfolio(): Promise<PortfolioApiResponse | null> {
  try {
    // Backend API: GET /api/portfolio/me
    const response = await apiClient.get<PortfolioApiResponse>("/api/portfolio/me");
    return response.data;
  } catch {
    return null;
  }
}

export async function getHoldings(): Promise<Holding[]> {
  try {
    // Backend API: GET /api/portfolio/me
    const response = await apiClient.get("/api/portfolio/me");
    const data = response.data;
    return data.holdings ?? data.rows ?? data;
  } catch {
    return initialHoldings;
  }
}
