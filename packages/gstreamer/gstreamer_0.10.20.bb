DESCRIPTION = "GStreamer is a multimedia framework for encoding and decoding video and sound. \
It supports a wide range of formats including mp3, ogg, avi, mpeg and quicktime."
SECTION = "multimedia"
PRIORITY = "optional"
LICENSE = "LGPL"
HOMEPAGE = "http://www.gstreamer.net/"
PR = "r1${TTIF_PR}"
DEPENDS = "glib-2.0 gettext-native libxml2 bison-native flex-native ${TTIF_DEPENDS}"

inherit autotools pkgconfig

SRC_URI = "http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.bz2 \
           file://gst-inspect-check-error.patch;patch=1 \
           file://po-makefile-fix.patch;patch=1 \
           ${@base_contains("DISTRO_FEATURES", "ttif", "file://ttif.patch;patch=1", "", d)} \
          "
EXTRA_OECONF = "--disable-docs-build --disable-dependency-tracking --with-check=no --disable-examples --disable-tests --disable-valgrind --disable-debug"

do_configure_prepend() {
	# This m4 file contains nastiness which conflicts with libtool 2.2.2
	rm -f ${S}/common/m4/lib-link.m4 || true
}

#do_compile_prepend () {
#	mv ${WORKDIR}/gstregistrybinary.[ch] ${S}/gst/
#}

PARALLEL_MAKE = ""

do_stage() {
	autotools_stage_all
}

FILES_${PN} += " ${libdir}/gstreamer-0.10/*.so"
FILES_${PN}-dev += " ${libdir}/gstreamer-0.10/*.la ${libdir}/gstreamer-0.10/*.a"
FILES_${PN}-dbg += " ${libdir}/gstreamer-0.10/.debug/"
