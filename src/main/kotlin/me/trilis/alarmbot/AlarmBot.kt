package me.trilis.alarmbot

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*

class AlarmBot : TelegramLongPollingBot() {

    private val token: String
    private val username: String

    init {
        val stream = javaClass.classLoader.getResourceAsStream("config.properties")
        val properties = Properties()
        properties.load(stream)
        token = properties.getProperty("token")
        username = properties.getProperty("username")
    }


    override fun getBotUsername() = username

    override fun getBotToken() = token

    override fun onUpdateReceived(update: Update?) {
        if (update != null && update.hasMessage() && update.message.hasText()) {
            val duration = update.message.text.toLongOrNull()
            if (duration != null) {
                sendMessage(update, "Bot will send you a message after $duration ${plural(duration)}")
                val thread = Thread(
                    Runnable {
                        Thread.sleep(duration * 1000)
                        sendMessage(update, "$duration ${plural(duration)} are over!")
                    })
                thread.start()
            } else {
                sendMessage(update, "Please enter the number of seconds to wait")
            }
        }
    }

    private fun plural(duration: Long?) = if (duration != 1L) "seconds" else "second"

    private fun sendMessage(update: Update, text: String) {
        execute(SendMessage().setChatId(update.message.chatId).setText(text))
    }

}