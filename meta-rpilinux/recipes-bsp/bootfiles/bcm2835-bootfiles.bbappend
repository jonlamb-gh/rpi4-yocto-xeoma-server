SUMMARY = "Recipe to create bcm2711-bootfiles directory in the image deploy directory that contains overlays and trimmed version of config.txt (also corrects improper naming convention that uses bcm2835, which is an older SoC)."

BCM2711_DIR = "bcm2711-bootfiles"

do_after_deploy() {
    install -d ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}

    for i in ${S}/*.elf ; do
        cp $i ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}
    done
    for i in ${S}/*.dat ; do
        cp $i ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}
    done

    cp -r ${RPIFW_S}/boot/overlays/ ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}/overlays

    # Make a simple config.txt
    touch ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}/config.txt
    echo 'kernel=kernel_rpilinux.img' >> ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}/config.txt
    echo 'arm_64bit=1' >> ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}/config.txt
    echo 'enable_uart=1' >> ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}/config.txt
    echo 'gpu_mem=512' >> ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}/config.txt
    echo 'dtoverlay=disable-bt' >> ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}/config.txt
    echo 'dtoverlay=disable-wifi' >> ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}/config.txt

    # Make a cmdline.txt
    # See https://madaidans-insecurities.github.io/guides/linux-hardening.html
    touch ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}/cmdline.txt
    echo 'dwc_otg.lpm_enable=0 console=serial0,115200 root=/dev/mmcblk0p2 rootfstype=ext4 elevator=deadline fsck.repair=yes rootwait ipv6.disable=1 slab_nomerge slub_debug=FZ init_on_alloc=1 init_on_free=1 page_alloc.shuffle=1 pti=on vsyscall=none debugfs=off oops=panic module.sig_enforce=1 lockdown=confidentiality quiet loglevel=0' > ${DEPLOY_DIR_IMAGE}/${BCM2711_DIR}/cmdline.txt

}

addtask after_deploy before do_build after do_install do_deploy
