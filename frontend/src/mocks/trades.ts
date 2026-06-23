import type { Trade } from "../types/domain";

export const initialTrades: Trade[] = [
  { id: 3, type: "BUY", stockId: 8, quantity: 2, price: 94600, createdAt: "2026-06-23 14:20" },
  { id: 2, type: "SELL", stockId: 1, quantity: 3, price: 51200, createdAt: "2026-06-23 13:40" },
  { id: 1, type: "BUY", stockId: 7, quantity: 10, price: 18100, createdAt: "2026-06-23 12:30" }
];
