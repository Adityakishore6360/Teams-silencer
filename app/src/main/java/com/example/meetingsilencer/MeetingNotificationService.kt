package com.example.meetingsilencer

import android.media.AudioManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MeetingNotificationService : NotificationListenerService() {

    private val TAG = "MeetingSilencer"
    private val TEAMS_PACKAGE = "com.microsoft.teams"

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        checkAndApplySilence()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        checkAndApplySilence()
    }

    private fun checkAndApplySilence() {
        val notifications = activeNotifications ?: return
        var shouldSilence = false

        for (sbn in notifications) {
            if (sbn.packageName == TEAMS_PACKAGE) {
                val extras = sbn.notification.extras
                val title = extras.getString("android.title") ?: ""
                val text = extras.getCharSequence("android.text")?.toString() ?: ""

                val isTrigger = title.contains("Ongoing", ignoreCase = true) ||
                                text.contains("Ongoing", ignoreCase = true) ||
                                title.contains("Busy", ignoreCase = true) ||
                                text.contains("Busy", ignoreCase = true) ||
                                title.contains("Do not disturb", ignoreCase = true) ||
                                text.contains("Do not disturb", ignoreCase = true)

                if (isTrigger) {
                    Log.d(TAG, "Trigger detected in Teams notification: $title")
                    shouldSilence = true
                    break
                }
            }
        }

        silencePhone(shouldSilence)
    }

    private fun silencePhone(shouldSilence: Boolean) {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        try {
            val currentMode = audioManager.ringerMode
            if (shouldSilence) {
                if (currentMode != AudioManager.RINGER_MODE_VIBRATE) {
                    Log.d(TAG, "Silencing phone (Vibrate mode)")
                    audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                }
            } else {
                if (currentMode == AudioManager.RINGER_MODE_VIBRATE) {
                    Log.d(TAG, "Restoring ringer (Normal mode)")
                    audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Missing Do Not Disturb permission", e)
        }
    }
}
