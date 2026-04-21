---
description: 스테이징된 변경을 분석해 커밋 메시지를 제안하고 사용자 확인 후 실제 커밋까지 수행
---

스테이징된 변경만 기준으로 커밋 메시지를 생성한다.
먼저 후보를 제안하고, 사용자가 옵션을 고르거나 직접 메시지를 주면 그때만 커밋을 실행한다.
스테이징된 변경이 없으면 커밋을 진행하지 않는다.

## Step 1: Gather Context

아래 명령으로 현재 커밋 컨텍스트를 수집한다.
언제나 staged 변경만 대상으로 보고, unstaged 변경이나 untracked 파일은 자동으로 포함하지 않는다.

```bash
# What's staged?
git diff --cached --stat
git diff --cached --name-only

# What branch are we on?
git branch --show-current

# Recent commits for style reference
git log --oneline -5
```

## Step 2: Analyze Changes

staged diff를 읽고 변경의 성격을 파악한다.

```bash
git diff --cached
```

변경 유형은 아래 중 하나로 분류한다.

- init: 초기 설정
- feat: 신규 기능
- docs: 문서 변경
- build: 빌드 관련 변경
- design: 화면 디자인 변경
- fix: 버그 수정
- chore: 자잘한 유지보수
- refactor: 리팩터링
- ci: CI 설정 변경
- test: 테스트 변경
- perf: 성능 개선

## Step 3: Generate Commit Message

기본 형식:

```text
<type>: <Subject>

<body - what and why, not how>
```

커밋 메시지는 아래 규칙을 따른다.

### 1. 커밋 메시지는 영문으로 쓴다

- type은 소문자 영어를 유지한다
- 제목과 본문 모두 영문으로 작성한다
- 고유명사나 코드 용어는 그대로 유지할 수 있다

### 2. 제목과 본문 사이는 한 줄 비운다

- 단순 변경이면 제목만 사용 가능
- 맥락이 필요하면 제목 + 빈 줄 + 본문 사용
- 제목 50자 안에 변경 의도를 충분히 담기 어렵다면 본문을 추가한다

### 3. 제목은 50자 이내를 우선한다

- 권장: 50자 이내
- 최대: 72자 이내

### 4. 제목은 명령형으로 쓴다

- 변경 결과 설명보다 수행 의도를 드러내는 표현을 우선한다

### 5. 제목 끝에는 마침표를 붙이지 않는다

### 6. 본문은 how보다 what과 why를 설명한다

- 왜 이 변경이 필요한지
- 이전 상태의 어떤 문제가 있었는지
- 어떤 효과를 기대하는지

### 7. 본문은 72자 기준으로 줄바꿈한다

## Step 4: Present Options

바로 커밋하지 말고 먼저 채팅에 커밋 메시지 후보 2~3개를 코드 블록으로 제안한다.
첫 번째 옵션은 가장 적절한 추천안으로 둔다.

예시:

```markdown
## Suggested Commits

### Option 1 (recommended)
```
feat: Add automatic token refresh

Refresh access tokens before session expiry so long-running usage
does not unexpectedly force the user to log in again.
```

### Option 2 (minimal)
```
feat: Add token refresh
```

### Option 3 (detailed)
```
feat: Introduce proactive JWT token refresh

Reduce unexpected session expiration during long-running usage by
renewing tokens before they expire.
```
```

사용자가 `1`, `option 1`, 또는 직접 커밋 메시지를 주면 그 메시지를 사용한다.
확인이 없으면 제안까지만 하고 멈춘다.

## Step 5: Execute on Confirmation

사용자가 옵션을 선택하거나 직접 메시지를 확정하면 그때만 실제 커밋을 실행한다.

```bash
git commit -m "<title>" \
  -m "<body if needed>"
```

다음 규칙을 반드시 지킨다.

- 사용자의 확인 없이 `git commit`을 실행하지 않는다
- staged 변경이 없으면 빈 커밋을 만들지 않는다
- staged 변경만 커밋 대상으로 사용한다
- 커밋 후 상태를 확인한다

```bash
git status
```

## Final rule

- 이 명령은 제안과 실행을 모두 다루지만, 실행은 반드시 사용자 확인 이후에만 한다
- 사용자가 고른 옵션이 있으면 그 메시지를 그대로 우선 적용한다
- 관련 규칙이 충돌하면 이 레포의 커밋 규칙 문서를 우선 따른다

# Reference article

@`.agent/rules/git-commit-rules.md`
