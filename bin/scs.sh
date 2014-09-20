export LD_LIBRARY_PATH=$JAVA_HOME/lib:%INSTALL_PATH/lib:${LD_LIBRARY_PATH}
splashOpts="-splash:%INSTALL_PATH/resources/scslogo.png"
case "$1" in
    "-nosplash" )
    splashOpts="";;
esac
$JAVA_HOME/bin/java $splashOpts -Xmx564m -cp %INSTALL_PATH/bin/scs.jar:%INSTALL_PATH/lib/nslj.jar:%INSTALL_PATH/lib/nslc.jar:%INSTALL_PATH/lib/j3dcore.jar:%INSTALL_PATH/lib/j3dutils.jar:%INSTALL_PATH/lib/jacl.jar:%INSTALL_PATH/lib/jmatio.jar:%INSTALL_PATH/lib/jmatlink.jar:%INSTALL_PATH/lib/vecmath.jar:%INSTALL_PATH/lib/epsGraphics.jar:%INSTALL_PATH/lib/odejava.jar:%INSTALL_PATH/lib/odejava-jni.jar:%INSTALL_PATH/lib/openmali.jar:%INSTALL_PATH/lib:$JAVA_HOME/lib/tools.jar:%INSTALL_PATH scs.ui.SchEditorFrame
