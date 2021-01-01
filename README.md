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
* iptable rules and other configs, xeoma-iptables recipe
  - replace `kernel-modules` in `core-image-minimal.bb` with only the needed modules like here: http://git.yoctoproject.org/cgit.cgi/poky/tree/meta/recipes-extended/iptables/iptables_1.4.9.bb?id=f992d6b4348bc2fde4a415bcc10b1a770aa9a0bc
  - use `-m conntrack --ctstate` instead of `-m state --state`
* static ip/networking
* put the `-archivecache` in `/dev/shm` or on the USB3 ssd
* bunch of things todo in https://madaidans-insecurities.github.io/guides/linux-hardening.html
* remove the multimedia/graphics layers/recipes/packages
 - top/etc
* ssl/tls configs
* use `nftables` instead of `iptables`

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

-savepassword
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

## Server

### Build

Setup environment:

```bash
# Used to sed replace variables in the iptables.rules file
export IP_TABLES_XEOMA_SERVER_PORT=12345
export IP_TABLES_XEOMA_SERVER_CIDR=a.b.c.d/e
export IP_TABLES_XEOMA_HTTPS_CIDR=a.b.c.d/e
export IP_TABLES_ICMP_ALLOW_CIDR=a.b.c.d/e
export IP_TABLES_SSH_ALLOW_CIDR=a.b.c.d/e
export BB_ENV_EXTRAWHITE="$BB_ENV_EXTRAWHITE IP_TABLES_XEOMA_SERVER_PORT IP_TABLES_XEOMA_SERVER_CIDR IP_TABLES_XEOMA_HTTPS_CIDR IP_TABLES_ICMP_ALLOW_CIDR IP_TABLES_SSH_ALLOW_CIDR"
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
* Kill `iptables` (if needed)
    ```bash
    systemctl stop iptables
    iptables -F

    iptables-restore /etc/iptables/iptables.rules
    systemctl start iptables
    ```

## Links

* [Linux Hardening](https://madaidans-insecurities.github.io/guides/linux-hardening.html)
* [Xeoma Downloads](https://felenasoft.com/xeoma/en/download/)
* [Xeoma Linux Article](https://felenasoft.com/xeoma/en/articles/linux-video-surveillance/)
* [Xeoma Manual](https://felenasoft.com/xeoma/downloads/xeoma_manual_en.pdf)
