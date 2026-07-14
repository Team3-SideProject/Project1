import { useEffect, useMemo, useState } from "react";
import { matchPath, Navigate, Route, Routes, useLocation, useNavigate } from "react-router-dom";
import { getMyProfile, login, logout, signup } from "./api/authApi";
import { ACCESS_TOKEN_KEY, REFRESH_TOKEN_KEY } from "./api/httpClient";
import { getPortfolio } from "./api/portfolioApi";
import { getRankings } from "./api/rankingApi";
import { getStocks } from "./api/stockApi";
import { buyStock, getTrades, sellStock } from "./api/tradeApi";
import { HomePage } from "./pages/HomePage";
import { LoginPage } from "./pages/LoginPage";
import { PortfolioPage } from "./pages/PortfolioPage";
import { RankingPage } from "./pages/RankingPage";
import { SignupPage } from "./pages/SignupPage";
import { StockDetailPage } from "./pages/StockDetailPage";
import { TradesPage } from "./pages/TradesPage";
import type {
  Holding,
  LoginRequest,
  Page,
  PortfolioApiResponse,
  PortfolioSummary,
  RankingRow,
  SignupRequest,
  Stock,
  Trade
} from "./types/domain";
import { formatWon } from "./utils/format";

type AuthStatus = "checking" | "guest" | "authenticated";

