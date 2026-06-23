import { useMemo, useState } from "react";
import { matchPath, Navigate, Route, Routes, useLocation, useNavigate } from "react-router-dom";
import { initialHoldings, initialStocks, initialTrades, rankingUsers } from "./mocks";
import type { Holding, Page, Stock, Trade } from "./types/domain";
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
  const [message, setMessage] = useState("");

  const selectedStock = initialStocks.find((stock) => stock.id === selectedStockId) ?? initialStocks[0];

  const portfolio = useMemo(() => {
    const rows = holdings.map((holding) => {
      const stock = initialStocks.find((item) => item.id === holding.stockId)!;
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
  }, [cash, holdings]);

  const userRanking = {
    nickname: "stockUser",
    cash: portfolio.cash,
    stockValue: portfolio.stockValue
  };

  const rankings = [...rankingUsers, userRanking]
    .map((user) => ({ ...user, totalAsset: user.cash + user.stockValue }))
    .sort((a, b) => b.totalAsset - a.totalAsset)
    .map((user, index) => ({ ...user, rank: index + 1 }));

  const selectedHolding = holdings.find((holding) => holding.stockId === selectedStock.id);
  const tradeAmount = selectedStock.currentPrice * quantity;

  const goToStock = (stockId: number) => {
    setQuantity(1);
    setMessage("");
    navigate(`/stocks/${stockId}`);
  };

  const handleLogin = () => {
    setMessage("");
    navigate("/");
  };

  const handleSignup = () => {
    setMessage("회원가입이 완료되었습니다. 초기 자산 10,000,000원이 지급되었습니다.");
    navigate("/");
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

    setCash((currentCash) => currentCash - tradeAmount);
    setHoldings((current) => {
      const owned = current.find((holding) => holding.stockId === selectedStock.id);
      if (!owned) {
        return [
          ...current,
          {
            stockId: selectedStock.id,
            quantity,
            averageBuyPrice: selectedStock.currentPrice
          }
        ];
      }

      return current.map((holding) => {
        if (holding.stockId !== selectedStock.id) return holding;
        const nextQuantity = holding.quantity + quantity;
        const nextAveragePrice =
          (holding.averageBuyPrice * holding.quantity + selectedStock.currentPrice * quantity) / nextQuantity;

        return {
          ...holding,
          quantity: nextQuantity,
          averageBuyPrice: nextAveragePrice
        };
      });
    });
    setTrades((current) => [
      {
        id: current.length + 1,
        type: "BUY",
        stockId: selectedStock.id,
        quantity,
        price: selectedStock.currentPrice,
        createdAt: "2026-06-23 15:00"
      },
      ...current
    ]);
    setMessage(`${selectedStock.code} ${quantity}주 매수가 완료되었습니다.`);
  };

  const handleSell = () => {
    if (!selectedHolding || selectedHolding.quantity < quantity) {
      setMessage("보유 주식 수량이 부족합니다.");
      return;
    }

    setCash((currentCash) => currentCash + tradeAmount);
    setHoldings((current) =>
      current
        .map((holding) =>
          holding.stockId === selectedStock.id
            ? { ...holding, quantity: holding.quantity - quantity }
            : holding
        )
        .filter((holding) => holding.quantity > 0)
    );
    setTrades((current) => [
      {
        id: current.length + 1,
        type: "SELL",
        stockId: selectedStock.id,
        quantity,
        price: selectedStock.currentPrice,
        createdAt: "2026-06-23 15:00"
      },
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
                stocks={initialStocks}
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
          <Route path="/trades" element={<TradesPage trades={trades} />} />
          <Route path="/ranking" element={<RankingPage rankings={rankings} currentNickname="stockUser" />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </section>
    </main>
  );
}

function LoginPage({
  onLogin,
  onGoSignup
}: {
  onLogin: () => void;
  onGoSignup: () => void;
}) {
  return (
    <AuthLayout title="로그인" description="더미 계정으로 MVP 화면을 확인하세요.">
      <label>
        이메일
        <input defaultValue="user@example.com" type="email" />
      </label>
      <label>
        비밀번호
        <input defaultValue="password123" type="password" />
      </label>
      <button className="primary-button" onClick={onLogin}>
        로그인
      </button>
      <button className="ghost-button" onClick={onGoSignup}>
        회원가입으로 이동
      </button>
    </AuthLayout>
  );
}

function SignupPage({
  onSignup,
  onGoLogin
}: {
  onSignup: () => void;
  onGoLogin: () => void;
}) {
  return (
    <AuthLayout title="회원가입" description="가입 즉시 가상 현금 10,000,000원이 지급됩니다.">
      <label>
        이메일
        <input defaultValue="new-user@example.com" type="email" />
      </label>
      <label>
        닉네임
        <input defaultValue="stockUser" />
      </label>
      <label>
        비밀번호
        <input defaultValue="password123" type="password" />
      </label>
      <label>
        비밀번호 확인
        <input defaultValue="password123" type="password" />
      </label>
      <button className="primary-button" onClick={onSignup}>
        회원가입
      </button>
      <button className="ghost-button" onClick={onGoLogin}>
        로그인으로 이동
      </button>
    </AuthLayout>
  );
}

function AuthLayout({
  title,
  description,
  children
}: {
  title: string;
  description: string;
  children: React.ReactNode;
}) {
  return (
    <main className="auth-shell">
      <section className="auth-brand">
        <span className="logo-mark">S</span>
        <h1>StockSim</h1>
        <p>더미 주식 데이터와 가상 자산으로 매수/매도, 포트폴리오, 랭킹 흐름을 검증합니다.</p>
        <div className="market-strip">
          <span>회원가입</span>
          <span>주식 조회</span>
          <span>매수/매도</span>
          <span>랭킹</span>
        </div>
        <div className="mini-bars">
          {[42, 48, 44, 52, 58, 62, 57, 69, 74, 82].map((value, index) => (
            <i key={index} style={{ height: `${value}%` }} />
          ))}
        </div>
      </section>

      <section className="auth-panel">
        <p className="eyebrow">Team3 Side Project</p>
        <h2>{title}</h2>
        <p>{description}</p>
        {children}
      </section>
    </main>
  );
}

function HomePage({
  cash,
  portfolio,
  stocks,
  onOpenStock
}: {
  cash: number;
  portfolio: ReturnType<typeof usePortfolioShape>;
  stocks: Stock[];
  onOpenStock: (stockId: number) => void;
}) {
  return (
    <section className="content-grid home-grid">
      <article className="panel span-2">
        <div className="panel-header">
          <div>
            <h2>주식 목록</h2>
            <span>GET /api/stocks</span>
          </div>
          <strong>{stocks.length}개 종목</strong>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>종목</th>
                <th>설명</th>
                <th>현재가</th>
                <th>등락률</th>
                <th>액션</th>
              </tr>
            </thead>
            <tbody>
              {stocks.map((stock) => (
                <tr key={stock.id}>
                  <td>
                    <strong>{stock.code}</strong>
                    <small>{stock.name}</small>
                  </td>
                  <td>{stock.description}</td>
                  <td>{formatWon(stock.currentPrice)}</td>
                  <td className={stock.changeRate >= 0 ? "up" : "down"}>
                    {stock.changeRate >= 0 ? "+" : ""}
                    {stock.changeRate}%
                  </td>
                  <td>
                    <button className="table-button" onClick={() => onOpenStock(stock.id)}>
                      상세
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </article>

      <article className="panel">
        <div className="panel-header">
          <div>
            <h2>오늘의 체크</h2>
            <span>MVP + 자산 요약</span>
          </div>
          <strong>{formatWon(portfolio.totalAsset)}</strong>
        </div>
        <div className="compact-assets">
          <div>
            <span>보유 현금</span>
            <strong>{formatWon(cash)}</strong>
          </div>
          <div>
            <span>평가 금액</span>
            <strong>{formatWon(portfolio.stockValue)}</strong>
          </div>
          <div>
            <span>총 자산</span>
            <strong>{formatWon(portfolio.totalAsset)}</strong>
          </div>
        </div>
        <ul className="check-list">
          <li>실제 주식 API 미사용</li>
          <li>시장가 매수/매도만 지원</li>
          <li>랭킹은 총 자산 기준</li>
          <li>거래 성공 시 내역 저장</li>
        </ul>
      </article>
    </section>
  );
}

function StockDetailPage({
  stock,
  holdingQuantity,
  quantity,
  tradeAmount,
  onQuantityChange,
  onBuy,
  onSell,
  onBack
}: {
  stock: Stock;
  holdingQuantity: number;
  quantity: number;
  tradeAmount: number;
  onQuantityChange: (quantity: number) => void;
  onBuy: () => void;
  onSell: () => void;
  onBack: () => void;
}) {
  return (
    <section className="content-grid detail-grid">
      <article className="panel chart-panel span-2">
        <div className="panel-header">
          <div>
            <button className="text-button" onClick={onBack}>
              ← 주식 목록
            </button>
            <h2>
              {stock.name} <span>{stock.code}</span>
            </h2>
            <p>{stock.description}</p>
          </div>
          <strong>{formatWon(stock.currentPrice)}</strong>
        </div>
        <div className="chart">
          {stock.history.map((value, index) => (
            <i key={index} className={index % 4 === 1 ? "warning" : ""} style={{ height: `${value}%` }} />
          ))}
        </div>
      </article>

      <article className="panel trade-panel">
        <div className="panel-header">
          <h2>시장가 주문</h2>
          <span>POST /api/trades</span>
        </div>
        <label>
          거래 수량
          <input
            min={1}
            type="number"
            value={quantity}
            onChange={(event) => onQuantityChange(Math.max(1, Number(event.target.value)))}
          />
        </label>
        <dl>
          <div>
            <dt>예상 거래 금액</dt>
            <dd>{formatWon(tradeAmount)}</dd>
          </div>
          <div>
            <dt>보유 수량</dt>
            <dd>{holdingQuantity}주</dd>
          </div>
        </dl>
        <button className="buy-button" onClick={onBuy}>
          매수
        </button>
        <button className="sell-button" onClick={onSell}>
          매도
        </button>
      </article>
    </section>
  );
}

function PortfolioPage({
  portfolio,
  onOpenStock
}: {
  portfolio: ReturnType<typeof usePortfolioShape>;
  onOpenStock: (stockId: number) => void;
}) {
  return (
    <>
      <SummaryGrid cash={portfolio.cash} portfolio={portfolio} />
      <article className="panel">
        <div className="panel-header">
          <div>
            <h2>보유 종목</h2>
            <span>GET /api/portfolio/me</span>
          </div>
          <strong>{portfolio.rows.length}개</strong>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>종목</th>
                <th>수량</th>
                <th>평균 매수가</th>
                <th>현재가</th>
                <th>평가 금액</th>
                <th>손익</th>
              </tr>
            </thead>
            <tbody>
              {portfolio.rows.map((row) => (
                <tr key={row.stock.id} onClick={() => onOpenStock(row.stock.id)}>
                  <td>
                    <strong>{row.stock.code}</strong>
                    <small>{row.stock.name}</small>
                  </td>
                  <td>{row.quantity}주</td>
                  <td>{formatWon(row.averageBuyPrice)}</td>
                  <td>{formatWon(row.stock.currentPrice)}</td>
                  <td>{formatWon(row.valuation)}</td>
                  <td className={row.profitLoss >= 0 ? "up" : "down"}>
                    {row.profitLoss >= 0 ? "+" : ""}
                    {formatWon(row.profitLoss)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </article>
    </>
  );
}

function TradesPage({ trades }: { trades: Trade[] }) {
  return (
    <article className="panel">
      <div className="panel-header">
        <div>
          <h2>거래 내역</h2>
          <span>GET /api/trades/me</span>
        </div>
        <strong>최신순</strong>
      </div>
      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>유형</th>
              <th>종목</th>
              <th>수량</th>
              <th>가격</th>
              <th>총 금액</th>
              <th>거래 일시</th>
            </tr>
          </thead>
          <tbody>
            {trades.map((trade) => {
              const stock = initialStocks.find((item) => item.id === trade.stockId)!;
              return (
                <tr key={trade.id}>
                  <td className={trade.type === "BUY" ? "up" : "down"}>{trade.type}</td>
                  <td>
                    <strong>{stock.code}</strong>
                    <small>{stock.name}</small>
                  </td>
                  <td>{trade.quantity}주</td>
                  <td>{formatWon(trade.price)}</td>
                  <td>{formatWon(trade.price * trade.quantity)}</td>
                  <td>{trade.createdAt}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </article>
  );
}

function RankingPage({
  rankings,
  currentNickname
}: {
  rankings: Array<{ rank: number; nickname: string; cash: number; stockValue: number; totalAsset: number }>;
  currentNickname: string;
}) {
  return (
    <article className="panel">
      <div className="panel-header">
        <div>
          <h2>자산 랭킹</h2>
          <span>GET /api/rankings/assets</span>
        </div>
        <strong>Top {rankings.length}</strong>
      </div>
      <div className="ranking-list">
        {rankings.map((user) => (
          <div className={user.nickname === currentNickname ? "rank-row current" : "rank-row"} key={user.nickname}>
            <span>#{user.rank}</span>
            <strong>{user.nickname}</strong>
            <em>현금 {formatWon(user.cash)}</em>
            <em>주식 {formatWon(user.stockValue)}</em>
            <b>{formatWon(user.totalAsset)}</b>
          </div>
        ))}
      </div>
    </article>
  );
}

function SummaryGrid({
  cash,
  portfolio
}: {
  cash: number;
  portfolio: ReturnType<typeof usePortfolioShape>;
}) {
  return (
    <section className="summary-grid">
      <article>
        <span>보유 현금</span>
        <strong>{formatWon(cash)}</strong>
        <em>초기 자산 10,000,000원</em>
      </article>
      <article>
        <span>주식 평가 금액</span>
        <strong>{formatWon(portfolio.stockValue)}</strong>
        <em>{portfolio.rows.length}개 종목 보유</em>
      </article>
      <article>
        <span>총 자산</span>
        <strong>{formatWon(portfolio.totalAsset)}</strong>
        <em className={portfolio.profitLoss >= 0 ? "up" : "down"}>
          손익 {portfolio.profitLoss >= 0 ? "+" : ""}
          {formatWon(portfolio.profitLoss)}
        </em>
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

function usePortfolioShape() {
  return {
    rows: [] as Array<{
      stock: Stock;
      stockId: number;
      quantity: number;
      averageBuyPrice: number;
      valuation: number;
      profitLoss: number;
    }>,
    cash: 0,
    stockValue: 0,
    totalAsset: 0,
    profitLoss: 0
  };
}

export default App;
