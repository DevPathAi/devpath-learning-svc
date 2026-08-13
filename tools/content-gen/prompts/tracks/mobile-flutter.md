트랙: MOBILE_FLUTTER (모바일 — Flutter/Dart)

이 트랙의 문항은 아래 실제 개념을 구체적으로 묻는다. 각 문항은 하나의 개념을 명확히 겨냥한다.

## 핵심 개념 목록

- **위젯·렌더링**: StatelessWidget vs StatefulWidget, 위젯/엘리먼트/렌더 트리, `build` 재호출 시점, `const` 위젯 최적화, `BuildContext`.
- **레이아웃**: Row/Column/Flex, `Expanded`/`Flexible`, 제약(constraints) 전파("constraints go down, sizes go up"), overflow, `Stack`/`Positioned`.
- **상태 관리**: `setState` 범위, `InheritedWidget`, Provider/Riverpod/Bloc 개념, 상태 끌어올리기, rebuild 최소화.
- **생명주기·비동기**: `initState`/`dispose`, `Future`/`async`/`await`, `FutureBuilder`/`StreamBuilder`, 이벤트 루프·isolate, `mounted` 체크.
- **네비게이션**: `Navigator` push/pop, named routes, 인자 전달, 결과 반환, 딥링크 개념.
- **플랫폼 통합**: platform channel, 권한, 플랫폼 분기(`Platform.isAndroid`), 네이티브 플러그인.
- **성능**: 불필요한 rebuild, `ListView.builder` 지연 생성, `RepaintBoundary`, 이미지 캐싱, jank 원인.
- **접근성·테스트**: Semantics, 위젯 테스트(`testWidgets`, `pumpAndSettle`), 골든 테스트 개념.

## CODE_READING 지침

짧은 Dart/Flutter 스니펫(5~15줄)을 `content`에 `\n`으로 넣고, `setState` 오용·`build` 안 부수효과·제약 위반 overflow·`dispose` 누락으로 인한 누수·`async` gap 후 `mounted` 미확인 등 **위 개념의 미묘한 동작·버그**를 읽어내게 한다.
