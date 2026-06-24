import type { Stock, Trade } from "../types/domain";
import { formatWon } from "../utils/format";

type TradesPageProps = {
  trades: Trade[];
  stocks: Stock[];
};

export function TradesPage({ trades, stocks }: TradesPageProps) {
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
              const stock = stocks.find((item) => item.id === trade.stockId);
              return (
                <tr key={trade.id}>
                  <td className={trade.type === "BUY" ? "up" : "down"}>{trade.type}</td>
                  <td>
                    <strong>{stock?.code ?? `#${trade.stockId}`}</strong>
                    <small>{stock?.name ?? "알 수 없는 종목"}</small>
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
