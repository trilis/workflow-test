package me.trilis.alarmbot

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

class AlarmBot : TelegramLongPollingBot() {
    companion object {
        const val BOT_TOKEN = "YOUR_TOKEN"
        const val BOT_USERNAME = "YOUR_USERNAME"
    }

    override fun getBotToken() = BOT_TOKEN

    override fun getBotUsername() = BOT_USERNAME

    override fun onUpdateReceived(update: Update?) {
        if (update != null && update.hasMessage() && update.message.hasText()) {
            val duration = update.message.text.toLongOrNull()
            if (duration != null) {
                sendMessage(update, "Bot will send you a message after $duration seconds.")
                val thread = Thread(
                    Runnable {
                        Thread.sleep(duration * 1000)
                        sendMessage(update, "$duration seconds are over!")
                    })
                thread.start()
            } else {
                sendMessage(update, "Please enter the number of seconds to wait")
            }
        }
    }

    private fun sendMessage(update: Update, text: String) {
        execute(SendMessage().setChatId(update.message.chatId).setText(text))
    }

}