FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://eth0.network \
"

FILES_${PN} += " \
    ${sysconfdir}/systemd/network/eth0.network \
"

do_install_append() {
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/eth0.network ${D}${sysconfdir}/systemd/network
    rm -f ${D}${systemd_unitdir}/network/80-wired.network
}
