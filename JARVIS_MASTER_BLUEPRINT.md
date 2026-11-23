# JARVIS Master Blueprint

## Privacy-first, battery-first personal assistant for Android and Windows

**Project status:** Planning blueprint --- implementation has not
started\
**Primary devices:** Nothing Phone (4a) Pro (8 GB RAM / 128 GB storage)
and a Windows laptop\
**Development assistant:** Claude Code using the user's existing Claude
Pro subscription\
**Budget target:** ₹0 additional spending; no paid APIs, cloud hosting,
API credits, or new subscriptions\
**Primary objective:** Build one dependable assistant that can accept
voice or text commands on the phone, perform approved actions on the
phone or laptop, and grow into a useful personal operating system for
productivity, business, fitness, and daily life.

------------------------------------------------------------------------

# 1. Product vision

JARVIS is a personal assistant made of two cooperating applications:

1.  **Android companion app** --- the primary voice/text interface and
    phone-side action engine.
2.  **Windows companion app** --- receives authenticated commands from
    the phone and performs approved laptop actions.

The two apps share a command format, security rules, and a consistent
assistant personality. Each device must continue to provide its own
local functions if the other device is unavailable.

### Example

The user taps the JARVIS shortcut on the phone and says:

> "JARVIS, open my development workspace."

The phone converts speech to text, recognizes the command, and sends a
single authenticated request over the local network. The Windows
companion checks the request against its allowlist, opens the approved
development applications, and returns a result. The phone speaks the
result and returns to idle.

### Product principles

-   **Battery first:** no unnecessary always-on microphone, polling,
    background processing, or constant AI use.
-   **Privacy first:** personal information stays on the device by
    default.
-   **Local first:** common commands should work without an internet
    connection wherever Android/Windows APIs allow.
-   **No surprise actions:** sensitive, destructive, financial, or
    externally visible actions require confirmation.
-   **Graceful failure:** if a device, network, or optional AI service
    is unavailable, explain the limitation and keep local features
    working.
-   **Small, testable releases:** build a reliable core before adding
    advanced features.
-   **No hidden surveillance:** no covert recording, notification
    harvesting, or automatic upload of personal data.
-   **Zero additional spending:** do not introduce paid services or
    usage-based API dependencies.

------------------------------------------------------------------------

# 2. Non-negotiable constraints

## 2.1 Budget

-   Do not use paid APIs or paid cloud services.
-   Do not create API usage charges.
-   Do not subscribe to additional services.
-   Do not add a paid database, hosting provider, or monitoring service.
-   Use local storage and free/open-source libraries where practical.
-   Claude Code is used through the user's existing Claude Pro
    subscription. Do not configure API-key billing.
-   If Claude's subscription usage limit is reached, stop coding with
    Claude and wait for the allowance to reset. Do not recommend buying
    extra usage as a default workaround.

**Important:** "Free" services can change their terms or limits. Never
make the system depend on a supposedly unlimited free tier.

## 2.2 Phone performance and battery

JARVIS must not noticeably degrade normal phone use.

-   No continuous microphone listening in the initial release.
-   No wake-word service in the initial release.
-   No permanent foreground service unless a clearly justified Android
    feature requires it.
-   No frequent background polling.
-   No repeated network pings to the laptop.
-   No heavy local language model on the phone.
-   No automatic scanning of the whole phone's storage.
-   No unnecessary analytics, advertising SDKs, or crash-reporting
    uploads.
-   Stop temporary work promptly after a command completes.
-   Use Android system services and hardware-accelerated capabilities
    where appropriate.
-   Keep the app responsive and avoid blocking the main/UI thread.
-   Measure battery, CPU, memory, storage, and temperature before and
    after each major feature.
-   If a feature creates unacceptable impact, disable it or redesign it.

A running app always has some overhead. The goal is **very low idle
overhead**, not a false promise of zero battery use.

## 2.3 Privacy and data boundaries

Default to local-only storage.

Never automatically upload: - Photos or videos - Contacts - SMS -
WhatsApp messages or chat history - Call history - Documents or
arbitrary files - Location - Clipboard contents - Notifications -
Fitness or health data

A feature may access a particular data category only after: 1. The user
deliberately enables the feature. 2. The app explains what it accesses
and why. 3. Access is limited to the minimum necessary. 4. The user can
revoke access. 5. The app shows when a sensitive action or transfer
occurs.

Cloud AI, if ever added, must be optional and off by default. Before any
cloud request, show what limited text/context will be sent and ask for
permission where the content is personal or sensitive. Never silently
send entire files or conversation histories.

