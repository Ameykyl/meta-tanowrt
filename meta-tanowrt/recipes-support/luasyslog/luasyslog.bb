# This file Copyright (C) 2020 Anton Kikin <a.kikin@tano-systems.com>

PR = "tano0"
PV = "1.0.0-2"

DESCRIPTION = "Addon for lualogging to log to the system log on unix systems"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=39b203681ae87e7b0caaba09e7c05351"
DEPENDS = "lua5.1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/patches:${THISDIR}/${PN}/files:"

inherit openwrt-lua

RDEPENDS_${PN} += "lua5.1 lualogging"

SRC_URI = "git://github.com/LuaDist2/luasyslog.git"
SRCREV = "4b95dd20b15cdc36b748055eedfe23dfed77deae"

SRC_URI += "file://0001-Fix-Makefile.patch"

S = "${WORKDIR}/git"
B = "${S}"

LUADIR = "${libdir}/lua/5.1"

CFLAGS += "-fPIC"
LDFLAGS += "-shared -fPIC"

do_install() {
	install -dm 0755 ${D}${LUADIR}/logging
	install -m 0644 ${S}/syslog.lua ${D}${LUADIR}/logging/
	install -m 0755 ${B}/lsyslog.so ${D}${LUADIR}/
}
