# RPi4 Xeoma Server Image

TODOS
* fill out the readme, links to things
  - https://felenasoft.com/xeoma/downloads/xeoma_beta_linux_arm8.tgz
* make a systemd unit + recipe for the fan controller
    https://download.argon40.com/argon1.sh
    https://www.raspberrypi.org/forums/viewtopic.php?t=266101
    https://github.com/kounch/argonone
       https://github.com/kounch/argonone/blob/feature/RaspberryPi4/argononed.py
    https://github.com/Elrondo46/argonone
* notes about the client
  - https://felenasoft.com/xeoma/en/download/
  - https://felenasoft.com/xeoma/downloads/xeoma_beta_linux64.tgz
  - https://felenasoft.com/xeoma/downloads/xeoma_linux64.tgz
  - linux guide: https://felenasoft.com/xeoma/en/articles/linux-video-surveillance/
  - manual: https://felenasoft.com/xeoma/downloads/xeoma_manual_en.pdf
* update fstab recipe for the USB3 ssd mount
  - add provisioning script for formatting the ssd
* iptable rules and other configs, xeoma-iptables recipe
* static ip/networking
* put the `-archivecache` in `/dev/shm` or on the USB3 ssd
* bunch of things todo in https://madaidans-insecurities.github.io/guides/linux-hardening.html
* remove the multimedia/graphics layers/recipes/packages


opts for systemd unit
```
-serverport <p>
-connectioninfoport <p>
-sslconnection
-webaddr <addr>
-programdir <d>
-archivecache <d>
-startdelay 5
-disableDownloads
-core
-noscan
-noscanptzandaudio
-log
```

## Hardware

TODO - list the other stuff, psu, ssd, etc
* https://www.argon40.com/argon-one-m-2-case-for-raspberry-pi-4.html
* https://www.argon40.com/argon-one-m-2-expansion-board.html
