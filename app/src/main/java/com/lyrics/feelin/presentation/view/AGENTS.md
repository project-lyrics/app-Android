# PRESENTATION VIEW GUIDE

## OVERVIEW
- Feature screens, feature ViewModels, and feature-local UI pieces live here.

## STRUCTURE
```text
presentation/view/
├── community/
├── component/      # shared feature-facing display components
├── login/
├── mypage/
├── note/
└── onboarding/
```

## WHERE TO LOOK
| Task | Location | Notes |
|---|---|---|
| Login flow | `login/` | OAuth UI + backend auth state |
| Multi-step signup | `onboarding/` | dedicated child guide exists |
| Profile/settings | `mypage/` | dedicated child guide exists |
| Note search | `note/search/` | list/search/pagination behavior |
| Community home | `community/` | currently simple UI state toggles |
| Shared display widgets | `component/` | artist/music/note/profile renderers |

## CONVENTIONS
- Screen state stays close to the feature: screen, viewmodel, screen-state/view-state files in the same feature directory.
- Use shared theme tokens and shared components before adding per-feature styling primitives.
- When a feature needs persistence/network access, route it through `core/data` abstractions.
- Follow existing Hilt + `hiltViewModel()` pattern from navigation and screen entrypoints.

## ANTI-PATTERNS
- Do not move shared design primitives into feature directories just to avoid imports.
- Do not let feature composables reach directly into SDK/network layers.
- Do not create new top-level feature directories for small one-screen experiments unless the feature actually branches.

## NOTES
- `login`, `community`, and `note` are currently small enough to stay under this parent guide.
- `onboarding` and `mypage` have deeper local structure and their own child guides.
