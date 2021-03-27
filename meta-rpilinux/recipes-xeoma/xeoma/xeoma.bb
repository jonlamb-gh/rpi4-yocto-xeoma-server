DESCRIPTION = "xeoma"
SECTION = "apps"
HOMEPAGE = "https://felenasoft.com/xeoma/en/"
LICENSE = "CLOSED"
RDEPENDS_${PN} = "libusb1 hidapi alsa-lib udev"

inherit systemd useradd

# Version: 21.3.4
# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
SRC_URI = "https://felenasoft.com/xeoma/downloads/xeoma_linux_arm8.tgz;subdir=${P}"
SRC_URI[md5sum] = "16f072cda5738609919adab804b87a0d"
SRC_URI[sha256sum] = "8800230f639fe3aa77827f8880175abb7cb197a17c84abaa3736060371e3be5b"

# Comes pre-stripped
INSANE_SKIP_${PN} = "ldflags"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "xeoma.service"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--uid 800 --system --shell /bin/false xeoma"

FILES_${PN} += "${bindir}/xeoma ${systemd_unitdir}/systemd/xeoma.service ${sysconfdir}/udev/rules.d/99-xeoma-usb-key.rules"

REQUIRED_DISTRO_FEATURES = "systemd"

do_install () {
    install -m 0755 -d ${D}${bindir}
    install -m 0755 ${S}/xeoma.app ${D}/${bindir}/xeoma

    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${THISDIR}/systemd/xeoma.service ${D}/${systemd_unitdir}/system

    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${THISDIR}/udev/99-xeoma-usb-key.rules ${D}${sysconfdir}/udev/rules.d/99-xeoma-usb-key.rules
}
