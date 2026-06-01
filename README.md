# Meeting Silencer 🔇

Meeting Silencer is an Android application designed to automatically silence your phone whenever you are in a Microsoft Teams meeting or have your status set to "Busy" or "Do Not Disturb". It ensures you are never interrupted by a loud ringer during important calls.

## 🚀 Features

- **Automated Silencing**: Automatically switches your phone to **Vibrate** mode when a Teams meeting starts.
- **Status Detection**: Detects "Busy" and "Do Not Disturb" statuses from Teams notifications.
- **Auto-Restore**: Automatically restores your phone to **Normal** ringer mode once the meeting ends or your status changes back.
- **Background Service**: Works silently in the background using a `NotificationListenerService`.
- **Easy Setup**: Built-in permission checker to help you grant the necessary Android access.

## 🛠️ How it Works

The app monitors notifications from Microsoft Teams. When it detects an "Ongoing meeting" notification or a status update like "Busy", it uses the Android `AudioManager` to toggle the ringer mode. It requires two main permissions:
1. **Notification Access**: To "see" when Teams starts a meeting.
2. **DND (Modes) Access**: To programmatically change the ringer mode.

## 📋 Prerequisites

- **Android Version**: Android 9.0 (Pie) / API 28 or higher.
- **Microsoft Teams**: Must be installed and logged in on the device.

## ⚙️ Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/MeetingSilencer.git
   ```
2. **Build and Install**: Use Android Studio or the provided `run_app.py` script.
3. **Grant Permissions**:
   - Open the app.
   - Follow the on-screen steps to grant **Notification Access** and **DND (Modes) Access**.
   - If permissions are blocked (Android 13+), go to **Settings > Apps > Meeting Silencer > ⋮ > Allow restricted settings**.

## 💻 Tech Stack

- **Language**: Kotlin
- **Build System**: Gradle (Kotlin DSL)
- **Android Components**: `NotificationListenerService`, `AudioManager`, `AppCompat`.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the [issues page](https://github.com/yourusername/MeetingSilencer/issues).

## 📄 License

This project is licensed under the MIT License.
