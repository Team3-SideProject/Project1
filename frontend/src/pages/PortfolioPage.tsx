import type { PortfolioSummary } from "../types/domain";
import { formatWon } from "../utils/format";

type PortfolioPageProps = {
  portfolio: PortfolioSummary;
  onOpenStock: (stockId: number) => void;
};

export function PortfolioPage({ portfolio, onOpenStock }: PortfolioPageProps) {
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

function SummaryGrid({
  cash,
  portfolio
}: {
  cash: number;
  portfolio: PortfolioSummary;
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
