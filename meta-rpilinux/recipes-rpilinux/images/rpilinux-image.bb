require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += "libstdc++ "
IMAGE_INSTALL += "openssh openssl openssh-sftp-server libcrypto libssl "
IMAGE_INSTALL += "usbutils gpio-utils mtd-utils "
IMAGE_INSTALL += "python3 i2c-tools python3-smbus python3-rpi-gpio "
IMAGE_INSTALL += "ffmpeg procps ssh-user"
IMAGE_INSTALL += "vim "
IMAGE_INSTALL += "kernel-modules "
IMAGE_INSTALL += "procps iptables netcat iproute2 net-tools iputils-ping "
IMAGE_INSTALL += "xeoma "
