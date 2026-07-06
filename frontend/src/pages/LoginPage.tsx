import { useState, type ReactNode } from "react";
import type { LoginRequest } from "../types/domain";

type LoginPageProps = {
  onLogin: (request: LoginRequest) => void;
  onGoSignup: () => void;
};

export function LoginPage({ onLogin, onGoSignup }: LoginPageProps) {
  const [email, setEmail] = useState("user@example.com");
  const [password, setPassword] = useState("password123");

  return (
    <AuthLayout title="로그인" description="더미 계정으로 MVP 화면을 확인하세요.">
      <label>
        이메일
        <input value={email} type="email" onChange={(event) => setEmail(event.target.value)} />
      </label>
      <label>
        비밀번호
        <input value={password} type="password" onChange={(event) => setPassword(event.target.value)} />
      </label>
      <button className="primary-button" onClick={() => onLogin({ email, password })}>
        로그인
      </button>
      <button className="ghost-button" onClick={onGoSignup}>
        회원가입으로 이동
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