## 2.4 Security

-   No hardcoded passwords, API keys, private keys, or pairing secrets.
-   No unrestricted remote shell execution.
-   No arbitrary command strings sent from phone to laptop.
-   No public internet port exposure.
-   Use authenticated and encrypted device-to-device communication.
-   Pair devices deliberately.
-   Use an allowlist of supported command identifiers and validated
    parameters.
-   Protect against replayed requests.
-   Apply request timeouts and rate limits.
-   Keep a local, user-visible activity log.
-   Require confirmation for sensitive or destructive actions.
-   Do not request administrator/root privileges for normal features.
-   Do not weaken Windows Defender, Android security, firewall, or OS
    protections.

------------------------------------------------------------------------

# 3. System architecture

``` text
                    USER
                     |
             Android voice / text
                     |
             Android JARVIS app
                     |
        Speech-to-text / text input
                     |
              Intent router
                     |
        +------------+-------------+
        |                          |
  Phone action engine        Cross-device client
        |                          |
 Android supported APIs      Authenticated encrypted
        |                    local network request
        |                          |
        |                    Windows JARVIS service
        |                          |
        |                    Request validation
        |                          |
        |                    Allowlisted action router
        |                          |
        |                    Windows action modules
        |                          |
        +------------+-------------+
                     |
              Result / status
                     |
            Phone voice / screen
```

Optional AI is a separate adapter, not the central engine:

``` text
User request -> intent router -> deterministic local command
                              -> optional AI adapter for complex language
                                 -> privacy filter -> user approval
```

The normal path must not require AI.

## 3.1 Main modules

### Android

-   UI and accessibility-friendly controls
-   Voice input adapter
-   Text-to-speech output
-   Intent/command router
-   Phone action modules
-   Local settings and data store
-   Secure pairing and network client
-   Permission and privacy controls
-   Local activity log
-   Optional AI adapter (later)

### Windows

-   Desktop UI/tray interface
-   Text and optional voice input
-   Authenticated local network listener
-   Pairing and request validation
-   Command allowlist/router
-   Windows action modules
-   Approved-folder file assistant
-   Local settings and SQLite database
-   Activity log
-   Optional local AI adapter (later)

### Shared protocol

-   Versioned command schema
-   Request ID
-   Device identity
-   Timestamp and expiry
-   Nonce / replay protection
-   Command identifier
-   Strictly validated parameters
-   Result status and safe error message

------------------------------------------------------------------------

# 4. Recommended technology choices

These are starting recommendations, not permission to add dependencies
without review.

  -----------------------------------------------------------------------
  Component               Preferred starting      Reason
                          technology              
  ----------------------- ----------------------- -----------------------
  Android app             Kotlin                  Native Android
                                                  integration

  Android UI              Jetpack Compose         Modern native UI

  Android build           Gradle                  Standard Android build
                                                  system

  Android local database  Room / SQLite           Local structured
                                                  storage

  Android preferences     DataStore               Local settings

  Android speech input    Android                 Avoid building speech
                          SpeechRecognizer /      infrastructure
                          available system        
                          provider                

  Android speech output   Android TextToSpeech    Native speech output

  Windows app             Python                  Fast development and
                                                  broad Windows
                                                  integration

  Windows UI              PySide6, if needed      Desktop UI

  Windows database        SQLite                  Local storage

  Windows automation      Supported Windows APIs  Avoid fragile,
                          / UI Automation         unrestricted scripts

  Cross-device transport  HTTPS over local        Authenticated encrypted
                          network, or a carefully traffic
                          reviewed secure local   
                          protocol                

  Version control         Git                     Safe checkpoints and
                                                  rollback

  IDE                     Android Studio +        Native Android
                          existing editor         development
  -----------------------------------------------------------------------

Do not add a library just because it is popular. Check license,
maintenance, privacy, size, and whether built-in platform APIs are
sufficient.

## 4.1 Speech recognition reality

Android's SpeechRecognizer may rely on a system recognition provider and
may use network services depending on the device/provider/settings. Do
not claim all speech recognition is offline.

-   First release: user taps a microphone button or shortcut, speaks one
    command, and recognition ends.
-   Check whether the Nothing Phone's speech provider supports on-device
    recognition.
-   If unavailable, allow text input as a reliable fallback.
-   Never keep the microphone open after the command is complete.
-   TTS should use Android's installed speech engine.

## 4.2 Why not run a large AI model on the phone?

A large local model can consume substantial RAM, storage, CPU, battery,
and heat. Do not install one on the phone as part of the MVP.

