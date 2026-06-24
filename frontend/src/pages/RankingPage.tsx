import type { RankingRow } from "../types/domain";
import { formatWon } from "../utils/format";

type RankingPageProps = {
  rankings: RankingRow[];
  currentNickname: string;
};

export function RankingPage({ rankings, currentNickname }: RankingPageProps) {
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
