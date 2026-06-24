import type { PortfolioSummary, Stock } from "../types/domain";
import { formatWon } from "../utils/format";

type HomePageProps = {
  cash: number;
  portfolio: PortfolioSummary;
  stocks: Stock[];
  onOpenStock: (stockId: number) => void;
};

export function HomePage({ cash, portfolio, stocks, onOpenStock }: HomePageProps) {
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