Common commands should be handled with ordinary Kotlin/Python logic.
Consider local AI on the laptop only in a later phase, after checking
laptop specifications and performance. The laptop must be on when its
local AI service is needed.

------------------------------------------------------------------------

# 5. Activation and battery strategy

## Initial activation model

1.  User taps the app, home-screen shortcut, or Quick Settings tile.
2.  JARVIS receives one command.
3.  Speech recognition stops after the command.
4.  The command router executes locally or sends one request to the
    laptop.
5.  JARVIS reports the result.
6.  Temporary resources are released and the app returns to idle.

## Later activation options

-   Headset button or supported system shortcut.
-   Quick Settings tile.
-   Optional wake word, only after feasibility and battery testing.
-   Do not promise unrestricted background wake-word listening while the
    phone is locked. Android versions and device power-management
    policies restrict background microphone access and foreground
    services.

## Battery acceptance tests

Record a baseline before installation. Repeat under similar conditions
after each major release.

Measure: - Battery percentage over a fixed idle period. - Battery use
during a defined number of voice commands. - App battery usage in
Android settings. - Idle and active CPU use where measurable. - Memory
use. - Temperature during repeated commands. - Overnight behaviour. -
Effect on normal calls, messaging, camera, music, navigation, and
gaming.

Set actual acceptable thresholds after measuring the phone's baseline.
Do not invent a guaranteed battery percentage in advance.

------------------------------------------------------------------------

# 6. Feature master list

The ten requested features are included below. Priority indicates build
order, not importance to the user's life.

  ---------------------------------------------------------------------------
  Feature        Build priority Privacy risk   Cost target    Feasibility
  -------------- -------------- -------------- -------------- ---------------
  Morning        High           Low--medium    ₹0             High
  briefing                                                    

  Smart work     High           Low            ₹0             High
  mode                                                        

  File assistant High           Medium         ₹0             High with
                                                              folder limits

  Emergency mode High           Medium         ₹0             Medium--high;
                                                              OS limits apply

  Cross-device   Medium         High           ₹0             High on local
  clipboard                                                   network

  Business       Medium         Depends on     ₹0 using local High
  assistant                     user data      tools          

  Fitness        Medium         Sensitive if   ₹0             High for manual
  assistant                     health data                   tracking
                                used                          

  Personal       Medium         High           ₹0             High
  memory                                                      

  Notification   Low--medium    High           ₹0             Medium; Android
  assistant                                                   permission
                                                              limits

  Smart home     Low            Depends on     ₹0 software    Medium
  integration                   devices        target;        
                                               hardware may   
                                               cost           
  ---------------------------------------------------------------------------

## 6.1 Morning briefing

**Purpose:** Give a concise start-of-day summary when requested or at a
user-configured time.

Possible contents: - Today's tasks and priorities - Reminders due
today - User-entered appointments - Weather only if a free,
privacy-acceptable source is available and user enables it - Selected
calendar events only after explicit integration/permission - Personal
goals and habits - A short motivational or focus statement

**MVP:** Use locally stored tasks, reminders, and goals. Do not require
a cloud service.

**Later:** Optional calendar/weather integrations. Clearly show the
source and freshness of external information.

**Privacy:** Do not read email, calendar, or notifications without
explicit opt-in.

## 6.2 Smart work mode

**Purpose:** Prepare the laptop for a focused work session.

Possible actions: - Open user-selected applications. - Open a selected
coding workspace or project folder. - Open user-approved browser
pages. - Set a focus timer. - Show today's work priorities. - Optionally
enable a supported Windows focus setting.

**MVP:** User-configured app list and workspace paths.

**Safety:** Never close unsaved applications, kill arbitrary processes,
or modify startup settings without confirmation.

## 6.3 Cross-device clipboard

**Purpose:** Send selected text between the phone and laptop.

**MVP behaviour:** - User deliberately chooses "Send clipboard to
laptop" or "Get clipboard from laptop". - Show a preview before
transfer. - Transfer only the selected clipboard text. - Use encrypted
authenticated transport. - Do not sync continuously. - Do not store
clipboard history by default. - Do not transfer passwords, one-time
codes, payment information, or other sensitive content automatically.

**Later:** Optional temporary clipboard history with a clear expiry and
delete control.

## 6.4 File assistant

**Purpose:** Find and open files in folders the user explicitly
approves.

**MVP:** - User selects allowed folders. - Search by filename and
supported metadata. - Show file path and type. - Open a file through the
operating system. - No automatic whole-drive scan. - No automatic file
upload to AI. - No deletion or modification in the first release.

