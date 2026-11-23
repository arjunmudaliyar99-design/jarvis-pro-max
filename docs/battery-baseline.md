# Battery baseline procedure (any Android phone)

Purpose: record how JARVIS-free phone behaves before installing anything, so later releases can be compared against it. Blueprint section 5 requires a baseline before installation and a repeat after each major release.

No cable, computer, or developer settings are needed. Everything uses the phone's own Settings app.

## Before you start
- Record the phone model and Android version: **Settings → About phone**.
- Note the current app version of JARVIS (none yet, so write "none").
- Close recent apps. Disable any battery saver mode and keep it off for the test.
- Keep the same Wi-Fi network and the same brightness for every run.

## Idle test (about 2 hours)
1. Charge the phone to **100%** and unplug it.
2. Turn the screen off and leave the phone face down, at room temperature, not in a case that traps heat.
3. Do not open any apps during the test.
4. After 2 hours, turn the screen on and read the battery percentage from **Settings → Battery**.
5. Record the start and end percentage, the start and end time, and the phone's temperature (feel the back; write "warm/normal/hot" if no thermometer is available).

## Battery usage check
1. **Settings → Battery → Battery usage** (the exact menu name varies by brand; search "battery usage" in Settings if needed).
2. Note the percentage used by "Screen", "Android System", and any app you expect to be active.
3. Take a screenshot for your own records. Do not upload it anywhere.

## Log
Copy one row per run into the table below. Keep every run, including failed ones.

| Date | Phone model | Android version | JARVIS version | Start % | End % | Duration | Temperature | Notes |
|------|-------------|-----------------|----------------|---------|-------|----------|-------------|-------|
| 23-11-2026 | Vivo Y73 (user-reported; screenshot showed Nothing Launcher, to confirm) | not recorded | none | not controlled | ~50 | 24 h history | not recorded | Usage snapshot from Battery usage screen, not an idle test. Screen time today 2 h 49 m. Top consumer: WhatsApp 48% of usage, with 1 h 47 m background. Not a valid baseline. |
|      |             |                 | none           |         |       |          |             |       |

## Pass criteria
Set these after the first baseline run, based on what your phone actually shows. Do not invent a threshold before measuring.
- Idle drain over 2 hours: ______ % (set after baseline)
- Battery usage by JARVIS while idle: must be near zero once JARVIS is installed (set after baseline)
