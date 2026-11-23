# JARVIS

Project: JARVIS, a privacy-first, battery-first Android + Windows personal assistant.
Full requirements: JARVIS_MASTER_BLUEPRINT.md (in this folder).

## Budget
- No paid APIs, cloud services, hosting, subscriptions, or usage-based API billing.
- Do not add any dependency that requires payment.
- Do not configure API-key billing.
- Optional cloud AI must never be required for core features.

## Privacy
- Local-first by default.
- Never automatically upload photos, contacts, SMS, WhatsApp data, files, location, clipboard, notifications, or fitness data.
- No analytics or advertising SDKs.
- Do not save raw audio by default.
- Ask before sensitive access, transfer, or external actions.
- Keep secrets out of source code.

## Battery and performance
- No continuous microphone listening in the MVP.
- No unnecessary background services, polling, or network retries.
- Do not run a large model on Android.
- Release temporary resources after commands.
- Measure battery, CPU, memory, and temperature for major features.

## Security
- No arbitrary remote shell execution.
- Use authenticated encrypted device communication.
- Use an allowlist of command IDs and validated parameters.
- Protect against replay, expiry, and unauthorized devices.
- Require confirmation for destructive or externally visible actions.
- No public port exposure.
- No root/admin requirement for ordinary features.

## Coding
- Inspect relevant files before editing.
- Give a short implementation plan before making changes.
- Make the smallest targeted change.
- Do not delete, rename, or rewrite existing files without approval.
- Do not add dependencies without explaining why they are needed.
- Preserve existing working features.
- Add or update tests for changed behaviour.
- Run relevant local tests/builds when possible.
- Never claim tests passed unless they were actually run.
- Report exact files changed, commands run, results, and blockers.
- Do not scan or explain the entire repository unless the task requires it.

## Workflow
- Work on one milestone at a time.
- Do not implement future phases early.
- Ask before expanding scope.
- If requirements are unclear, ask one focused question.
- Prefer simple deterministic logic over AI.
