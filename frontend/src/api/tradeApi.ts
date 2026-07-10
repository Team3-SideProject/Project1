import type { Trade } from "../types/domain";
import { apiClient } from "./httpClient";

type TradeRequest = {
  stockId: number;
  quantity: number;
};

type TradeResponse = {
  id: number;
  userId?: number;
  stockId: number;
  tradeType?: "BUY" | "SELL";
  type?: "BUY" | "SELL";
  quantity: number;
  price: number | string;
  totalAmount?: number | string;
  createdAt: string;
};

function toTrade(response: TradeResponse): Trade {
  return {
    id: Number(response.id),
    type: response.tradeType ?? response.type ?? "BUY",
    stockId: Number(response.stockId),
    quantity: Number(response.quantity),
    price: Number(response.price),
    createdAt: String(response.createdAt)
  };
}

export async function getTrades(): Promise<Trade[]> {
  try {
    // Backend API: GET /api/trades/me
    const response = await apiClient.get<TradeResponse[]>("/api/trades/me");
    return response.data.map(toTrade);
  } catch {
    return [];
  }
}

export async function buyStock(request: TradeRequest): Promise<Trade | null> {
  try {
    // Backend API: POST /api/trades/buy
    const response = await apiClient.post<TradeResponse>("/api/trades/buy", request);
    return toTrade(response.data);
  } catch {
    return null;
  }
}

export async function sellStock(request: TradeRequest): Promise<Trade | null> {
  try {
    // Backend API: POST /api/trades/sell
    const response = await apiClient.post<TradeResponse>("/api/trades/sell", request);
    return toTrade(response.data);
  } catch {
    return null;
  }
}
