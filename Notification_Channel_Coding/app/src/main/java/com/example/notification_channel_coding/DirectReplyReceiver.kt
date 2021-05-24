package com.example.notification_channel_coding

import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle


class DirectReplyReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val remoteInput:Bundle = RemoteInput.getResultsFromIntent(intent)

        context?.apply {
            val replyText: CharSequence? = remoteInput.getCharSequence(MainActivity.KEY_TEXT_REPLY)
            val answer: Message? = replyText?.let { Message(it, null) }
            if (answer != null) {
                MainActivity.MESSAGES.add(answer)

                MainActivity.showNotificationReplyNotification(context, resources)
            }
        }
    }
}