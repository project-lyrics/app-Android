# CORE LAYER GUIDE

## OVERVIEW
- Shared runtime layer. Data access, domain models, and reusable UI primitives live here.

## STRUCTURE
```text
core/
├── data/           # datasource, repository, interceptor, DI, manager
├── designsystem/   # reusable Compose components + icons
└── domain/model/   # app-wide business models
```

## WHERE TO LOOK
| Task | Location | Notes |
|---|---|---|
| Auth/session persistence | `data/manager/AuthManager.kt` | central token/OAuth state owner |
| Remote auth | `data/datasource/remote/` | Retrofit service, DTOs, remote datasource |
| SDK auth | `data/datasource/sdk/` | Kakao/Google SDK bridging |
| Local auth storage | `data/datasource/local/` | DataStore-backed storage |
| Network DI | `data/di/NetworkModule.kt` | Retrofit/OkHttp wiring |
| Shared models | `domain/model/` | `OAuthToken`, `ProfileType`, `SignUpData` |
| Shared UI widgets | `designsystem/component/` | tabs, dialogs, inputs, app bars |

## CONVENTIONS
- Keep `core` feature-agnostic. Feature-specific screen logic belongs in `presentation/view`.
- New shared UI belongs here only if reused across multiple features.
- Prefer extending existing repositories/datasources over adding direct SDK or Retrofit calls in feature screens.
- Preserve existing separation: `sdk`, `remote`, `local` datasource boundaries are meaningful here.

## ANTI-PATTERNS
- Do not import feature screen packages into `core`.
- Do not put navigation decisions inside repositories or datasources.
- Do not collapse `AuthManager` persistence rules into UI/ViewModel code.
- Do not duplicate shared models inside feature directories.

## NOTES
- `core/data` is the heaviest shared area in the repo.
- `core/designsystem/component` is dense but still reusable-parent territory; avoid feature-only widgets here.
