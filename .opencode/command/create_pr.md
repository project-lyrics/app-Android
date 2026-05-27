---
description: GitHub CLI로 PR 제목과 본문을 준비하고 사용자 확인 후 PR 생성
---

현재 브랜치의 변경사항을 기준으로 PR 제목과 본문을 작성한다.
사용자 확인 전에는 절대로 `gh pr create`를 실행하지 않는다.
별도 base branch 지시가 없으면 `upstream/develop`으로 병합 요청을 생성한다.

## Step 1: Gather Context

아래 명령으로 PR 생성에 필요한 컨텍스트를 수집한다.

```bash
# GitHub CLI availability and auth
gh auth status

# Current branch and remotes
git branch --show-current
git remote -v

# Base branch freshness
git fetch upstream develop

# Current working tree and PR scope
git status --short
git log --oneline -5
git diff --stat upstream/develop...HEAD
git diff --name-only upstream/develop...HEAD
```

`gh auth status`가 실패하거나 `upstream` remote가 없으면 PR 생성을 진행하지 않고 사용자에게 안내한다.
별도 base branch 지시가 있으면 해당 base를 사용하되, 지시가 없으면 `upstream/develop`을 기준으로 한다.

## Step 2: Analyze PR Scope

아래 명령으로 변경 내용을 읽고 PR의 범위와 목적을 파악한다.

```bash
git diff upstream/develop...HEAD
```

PR 제목의 접두사는 커밋 메시지와 동일한 규칙을 따른다.

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

작업 범위가 모호하거나 복합적이면 `docs/ai`처럼 복합 접두사를 사용할 수 있다.
브랜치명에 Jira 티켓 번호가 포함되어 있으면 PR 제목의 접두사 앞에 티켓 번호를 작성한다.

예시:

```text
SCRUM-123 feat: Add social login
docs/ai: Add PR creation command
```

## Step 3: Draft PR Body

`.github/PULL_REQUEST_TEMPLATE.md`를 반드시 참조해 PR 본문을 작성한다.

```bash
cat .github/PULL_REQUEST_TEMPLATE.md
```

oh-my-openagent 플러그인을 사용할 수 있는 환경이라면 `writing` 카테고리의 서브에이전트가 본문 초안을 작성한다.
서브에이전트에는 아래 컨텍스트를 제공한다.

- PR template: `@.github/PULL_REQUEST_TEMPLATE.md`
- base branch: 기본 `upstream/develop`
- current branch
- changed file list
- diff summary
- relevant commit log

본문은 템플릿의 섹션을 유지하되, 불필요한 placeholder는 실제 내용으로 정리한다.
스크린샷이 필요하지 않은 변경이면 `ScreenShots` 섹션에는 해당 없음으로 명시한다.

## Step 4: Present Draft

바로 PR을 생성하지 말고 먼저 채팅에 PR 제목과 본문 초안을 제안한다.
사용자가 수정 요청을 하면 제목과 본문을 반영해 다시 제안한다.

## Step 5: Create PR on Confirmation

사용자가 PR 생성을 명시적으로 승인하면 본문을 임시 파일에 저장한 뒤 GitHub CLI로 PR을 생성한다.

```bash
gh pr create \
  --repo project-lyrics/app-Android \
  --base develop \
  --head "$(git branch --show-current)" \
  --title "<confirmed title>" \
  --body-file "<prepared body file>"
```

fork 브랜치를 명시해야 하는 상황이면 `--head "gdaegeun539:$(git branch --show-current)"`처럼 owner를 포함한다.

PR 생성 후에는 결과를 확인한다.

```bash
gh pr view --json url,title,state,baseRefName,headRefName
```

## Final rule

- 사용자 확인 없이 `gh pr create`를 실행하지 않는다
- 별도 base branch 지시가 없으면 `upstream/develop`을 사용한다
- PR 본문은 `.github/PULL_REQUEST_TEMPLATE.md`를 기반으로 작성한다
- PR 본문 초안은 가능한 경우 `writing` 카테고리 서브에이전트가 작성한다
- PR 제목은 Jira ticket + commit prefix 규칙을 따른다
- `gh auth status` 또는 `upstream` remote 확인에 실패하면 PR 생성을 중단한다

# Reference article

@`.agent/rules/git-commit-rules.md`
@`.github/PULL_REQUEST_TEMPLATE.md`
