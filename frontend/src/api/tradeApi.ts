import { initialTrades } from "../mocks";
import type { Trade } from "../types/domain";

const API_BASE_URL = "http://localhost:8080";

type TradeRequest = {
  stockId: number;
  quantity: number;
};

export async function getTrades(): Promise<Trade[]> {
  try {
    // Backend API: GET /api/trades/me
    const response = await fetch(`${API_BASE_URL}/api/trades/me`);
    if (!response.ok) throw new Error("거래 내역 조회 실패");
    return await response.json();
  } catch {
    return initialTrades;
  }
}

export async function buyStock(request: TradeRequest): Promise<void> {
  try {
    // Backend API: POST /api/trades/buy
    await fetch(`${API_BASE_URL}/api/trades/buy`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(request)
    });
  } catch {
    return;
  }
}

export async function sellStock(request: TradeRequest): Promise<void> {
  try {
    // Backend API: POST /api/trades/sell
    await fetch(`${API_BASE_URL}/api/trades/sell`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(request)
    });
  } catch {
    return;
  }
}
