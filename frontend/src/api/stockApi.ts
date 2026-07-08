import type { Stock } from "../types/domain";
import { apiClient } from "./httpClient";

const DEFAULT_HISTORY = [42, 48, 44, 52, 58, 62, 57, 69, 74, 82];

type StockResponse = {
  id: number;
  code: string;
  name: string;
  description?: string;
  currentPrice: number | string;
  changeRate?: number;
  history?: number[];
};

function toStock(response: StockResponse): Stock {
  return {
    id: Number(response.id),
    code: response.code,
    name: response.name,
    description: response.description ?? "종목 설명 준비 중",
    currentPrice: Number(response.currentPrice),
    changeRate: response.changeRate ?? 0,
    history: response.history ?? DEFAULT_HISTORY
  };
}

export async function getStocks(): Promise<Stock[]> {
  try {
    // Backend API: GET /api/stocks
    const response = await apiClient.get<StockResponse[]>("/api/stocks");
    return response.data.map(toStock);
  } catch {
    return [];
  }
}

export async function getStock(stockId: number): Promise<Stock | null> {
  try {
    // Backend API: GET /api/stocks/{stockId}
    const response = await apiClient.get<StockResponse>(`/api/stocks/${stockId}`);
    return toStock(response.data);
  } catch {
    return null;
  }
}

export async function getStockPrices(stockId: number): Promise<number[]> {
  try {
    // Backend API: GET /api/stocks/{stockId}/prices
    const response = await apiClient.get<Array<{ price: number | string }>>(`/api/stocks/${stockId}/prices`);
    return response.data.map((item) => Number(item.price));
  } catch {
    return DEFAULT_HISTORY;
  }
}
