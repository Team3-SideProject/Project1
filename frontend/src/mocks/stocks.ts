import type { Stock } from "../types/domain";

export const initialStocks: Stock[] = [
  {
    id: 1,
    code: "ALP",
    name: "Alpha Tech",
    description: "안정적인 기술주",
    currentPrice: 50000,
    changeRate: 2.43,
    history: [42, 48, 44, 52, 58, 62, 57, 69, 74, 82]
  },
  {
    id: 2,
    code: "BET",
    name: "Beta Bio",
    description: "변동성이 큰 바이오주",
    currentPrice: 12000,
    changeRate: -1.18,
    history: [68, 61, 64, 56, 50, 53, 45, 42, 46, 39]
  },
  {
    id: 3,
    code: "CRN",
    name: "Crown Energy",
    description: "에너지 관련 종목",
    currentPrice: 30000,
    changeRate: 0.72,
    history: [38, 42, 41, 47, 51, 55, 53, 59, 61, 65]
  },
  {
    id: 4,
    code: "DLT",
    name: "Delta Mobility",
    description: "모빌리티 관련 종목",
    currentPrice: 24000,
    changeRate: 3.91,
    history: [32, 36, 44, 49, 58, 63, 67, 72, 76, 83]
  },
  {
    id: 5,
    code: "ECH",
    name: "Echo Games",
    description: "게임/엔터 관련 종목",
    currentPrice: 8000,
    changeRate: -2.36,
    history: [72, 67, 61, 56, 59, 51, 47, 43, 46, 40]
  },
  {
    id: 6,
    code: "FRN",
    name: "Front Finance",
    description: "금융 관련 종목",
    currentPrice: 70000,
    changeRate: 1.04,
    history: [46, 49, 48, 52, 55, 54, 59, 61, 60, 64]
  },
  {
    id: 7,
    code: "GLD",
    name: "Gold Retail",
    description: "리테일 관련 종목",
    currentPrice: 18000,
    changeRate: 0.31,
    history: [43, 45, 44, 46, 48, 50, 49, 52, 54, 56]
  },
  {
    id: 8,
    code: "HYP",
    name: "Hyper AI",
    description: "AI 성장주",
    currentPrice: 95000,
    changeRate: 6.85,
    history: [30, 36, 50, 46, 62, 71, 66, 83, 88, 96]
  },
  {
    id: 9,
    code: "IVY",
    name: "Ivy Foods",
    description: "식품 관련 종목",
    currentPrice: 15000,
    changeRate: -0.64,
    history: [55, 56, 53, 54, 51, 52, 50, 49, 48, 47]
  }
];
