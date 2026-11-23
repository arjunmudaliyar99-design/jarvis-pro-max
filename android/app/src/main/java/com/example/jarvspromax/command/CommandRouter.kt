package com.example.jarvspromax.command

/**
 * Deterministic command parsing. Speech or text is never executed directly:
 * it is mapped to one of a fixed set of command IDs, or rejected.
 */
sealed class Command {
    /** Open one allowlisted app. The id is a key from [AppAllowlist], never free text. */
    data class OpenApp(val appId: String) : Command()

    /** Input did not match any allowlisted command. */
    data object Unknown : Command()
}

object AppAllowlist {
    /** Maps a spoken/typed name to a stable app ID. Only these can ever be opened. */
    private val apps: Map<String, String> = mapOf(
        "settings" to "settings",
        "camera" to "camera",
        "dialer" to "dialer",
        "phone" to "dialer",
        "clock" to "clock",
    )

    fun resolve(name: String): String? = apps[name]
}

object CommandRouter {

    private const val MAX_INPUT_LENGTH = 100

    fun parse(input: String): Command {
        val text = input.trim().lowercase()
        if (text.isEmpty() || text.length > MAX_INPUT_LENGTH) return Command.Unknown

        val target = when {
            text.startsWith("open ") -> text.removePrefix("open ").trim()
            text.startsWith("jarvis, open ") -> text.removePrefix("jarvis, open ").trim()
            else -> return Command.Unknown
        }

        val appId = AppAllowlist.resolve(target) ?: return Command.Unknown
        return Command.OpenApp(appId)
    }
}
