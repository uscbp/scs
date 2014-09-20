@echo off
SET splashOpts=-splash:"$INSTALL_PATH\resources\scslogo.png"
if "%1" == "-nosplash" SET splashOpts=
"%JAVA_HOME%\bin\java" %splashOpts% -Djava.library.path="$INSTALL_PATH\lib" -Xmx564m -cp "$INSTALL_PATH";"$INSTALL_PATH\lib";"$INSTALL_PATH\bin\scs.jar";"$INSTALL_PATH\lib\nslj.jar";"$INSTALL_PATH\lib\nslc.jar";"$INSTALL_PATH\lib\j3dcore.jar";"$INSTALL_PATH\lib\j3dutils.jar";"$INSTALL_PATH\lib\jacl.jar";"$INSTALL_PATH\lib\jmatlink.jar";"$INSTALL_PATH\lib\vecmath.jar";"$INSTALL_PATH\lib\epsGraphics.jar";"$INSTALL_PATH\lib\jmatio.jar";"$INSTALL_PATH\lib\odejava.jar";"$INSTALL_PATH\lib\odejava-jni.jar";"$INSTALL_PATH\lib\openmali.jar";"%JAVA_HOME%\lib\tools.jar" scs.ui.SchEditorFrame
