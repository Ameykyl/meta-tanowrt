#
# open62541_git.bb
#
# open62541 is an open source and free implementation of OPC UA
# (OPC Unified Architecture) written in the common subset of the
# C99 and C++98 languages
#
SUMMARY = "Open source implementation of OPC UA"
HOMEPAGE = "http://open62541.org/"
LICENSE = "MPL-2.0"

LIC_FILES_CHKSUM = "file://LICENSE;md5=815ca599c9df247a0c7f619bab123dad"

SRC_URI = "git://github.com/open62541/open62541.git;protocol=https;branch=${BRANCH} \
           git://github.com/OPCFoundation/UA-Nodeset.git;protocol=https;branch=v1.04;destsuffix=deps/ua-nodeset;name=ua-nodeset \
           git://github.com/Pro/mdnsd.git;protocol=https;branch=master;destsuffix=deps/mdnsd;name=mdnsd \
           file://0001-examples-client-allow-configurable-server.patch \
           file://0002-examples-don-t-build-some-examples-when-BUILD_SHARED_LIBS-is-ON.patch \
"

BRANCH = "master"
SRCREV = "c09da0b76dbe81657a428ae132b93ccfe940b4b6"

#SRCREV_ua-nodeset = "0777abd1bc407b4dbd79abc515864f8c3ce6812b"
SRCREV_ua-nodeset = "bd41df43a8c48495bc7316227aa2ffbefe336c05"
SRCREV_mdnsd = "4bd993e0fdd06d54c8fd0b8f416cda6a8db18585"

SRCREV_FORMAT = "default"

PV = "1.0.1-dev+git${SRCPV}"
PR = "tano0"

inherit cmake python3native

DEPENDS += "python3-six-native libcheck"

S = "${WORKDIR}/git"

# UA_BUILD_UNIT_TESTS - requires libsubunit
EXTRA_OECMAKE = "\
	-DCMAKE_BUILD_TYPE=RelWithDebInfo \
	-DUA_BUILD_EXAMPLES=ON \
	-DUA_BUILD_UNIT_TESTS=OFF \
"

#
# Disable BUILD_OPTIMIZATION as unittests fail due to "strict-overflow"
#   BUILD_OPTIMIZATION = ""
#
# Debug build
#   DEBUG_BUILD = "1"
#   EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Debug"
#

DEBUG_BUILD = "0"
EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Release"

PACKAGECONFIG[sharedlibs] = "-DBUILD_SHARED_LIBS=1,-DBUILD_SHARED_LIBS=0,,"
PACKAGECONFIG[encrypt] = "-DUA_ENABLE_ENCRYPTION=1 -DMBEDTLS_FOLDER_LIBRARY=${STAGING_LIBDIR} -DMBEDTLS_FOLDER_INCLUDE=${STAGING_INCDIR},-DUA_ENABLE_ENCRYPTION=0,mbedtls,"
PACKAGECONFIG[pubsub] = "-DUA_ENABLE_PUBSUB=1,-DUA_ENABLE_PUBSUB=0,,"
PACKAGECONFIG[pubsub_uadp] = "-DUA_ENABLE_PUBSUB_ETH_UADP=1,-DUA_ENABLE_PUBSUB_ETH_UADP=0,,"
PACKAGECONFIG[pubsub_delta_frames] = "-DUA_ENABLE_PUBSUB_DELTAFRAMES=1,-DUA_ENABLE_PUBSUB_DELTAFRAMES=0,,"
PACKAGECONFIG[pubsub_informationmodel] = "-DUA_ENABLE_PUBSUB_INFORMATIONMODEL=1,-DUA_ENABLE_PUBSUB_INFORMATIONMODEL=0,,"
PACKAGECONFIG[pubsub_informationmodel_methods] = "-DUA_ENABLE_PUBSUB_INFORMATIONMODEL_METHODS=1,-DUA_ENABLE_PUBSUB_INFORMATIONMODEL_METHODS=0,,"
PACKAGECONFIG[subscription_events] = "-DUA_ENABLE_SUBSCRIPTIONS_EVENTS=1,-DUA_ENABLE_SUBSCRIPTIONS_EVENTS=0,,"

PACKAGECONFIG[certificate] = "-DUA_BUILD_SELFSIGNED_CERTIFICATE=1,-DUA_BUILD_SELFSIGNED_CERTIFICATE=0,,"

PACKAGECONFIG ?= "pubsub \
                  pubsub_delta_frames \
                  pubsub_informationmodel \
                  pubsub_informationmodel_methods \
                  pubsub_uadp \
                  sharedlibs"

# Install examples and unit tests
do_install_append() {
	chrpath -d ${D}${bindir}/ua_server_ctt.exe
	chrpath -d ${D}${bindir}/ua_client

	# Install examples
	install -d "${D}${datadir}/${BPN}/examples"
	for example in ${B}/bin/examples/*
	do
		install -m 755 "$example" "${D}${datadir}/${BPN}/examples"
		chrpath -d "${D}${datadir}/${BPN}/examples/$(basename $example)"
	done
}

PACKAGES =+ "${PN}-examples"
FILES_${PN}-dev += "${libdir}/cmake/*"
FILES_${PN}-examples += "${datadir}/${BPN}/examples"
RDEPENDS_${PN}-examples += "${PN}"

# Allow staticdev package to be empty incase sharedlibs is switched on
ALLOW_EMPTY_${PN}-staticdev = "1"

BBCLASSEXTEND = "native nativesdk"

INSANE_SKIP_${PN} += "already-stripped"
