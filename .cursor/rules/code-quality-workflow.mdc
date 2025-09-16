---
description: 코드 편집 후 품질 관리 워크플로우
globs: []
alwaysApply: true
---

# 코드 품질 관리 워크플로우

## 🔄 편집 완료 후 필수 워크플로우

모든 Kotlin 코드 편집이 완료되면 **반드시** 다음 순서로 실행:

### 1단계: detekt 실행 (분석 + 자동 formatting)

```bash
./gradlew detekt
```

**중요**: `detektFormat`은 존재하지 않는 task입니다!

### 2단계: 리포트 확인 (필요시)

```bash
# HTML 리포트 확인: app/build/reports/detekt/detekt.html
```

## ⚡ 설정된 최적화

### 자동 수정 (autoCorrect: true)

- 들여쓰기 및 공백 정리
- import 문 정리
- 라인 길이 조정
- 괄호 및 대괄호 정렬

### 예외 처리 완료

- **@Composable/@Preview**: complexity 검사 제외
- **modifier**: unused parameter 제외
- **fromXXX**: return count 제한 제외

## 🛠️ 수동 수정 필요한 이슈들

- 함수/클래스 복잡도 축소
- 매개변수 개수 줄이기 (6개 이하 권장)
- 네이밍 컨벤션 수정
- Compose 모범 사례 적용

## 📊 품질 기준

- detekt weighted issues: 0개 목표
- 최대 라인 길이: 120자
- 함수 매개변수: 6개 이하
- 순환 복잡도: 15 이하
