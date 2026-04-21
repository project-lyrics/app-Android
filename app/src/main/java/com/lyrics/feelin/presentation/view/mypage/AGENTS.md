# MYPAGE FEATURE GUIDE

## OVERVIEW
- User profile area. Combines main profile summary, settings entry, and user info detail screens.

## STRUCTURE
```text
mypage/
├── MyPageScreen.kt
├── MyPageScreenState.kt
├── MyPageViewModel.kt
├── component/
├── setting/
└── userinfo/
```

## WHERE TO LOOK
| Task | Location | Notes |
|---|---|---|
| Root screen | `MyPageScreen.kt` | tab selection + navigation entry |
| Feature state | `MyPageScreenState.kt` | screen/tab/user state models |
| Data loading/logout | `MyPageViewModel.kt` | DataStore-backed user state |
| Reusable rows/items | `component/` | settings/user rows |
| Settings screen | `setting/SettingScreen.kt` | route from main mypage |
| User info screen | `userinfo/UserInfoScreen.kt` | nested detail screen |

## CONVENTIONS
- Keep user-facing state in `MyPageScreenState.kt`; avoid scattering flags across multiple files.
- Use `UserRepository` as the source for login/profile persistence decisions.
- Treat `component/` as mypage-local, not global design system.
- Navigation from mypage to nested settings/userinfo stays declared in `FeelinNavHost.kt`.

## ANTI-PATTERNS
- Do not move mypage-only rows into `core/designsystem` unless another feature truly reuses them.
- Do not duplicate logout or user loading logic in nested screens.
- Do not make `setting/` and `userinfo/` independent feature roots; they are children of mypage.

## NOTES
- Current feature shape is profile summary -> settings -> user info.
- This area already has enough local structure to justify a focused child guide.
