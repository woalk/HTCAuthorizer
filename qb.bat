@echo off
call gradlew build
IF NOT ERRORLEVEL 0 GOTO errorHandling
adb install -r .\app\build\outputs\apk\app-debug.apk
timeout /t 5
adb reboot
goto EOF
:errorHandling
echo Error compiling, press any key to exit.
pause