@echo off
call gradlew build
adb install -r .\app\build\outputs\apk\app-debug.apk
adb reboot