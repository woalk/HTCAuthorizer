@echo off
gradlew build
adb install -r .\app\build\outputs\apk\app-debug.apk
adb reboot