**Later:** - User-approved local summaries for supported formats. - File
organisation suggestions. - Duplicate detection. - Move/rename actions
only after preview and confirmation.

**Security:** Windows file assistant must be restricted to configured
folders. Resolve paths and verify they remain inside the approved folder
to prevent path traversal.

## 6.5 Emergency mode

**Purpose:** Provide a fast, user-controlled protective action.

Possible actions: - Stop accepting remote commands. - Revoke or suspend
the paired device session. - Lock the Windows session using supported OS
functionality. - Show emergency contacts or user-written instructions. -
Display a clear status report.

**MVP:** A local emergency button and a phone command that requests
Windows session lock, with confirmation or a carefully configured
trusted-device policy.

**Limitations:** - Android cannot guarantee remote control of every
system function. - A phone app cannot guarantee it can disable all
radios or lock every device under every OS state. - Do not implement
covert tracking, remote wiping, or silent data extraction. - Do not
present this as a replacement for emergency services.

## 6.6 Business assistant

**Purpose:** Help manage business ideas, execution, and basic records.

Possible modules: - Idea capture - Project and milestone tracking -
Daily priorities - Meeting/action notes - Basic expense records entered
by the user - Revenue and cost calculations - Follow-up reminders -
Weekly review - Decision log

**MVP:** Local notes, tasks, projects, and user-entered numbers. No bank
access or automatic financial account connection.

**Later:** Optional export to CSV/PDF and approved integrations.

## 6.7 Fitness assistant

**Purpose:** Support training consistency and progress tracking.

Possible modules: - Workout plan and session log - Exercise
sets/reps/weight - Training reminders - Recovery notes - Body
measurements entered by the user - Goal tracking - Weekly progress
summary

**MVP:** Manual entry and local reminders.

**Privacy:** Treat health and fitness records as sensitive. Keep them
local, protect access, and never send them to an AI provider by default.
JARVIS is not a medical diagnostic system.

## 6.8 Personal memory

**Purpose:** Remember only user-approved information that makes the
assistant more useful.

Examples: - Preferred name for the assistant to use (only if user
configures it) - Preferred apps and workspaces - Frequently used
commands - Active projects - User-defined routines and preferences

**Rules:** - User explicitly chooses what is saved. - Show saved items
in a memory screen. - Allow viewing, editing, exporting, and deleting. -
Do not infer or silently store sensitive attributes. - Do not store
passwords, authentication tokens, bank details, or one-time codes. - Do
not upload memory to cloud AI automatically.

**MVP:** Small local key-value or structured memory store.

## 6.9 Notification assistant

**Purpose:** Optionally show selected phone notifications on the laptop
or summarise selected notifications.

**MVP:** Defer until the security model is mature.

If implemented: - Use Android's notification-listener permission only
after a clear explanation and explicit user action. - Let the user
choose allowed apps. - Exclude sensitive apps by default. - Do not read
message history; process only notifications the user has permitted. - Do
not forward notification contents to cloud AI. - Provide a visible pause
switch and immediate revoke instructions. - Avoid continuous polling;
use the platform's event mechanism where appropriate.

Android notification access is powerful. Treat it as a high-risk
permission.

## 6.10 Smart home integration

**Purpose:** Control compatible smart-home devices through their
supported interfaces.

**MVP:** Do not buy hardware or commit to a cloud platform. First
identify devices the user already owns and whether they offer a
documented local interface.

Possible later actions: - Switch a compatible light on/off. - Control a
compatible plug. - Read a supported sensor. - Run user-configured
scenes.

**Security:** - Prefer local control over cloud control. - Keep
smart-home credentials out of source code. - Require confirmation for
security-sensitive devices such as locks, garage doors, or cameras. -
Never expose a home-control server directly to the public internet.

------------------------------------------------------------------------

# 7. Phone capabilities and OS limitations

The Android app should use supported Android intents and APIs.

Possible supported actions, subject to device/OS version and
permissions: - Open installed apps. - Open dialler with a number filled
in. - Open a supported website or app destination. - Create
reminders/alarms using supported system APIs or intents. - Speak
responses. - Send a command to the paired Windows device.

Important limitations: - Opening WhatsApp or preparing a message is not
the same as silently sending a message. Use supported share/deep-link
flows and require user confirmation. - Direct phone calls require
permissions and may be restricted. Prefer opening the dialler first. -
Some settings cannot be changed by ordinary third-party apps. - Do not
use Accessibility Service as a shortcut for unrestricted control. If a
future feature genuinely requires it, explain the risk and request
explicit approval. - Do not attempt to bypass Android security or app
sandboxing.

