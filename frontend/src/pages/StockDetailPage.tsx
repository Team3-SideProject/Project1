import type { Stock } from "../types/domain";
import { formatWon } from "../utils/format";

type StockDetailPageProps = {
  stock: Stock;
  holdingQuantity: number;
  quantity: number;
  tradeAmount: number;
  onQuantityChange: (quantity: number) => void;
  onBuy: () => void;
  onSell: () => void;
  onBack: () => void;
};

export function StockDetailPage({
  stock,
  holdingQuantity,
  quantity,
  tradeAmount,
  onQuantityChange,
  onBuy,
  onSell,
  onBack
}: StockDetailPageProps) {
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
          <span>POST /api/trades/buy · POST /api/trades/sell</span>
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
