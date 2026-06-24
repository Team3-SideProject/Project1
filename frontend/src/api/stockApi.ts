import { initialStocks } from "../mocks";
import type { Stock } from "../types/domain";

const API_BASE_URL = "http://localhost:8080";
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
  const mockStock = initialStocks.find((stock) => stock.id === Number(response.id));

  return {
    id: Number(response.id),
    code: response.code,
    name: response.name,
    description: response.description ?? mockStock?.description ?? "더미 종목",
    currentPrice: Number(response.currentPrice),
    changeRate: response.changeRate ?? mockStock?.changeRate ?? 0,
    history: response.history ?? mockStock?.history ?? DEFAULT_HISTORY
  };
}

export async function getStocks(): Promise<Stock[]> {
  try {
    // Backend API: GET /api/stocks
    const response = await fetch(`${API_BASE_URL}/api/stocks`);
    if (!response.ok) throw new Error("주식 목록 조회 실패");
    const data: StockResponse[] = await response.json();
    return data.map(toStock);
  } catch {
    return initialStocks;
  }
}

export async function getStock(stockId: number): Promise<Stock> {
  try {
    // Backend API: GET /api/stocks/{stockId}
    const response = await fetch(`${API_BASE_URL}/api/stocks/${stockId}`);
    if (!response.ok) throw new Error("주식 상세 조회 실패");
    const data: StockResponse = await response.json();
    return toStock(data);
  } catch {
    return initialStocks.find((stock) => stock.id === stockId) ?? initialStocks[0];
  }
}

export async function getStockPrices(stockId: number): Promise<number[]> {
  try {
    // Backend API: GET /api/stocks/{stockId}/prices
    const response = await fetch(`${API_BASE_URL}/api/stocks/${stockId}/prices`);
    if (!response.ok) throw new Error("주식 가격 히스토리 조회 실패");
    const data: Array<{ price: number | string }> = await response.json();
    return data.map((item) => Number(item.price));
  } catch {
    return initialStocks.find((stock) => stock.id === stockId)?.history ?? DEFAULT_HISTORY;
  }
}
