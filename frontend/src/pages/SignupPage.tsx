import { useState, type ReactNode } from "react";
import type { SignupRequest } from "../types/domain";

type SignupPageProps = {
  onSignup: (request: SignupRequest) => void;
  onGoLogin: () => void;
};

export function SignupPage({ onSignup, onGoLogin }: SignupPageProps) {
  const [email, setEmail] = useState("new-user@example.com");
  const [nickname, setNickname] = useState("stockUser");
  const [password, setPassword] = useState("password123");
  const [passwordConfirm, setPasswordConfirm] = useState("password123");

  return (
    <AuthLayout title="회원가입" description="가입 즉시 가상 현금 10,000,000원이 지급됩니다.">
      <label>
        이메일
        <input value={email} type="email" onChange={(event) => setEmail(event.target.value)} />
      </label>
      <label>
        닉네임
        <input value={nickname} onChange={(event) => setNickname(event.target.value)} />
      </label>
      <label>
        비밀번호
        <input value={password} type="password" onChange={(event) => setPassword(event.target.value)} />
      </label>
      <label>
        비밀번호 확인
        <input
          value={passwordConfirm}
          type="password"
          onChange={(event) => setPasswordConfirm(event.target.value)}
        />
      </label>
      <button
        className="primary-button"
        onClick={() => {
          if (password === passwordConfirm) {
            onSignup({ email, nickname, password });
          }
        }}
      >
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
