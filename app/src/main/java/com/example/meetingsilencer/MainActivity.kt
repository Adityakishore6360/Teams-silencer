package com.example.meetingsilencer

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatusNotification: TextView
    private lateinit var tvStatusDND: TextView
    private lateinit var tvStatusService: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatusNotification = findViewById(R.id.tvStatusNotification)
        tvStatusDND = findViewById(R.id.tvStatusDND)
        tvStatusService = findViewById(R.id.tvStatusService)
        val btnPermission = findViewById<Button>(R.id.btnPermission)

        btnPermission.setOnClickListener {
            val notificationManager = getSystemService(NotificationManager::class.java)

            if (!isNotificationServiceEnabled()) {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            } else if (!notificationManager.isNotificationPolicyAccessGranted) {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            } else {
                Toast.makeText(this, "All permissions granted! Ready to silence meetings.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun updateStatus() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val isServiceEnabled = isNotificationServiceEnabled()
        
        if (isServiceEnabled) {
            tvStatusNotification.text = "✓ Notification Access: GRANTED"
            tvStatusNotification.setTextColor(Color.parseColor("#4CAF50"))
            
            // If notification access is on, the listener service is active
            tvStatusService.text = "✓ Background Service: ACTIVE"
            tvStatusService.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            tvStatusNotification.text = "○ Notification Access: PENDING"
            tvStatusNotification.setTextColor(Color.GRAY)
            
            tvStatusService.text = "○ Background Service: INACTIVE"
            tvStatusService.setTextColor(Color.GRAY)
        }

        if (notificationManager.isNotificationPolicyAccessGranted) {
            tvStatusDND.text = "✓ DND (Modes) Access: GRANTED"
            tvStatusDND.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            tvStatusDND.text = "○ DND (Modes) Access: PENDING"
            tvStatusDND.setTextColor(Color.GRAY)
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (!flat.isNullOrEmpty()) {
            val names = flat.split(":")
            for (name in names) {
                val cn = ComponentName.unflattenFromString(name)
                if (cn != null && cn.packageName == pkgName) return true
            }
        }
        return false
    }
}
