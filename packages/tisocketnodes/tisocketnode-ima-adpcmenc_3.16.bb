PRIORITY = "optional"
DESCRIPTION = "Texas Instruments IMA-ADPCM Encoder Socket Node."
LICENSE = "LGPL"
PR = "r0"
DEPENDS = "baseimage \
	   tisocketnode-ringio \
	   tisocketnode-usn \
	   tisocketnode-ima-adpcmenc-codec"

CCASE_SPEC = "%\
	      element /vobs/wtbu/OMAPSW_DSP/audio/node/ima-adpcm/enc/... DSP-MM-TID-AUDIO_RLS_${PV}%\
	      element * /main/LATEST%"

CCASE_PATHFETCH = "/vobs/wtbu/OMAPSW_DSP/audio/node/ima-adpcm/enc"
CCASE_PATHCOMPONENT = "OMAPSW_DSP"
CCASE_PATHCOMPONENTS = "2"

ENV_VAR = "DEPOT=${STAGING_BINDIR}/dspbridge/tools \
           MMCODEC_ROOT=${STAGING_BINDIR}/dspbridge \
	   DSPMAKEROOT=${S}/make \
	   DBS_BRIDGE_DIR_C64=${STAGING_BINDIR}/dspbridge/dsp \
	   DBS_SABIOS_DIR_C64=${STAGING_BINDIR}/dspbridge/tools \
	   DBS_CGTOOLS_DIR_C64=${STAGING_BINDIR}/dspbridge/tools/cgt6x-6.0.7 \
	   DBS_FC=${STAGING_BINDIR}/dspbridge/dsp/bdsptools/framework_components_1_10_04/packages-bld \
	   DLLCREATE_DIR=${STAGING_BINDIR}/DLLcreate \
"

#set to release or debug
RELEASE = "release"

inherit ccasefetch

do_compile() {
## Getting MasterConfig files
        mkdir -p ${S}/include
        cp -a ${STAGING_INCDIR}/dspbridge/include/* ${S}/include
## Getting the dsp make system
        mkdir -p ${S}/make
        cp -a ${STAGING_BINDIR}/dspbridge/make/* ${S}/make
        chmod -R +w ${S}/make
## Getting utils files
        mkdir -p ${S}/system/utils
        cp -a ${STAGING_BINDIR}/dspbridge/system/utils/* ${S}/system/utils
## Getting usn files
        mkdir -p ${S}/system/usn
        cp -a ${STAGING_BINDIR}/dspbridge/system/usn/* ${S}/system/usn
## Getting dasf files
        mkdir -p ${S}/system/dasf
        cp -a ${STAGING_BINDIR}/dspbridge/system/dasf/* ${S}/system/dasf
## Setting PATH for gmake
        pathorig=$PATH
        export PATH=$PATH:${STAGING_BINDIR}/dspbridge/tools/xdctools
        chmod -R +w ${S}/*
	cd ${S}/audio/node/ima-adpcm/enc
	sed -e 's%\\%\/%g' makefile > makefile.linux
	${ENV_VAR} oe_runmake -f makefile.linux build=omap3430${RELEASE}
        export PATH=$pathorig
        unset pathorig
}

do_install() {
	install -d ${D}${libdir}/dsp
	install -m 0644 ${S}/audio/node/ima-adpcm/enc/out/omap3430/${RELEASE}/ima_adpcm_enc_sn.dll64P ${D}${libdir}/dsp
}
