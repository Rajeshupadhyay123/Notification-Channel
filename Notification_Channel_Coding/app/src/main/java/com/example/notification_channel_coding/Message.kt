package com.example.notification_channel_coding

data class Message(
    val text:CharSequence,
    val sender: CharSequence?
){
     val timestamp:Long=System.currentTimeMillis()
}
