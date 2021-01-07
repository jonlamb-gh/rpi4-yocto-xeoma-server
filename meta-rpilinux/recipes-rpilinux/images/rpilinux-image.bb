require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += " libstdc++"
IMAGE_INSTALL += " openssl libcrypto libssl"
IMAGE_INSTALL += " usbutils mtd-utils"
IMAGE_INSTALL += " tzdata"
IMAGE_INSTALL += " ffmpeg x264"
IMAGE_INSTALL += " vim"
IMAGE_INSTALL += " kernel-modules"
IMAGE_INSTALL += " procps iptables"
IMAGE_INSTALL += " xeoma"

# dev stuff
#IMAGE_INSTALL += " python3 i2c-tools python3-smbus python3-rpi-gpio gpio-utils"
#IMAGE_INSTALL += " netcat iproute2 net-tools iputils-ping"
#IMAGE_INSTALL += " openssh openssh-sftp-server"
