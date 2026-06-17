import { useMemo, useState } from "react";

type Stock = {
  code: string;
  name: string;
  price: number;
  change: number;
  volume: string;
  history: number[];
};

type Holding = {
  code: string;
  quantity: number;
  averagePrice: number;
};

type Trade = {
  type: "BUY" | "SELL";
  code: string;
  quantity: number;
  price: number;
  time: string;
};

const initialStocks: Stock[] = [
  { code: "ALP", name: "Alpha Tech", price: 51200, change: 2.43, volume: "1.2M", history: [36, 42, 38, 51, 48, 64, 58, 74, 80, 92] },
  { code: "BET", name: "Beta Bio", price: 11850, change: -1.18, volume: "892K", history: [64, 55, 61, 50, 45, 53, 40, 48, 39, 35] },
  { code: "CRN", name: "Crown Energy", price: 30300, change: 0.72, volume: "530K", history: [40, 43, 41, 47, 52, 56, 50, 58, 61, 65] },
  { code: "DLT", name: "Delta Mobility", price: 24600, change: 3.91, volume: "1.8M", history: [28, 34, 40, 44, 56, 62, 68, 73, 78, 84] },
  { code: "ECH", name: "Echo Games", price: 8250, change: -2.36, volume: "774K", history: [72, 68, 60, 57, 62, 54, 49, 44, 47, 41] },
  { code: "FRN", name: "Front Finance", price: 70100, change: 1.04, volume: "390K", history: [50, 52, 51, 55, 57, 56, 60, 63, 62, 66] },
  { code: "GLD", name: "Gold Retail", price: 18200, change: 0.31, volume: "610K", history: [44, 46, 45, 47, 48, 51, 50, 53, 55, 57] },
  { code: "HYP", name: "Hyper AI", price: 96400, change: 6.85, volume: "2.4M", history: [30, 35, 49, 45, 62, 70, 66, 83, 88, 96] },
  { code: "IVY", name: "Ivy Foods", price: 15100, change: -0.64, volume: "450K", history: [55, 56, 53, 54, 51, 52, 50, 49, 48, 47] }
];

const initialHoldings: Holding[] = [
  { code: "ALP", quantity: 18, averagePrice: 48200 },
  { code: "HYP", quantity: 4, averagePrice: 90200 },
  { code: "GLD", quantity: 35, averagePrice: 17800 }
];

const rankings = [
  { rank: 1, name: "CryptoMidas", asset: 14892500, profit: 48.9 },
  { rank: 2, name: "ShadowTrader", asset: 13780120, profit: 37.8 },
  { rank: 3, name: "LunaCapital", asset: 12204000, profit: 22.0 },
  { rank: 4, name: "Your Portfolio", asset: 11132400, profit: 11.3 },
  { rank: 5, name: "GhostOfTheFed", asset: 10340210, profit: 3.4 }
];

const formatWon = (value: number) =>
  new Intl.NumberFormat("ko-KR", {
    style: "currency",
    currency: "KRW",
    maximumFractionDigits: 0
  }).format(value);

const formatCompact = (value: number) => value.toLocaleString("ko-KR");

