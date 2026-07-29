트랙: DEVOPS (데브옵스 — 컨테이너·오케스트레이션·CI/CD·운영)

이 트랙의 문항은 아래 실제 개념을 구체적으로 묻는다. 각 문항은 하나의 개념을 명확히 겨냥한다.

## 핵심 개념 목록

- **Docker**: 이미지 레이어·캐시, 멀티스테이지 빌드, `CMD` vs `ENTRYPOINT`, 볼륨/바인드 마운트, 네트워크, 이미지 크기 최적화, `.dockerignore`.
- **Kubernetes**: Pod/Deployment/ReplicaSet/Service, **liveness vs readiness vs startupProbe**, 롤링 업데이트·롤백, 리소스 requests/limits, ConfigMap/Secret, label/selector, Ingress, HPA.
- **CI/CD**: 파이프라인 단계, 아티팩트, 캐싱, 배포 전략(blue-green·canary·rolling), GitOps(ArgoCD) 개념, 롤백.
- **관측성(Observability)**: 로그/메트릭/트레이스 3요소, Prometheus 메트릭 타입, 대시보드, 알림(alert), SLO/SLI.
- **비밀·보안**: 시크릿 관리, 최소 권한, 이미지 스캔, 네트워크 정책.
- **네트워킹**: DNS, 로드 밸런싱, 리버스 프록시, TLS 종료, 포트/서비스 디스커버리.
- **신뢰성·장애 대응**: 헬스체크, 재시도/서킷 브레이커, 그레이스풀 셧다운, 무중단 배포, 장애 회고(postmortem), 멱등성.
- **클라우드 기초**: IaC 개념, 오토스케일링, 가용 영역(AZ), 관리형 서비스 vs self-host.

## CODE_READING 지침

짧은 YAML/Dockerfile/셸 스니펫(5~15줄)을 `content`에 `\n`으로 넣고, probe 설정 오류로 인한 CrashLoop·리소스 limit 누락·멀티스테이지 캐시 무효화·`ENTRYPOINT`/`CMD` 오용·셀렉터 불일치 등 **위 개념의 미묘한 설정 문제**를 읽어내게 한다.
