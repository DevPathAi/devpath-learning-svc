트랙: FRONTEND_REACT (프론트엔드 — React)

이 트랙의 문항은 아래 실제 개념을 구체적으로 묻는다. 각 문항은 하나의 개념을 명확히 겨냥한다.

## 핵심 개념 목록

- **컴포넌트·JSX**: 함수형 컴포넌트, props, 조건부/리스트 렌더링, `key` 규칙, 합성(composition) vs 상속.
- **훅(Hooks)**: `useState` 배치 업데이트, `useEffect` 의존성 배열·클린업·실행 시점, `useMemo`/`useCallback` 메모이제이션, `useRef`, `useContext`, 커스텀 훅, **훅 규칙(호출 순서·최상위)**.
- **상태 관리**: 지역 상태 vs 전역 상태, 상태 끌어올리기, Context API 리렌더 범위, Redux/Zustand 등 외부 스토어 개념, 불변성.
- **렌더링·성능**: 리렌더 원인, `React.memo`, 리스트 가상화, 참조 동일성, key로 인한 재마운트, 리컨실리에이션.
- **라우팅**: 클라이언트 라우팅, 중첩 라우트, 동적 파라미터, 코드 스플리팅/lazy.
- **비동기 데이터**: fetch/effect에서의 경쟁 상태(race), 로딩/에러 상태, 취소(AbortController), 캐싱(react-query 개념), Suspense.
- **폼·접근성**: 제어 vs 비제어 컴포넌트, 검증, `label`/aria 속성, 키보드 접근성, 시맨틱 마크업.
- **테스트**: React Testing Library의 사용자 관점 쿼리, act 경고, 비동기 단언.

## CODE_READING 지침

짧은 JSX/JS/TS 스니펫(5~15줄)을 `content`에 `\n`으로 넣고, `useEffect` 의존성 누락으로 인한 stale closure·무한 루프, 배치 업데이트, key 오용으로 인한 상태 유실, 잘못된 훅 호출 등 **위 개념의 미묘한 동작·버그**를 읽어내게 한다.
