package io.github.devalphagot.springfield

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin

val Any.main: Main
    get() = Main.main

class Main: JavaPlugin() {
    companion object {
        lateinit var main: Main
    }

    lateinit var mm: MiniMessage

    override fun onEnable() {
        Main.main = this
        mm = MiniMessage.miniMessage()

        saveDefaultConfig()
    }

    override fun onDisable() {
        logger.info("Goodbye!")
    }
}