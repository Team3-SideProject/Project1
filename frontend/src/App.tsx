import { useEffect, useMemo, useState } from "react";
import { matchPath, Navigate, Route, Routes, useLocation, useNavigate } from "react-router-dom";
import { login, signup } from "./api/authApi";
import { getHoldings } from "./api/portfolioApi";
import { getRankings } from "./api/rankingApi";
import { getStocks } from "./api/stockApi";
import { buyStock, getTrades, sellStock } from "./api/tradeApi";
import { initialHoldings, initialStocks, initialTrades, rankingUsers } from "./mocks";
import { HomePage } from "./pages/HomePage";
import { LoginPage } from "./pages/LoginPage";
import { PortfolioPage } from "./pages/PortfolioPage";
import { RankingPage } from "./pages/RankingPage";
import { SignupPage } from "./pages/SignupPage";
import { StockDetailPage } from "./pages/StockDetailPage";
import { TradesPage } from "./pages/TradesPage";
import type { Holding, Page, PortfolioSummary, RankingUser, Stock, Trade } from "./types/domain";
import { formatWon } from "./utils/format";

function App() {
  const location = useLocation();
  const navigate = useNavigate();
  const stockMatch = matchPath("/stocks/:stockId", location.pathname);
  const routeStockId = Number(stockMatch?.params.stockId);
  const selectedStockId = Number.isFinite(routeStockId) && routeStockId > 0 ? routeStockId : 8;

  const [quantity, setQuantity] = useState(1);
  const [cash, setCash] = useState(10_000_000);
  const [holdings, setHoldings] = useState<Holding[]>(initialHoldings);
  const [trades, setTrades] = useState<Trade[]>(initialTrades);
  const [stocks, setStocks] = useState<Stock[]>(initialStocks);
  const [rankingData, setRankingData] = useState<RankingUser[]>(rankingUsers);
  const [message, setMessage] = useState("");

  useEffect(() => {
    getStocks().then(setStocks);
    getHoldings().then(setHoldings);
    getTrades().then(setTrades);
    getRankings().then(setRankingData);
  }, []);

  const selectedStock = stocks.find((stock) => stock.id === selectedStockId) ?? stocks[0] ?? initialStocks[0];
  const selectedHolding = holdings.find((holding) => holding.stockId === selectedStock.id);
  const tradeAmount = selectedStock.currentPrice * quantity;

  const portfolio: PortfolioSummary = useMemo(() => {
    const rows = holdings.map((holding) => {
      const stock = stocks.find((item) => item.id === holding.stockId) ?? initialStocks[0];
      const valuation = stock.currentPrice * holding.quantity;
      const principal = holding.averageBuyPrice * holding.quantity;

      return {
        ...holding,
        stock,
        valuation,
        profitLoss: valuation - principal
      };
    });

    const stockValue = rows.reduce((sum, row) => sum + row.valuation, 0);
    const principal = rows.reduce((sum, row) => sum + row.averageBuyPrice * row.quantity, 0);

    return {
      rows,
      cash,
      stockValue,
      totalAsset: cash + stockValue,
      profitLoss: stockValue - principal
    };
  }, [cash, holdings, stocks]);

  const rankings = useMemo(() => {
    const userRanking = {
      nickname: "stockUser",
      cash: portfolio.cash,
      stockValue: portfolio.stockValue
    };

    return [...rankingData, userRanking]
      .map((user) => ({ ...user, totalAsset: user.cash + user.stockValue }))
      .sort((a, b) => b.totalAsset - a.totalAsset)
      .map((user, index) => ({ ...user, rank: index + 1 }));
  }, [portfolio.cash, portfolio.stockValue, rankingData]);

  const goToStock = (stockId: number) => {
    setQuantity(1);
    setMessage("");
    navigate(`/stocks/${stockId}`);
  };

  const handleLogin = () => {
    login({ email: "user@example.com", password: "password123" }).then(() => {
      setMessage("");
      navigate("/");
    });
  };

  const handleSignup = () => {
    signup({
      email: "new-user@example.com",
      nickname: "stockUser",
      password: "password123"
    }).then(() => {
      setMessage("회원가입이 완료되었습니다. 초기 자산 10,000,000원이 지급되었습니다.");
      navigate("/");
    });
  };

  const handleBuy = () => {
    if (quantity < 1) {
      setMessage("거래 수량은 1 이상이어야 합니다.");
      return;
    }

    if (tradeAmount > cash) {
      setMessage("보유 현금이 부족합니다.");
      return;
    }

    buyStock({ stockId: selectedStock.id, quantity });
    setCash((currentCash) => currentCash - tradeAmount);
    setHoldings((current) => updateHoldingAfterBuy(current, selectedStock, quantity));
    setTrades((current) => [
      createTrade("BUY", current.length + 1, selectedStock, quantity),
      ...current
    ]);
    setMessage(`${selectedStock.code} ${quantity}주 매수가 완료되었습니다.`);
  };

  const handleSell = () => {
    if (!selectedHolding || selectedHolding.quantity < quantity) {
      setMessage("보유 주식 수량이 부족합니다.");
      return;
    }

    sellStock({ stockId: selectedStock.id, quantity });
    setCash((currentCash) => currentCash + tradeAmount);
    setHoldings((current) => updateHoldingAfterSell(current, selectedStock.id, quantity));
    setTrades((current) => [
      createTrade("SELL", current.length + 1, selectedStock, quantity),
      ...current
    ]);
    setMessage(`${selectedStock.code} ${quantity}주 매도가 완료되었습니다.`);
  };

  if (location.pathname === "/login") {
    return <LoginPage onLogin={handleLogin} onGoSignup={() => navigate("/signup")} />;
  }

  if (location.pathname === "/signup") {
    return <SignupPage onSignup={handleSignup} onGoLogin={() => navigate("/login")} />;
  }

  const page = getPageFromPath(location.pathname);

  return (
    <main className="app-shell">
      <aside className="sidebar">
        <button className="brand" onClick={() => navigate("/")}>
          <span className="logo-mark">S</span>
          <strong>StockSim</strong>
        </button>
        <nav>
          <button className={page === "home" ? "active" : ""} onClick={() => navigate("/")}>
            메인
          </button>
          <button className={page === "portfolio" ? "active" : ""} onClick={() => navigate("/portfolio")}>
            포트폴리오
          </button>
          <button className={page === "trades" ? "active" : ""} onClick={() => navigate("/trades")}>
            거래 내역
          </button>
          <button className={page === "ranking" ? "active" : ""} onClick={() => navigate("/ranking")}>
            랭킹
          </button>
        </nav>
        <div className="account-card">
          <span>총 자산</span>
          <strong>{formatWon(portfolio.totalAsset)}</strong>
          <em className={portfolio.profitLoss >= 0 ? "up" : "down"}>
            {portfolio.profitLoss >= 0 ? "+" : ""}
            {formatWon(portfolio.profitLoss)}
          </em>
        </div>
      </aside>

      <section className="workspace">
        <header className="topbar">
          <div>
            <p className="eyebrow">REST API Mock UI</p>
            <h1>{getPageTitle(page)}</h1>
          </div>
          <div className="top-actions">
            <input placeholder="종목 검색" />
            <button onClick={() => navigate("/login")}>로그아웃</button>
          </div>
        </header>

        {message && <p className="toast">{message}</p>}

        <Routes>
          <Route
            path="/"
            element={
              <HomePage
                cash={cash}
                portfolio={portfolio}
                stocks={stocks}
                onOpenStock={goToStock}
              />
            }
          />
          <Route
            path="/stocks/:stockId"
            element={
              <StockDetailPage
                stock={selectedStock}
                holdingQuantity={selectedHolding?.quantity ?? 0}
                quantity={quantity}
                tradeAmount={tradeAmount}
                onQuantityChange={setQuantity}
                onBuy={handleBuy}
                onSell={handleSell}
                onBack={() => navigate("/")}
              />
            }
          />
          <Route path="/portfolio" element={<PortfolioPage portfolio={portfolio} onOpenStock={goToStock} />} />
          <Route path="/trades" element={<TradesPage trades={trades} stocks={stocks} />} />
          <Route path="/ranking" element={<RankingPage rankings={rankings} currentNickname="stockUser" />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </section>
    </main>
  );
}

function createTrade(type: "BUY" | "SELL", id: number, stock: Stock, quantity: number): Trade {
  return {
    id,
    type,
    stockId: stock.id,
    quantity,
    price: stock.currentPrice,
    createdAt: "2026-06-23 15:00"
  };
}

function updateHoldingAfterBuy(current: Holding[], stock: Stock, quantity: number): Holding[] {
  const owned = current.find((holding) => holding.stockId === stock.id);
  if (!owned) {
    return [
      ...current,
      {
        stockId: stock.id,
        quantity,
        averageBuyPrice: stock.currentPrice
      }
    ];
  }

  return current.map((holding) => {
    if (holding.stockId !== stock.id) return holding;
    const nextQuantity = holding.quantity + quantity;
    const nextAveragePrice =
      (holding.averageBuyPrice * holding.quantity + stock.currentPrice * quantity) / nextQuantity;

    return {
      ...holding,
      quantity: nextQuantity,
      averageBuyPrice: nextAveragePrice
    };
  });
}

function updateHoldingAfterSell(current: Holding[], stockId: number, quantity: number): Holding[] {
  return current
    .map((holding) =>
      holding.stockId === stockId
        ? { ...holding, quantity: holding.quantity - quantity }
        : holding
    )
    .filter((holding) => holding.quantity > 0);
}

function getPageTitle(page: Page) {
  const titles: Record<Page, string> = {
    home: "메인",
    stock: "주식 상세",
    portfolio: "포트폴리오",
    trades: "거래 내역",
    ranking: "랭킹"
  };

  return titles[page];
}

function getPageFromPath(pathname: string): Page {
  if (pathname.startsWith("/stocks/")) return "stock";
  if (pathname === "/portfolio") return "portfolio";
  if (pathname === "/trades") return "trades";
  if (pathname === "/ranking") return "ranking";
  return "home";
}

export default App;
