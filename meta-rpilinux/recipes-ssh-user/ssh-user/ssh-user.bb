DESCRIPTION = "ssh user account"
SECTION = "apps"
LICENSE = "CLOSED"

inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--uid 1201 --home-dir /home/me --create-home --shell /bin/sh --password '*' me"

USER="me"

FILES_${PN} = "/home/${USER}/.ssh/authorized_keys /home/${USER}/.ssh/id_rsa /home/${USER}/.ssh/id_rsa.pub"

do_install () {
    if [ -z "${SSH_AUTH_KEYS_ME_USER}" ]; then
        bbfatal "Missing env var SSH_AUTH_KEYS_ME_USER"
    fi

    install -d -m 0700 ${D}/home/${USER}/.ssh
    install -m 0600 ${SSH_AUTH_KEYS_ME_USER} ${D}/home/${USER}/.ssh/authorized_keys

    /usr/bin/ssh-keygen -t rsa -f ${D}/home/${USER}/.ssh/id_rsa -q -N ""

    chown -R me ${D}/home/${USER}
    chgrp -R me ${D}/home/${USER}

    chmod 0700 ${D}/home/${USER}
}
