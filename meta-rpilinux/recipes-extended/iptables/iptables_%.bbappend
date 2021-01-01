FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Replace the iptable.rules variables with the env var values using sed
# NOTE: these env vars must also be in BB_ENV_EXTRAWHITE
do_install_append() {
    if [ -z "${IP_TABLES_XEOMA_SERVER_PORT}" ]; then
        bbfatal "Missing env var IP_TABLES_XEOMA_SERVER_PORT"
    fi
    if [ -z "${IP_TABLES_XEOMA_SERVER_CIDR}" ]; then
        bbfatal "Missing env var IP_TABLES_XEOMA_SERVER_CIDR"
    fi
    if [ -z "${IP_TABLES_XEOMA_HTTPS_CIDR}" ]; then
        bbfatal "Missing env var IP_TABLES_XEOMA_HTTPS_CIDR"
    fi
    if [ -z "${IP_TABLES_ICMP_ALLOW_CIDR}" ]; then
        bbfatal "Missing env var IP_TABLES_ICMP_ALLOW_CIDR"
    fi
    if [ -z "${IP_TABLES_SSH_ALLOW_CIDR}" ]; then
        bbfatal "Missing env var IP_TABLES_SSH_ALLOW_CIDR"
    fi

    # Delimter == '#'
    sed -i "s#IP_TABLES_XEOMA_SERVER_PORT#${IP_TABLES_XEOMA_SERVER_PORT}#" ${D}/${sysconfdir}/iptables/iptables.rules
    sed -i "s#IP_TABLES_XEOMA_SERVER_CIDR#${IP_TABLES_XEOMA_SERVER_CIDR}#" ${D}/${sysconfdir}/iptables/iptables.rules
    sed -i "s#IP_TABLES_XEOMA_HTTPS_CIDR#${IP_TABLES_XEOMA_HTTPS_CIDR}#" ${D}/${sysconfdir}/iptables/iptables.rules
    sed -i "s#IP_TABLES_ICMP_ALLOW_CIDR#${IP_TABLES_ICMP_ALLOW_CIDR}#" ${D}/${sysconfdir}/iptables/iptables.rules
    sed -i "s#IP_TABLES_SSH_ALLOW_CIDR#${IP_TABLES_SSH_ALLOW_CIDR}#" ${D}/${sysconfdir}/iptables/iptables.rules
}