------------------------------------------------------------------------

# 8. Windows companion behaviour

The Windows app should start as a normal user-level application.

Initial capabilities: - Start/stop the companion service. - Show
paired-device status. - Accept authenticated requests only while
enabled. - Open allowlisted applications. - Open configured project
folders. - Search approved folders. - Set supported volume controls. -
Lock the Windows session. - Return structured success/failure results.

Not included in the first release: - Arbitrary PowerShell or command
prompt execution from the phone. - Silent file deletion. -
Administrator-level operations. - Remote desktop streaming. -
Unrestricted keyboard/mouse control. - Hidden startup persistence.

The laptop must be powered on, awake, connected to the network, and
running the JARVIS companion to receive commands. It cannot perform
actions while powered off or asleep. Wake-on-LAN may be researched
later, but support depends on laptop firmware, network adapter, router,
and network configuration.

------------------------------------------------------------------------

# 9. Secure phone-to-laptop communication

## MVP network boundary

Start with the phone and laptop on the same trusted Wi-Fi network. Do
not expose a listener to the public internet.

## Pairing flow

1.  User opens the pairing screen on both devices.
2.  One device displays a short-lived pairing code or QR code.
3.  The other device enters/scans it.
4.  Both devices confirm the pairing.
5.  Store credentials securely using platform-appropriate secure
    storage.
6.  Show paired device name and last connection status.
7.  Provide a revoke/unpair button.

Do not use a permanent hardcoded PIN or shared secret in source code.

## Request model

Use a versioned structured message, conceptually:

``` json
{
  "protocol_version": 1,
  "request_id": "random-unique-id",
  "issued_at": "timestamp",
  "expires_at": "timestamp",
  "nonce": "single-use-random-value",
  "command": "open_workspace",
  "parameters": {
    "workspace_id": "primary"
  }
}
```

This is an illustrative schema. Implement cryptographic authentication
and replay protection; do not treat a random JSON field alone as
security.

The laptop must: - Authenticate the sender. - Verify timestamp and
expiry. - Reject reused nonces/request IDs. - Validate the command
against an allowlist. - Validate every parameter. - Apply timeouts and
rate limits. - Log the result. - Return a structured response.

The phone must: - Verify the laptop identity. - Display connection
state. - Avoid retry loops. - Stop waiting after a reasonable timeout. -
Tell the user when the laptop is offline.

## Remote access later

Do not build remote access by opening router ports. If remote access is
ever required, research a reputable, maintained, end-to-end encrypted
solution and review its cost, privacy, and security implications first.
Keep same-Wi-Fi mode working independently.

------------------------------------------------------------------------

# 10. Command design

Use deterministic command IDs rather than executing free-form text as
code.

Example command catalogue:

  -------------------------------------------------------------------------
  Command ID                Meaning                 Confirmation
  ------------------------- ----------------------- -----------------------
  `open_app`                Open an allowlisted     Usually no
                            phone app               

  `open_workspace`          Open configured laptop  No
                            workspace               

  `search_approved_files`   Search configured       No
                            folders                 

  `lock_windows`            Lock laptop session     Optional configurable

  `send_clipboard_text`     Transfer selected text  Yes

  `create_reminder`         Save a reminder         Confirm parsed time

  `open_dialer`             Open dialler with       Yes before call
                            number                  

  `prepare_message`         Prepare a message in    User sends manually
                            supported app           

  `enable_emergency_mode`   Suspend remote access / Yes, except a
                            initiate configured     deliberately configured
                            protection              local emergency control
  -------------------------------------------------------------------------

Every command needs: - A unique ID - A human-readable description -
Parameter schema - Permission requirement - Confirmation policy -
Timeout - Result format - Unit tests

Never pass recognized speech directly to a shell.

------------------------------------------------------------------------

# 11. Data storage and retention

## Android

Use Room/SQLite for structured local records and DataStore for settings
where appropriate.

Possible local records: - User preferences - Approved app list -
Reminders - Tasks and goals - Workout logs - Business projects -
Explicitly saved memory - Paired device metadata - Activity log

## Windows

Use SQLite for: - Settings - Paired-device metadata - Approved folders
and workspaces - Activity log - Local business/productivity records if
enabled

## Retention rules

-   Store the minimum data required.
-   Set a clear retention policy for logs.
-   Avoid storing raw audio. Default: do not save voice recordings.
-   Do not store clipboard contents by default.
-   Provide clear-data and export controls.
-   Protect sensitive local data with platform security facilities where
    appropriate.
