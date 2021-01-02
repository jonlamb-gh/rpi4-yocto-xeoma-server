DESCRIPTION = "xeoma"
SECTION = "apps"
HOMEPAGE = "https://felenasoft.com/xeoma/en/"
LICENSE = "CLOSED"

inherit systemd useradd

# Version: 20.12.18
# Set SRC_URI subdir to ${P} so that files are unpacked into ${S}
SRC_URI = "https://felenasoft.com/xeoma/downloads/xeoma_beta_linux_arm8.tgz;subdir=${P}"
SRC_URI[md5sum] = "b390820eeae5981e2f70568cb8dc69b9"
SRC_URI[sha256sum] = "0f55c4cab0ada6b4008d1aa6fde2b79fee2a31a48c7a9e82bd8c8eefc6a55741"

# Comes pre-stripped
INSANE_SKIP_${PN} = "ldflags"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "xeoma.service"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--uid 1200 --home-dir /home/xeoma --system --create-home --shell /bin/false xeoma"

FILES_${PN} = "${bindir}/xeoma /home/xeoma/.config/Xeoma ${systemd_unitdir}/systemd/xeoma.service"

do_install () {
    install -m 0755 -d ${D}${bindir}
    install -m 0755 ${S}/xeoma.app ${D}/${bindir}/xeoma

    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${THISDIR}/systemd/xeoma.service ${D}/${systemd_unitdir}/system

    install -d -m 755 ${D}/home/xeoma/.config/Xeoma
    
    chown -R xeoma ${D}/home/xeoma
    chgrp -R xeoma ${D}/home/xeoma
}
