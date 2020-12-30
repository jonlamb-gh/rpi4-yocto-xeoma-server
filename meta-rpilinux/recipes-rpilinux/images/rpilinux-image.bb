require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += "libstdc++ mtd-utils"
IMAGE_INSTALL += "openssh openssl openssh-sftp-server usbutils"
IMAGE_INSTALL += "libcrypto libssl gpio-utils"
IMAGE_INSTALL += "python3"
IMAGE_INSTALL += "ffmpeg"
IMAGE_INSTALL += "vim"
IMAGE_INSTALL += "netcat iptables iproute2 net-tools iputils-ping"