-   Do not claim encryption at rest unless it is actually implemented
    and tested.

------------------------------------------------------------------------

# 12. User interface plan

## Android screens

1.  **Home**
    -   Large microphone button
    -   Text command input
    -   Listening/processing/result status
    -   Phone/laptop connection indicator
    -   Quick actions
2.  **Devices**
    -   Pair laptop
    -   Connection status
    -   Last seen
    -   Revoke pairing
3.  **Features**
    -   Toggle each feature on/off
    -   Explain permissions and data use
    -   Show whether feature is local or network-dependent
4.  **Tasks and briefing**
    -   Tasks, reminders, goals
    -   Morning briefing configuration
5.  **Memory and privacy**
    -   View/edit/delete saved memory
    -   Permission status
    -   Data export/clear controls
6.  **Activity**
    -   Recent commands
    -   Device targeted
    -   Result
    -   Timestamp
    -   Sensitive action indicator
7.  **Settings**
    -   Voice and speech engine
    -   Language
    -   Battery-conscious mode
    -   Network mode
    -   Privacy and security

## Windows screens

1.  Main status and command input
2.  Paired devices and pairing
3.  Approved applications and workspaces
4.  Approved file folders
5.  Feature settings
6.  Activity log
7.  Security and emergency controls

The UI should be simple. Avoid decorative animations and unnecessary
background refreshes.

------------------------------------------------------------------------

# 13. Development roadmap

Build in small, verifiable milestones. Do not implement every feature at
once.

## Phase 0 --- Environment and architecture

**Goal:** Verify the development environment and create the project
foundation.

Tasks: - Confirm Windows version, laptop CPU/RAM, and available disk
space. - Install/check Android Studio and Android SDK. - Install/check
Git. - Confirm Kotlin/Gradle toolchain. - Create a Git repository. -
Create a short `CLAUDE.md` with project rules. - Agree on project
structure and protocol. - Record baseline phone battery behaviour. -
Build and run a blank Android app.

**Exit criteria:** - Blank app builds and launches. - Git repository
works. - No paid service or API key is required. - Architecture and
permissions are documented.

## Phase 1 --- Android MVP

**Goal:** A reliable phone assistant that responds to user-initiated
commands.

Tasks: - Home screen with microphone and text input. - Speech-to-text
adapter. - Text-to-speech adapter. - Command parser/router. - A few safe
local commands, such as opening selected apps. - Clear permission
handling. - Local settings. - Activity log without raw audio. - Idle
state releases temporary resources.

**Exit criteria:** - Text commands work. - Voice commands work with
available system provider. - Text fallback works. - No always-on
microphone. - App handles denied permissions and recognition failures
gracefully.

## Phase 2 --- Windows MVP

**Goal:** A safe desktop companion that can perform a few approved
actions locally.

Tasks: - Python environment and minimal app. - Start/stop control. -
Open allowlisted applications. - Open configured workspaces. - Search
approved folders. - Lock Windows session. - Structured results and local
logs. - No remote network listener until local action tests pass.

**Exit criteria:** - Each action works when initiated locally. -
Unapproved paths and commands are rejected. - No administrator
privileges required for normal actions.

## Phase 3 --- Secure device pairing

**Goal:** Send a small number of commands from Android to Windows over
the same trusted Wi-Fi.

Tasks: - Design pairing and key lifecycle. - Implement authenticated
encrypted transport. - Implement request validation, expiry,
nonce/replay protection. - Add allowlisted command routing. - Add
request timeout and offline handling. - Add unpair/revoke
functionality. - Test malicious/invalid requests.

**Exit criteria:** - Only paired devices can send commands. - Invalid,
expired, and replayed requests are rejected. - Phone reports laptop
offline without retrying continuously. - No public internet port is
exposed.

## Phase 4 --- Core productivity features

Build in this order: 1. Morning briefing from local tasks/reminders. 2.
Smart work mode using user-selected apps/workspaces. 3. File assistant
restricted to approved folders. 4. Emergency mode with clearly defined
supported actions. 5. Cross-device clipboard with preview and
confirmation.

**Exit criteria:** Each feature has tests, permission explanation, and a
clear disable/revoke path.

## Phase 5 --- Personal assistant modules

Build: - Business assistant with local projects, ideas, tasks, and
manually entered expenses. - Fitness assistant with manual workout
tracking and reminders. - Personal memory with explicit
save/edit/delete. - Weekly reviews and summaries generated from local
records.

