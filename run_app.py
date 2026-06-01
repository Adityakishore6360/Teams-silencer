import subprocess
import os

def run_command(command):
    print(f"Executing: {command}")
    process = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
    for line in process.stdout:
        print(line, end='')
    process.wait()
    return process.returncode

def find_adb():
    # Try to find ADB in common locations or from environment
    sdk_path = os.environ.get("ANDROID_HOME") or os.environ.get("ANDROID_SDK_ROOT")
    if not sdk_path:
        # Check default Windows path
        default_path = os.path.expandvars(r"%LOCALAPPDATA%\Android\Sdk")
        if os.path.exists(default_path):
            sdk_path = default_path

    if sdk_path:
        adb_path = os.path.join(sdk_path, "platform-tools", "adb.exe" if os.name == 'nt' else "adb")
        if os.path.exists(adb_path):
            return adb_path
    
    return "adb" # Fallback to just 'adb' and hope it's in PATH

def main():
    adb_path = find_adb()
    
    # 1. Build and install the app using Gradle
    print("Building and installing the app...")
    gradle_command = "gradlew.bat :app:installDebug" if os.name == 'nt' else "./gradlew :app:installDebug"
    if run_command(gradle_command) != 0:
        print("Build/Install failed.")
        return

    # 2. Start the main activity
    package_name = "com.example.meetingsilencer"
    activity_name = ".MainActivity"
    print(f"Starting {package_name}...")
    start_command = f'"{adb_path}" shell am start -n {package_name}/{activity_name}'
    if run_command(start_command) != 0:
        print("Failed to start the activity.")
        return

    print("Success! App is running.")

if __name__ == "__main__":
    main()