function App() {
  const location = useLocation();
  const navigate = useNavigate();
  const isAuthPage = location.pathname === "/login" || location.pathname === "/signup";
  const stockMatch = matchPath("/stocks/:stockId", location.pathname);
  const routeStockId = Number(stockMatch?.params.stockId);
  const selectedStockId = Number.isFinite(routeStockId) && routeStockId > 0 ? routeStockId : 8;

  const [quantity, setQuantity] = useState(1);
  const [cash, setCash] = useState(0);
  const [holdings, setHoldings] = useState<Holding[]>([]);
  const [trades, setTrades] = useState<Trade[]>([]);
  const [stocks, setStocks] = useState<Stock[]>([]);
  const [rankingData, setRankingData] = useState<RankingRow[]>([]);
  const [currentNickname, setCurrentNickname] = useState("stockUser");
  const [message, setMessage] = useState("");
  const [authStatus, setAuthStatus] = useState<AuthStatus>("checking");

  const refreshDashboard = async () => {
    const [nextStocks, portfolioResponse, nextTrades, nextRankings] = await Promise.all([
      getStocks(),
      getPortfolio(),
      getTrades(),
      getRankings()
    ]);
    const nextHoldings = portfolioResponse ? toHoldings(portfolioResponse, nextStocks) : [];

    setStocks(nextStocks);
    setHoldings(nextHoldings);
    setTrades(nextTrades);
    setRankingData(nextRankings);

    if (portfolioResponse) {
      setCurrentNickname(portfolioResponse.nickname);
      const stockValue = nextHoldings.reduce((sum, holding) => {
        const stock = nextStocks.find((item) => item.id === holding.stockId);
        return sum + (stock?.currentPrice ?? 0) * holding.quantity;
      }, 0);
      setCash(Math.max(0, Number(portfolioResponse.totalAsset) - stockValue));
    } else {
      setCash(0);
    }
  };

  useEffect(() => {
    let ignore = false;

    async function checkAuth() {
      const token = localStorage.getItem(ACCESS_TOKEN_KEY);
      if (!token) {
        if (!ignore) {
          setAuthStatus("guest");
        }
        return;
      }

      const profile = await getMyProfile();
      if (ignore) return;

      if (!profile) {
        localStorage.removeItem(ACCESS_TOKEN_KEY);
        localStorage.removeItem(REFRESH_TOKEN_KEY);
        setAuthStatus("guest");
        if (!isAuthPage) {
          navigate("/login", { replace: true });
        }
        return;
      }

      setCurrentNickname(profile.nickname ?? "stockUser");
      setAuthStatus("authenticated");
      await refreshDashboard();
    }

    checkAuth();

    return () => {
      ignore = true;
    };
  }, []);

  const selectedStock = stocks.find((stock) => stock.id === selectedStockId) ?? stocks[0];
  const selectedHolding = selectedStock
    ? holdings.find((holding) => holding.stockId === selectedStock.id)
    : undefined;
  const tradeAmount = selectedStock ? selectedStock.currentPrice * quantity : 0;

  const portfolio: PortfolioSummary = useMemo(() => {
    const rows = holdings.flatMap((holding) => {
      const stock = stocks.find((item) => item.id === holding.stockId);
      if (!stock) return [];

      const valuation = stock.currentPrice * holding.quantity;
      const principal = holding.averageBuyPrice * holding.quantity;

      return [{
        ...holding,
        stock,
        valuation,
        profitLoss: valuation - principal
      }];
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

  const rankings = useMemo(
    () => [...rankingData].sort((a, b) => a.rank - b.rank),
    [rankingData]
  );

  const goToStock = (stockId: number) => {
    setQuantity(1);
    setMessage("");
    navigate(`/stocks/${stockId}`);
  };

  const handleLogin = (request: LoginRequest) => {
    login(request).then((success) => {
      if (!success) {
        setMessage("로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.");
        return;
      }
      setMessage("");
      setAuthStatus("authenticated");
      refreshDashboard();
      navigate("/");
    });
  };

  const handleSignup = (request: SignupRequest) => {
    signup(request).then((success) => {
      if (!success) {
        setMessage("회원가입에 실패했습니다. 입력값을 확인해주세요.");
        return;
      }
      setMessage("회원가입이 완료되었습니다. 로그인 후 자산 정보를 확인해주세요.");
      navigate("/login");
    });
  };

  const handleBuy = async () => {
    if (!selectedStock) {
      setMessage("종목 정보를 불러온 뒤 거래할 수 있습니다.");
      return;
    }

    if (quantity < 1) {
      setMessage("거래 수량은 1 이상이어야 합니다.");
      return;
    }

    if (tradeAmount > cash) {
      setMessage("보유 현금이 부족합니다.");
      return;
    }

    const trade = await buyStock({ stockId: selectedStock.id, quantity });
    if (!trade) {
      setMessage("매수 요청에 실패했습니다. 로그인 상태와 백엔드 API를 확인해주세요.");
      return;
    }

    await refreshDashboard();
    setMessage(`${selectedStock.code} ${quantity}주 매수가 완료되었습니다.`);
  };

  const handleSell = async () => {
    if (!selectedStock) {
      setMessage("종목 정보를 불러온 뒤 거래할 수 있습니다.");
      return;
    }

    if (!selectedHolding || selectedHolding.quantity < quantity) {
      setMessage("보유 주식 수량이 부족합니다.");
      return;
    }

    const trade = await sellStock({ stockId: selectedStock.id, quantity });
    if (!trade) {
      setMessage("매도 요청에 실패했습니다. 로그인 상태와 백엔드 API를 확인해주세요.");
      return;
    }

    await refreshDashboard();
    setMessage(`${selectedStock.code} ${quantity}주 매도가 완료되었습니다.`);
  };

  const handleLogout = async () => {
    await logout();
    setAuthStatus("guest");
    setCash(0);
    setHoldings([]);
    setTrades([]);
    setRankingData([]);
    setMessage("");
    navigate("/login");
  };

  if (authStatus === "checking") {
    return <LoadingScreen />;
  }

  if (authStatus === "guest" && !isAuthPage) {
    return <Navigate to="/login" replace />;
  }

  if (authStatus === "authenticated" && isAuthPage) {
    return <Navigate to="/" replace />;
  }

  if (location.pathname === "/login") {
    return (
      <LoginPage
        message={message}
        onLogin={handleLogin}
        onGoSignup={() => {
          setMessage("");
          navigate("/signup");
        }}
      />
    );
  }

  if (location.pathname === "/signup") {
    return (
      <SignupPage
        message={message}
        onSignup={handleSignup}
        onGoLogin={() => {
          setMessage("");
          navigate("/login");
        }}
      />
    );
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
            <p className="eyebrow">REST API Connected UI</p>
            <h1>{getPageTitle(page)}</h1>
          </div>
          <div className="top-actions">
            <input placeholder="종목 검색" />
            <button
              onClick={handleLogout}
            >
              로그아웃
            </button>
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
              selectedStock ? (
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
              ) : (
                <EmptyState message="종목 데이터를 불러오지 못했습니다. 백엔드 /api/stocks 응답을 확인해주세요." />
              )
            }
          />
          <Route path="/portfolio" element={<PortfolioPage portfolio={portfolio} onOpenStock={goToStock} />} />
          <Route path="/trades" element={<TradesPage trades={trades} stocks={stocks} />} />
          <Route path="/ranking" element={<RankingPage rankings={rankings} currentNickname={currentNickname} />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </section>
    </main>
  );
}

function LoadingScreen() {
  return (
    <main className="auth-shell">
      <section className="auth-panel">
        <p className="eyebrow">StockSim</p>
        <h2>인증 확인 중</h2>
        <p>잠시만 기다려주세요.</p>
      </section>
    </main>
  );
}

function toHoldings(portfolio: PortfolioApiResponse, stocks: Stock[]): Holding[] {
  return portfolio.stocks.map((item, index) => {
    const stock = stocks.find((candidate) => candidate.code === item.code);

    return {
      stockId: stock?.id ?? index + 1,
      quantity: Number(item.quantity),
      averageBuyPrice: Number(item.averageBuyPrice)
    };
  });
}

function EmptyState({ message }: { message: string }) {
  return (
    <section className="content-grid">
      <article className="panel">
        <h2>데이터 없음</h2>
        <p>{message}</p>
      </article>
    </section>
  );
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