Do not add cloud AI as a dependency.

## Phase 6 --- Sensitive integrations

Only after security review: - Notification assistant, with selected apps
only and explicit Android notification access. - Smart-home integration,
only for already-owned compatible devices and preferably local APIs. -
Optional calendar/weather integrations if a free and privacy-acceptable
source is available.

## Phase 7 --- Optional intelligence and wake word

Only after the deterministic assistant is stable: - Optional AI adapter
for complex natural-language requests. - Privacy filter and user
approval before cloud processing. - Optional local laptop AI after
checking laptop specifications. - Wake-word feasibility test with
measured battery impact. - Disable the feature if battery, heat,
reliability, or privacy impact is unacceptable.

## Phase 8 --- Release hardening

-   Test permissions and revocation.
-   Test phone/laptop disconnection.
-   Test corrupted and malicious requests.
-   Test app restart and laptop restart.
-   Test battery and memory.
-   Test backup/export and data deletion.
-   Review dependencies and licenses.
-   Create a user guide and release build.
-   Keep a rollback path.

------------------------------------------------------------------------

# 14. Testing and release gates

A feature is not complete just because it works once.

## Functional

-   Normal command succeeds.
-   Unknown command is rejected safely.
-   Invalid parameters are rejected.
-   Permission denied is handled.
-   Offline state is handled.
-   Timeout is handled.
-   Duplicate request is rejected.

## Battery/performance

-   No always-on microphone in MVP.
-   No unnecessary background polling.
-   Idle app returns to low activity.
-   No sustained unexpected CPU use.
-   Memory does not grow continuously.
-   No unacceptable heating.
-   Compare battery against baseline.

## Privacy/security

-   No secret in source code.
-   No automatic upload of personal data.
-   No raw voice recordings saved by default.
-   No clipboard history by default.
-   No unapproved file access.
-   Pairing can be revoked.
-   Sensitive actions require confirmation.
-   Activity log is visible and locally stored.
-   No arbitrary shell execution.

## Compatibility

-   Test on the actual Nothing Phone.
-   Test on the actual Windows laptop.
-   Test after OS/app restart.
-   Test when battery saver is enabled.
-   Test with Wi-Fi disconnected/reconnected.
-   Test Android permission revocation.

------------------------------------------------------------------------

# 15. Claude Code working rules

The project must be developed with small prompts to conserve the user's
Claude Pro allowance.

## Permanent instructions for `CLAUDE.md`

``` text
Project: JARVIS, a privacy-first, battery-first Android + Windows personal assistant.

Budget:
- No paid APIs, cloud services, hosting, subscriptions, or usage-based API billing.
- Do not add any dependency that requires payment.
- Do not configure API-key billing.
- Optional cloud AI must never be required for core features.

Privacy:
- Local-first by default.
- Never automatically upload photos, contacts, SMS, WhatsApp data, files, location,
  clipboard, notifications, or fitness data.
- No analytics or advertising SDKs.
- Do not save raw audio by default.
- Ask before sensitive access, transfer, or external actions.
- Keep secrets out of source code.

Battery and performance:
- No continuous microphone listening in the MVP.
- No unnecessary background services, polling, or network retries.
- Do not run a large model on Android.
- Release temporary resources after commands.
- Measure battery, CPU, memory, and temperature for major features.

Security:
- No arbitrary remote shell execution.
- Use authenticated encrypted device communication.
- Use an allowlist of command IDs and validated parameters.
- Protect against replay, expiry, and unauthorized devices.
- Require confirmation for destructive or externally visible actions.
- No public port exposure.
- No root/admin requirement for ordinary features.

Coding:
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

Workflow:
- Work on one milestone at a time.
- Do not implement future phases early.
- Ask before expanding scope.
- If requirements are unclear, ask one focused question.
- Prefer simple deterministic logic over AI.
```

## Recommended Claude Code task cycle

For every milestone:

1.  Start Claude Code in the correct project folder.
2.  Ask it to inspect only the relevant project files.
3.  Ask for a short plan and files it intends to change.
4.  Review the plan.
5.  Approve the smallest implementation.
6.  Run the build/tests locally.
7.  Ask Claude to fix only specific failures.
8.  Review the diff.
9.  Commit a working milestone in Git.
10. Update the progress checklist.

Use Claude's `/usage` or available status command to monitor
subscription usage. Keep prompts short and scoped. Do not ask Claude to
repeatedly restate this entire blueprint.

------------------------------------------------------------------------

# 16. First prompt to give Claude Code

