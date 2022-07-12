FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://local.conf \
"

FILES_${PN} += " \
    ${sysconfdir}/systemd/timesyncd.conf.d/local.conf \
"

do_install_append() {
    install -d ${D}${sysconfdir}/systemd/timesyncd.conf.d
    install -m 0644 ${WORKDIR}/local.conf ${D}${sysconfdir}/systemd/timesyncd.conf.d
}
