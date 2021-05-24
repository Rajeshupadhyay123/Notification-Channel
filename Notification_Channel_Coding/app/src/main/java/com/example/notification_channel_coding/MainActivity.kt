package com.example.notification_channel_coding

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import com.example.notification_channel_coding.App.Companion.CHANNEL_1_ID
import com.example.notification_channel_coding.App.Companion.CHANNEL_2_ID
import com.example.notification_channel_coding.App.Companion.putExtraText
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManagerCompat
    lateinit var editTextMessage: EditText
    lateinit var editTextTitle: EditText



//    lateinit var MESSAGES:List<Message>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = NotificationManagerCompat.from(this)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextMessage = findViewById(R.id.edit_text_message)

        MESSAGES.add(Message("Good Morning","Ram"))
        MESSAGES.add(Message("Good Morning","null"))
    }

    fun sendOnChannel1(view: View) {

        val title = editTextTitle.text.toString()
        val message = editTextMessage.text.toString()

        //For adding the tap action that when we click on the notification then it
        // will redirect to the corresponding activity
        //So for this we will be use here Pending Intent
        val activityIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val contentIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0, activityIntent, 0
        )


        val broadcastIntent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra(putExtraText, message)
        val actionIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        //for sending image with notification
        val largeIcon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.movie_icon)


        //Now we need a builder that create a Notification
        val builder = NotificationCompat.Builder(this, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_notifications_active)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(Color.RED)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            //here we set the large icon in the builder
            .setLargeIcon(largeIcon)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(getString(R.string.long_dummy_text))
                    .setBigContentTitle("Big content Tiitle")
                    .setSummaryText("Summery Text")
            )
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(contentIntent)
            // setAutoCancel() automatically removes the notification when the user taps it.
            .setAutoCancel(true)
            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)

        //For show thw notification we have to notify the NotificationManager about the builder
        //Here we need to pass the unique id for the Notification
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }

    }

    fun sendOnChannel2(view: View) {
        val title = editTextTitle.text.toString()
        val message = editTextMessage.text.toString()

        val activityIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val activityPendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0, activityIntent, 0
        )


        //adding the action for broadcast
        val broadcastIntent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra(putExtraText, message)
        val broadcastPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_notifications2)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(Color.BLUE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("This is line 1")
                    .addLine("This is line 2")
                    .addLine("This is line 3")
                    .addLine("This is line 4")
                    .addLine("This is line 5")
                    .addLine("This is line 6")
                    .setBigContentTitle("Big content Title")
                    .setSummaryText("Summery Text")
            )
            .setContentIntent(activityPendingIntent)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
//            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .addAction(R.drawable.ic_baseline_fmd_good_24, "Activity Action", activityPendingIntent)
            .addAction(R.drawable.ic_baseline_fmd_good_24, "Broadcast Action", broadcastPendingIntent
            )

        with(NotificationManagerCompat.from(this)) {
            notify(2, builder.build())
        }

    }

    fun showNotificationReply(view: View){
        showNotificationReplyNotification(this,resources)
    }


    companion object {

        var MESSAGES:MutableList<Message> = ArrayList<Message>()
        val notificationId = 101
        val KEY_TEXT_REPLY = "key_text_reply"

        fun showNotificationReplyNotification(context: Context, resources: Resources) {

            //For adding the tap action that when we click on the notification then it
            // will redirect to the corresponding activity
            //So for this we will be use here Pending Intent
            val activityIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val contentIntent: PendingIntent = PendingIntent.getActivity(context,
                0, activityIntent, 0
            )

            val remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel("Your answer...")
                .build()

            val replyIntent = Intent(context, DirectReplyReceiver::class.java)
            val replyPendingIntent: PendingIntent = PendingIntent.getBroadcast(
                context,
                0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val replyAction: NotificationCompat.Action = NotificationCompat.Action.Builder(
                R.drawable.ic_reply,
                "Reply",
                replyPendingIntent
            ).addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true).build()

            val messagingStyle = NotificationCompat.MessagingStyle("Me")
            messagingStyle.conversationTitle = "Group Chat"

            for (chatMessage: Message in MESSAGES) {
                val notificationMessage = NotificationCompat.MessagingStyle.Message(
                    chatMessage.text,
                    chatMessage.timestamp,
                    chatMessage.sender
                )
                messagingStyle.addMessage(notificationMessage)
            }

            //Now we need a builder that create a Notification
            val builder = NotificationCompat.Builder(context, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_notifications_active)
                .addAction(replyAction)
                .setColor(Color.BLUE)
                .setStyle(messagingStyle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                //here we set the large icon in the builder
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(contentIntent)
                // setAutoCancel() automatically removes the notification when the user taps it.
                .setAutoCancel(true)


            //This is for Android version Pie or greater
            //https://medium.com/google-developer-experts/exploring-android-p-enhanced-notifications-a9adb8d78387
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
                val sender: Person = Person.Builder()
                    .setName("Me")
                    .build()

                val messagingStyleNew=NotificationCompat.MessagingStyle(sender)

                for (chatMessage: Message in MESSAGES) {
                    val notificationMessage = NotificationCompat.MessagingStyle.Message(
                        chatMessage.text,
                        chatMessage.timestamp,
                        chatMessage.sender
                    )
                    messagingStyleNew.addMessage(notificationMessage)
                    messagingStyleNew.setBuilder(builder)
                }
            }

            //For show thw notification we have to notify the NotificationManager about the builder
            //Here we need to pass the unique id for the Notification
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }

            //for more https://www.youtube.com/watch?v=DsFYPTnCbs8&t=985s
        }
    }
}


/**
 * Note:
 *      In the NotificationCompat.Builder we define the priority that we already defin in the
 *      Notification channel creation bez in below of the Android API 26 there are no
 *      concept of the channel creation so uf our app will be run in that case it can also
 *      handle the priority configuration as well..
 **/

/**
 * But below the android oreo we can create an Notification Builder for creating our customized notification
 * and then with the create a manager for handling this notification that are recently created by builder but note
 * here uses the NotificationManagerCompat and then call notify
 */

/**
 * https://medium.com/swlh/android-notifications-1-creating-basic-notifications-861c5a3cf7e8
 * https://medium.com/swlh/android-notifications-2-adding-actions-and-modifying-properties-33db057fcc58
 * https://medium.com/@shashankmohabia/android-notifications-3-expandable-notifications-and-notifications-groups-1cfed308fe3a
 * https://www.techotopia.com/index.php/A_Kotlin_Android_Direct_Reply_Notification_Tutorial
 * https://www.youtube.com/watch?v=DsFYPTnCbs8&t=985s
 */
