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
          <span>GET /api/rankings</span>
        </div>
        <strong>Top {rankings.length}</strong>
      </div>
      <div className="ranking-list">
        {rankings.map((user) => (
          <div className={user.nickname === currentNickname ? "rank-row current" : "rank-row"} key={user.userId}>
            <span>#{user.rank}</span>
            <strong>{user.nickname}</strong>
            <b>{formatWon(user.totalAsset)}</b>
          </div>
        ))}
        {rankings.length === 0 && (
          <p className="empty-text">랭킹 데이터가 없습니다. 백엔드 랭킹 집계 또는 /api/rankings 응답을 확인해주세요.</p>
        )}
      </div>
    </article>
  );
}
