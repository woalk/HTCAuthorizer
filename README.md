#HTC Authorizer
##Xposed Module
This [Xposed Module](http://repo.xposed.info/) removes the message

!["Unauthorized device - This app is not compatible with your phone."](http://ext.woalk.de/img/github/htcblinkfeedunauth.png)

from [HTC BlinkFeed/Sense Home](https://play.google.com/store/apps/details?id=com.htc.launcher&hl=en) and other HTC apps.

###Requirements
This app requires Android 5 or 5.1 Lollipop and the **correct** Xposed Framework
for [5.0](http://forum.xda-developers.com/showthread.php?t=3034811)
or [5.1](http://forum.xda-developers.com/showthread.php?t=3072979).

Furthermore, this requires obviously HTC Sense (7) apps.

Also, this Xposed Module **does not** make a really incompatible device compatible.
It just circumvents an occurring problem with HTC's "copy protection" of BlinkFeed's Sense libraries,
where a usually compatible, legal HTC device gets this message without justification.

###Version
The currently supported BlinkFeed/Sense Home version is
**`7.12.563037`**.

The current BlinkFeed/Sense Home plug-ins supported:
- Facebook for HTC Sense
- Twitter for HTC Sense
- Google+ for HTC Sense
- LinkedIn for HTC Sense
- Instagram for HTC Sense
All of them in their currently newest version (`7.00.4xxxxx`/`7.00.5xxxxx`).

Other Sense apps currently supported:
- *none yet. Be patient!*

###Download
You can download this module from either the GitHub release page (see on top of the page -> Releases),
or the official [Xposed Repo page](http://repo.xposed.info/module/com.woalk.apps.xposed.htcblinkfeedauthorizer).
I recommend the last one, it is the best integrated way of installing an Xposed Module.
(Also I can count downloads.)

###Legal
```
(C) 2015 Alexander "Woalk" Köster
         http://woalk.com
         xda-developers user woalk
(C) 2015 GitHub user d8ahazard
         xda-developers user DigitalHigh

Licensed under GNU GPL v2.
A copy of the license can be found in this repository under `LICENSE`.

ABSOLUTELY NO WARRANTY.
This application comes AS-IS.
The USER HIMSELF is RESPONSIBLE FOR ANY DAMAGE to his software, device, ...
Even while it is very unlikely that such a thing happens with this small app.
```