Do not ask Claude to build the entire JARVIS system in one prompt. Begin
with environment inspection and architecture validation.

Copy this prompt after opening Claude Code in the intended project
directory:

``` text
We are starting the JARVIS project.

Read the existing project files first. If this is an empty directory, confirm that.
Do not create, delete, rename, or modify any files yet.

Our goal is a privacy-first, battery-first Android + Windows assistant.
The full requirements are in JARVIS_MASTER_BLUEPRINT.md.

For this task only:
1. Inspect the available development environment and project directory.
2. Identify missing prerequisites for Android Kotlin development and Windows Python development.
3. Check whether Git, Android Studio/SDK, Java/JDK, Python, and Gradle are available where relevant.
4. Do not install anything.
5. Do not access personal files or scan unrelated directories.
6. Give me a concise readiness report.
7. Recommend the smallest safe next step.

Budget is zero additional spending. Do not use paid APIs, cloud services,
or API-key billing. Do not implement features yet.
```

After the environment report, ask Claude to implement **Phase 0 only**.
Do not jump to Phase 1 until Phase 0 exit criteria are met.

------------------------------------------------------------------------

# 17. Progress checklist

Update this checklist as the project advances.

## Foundation

-   [ ] Confirm laptop specifications and Windows version
-   [ ] Confirm Android Studio and SDK
-   [ ] Confirm Git
-   [ ] Confirm Python and Java/JDK
-   [ ] Create repository
-   [ ] Create `CLAUDE.md`
-   [ ] Add this blueprint to repository
-   [ ] Record phone battery baseline
-   [ ] Build blank Android app

## Android MVP

-   [ ] Home screen
-   [ ] Text input
-   [ ] User-initiated voice input
-   [ ] Text-to-speech
-   [ ] Intent router
-   [ ] Safe local commands
-   [ ] Local settings and logs
-   [ ] Permission-denied handling
-   [ ] Battery/idle test

## Windows MVP

-   [ ] Desktop companion
-   [ ] Start/stop control
-   [ ] Allowlisted app launching
-   [ ] Workspace opening
-   [ ] Approved-folder search
-   [ ] Lock Windows
-   [ ] Local logs and tests

## Device connection

-   [ ] Pairing
-   [ ] Authentication
-   [ ] Encryption
-   [ ] Request expiry and replay protection
-   [ ] Allowlisted remote commands
-   [ ] Timeout/offline handling
-   [ ] Revoke pairing
-   [ ] Security tests

## Feature releases

-   [ ] Morning briefing
-   [ ] Smart work mode
-   [ ] File assistant
-   [ ] Emergency mode
-   [ ] Cross-device clipboard
-   [ ] Business assistant
-   [ ] Fitness assistant
-   [ ] Personal memory
-   [ ] Notification assistant
-   [ ] Smart home integration
-   [ ] Optional AI adapter
-   [ ] Wake-word feasibility test

## Final release

-   [ ] Battery/performance review
-   [ ] Privacy review
-   [ ] Security review
-   [ ] Dependency/license review
-   [ ] Backup/export/delete tests
-   [ ] User guide
-   [ ] Release build

------------------------------------------------------------------------

# 18. Decisions still required before implementation

Do not guess these details. Confirm them during Phase 0:

-   Exact Windows version.
-   Laptop CPU, RAM, and available storage.
-   Whether Android Studio, Android SDK, Git, Python, and JDK are
    already installed.
-   Preferred JARVIS activation language and voice.
-   Whether the first release should support English only or English +
    Hindi + Tamil.
-   Which applications should be included in the first Windows work
    mode.
-   Which folders the file assistant may access.
-   Whether the laptop and phone normally share the same Wi-Fi network.

The assistant should remain useful even if some optional choices are
postponed.

------------------------------------------------------------------------

# 19. Definition of success

JARVIS is successful when:

1.  The phone can accept a user-initiated voice or text command
    reliably.
2.  Basic phone actions work without cloud AI.
3.  The phone can securely command the laptop over the approved network.
4.  The laptop executes only allowlisted actions.
5.  The system handles offline devices and errors safely.
6.  Personal data remains local unless the user explicitly approves a
    transfer.
7.  The phone remains responsive and battery impact is measured and
    acceptable.
8.  Features can be disabled and permissions revoked.
9.  The system does not depend on paid services or API credits.
10. Every release is tested and recoverable.

**Final principle:** Build a dependable assistant, not a constantly
listening science project. Start small, keep it local, measure its
impact, and add intelligence only when the core system is secure and
reliable.
