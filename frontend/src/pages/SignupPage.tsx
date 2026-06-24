import type { ReactNode } from "react";

type SignupPageProps = {
  onSignup: () => void;
  onGoLogin: () => void;
};

export function SignupPage({ onSignup, onGoLogin }: SignupPageProps) {
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
  children: ReactNode;
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
