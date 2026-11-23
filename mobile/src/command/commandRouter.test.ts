import {parseCommand} from './commandRouter';

describe('parseCommand', () => {
  test('opens an allowlisted app', () => {
    expect(parseCommand('open settings')).toEqual({
      kind: 'openApp',
      appId: 'settings',
    });
  });

  test('accepts the JARVIS prefix and mixed case', () => {
    expect(parseCommand('JARVIS, open Camera')).toEqual({
      kind: 'openApp',
      appId: 'camera',
    });
  });

  test('alias resolves to the same app ID', () => {
    expect(parseCommand('open phone')).toEqual({kind: 'openApp', appId: 'dialer'});
  });

  test('rejects apps not on the allowlist', () => {
    expect(parseCommand('open whatsapp')).toEqual({kind: 'unknown'});
  });

  test('rejects shell-like input', () => {
    expect(parseCommand('open settings; rm -rf /')).toEqual({kind: 'unknown'});
  });

  test('rejects empty and blank input', () => {
    expect(parseCommand('')).toEqual({kind: 'unknown'});
    expect(parseCommand('   ')).toEqual({kind: 'unknown'});
  });

  test('rejects overlong input', () => {
    expect(parseCommand('open ' + 'a'.repeat(200))).toEqual({kind: 'unknown'});
  });

  test('rejects unknown verbs', () => {
    expect(parseCommand('delete settings')).toEqual({kind: 'unknown'});
  });

  test('does not treat prototype keys as apps', () => {
    expect(parseCommand('open constructor')).toEqual({kind: 'unknown'});
  });
});
