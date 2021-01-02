# RPi4 Xeoma Server Image

Yocto based [xeoma](https://felenasoft.com/xeoma/en/) server running on a Raspberry Pi 4.

TODOS
* make a systemd unit + recipe for the fan controller
    https://download.argon40.com/argon1.sh
    https://www.raspberrypi.org/forums/viewtopic.php?t=266101
    https://github.com/kounch/argonone
       https://github.com/kounch/argonone/blob/feature/RaspberryPi4/argononed.py
    https://github.com/Elrondo46/argonone
* update fstab recipe for the USB3 ssd mount
  - add provisioning script for formatting the ssd, chmod/chown xeoma user stuff
* replace `kernel-modules` in `core-image-minimal.bb` with only the needed modules like here: http://git.yoctoproject.org/cgit.cgi/poky/tree/meta/recipes-extended/iptables/iptables_1.4.9.bb?id=f992d6b4348bc2fde4a415bcc10b1a770aa9a0bc
* put the `-archivecache` in `/dev/shm` or on the USB3 ssd
* remove the multimedia/graphics/unused layers/recipes/packages
* ssl/tls configs
* module blacklist
* sudo for `me` user

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

* [Raspberry Pi 4](https://www.raspberrypi.org/products/raspberry-pi-4-model-b/)
* [Argon ONE M.2 Case](https://www.argon40.com/argon-one-m-2-case-for-raspberry-pi-4.html)
* [Argon ONE M.2 Expansion Board](https://www.argon40.com/argon-one-m-2-expansion-board.html)
* [5V 3A USB-C Power Supply](https://www.amazon.com/gp/product/B07X8C6PV6/ref=ppx_yo_dt_b_asin_title_o01_s00?ie=UTF8&psc=1)
* [Samsung 32GB EVO Plus Class 10 Micro SDHC](https://www.amazon.com/gp/product/B00WR4IJBE/ref=ppx_yo_dt_b_asin_title_o03_s00?ie=UTF8&psc=1)
* [Kingston A400 240G Internal SSD M.2 2280 SA400M8/240G](https://www.amazon.com/gp/product/B07P22RK1G/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)

## Client

```bash
wget https://felenasoft.com/xeoma/downloads/xeoma_linux64.tgz
tar xf xeoma_linux64.tgz
mv xeoma.app $HOME/bin/xeoma

xeoma -client
```

Client wrapper script:

```bash
#!/usr/bin/env bash
# file: $HOME/bin/xeoma-client
set -e
xeoma -client -noscan -noscanptzandaudio
exit 0
```

## Server

### Config

* Bitbake config in [local.conf](conf/local.conf)
* [securetty](meta-rpilinux/recipes-extended/shadow-securetty/files/securetty) configured to only allow root login from `ttyAMA0` (UART1, GPIO 14/15) in [`shadow-securetty_%.bbappend`](meta-rpilinux/recipes-extended/shadow-securetty/shadow-securetty_%.bbappend)
* [iptables.rules](meta-rpilinux/recipes-extended/iptables/files/iptables.rules) configured at build-time using environment variables in [`iptables_%.bbappend`](meta-rpilinux/recipes-extended/iptables/iptables_%.bbappend)
* [sysctl.conf](meta-rpilinux/recipes-extended/procps/files/sysctl.conf) settings in [`procps_%.bbappend`](meta-rpilinux/recipes-extended/procps/procps_%.bbappend)
* [sshd_config](meta-rpilinux/recipes-extended/openssh/files/sshd_config) and `me` user setup in [`openssh_%.bbappend`](meta-rpilinux/recipes-extended/openssh/openssh_%.bbappend)
  - `sshd_config` only allows user `me` via pki
* Env var `SSH_AUTH_KEYS_ME_USER` gets copied to rootfs `/home/me/.ssh/authorized_keys` in [ssh-user.bb](meta-rpilinux/recipes-ssh-user/ssh-user/ssh-user.bb)
* Image packages in [rpilinux-image.bb](meta-rpilinux/recipes-rpilinux/images/rpilinux-image.bb)
* Xeoma recipe in [xeoma.bb](meta-rpilinux/recipes-xeoma/xeoma/xeoma.bb)
  - Systemd unit in [xeoma.service](meta-rpilinux/recipes-xeoma/xeoma/systemd/xeoma.service)
* TODO bootfiles/etc all the config stuff

### Build

Setup environment:

```bash
# Used to sed replace variables in the iptables.rules file
export IPTABLES_XEOMA_RTSP_UDP_ALLOW_PORT_RANGE=12345:434545
export IPTABLES_XEOMA_RTSP_ALLOW_IP_RANGE=a.b.c.d-a.b.c.e
export IPTABLES_XEOMA_SERVER_ALLOW_PORT_RANGE=12345:434545
export IPTABLES_XEOMA_SERVER_ALLOW_IP_RANGE=a.b.c.d-a.b.c.e
export IPTABLES_XEOMA_HTTPS_ALLOW_IP_RANGE=a.b.c.d-a.b.c.e
export IPTABLES_ICMP_ALLOW_IP_RANGE=a.b.c.d-a.b.c.e
export IPTABLES_SSH_ALLOW_CIDR=a.b.c.d/e
export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE IPTABLES_XEOMA_RTSP_UDP_ALLOW_PORT_RANGE IPTABLES_XEOMA_RTSP_ALLOW_IP_RANGE IPTABLES_XEOMA_SERVER_ALLOW_PORT_RANGE IPTABLES_XEOMA_SERVER_ALLOW_IP_RANGE IPTABLES_XEOMA_HTTPS_ALLOW_IP_RANGE IPTABLES_ICMP_ALLOW_IP_RANGE IPTABLES_SSH_ALLOW_CIDR"

# Used to setup `me` user keys for ssh
expoirt SSH_AUTH_KEYS_ME_USER="/path/to/authorized_keys"
export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE SSH_ME_USER_PUB_KEY_PATH"
```

```bash
./setup

./build
```

### Initial Setup

* Change the `root` password
    ```bash
    passwd
    ```
* Set `xeoma` server admin password
    ```bash
    systemctl stop xeoma
    xeoma -setpassword ...
    systemctl start xeoma
    ```
* Format the USB3 SSD (if needed)
    ```bash
    TODO
    ```
* Kill `iptables` (if needed)
    ```bash
    systemctl stop iptables
    iptables -F
    iptables -P INPUT ACCEPT
    iptables -P OUTPUT ACCEPT
    iptables -P FORWARD ACCEPT

    iptables-restore /etc/iptables/iptables.rules
    systemctl start iptables
    iptables -L -n -v
    ```

## Links

* [Linux Hardening](https://madaidans-insecurities.github.io/guides/linux-hardening.html)
* [Xeoma Downloads](https://felenasoft.com/xeoma/en/download/)
* [Xeoma Linux Article](https://felenasoft.com/xeoma/en/articles/linux-video-surveillance/)
* [Xeoma Manual](https://felenasoft.com/xeoma/downloads/xeoma_manual_en.pdf)
