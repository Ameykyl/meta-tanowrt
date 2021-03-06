#
PR_append = ".tano0"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/patches:${THISDIR}/${PN}/files:"

inherit openwrt-services

PACKAGECONFIG ??= ""
PACKAGECONFIG[rngtest] = ",,"

SRC_URI += "\
	file://rngd.init \
	file://rngd.config \
"

do_install_append() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/rngd.init ${D}${sysconfdir}/init.d/rngd

	install -d ${D}${sysconfdir}/config
	install -m 0644 ${WORKDIR}/rngd.config ${D}${sysconfdir}/config/rngd

	if [ "${@bb.utils.contains('PACKAGECONFIG', 'rngtest', '1', '0', d)}" = "0" ]; then
		rm -rf ${D}${bindir}
	fi
}

FILES_${PN} += "${bindir}"
CONFFILES_${PN} = "${sysconfdir}/config/rngd"

OPENWRT_SERVICE_PACKAGES = "rng-tools"
OPENWRT_SERVICE_SCRIPTS_rng-tools += "rngd"
OPENWRT_SERVICE_STATE_rng-tools-rngd ?= "enabled"
