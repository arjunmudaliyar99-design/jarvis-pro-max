/**
 * Deterministic command parsing. Input is never executed directly: it is
 * mapped to a fixed command ID from an allowlist, or rejected.
 */

export type Command =
  | {kind: 'openApp'; appId: string}
  | {kind: 'unknown'};

const APP_ALLOWLIST: Record<string, string> = {
  settings: 'settings',
  camera: 'camera',
  dialer: 'dialer',
  phone: 'dialer',
  clock: 'clock',
};

const MAX_INPUT_LENGTH = 100;

export function parseCommand(input: string): Command {
  const text = input.trim().toLowerCase();
  if (text.length === 0 || text.length > MAX_INPUT_LENGTH) {
    return {kind: 'unknown'};
  }

  let target: string;
  if (text.startsWith('jarvis, open ')) {
    target = text.slice('jarvis, open '.length).trim();
  } else if (text.startsWith('open ')) {
    target = text.slice('open '.length).trim();
  } else {
    return {kind: 'unknown'};
  }

  // Only exact allowlist keys match; anything else (including punctuation
  // such as ";" or shell-like text) is rejected.
  const appId = Object.prototype.hasOwnProperty.call(APP_ALLOWLIST, target)
    ? APP_ALLOWLIST[target]
    : undefined;
  return appId ? {kind: 'openApp', appId} : {kind: 'unknown'};
}
