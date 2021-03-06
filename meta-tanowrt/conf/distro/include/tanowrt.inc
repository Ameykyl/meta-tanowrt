require conf/distro/include/tanowrt-toolchain.inc
require conf/distro/include/tanowrt-sdk.inc

IMAGE_CLASSES += "openwrt-image-types"

# Virtual runtimes
VIRTUAL-RUNTIME_dev_manager = "udev"
VIRTUAL-RUNTIME_login_manager = "busybox"
VIRTUAL-RUNTIME_init_manager = "procd"
VIRTUAL-RUNTIME_kmod_manager = "ubox"
VIRTUAL-RUNTIME_syslog = "ubox"
VIRTUAL-RUNTIME_base-utils = "busybox"
VIRTUAL-RUNTIME_network_manager = "netifd"

# Start with default set of distro features
DISTRO_FEATURES = "${DISTRO_FEATURES_DEFAULT} ${DISTRO_FEATURES_LIBC}"

# Set procd as init manager
DISTRO_FEATURES_append = " procd "

# Enable cgroups support
DISTRO_FEATURES_append = " cgroup "

# Enable wifi support
DISTRO_FEATURES_append = " wifi "

# Enable watchdog support
DISTRO_FEATURES_append = " watchdog "

# Graphics
DISTRO_FEATURES_append = " graphics screen splash opengl "

# IPv6
DISTRO_FEATURES_append = " ipv6"

# Remove sysvinit and systemd from DISTRO_FEATURES
DISTRO_FEATURES_BACKFILL_CONSIDERED_append = " sysvinit systemd"

# Clean DISTRO_FEATURES
DISTRO_FEATURES_remove = "\
    wayland \
    3g \
    nfc \
    pulseaudio \
    bluetooth \
    irda \
    pcmcia \
    bluez5 \
    largefile \
    sysvinit \
    systemd \
    pam \
    x11 \
"

# Licenses
LICENSE_FLAGS_WHITELIST = "commercial_gst-ffmpeg commercial_gstreamer1.0-libav"

# Image
IMAGE_FSTYPES_remove = "tar.xz.md5"

# Root home path
ROOT_HOME ??= "/root"

# Set preferred Qt version
QT_PROVIDER ?= "qt5"

require conf/distro/include/tanowrt-packageconfig.inc
require conf/distro/include/tanowrt-prefs.inc
require conf/distro/include/tanowrt-repairs.inc
require conf/distro/include/tanowrt-mirrors.inc

# Unbreak multimachine builds
LICENSE_DIRECTORY = "${DEPLOY_DIR}/licenses/${MACHINE_ARCH}"

# Distro maintainer
MAINTAINER = "TanoWrt Developers <https://github.com/tano-systems/meta-tanowrt>"

#
# Dynamic includes
#
include conf/distro/include/tanowrt-hw.inc
