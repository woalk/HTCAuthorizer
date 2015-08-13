@echo off
call gradlew build
IF NOT ERRORLEVEL 0 GOTO errorHandling
adb install -r .\app\build\outputs\apk\app-debug.apk
SET Choice=
SET /P Choice=Press enter to reboot device, any other to exit.
IF "%Choice%"=="" GOTO Start
GOTO End
:Start
adb reboot
:End
goto :EOF
:errorHandling
echo Error compiling, press any key to exit.
pause
