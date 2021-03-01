# WhatsDeleted

[![f-droid](https://img.shields.io/f-droid/v/com.gmail.anubhavdas54.whatsdeleted.svg)](https://f-droid.org/packages/com.gmail.anubhavdas54.whatsdeleted/)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

<p align="center">
  <img src="https://raw.githubusercontent.com/4nubhav/WhatsDeleted/master/app/src/main/res/mipmap-xxhdpi/ic_launcher_round.png" alt="app-icon">
</p>

<p align="center">See WhatsApp messages & media deleted by the sender.</p>
<p align="center">Messages & media are saved to a backup location so that you can access them even if the sender deletes them.</p>

## How it works:

* **Notification Listener** logs new messages.
* **Media Observer** watches for new images.

## How to use:

* Allow storage access.
* Allow notification access (tap on the switch next to **Notification Listener** and follow the instructions).
* **Media Observer** is turned on by default.

## Backup Location

* Images -> External Storage/WhatsDeleted/WhatsDeleted Images/

## Permissions

* Notification Access (to access new messages from notifications)
* Storage

## Download

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="70">](https://f-droid.org/packages/com.gmail.anubhavdas54.whatsdeleted/)

## Points to note

* Messages received **directly in the chat window (and not in the notifications) won't be saved**.
* Images will be saved to the backup directory **only if they were downloaded**.
* Clear the message log & image directory periodically to save storage space.

## Changelog

### v0.4.1

* Added support for [Signal Private Messenger](https://play.google.com/store/apps/details?id=org.thoughtcrime.securesms)
* Added Italian translation thanks to Rocket Simo.
* Added Turkish translation thanks to [anilmavis](https://github.com/anilmavis).

### v0.4

* Added Brazilian Portuguese translation thanks to [Ivan A.](https://github.com/iraamaro).
* Added Spanish translation thanks to [Elaborendum](https://github.com/Elaborendum).

### v0.3

* Newest messages first in the message log viewer.

### v0.2

* Added message log viewer.
* Message log moved to internal storage to prevent unauthorized access.
* Dark theme.
* Added Russian translation thanks to [Andrei Guliaikin](https://github.com/guland2000).

### v0.1

* Initial release
