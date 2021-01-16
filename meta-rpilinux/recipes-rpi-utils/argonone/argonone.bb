SUMMARY = "Argon ONE Utilities"
HOMEPAGE = "https://github.com/jonlamb-gh/rpi4-argon-fan-controller"
LICENSE = "MIT"

inherit cargo systemd

SRC_URI = "gitsm://github.com/jonlamb-gh/rpi4-argon-fan-controller.git;protocol=https;branch=master"
SRCREV="56d9885f5b8733bf7a78984a8d9305976248cb35"
S = "${WORKDIR}/git"
LIC_FILES_CHKSUM = "file://LICENSE;md5=da03eaa3ade12bc7503887bf98bfea8b"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "argononed.service"

FILES_${PN} += "${bindir}/argon-fan-ctl ${sysconfdir}/argonone/config.toml ${systemd_unitdir}/systemd/argononed.service"

do_install_append () {
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${THISDIR}/systemd/argononed.service ${D}/${systemd_unitdir}/system

    install -d ${D}/${sysconfdir}/argonone
    install -m 0644 ${THISDIR}/files/config.toml ${D}/${sysconfdir}/argonone
}
