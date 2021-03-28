# RPi4 Xeoma Server Image

Yocto based [xeoma](https://felenasoft.com/xeoma/en/) server running on a Raspberry Pi 4.

TODOS
* remove the multimedia/graphics/unused layers/recipes/packages
* ssl/tls configs
* change ip tables xeoma server range to just the single ip, doesn't need to be a range
* use a config file with env var for path instead of all the individual vars

other opts for systemd unit
```
-connectioninfoport <p>
-sslconnection
-webaddr <addr>
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
xeoma -noscan -noscanptzandaudio -uselocaltime -client xeoma.home:8897
exit 0
```

## Server

### Config

* Bitbake config in [local.conf](conf/local.conf)
* [securetty](meta-rpilinux/recipes-extended/shadow-securetty/files/securetty) configured to only allow root login from `ttyAMA0` (UART1, GPIO 14/15) in [`shadow-securetty_%.bbappend`](meta-rpilinux/recipes-extended/shadow-securetty/shadow-securetty_%25.bbappend)
* [iptables.rules](meta-rpilinux/recipes-extended/iptables/files/iptables.rules) configured at build-time using environment variables in [`iptables_%.bbappend`](meta-rpilinux/recipes-extended/iptables/iptables_%25.bbappend)
* [sysctl.conf](meta-rpilinux/recipes-extended/procps/files/sysctl.conf) settings in [`procps_%.bbappend`](meta-rpilinux/recipes-extended/procps/procps_%25.bbappend)
* Image packages in [rpilinux-image.bb](meta-rpilinux/recipes-rpilinux/images/rpilinux-image.bb)
* Xeoma recipe in [xeoma.bb](meta-rpilinux/recipes-xeoma/xeoma/xeoma.bb)
  - Systemd unit in [xeoma.service](meta-rpilinux/recipes-xeoma/xeoma/systemd/xeoma.service)
  - Udev rule for the Senselock USB license key in [99-xeoma-usb-key.rules](meta-rpilinux/recipes-xeoma/xeoma/udev/99-xeoma-usb-key.rules)
  - Unit checks existence/permissions of the storage drive `/mnt/xeoma`
  - Depends on `mnt-xeoma.mount`
* Argon ONE M.2 fan controller recipe in [argonone.bb](meta-rpilinux/recipes-rpi-utils/argonone/argonone.bb)
  - A Rust port of the `argononed.py` service in [argon1.sh](https://download.argon40.com/argon1.sh)
  - Source git repo: [rpi4-argon-fan-controller](https://github.com/jonlamb-gh/rpi4-argon-fan-controller)
  - Systemd unit in [argononed.service](meta-rpilinux/recipes-rpi-utils/argonone/systemd/argononed.service)
  - Default [config.toml](meta-rpilinux/recipes-rpi-utils/argonone/files/config.toml)
* Custom `config.txt` and `cmdline.txt` in [bcm2711-bootfiles (`bcm2835-bootfiles.bbappend`)](meta-rpilinux/recipes-bsp/bootfiles/bcm2835-bootfiles.bbappend)
* [sshd_config](meta-rpilinux/recipes-extended/openssh/files/sshd_config) setup in [`openssh_%.bbappend`](meta-rpilinux/recipes-extended/openssh/openssh_%25.bbappend)
  - `sshd_config` only allows user `me` via pki
* `me` user setup in [ssh-user.bb](meta-rpilinux/recipes-ssh-user/ssh-user/ssh-user.bb)
* Env var `SSH_AUTH_KEYS_ME_USER` gets copied to rootfs `/home/me/.ssh/authorized_keys` in [ssh-user.bb](meta-rpilinux/recipes-ssh-user/ssh-user/ssh-user.bb)
* [fstab](meta-rpilinux/recipes-core/base-files/fstab) in [`base-files_3.0.14.bbappend`](meta-rpilinux/recipes-core/base-files/base-files_3.0.14.bbappend)
  - Assumes disk is `/dev/sda`, ext4 partion `/dev/sda1`
  - Mount point `/mnt/xeoma`
  - Cache `-archivecache` in `/mnt/xeoma/cache`
  - Data `-programdir` in `/mnt/xeoma/data`

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
export IPTABLES_ROUTER_IP=a.b.c.d
export IPTABLES_VPN_CIDR=a.b.c.d/e

export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE IPTABLES_XEOMA_RTSP_UDP_ALLOW_PORT_RANGE IPTABLES_XEOMA_RTSP_ALLOW_IP_RANGE IPTABLES_XEOMA_SERVER_ALLOW_PORT_RANGE IPTABLES_XEOMA_SERVER_ALLOW_IP_RANGE IPTABLES_XEOMA_HTTPS_ALLOW_IP_RANGE IPTABLES_ICMP_ALLOW_IP_RANGE IPTABLES_SSH_ALLOW_CIDR IPTABLES_ROUTER_IP IPTABLES_VPN_CIDR"

# Used to setup `me` user keys for ssh
export SSH_AUTH_KEYS_ME_USER="/path/to/authorized_keys"
export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE SSH_AUTH_KEYS_ME_USER"
```

```bash
./setup

./build
```

### Deploy to SD Card

Find the image files:

```bash
bitbake -e rpilinux-image | grep ^DEPLOY_DIR_IMAGE
```

```bash
# dtb
cd /path/to/build/tmp/deploy/images/raspberrypi4-64/
cp bcm2711-rpi-4-b.dtb /media/card/BOOT/

# firmware
cd /path/to/build/tmp/deploy/images/raspberrypi4-64/bcm2711-bootfiles
cp -a ./* /media/card/BOOT/

# kernel
cp Image /media/card/BOOT/kernel_rpilinux.img

# rootfs
cd /media/card/ROOT/
sudo tar -xjf /path/tobuild/tmp/deploy/images/raspberrypi4-64/rpilinux-image-raspberrypi4-64.tar.bz2
```

### Initial Setup

* Change the `root` password, default is `root`
    ```bash
    passwd
    ```
* Setup archive mount permissions
    ```bash
    mkdir -p /mnt/xeoma/data
    chmod 0700 /mnt/xeoma/data

    mkdir -p /mnt/xeoma/cache
    chmod 0700 /mnt/xeoma/cache

    # Could also use 800:800 for running on the build host
    chown -R xeoma:xeoma /mnt/xeoma
    chmod 0700 /mnt/xeoma
    ```
* Set `xeoma` server admin password
    ```bash
    systemctl stop xeoma
    xeoma -programdir /mnt/xeoma/data -setpassword ...
    chown -R xeoma:xeoma /mnt/xeoma/data
    systemctl start xeoma
    ```
* Format the USB3 SSD (if needed)
    ```bash
    TODO ext4 mkfs stuff
    ```
* Temporarily disable `iptables` (if needed)
    ```bash
    systemctl stop iptables
    iptables -F && iptables -P INPUT ACCEPT && iptables -P OUTPUT ACCEPT && iptables -P FORWARD ACCEPT

    iptables-restore /etc/iptables/iptables.rules
    systemctl start iptables
    iptables -L -n -v
    ```
* Check the services
    ```bash
    systemctl status
    ```
* Check time/date/NTP
    ```bash
    timedatectl status
    ```

## Links

* [Linux Hardening](https://madaidans-insecurities.github.io/guides/linux-hardening.html)
* [Xeoma Downloads](https://felenasoft.com/xeoma/en/download/)
* [Xeoma Linux Article](https://felenasoft.com/xeoma/en/articles/linux-video-surveillance/)
* [Xeoma Manual](https://felenasoft.com/xeoma/downloads/xeoma_manual_en.pdf)
