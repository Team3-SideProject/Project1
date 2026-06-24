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

export type PortfolioRow = Holding & {
  stock: Stock;
  valuation: number;
  profitLoss: number;
};

export type PortfolioSummary = {
  rows: PortfolioRow[];
  cash: number;
  stockValue: number;
  totalAsset: number;
  profitLoss: number;
};

export type RankingRow = RankingUser & {
  totalAsset: number;
  rank: number;
};

export type LoginRequest = {
  email: string;
  password: string;
};

export type SignupRequest = {
  email: string;
  nickname: string;
  password: string;
};
