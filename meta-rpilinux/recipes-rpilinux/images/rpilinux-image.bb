require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += "libstdc++ mtd-utils "
IMAGE_INSTALL += "openssh openssl openssh-sftp-server usbutils "
IMAGE_INSTALL += "libcrypto libssl gpio-utils "
IMAGE_INSTALL += "python3 i2c-tools python3-smbus python3-rpi-gpio "
IMAGE_INSTALL += "ffmpeg procps "
IMAGE_INSTALL += "vim "
IMAGE_INSTALL += "netcat iptables iproute2 net-tools iputils-ping "
IMAGE_INSTALL += "xeoma "
