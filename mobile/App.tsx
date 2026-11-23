import React, {useState} from 'react';
import {
  Platform,
  Pressable,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import {parseCommand} from './src/command/commandRouter';

// HUD palette, inspired by a dark holographic interface.
const COLORS = {
  background: '#05080D',
  panel: '#0B1620',
  cyan: '#28E0FF',
  cyanDim: '#1B6E82',
  text: '#BFF6FF',
  muted: '#5C8A99',
};

function App(): React.JSX.Element {
  const [input, setInput] = useState('');
  const [status, setStatus] = useState('Systems online. Type a command.');

  const submit = () => {
    const command = parseCommand(input);
    if (command.kind === 'openApp') {
      // Launching apps arrives in Phase 1 step 3.
      setStatus(`Opening ${command.appId}. (Launch action arrives next step.)`);
    } else {
      setStatus('Command not recognised or not allowed.');
    }
    setInput('');
  };

  const onMicPress = () => {
    // Voice input arrives in Phase 1 step 5.
    setStatus('Voice input arrives later. Use text for now.');
  };

  return (
    <SafeAreaView style={styles.root}>
      <StatusBar barStyle="light-content" backgroundColor={COLORS.background} />
      <View style={styles.container}>
        <Text style={styles.title}>J.A.R.V.I.S.</Text>

        <Pressable
          onPress={onMicPress}
          accessibilityRole="button"
          accessibilityLabel="Start voice command"
          style={({pressed}) => [styles.reactor, pressed && styles.reactorPressed]}>
          <View style={styles.reactorOuter}>
            <View style={styles.reactorInner} />
          </View>
        </Pressable>

        <View style={styles.statusPanel}>
          <Text style={styles.statusText}>{status}</Text>
        </View>

        <View style={styles.inputRow}>
          <TextInput
            value={input}
            onChangeText={setInput}
            onSubmitEditing={submit}
            placeholder="Type a command, e.g. open settings"
            placeholderTextColor={COLORS.muted}
            autoCapitalize="none"
            autoCorrect={false}
            returnKeyType="send"
            style={styles.input}
          />
          <Pressable
            onPress={submit}
            accessibilityRole="button"
            style={({pressed}) => [styles.sendButton, pressed && styles.sendPressed]}>
            <Text style={styles.sendText}>SEND</Text>
          </Pressable>
        </View>
      </View>
    </SafeAreaView>
  );
}

const MONO = Platform.select({ios: 'Menlo', android: 'monospace', default: 'monospace'});

const styles = StyleSheet.create({
  root: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  container: {
    flex: 1,
    padding: 20,
    justifyContent: 'space-between',
  },
  title: {
    color: COLORS.cyan,
    fontFamily: MONO,
    fontSize: 22,
    letterSpacing: 4,
    textAlign: 'center',
  },
  reactor: {
    alignSelf: 'center',
    width: 200,
    height: 200,
    borderRadius: 100,
    borderWidth: 3,
    borderColor: COLORS.cyan,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: COLORS.cyan,
    shadowOpacity: 0.8,
    shadowRadius: 24,
    shadowOffset: {width: 0, height: 0},
    elevation: 12,
  },
  reactorPressed: {
    opacity: 0.8,
  },
  reactorOuter: {
    width: 150,
    height: 150,
    borderRadius: 75,
    borderWidth: 2,
    borderColor: COLORS.cyanDim,
    alignItems: 'center',
    justifyContent: 'center',
  },
  reactorInner: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: COLORS.cyan,
    opacity: 0.85,
  },
  statusPanel: {
    borderWidth: 1,
    borderColor: COLORS.cyanDim,
    backgroundColor: COLORS.panel,
    borderRadius: 8,
    padding: 14,
    minHeight: 60,
    justifyContent: 'center',
  },
  statusText: {
    color: COLORS.text,
    fontFamily: MONO,
    fontSize: 15,
  },
  inputRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  input: {
    flex: 1,
    color: COLORS.text,
    fontFamily: MONO,
    fontSize: 15,
    borderWidth: 1,
    borderColor: COLORS.cyanDim,
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 10,
    backgroundColor: COLORS.panel,
  },
  sendButton: {
    borderWidth: 1,
    borderColor: COLORS.cyan,
    borderRadius: 8,
    paddingHorizontal: 16,
    paddingVertical: 12,
  },
  sendPressed: {
    backgroundColor: COLORS.cyanDim,
  },
  sendText: {
    color: COLORS.cyan,
    fontFamily: MONO,
    fontWeight: '700',
    letterSpacing: 2,
  },
});

export default App;
