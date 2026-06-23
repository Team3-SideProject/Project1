export type Page = "home" | "stock" | "portfolio" | "trades" | "ranking";

export type Stock = {
  id: number;
  code: string;
  name: string;
  description: string;
  currentPrice: number;
  changeRate: number;
  history: number[];
};

export type Holding = {
  stockId: number;
  quantity: number;
  averageBuyPrice: number;
};

export type Trade = {
  id: number;
  type: "BUY" | "SELL";
  stockId: number;
  quantity: number;
  price: number;
  createdAt: string;
};

export type RankingUser = {
  nickname: string;
  cash: number;
  stockValue: number;
};