function App() {
  const [screen, setScreen] = useState<"login" | "dashboard">("login");
  const [selectedCode, setSelectedCode] = useState("HYP");
  const [quantity, setQuantity] = useState(1);
  const [cash, setCash] = useState(10_000_000);
  const [holdings, setHoldings] = useState(initialHoldings);
  const [trades, setTrades] = useState<Trade[]>([
    { type: "BUY", code: "HYP", quantity: 2, price: 94600, time: "14:20" },
    { type: "SELL", code: "ALP", quantity: 3, price: 51200, time: "13:40" },
    { type: "BUY", code: "GLD", quantity: 10, price: 18100, time: "12:30" }
  ]);

  const selectedStock = initialStocks.find((stock) => stock.code === selectedCode) ?? initialStocks[0];

  const portfolio = useMemo(() => {
    const rows = holdings.map((holding) => {
      const stock = initialStocks.find((item) => item.code === holding.code)!;
      const valuation = stock.price * holding.quantity;
      const principal = holding.averagePrice * holding.quantity;
      return { ...holding, stock, valuation, profit: valuation - principal };
    });

    const stockValue = rows.reduce((sum, row) => sum + row.valuation, 0);
    return {
      rows,
      stockValue,
      totalAsset: cash + stockValue,
      profit: rows.reduce((sum, row) => sum + row.profit, 0)
    };
  }, [cash, holdings]);

  const tradeAmount = selectedStock.price * quantity;

  const handleBuy = () => {
    if (tradeAmount > cash) return;
    setCash((currentCash) => currentCash - tradeAmount);
    setHoldings((current) => {
      const existing = current.find((holding) => holding.code === selectedStock.code);
      if (!existing) {
        return [...current, { code: selectedStock.code, quantity, averagePrice: selectedStock.price }];
      }

      return current.map((holding) => {
        if (holding.code !== selectedStock.code) return holding;
        const totalQuantity = holding.quantity + quantity;
        const averagePrice =
          (holding.averagePrice * holding.quantity + selectedStock.price * quantity) / totalQuantity;
        return { ...holding, quantity: totalQuantity, averagePrice };
      });
    });
    setTrades((current) => [
      { type: "BUY", code: selectedStock.code, quantity, price: selectedStock.price, time: "Now" },
      ...current
    ]);
  };

  const handleSell = () => {
    const owned = holdings.find((holding) => holding.code === selectedStock.code);
    if (!owned || owned.quantity < quantity) return;

    setCash((currentCash) => currentCash + tradeAmount);
    setHoldings((current) =>
      current
        .map((holding) =>
          holding.code === selectedStock.code ? { ...holding, quantity: holding.quantity - quantity } : holding
        )
        .filter((holding) => holding.quantity > 0)
    );
    setTrades((current) => [
      { type: "SELL", code: selectedStock.code, quantity, price: selectedStock.price, time: "Now" },
      ...current
    ]);
  };

  if (screen === "login") {
    return (
      <main className="auth-shell">
        <section className="auth-brand">
          <div className="logo-mark">S</div>
          <h1>StockSim</h1>
          <p>가상 자산으로 주식 매매를 연습하고, 6주 MVP 안에서 투자 게임의 핵심 흐름을 검증합니다.</p>
          <div className="market-strip">
            <span>ALP +2.43%</span>
            <span>HYP +6.85%</span>
            <span>BET -1.18%</span>
          </div>
          <div className="mini-bars">
            {selectedStock.history.map((value, index) => (
              <i key={index} style={{ height: `${value}%` }} />
            ))}
          </div>
        </section>

        <section className="auth-panel">
          <div>
            <p className="eyebrow">Team3 Side Project</p>
            <h2>로그인</h2>
            <p>더미 계정으로 바로 트레이딩 화면을 확인하세요.</p>
          </div>
          <label>
            이메일
            <input defaultValue="trader@stocksim.io" type="email" />
          </label>
          <label>
            비밀번호
            <input defaultValue="password123" type="password" />
          </label>
          <button className="primary-button" onClick={() => setScreen("dashboard")}>
            대시보드 입장
          </button>
          <button className="ghost-button" onClick={() => setScreen("dashboard")}>
            간편 회원가입
          </button>
        </section>
      </main>
    );
  }

  return (
    <main className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <div className="logo-mark">S</div>
          <strong>StockSim</strong>
        </div>
        <nav>
          <button className="active">대시보드</button>
          <button>포트폴리오</button>
          <button>거래 내역</button>
          <button>랭킹</button>
        </nav>
        <div className="account-card">
          <span>총 자산</span>
          <strong>{formatWon(portfolio.totalAsset)}</strong>
          <em>{portfolio.profit >= 0 ? "+" : ""}{formatWon(portfolio.profit)}</em>
        </div>
      </aside>

      <section className="workspace">
        <header className="topbar">
          <div>
            <p className="eyebrow">Market Simulator</p>
            <h1>Trading Dashboard</h1>
          </div>
          <div className="top-actions">
            <input placeholder="종목 검색" />
            <button onClick={() => setScreen("login")}>로그아웃</button>
          </div>
        </header>

        <section className="summary-grid">
          <article>
            <span>보유 현금</span>
            <strong>{formatWon(cash)}</strong>
            <em>초기 자산 10,000,000원</em>
          </article>
          <article>
            <span>주식 평가금</span>
            <strong>{formatWon(portfolio.stockValue)}</strong>
            <em>{portfolio.rows.length}개 종목 보유</em>
          </article>
          <article className="positive">
            <span>평가 손익</span>
            <strong>{portfolio.profit >= 0 ? "+" : ""}{formatWon(portfolio.profit)}</strong>
            <em>더미 가격 기준</em>
          </article>
        </section>

        <section className="main-grid">
          <article className="market-list panel">
            <div className="panel-header">
              <h2>주식 목록</h2>
              <span>9 stocks</span>
            </div>
            {initialStocks.map((stock) => (
              <button
                key={stock.code}
                className={stock.code === selectedCode ? "stock-row selected" : "stock-row"}
                onClick={() => setSelectedCode(stock.code)}
              >
                <span>
                  <strong>{stock.code}</strong>
                  <small>{stock.name}</small>
                </span>
                <span>
                  <b>{formatWon(stock.price)}</b>
                  <em className={stock.change >= 0 ? "up" : "down"}>
                    {stock.change >= 0 ? "+" : ""}{stock.change}%
                  </em>
                </span>
              </button>
            ))}
          </article>

          <article className="chart-panel panel">
            <div className="panel-header">
              <div>
                <h2>{selectedStock.name}</h2>
                <span>{selectedStock.code} / {selectedStock.volume}</span>
              </div>
              <strong>{formatWon(selectedStock.price)}</strong>
            </div>
            <div className="chart">
              {selectedStock.history.map((value, index) => (
                <i
                  key={index}
                  className={index % 4 === 1 ? "warning" : ""}
                  style={{ height: `${value}%` }}
                />
              ))}
            </div>
          </article>

          <article className="trade-panel panel">
            <div className="panel-header">
              <h2>주문</h2>
              <span>시장가</span>
            </div>
            <div className="stepper">
              <button onClick={() => setQuantity(Math.max(1, quantity - 1))}>-</button>
              <strong>{quantity}</strong>
              <button onClick={() => setQuantity(quantity + 1)}>+</button>
            </div>
            <dl>
              <div>
                <dt>예상 금액</dt>
                <dd>{formatWon(tradeAmount)}</dd>
              </div>
              <div>
                <dt>보유 수량</dt>
                <dd>{holdings.find((holding) => holding.code === selectedCode)?.quantity ?? 0}주</dd>
              </div>
            </dl>
            <button className="buy-button" onClick={handleBuy}>매수</button>
            <button className="sell-button" onClick={handleSell}>매도</button>
          </article>

          <article className="holdings-panel panel">
            <div className="panel-header">
              <h2>포트폴리오</h2>
              <span>{formatWon(portfolio.totalAsset)}</span>
            </div>
            <table>
              <thead>
                <tr>
                  <th>종목</th>
                  <th>수량</th>
                  <th>평균가</th>
                  <th>손익</th>
                </tr>
              </thead>
              <tbody>
                {portfolio.rows.map((row) => (
                  <tr key={row.code}>
                    <td>{row.code}</td>
                    <td>{row.quantity}</td>
                    <td>{formatCompact(Math.round(row.averagePrice))}</td>
                    <td className={row.profit >= 0 ? "up" : "down"}>
                      {row.profit >= 0 ? "+" : ""}{formatCompact(Math.round(row.profit))}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </article>

          <article className="activity-panel panel">
            <div className="panel-header">
              <h2>거래 내역</h2>
              <span>latest</span>
            </div>
            {trades.slice(0, 5).map((trade, index) => (
              <div className="activity" key={`${trade.code}-${trade.time}-${index}`}>
                <b className={trade.type === "BUY" ? "up" : "down"}>{trade.type}</b>
                <span>{trade.code} {trade.quantity}주</span>
                <em>{formatWon(trade.price)} · {trade.time}</em>
              </div>
            ))}
          </article>

          <article className="ranking-panel panel">
            <div className="panel-header">
              <h2>랭킹</h2>
              <span>Top 5</span>
            </div>
            {rankings.map((item) => (
              <div className={item.name === "Your Portfolio" ? "rank-row me" : "rank-row"} key={item.name}>
                <span>#{item.rank}</span>
                <strong>{item.name}</strong>
                <em>+{item.profit}%</em>
                <b>{formatWon(item.asset)}</b>
              </div>
            ))}
          </article>
        </section>
      </section>
    </main>
  );
}

export default App;
