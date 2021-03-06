
PACKAGECONFIG_pn-libwebsockets = "\
	client \
	server \
	ssl \
	libuv \
	${@bb.utils.filter('DISTRO_FEATURES', 'ipv6', d)} \
"

# Prefer OpenSSL over GnuTLS for curl due to TSU exemptions
PACKAGECONFIG_pn-curl = "${@bb.utils.contains('DISTRO_FEATURES', 'ipv6', 'ipv6', '', d)} ssl zlib"

# Enable UI and scripting for perf
PERF_FEATURES_ENABLE ?= "perf-scripting perf-tui"
