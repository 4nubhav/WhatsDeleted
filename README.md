# WhatsDeleted

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

<p align="center">
  <img src="https://raw.githubusercontent.com/4nubhav/WhatsDeleted/master/app/src/main/res/mipmap-xxhdpi/ic_launcher_round.png" alt="app-icon">
</p>

<p align="center">See WhatsApp messages & media deleted by the sender.</p>
<p align="center">Messages & media are saved to a backup location so that you can access them even if the sender deletes them.</p>

## How it works:

* **Notification Listener** logs new messages to a text file. Open it with a text editor like [Markor](https://f-droid.org/en/packages/net.gsantner.markor/).
* **Media Observer** watches for new images.

## How to use:

* Allow storage access.
* Allow notification access (tap on the switch next to **Notification Listener** and follow the instructions).
* **Media Observer** is turned on by default.

## Backup Location

* Messages -> Internal Storage/WhatsDeleted/msgLog.txt
* Images -> Internal Storage/WhatsDeleted/WhatsDeleted Images/

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
* Clear the message log & image directory periodically **(after saving the important ones)** to save storage space.
