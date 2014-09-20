package scs;

import nslc.src.NslCompiler;
import nslj.src.main.NslMain;
import scs.util.SCSUtility;
import scs.ui.SchEditorFrame;

import javax.swing.*;
import java.io.*;
import java.util.Vector;

public class NslJavaInterface
{
    static Vector<String> builtModules;

    public static void build(Module currModule, SchEditorFrame parent)
    {
        builtModules=new Vector<String>(1);
        recursiveBuild(currModule, parent);
    }

    protected static void recursiveBuild(Module currModule, SchEditorFrame parent)
    {
        for (Object variable : currModule.variables)
        {
            Declaration var = (Declaration) variable;
            if (var.varDialogType.equals("SubModule"))
            {
                StringBuilder sb=new StringBuilder(var.modLibNickName).append('.').append(var.varType).append('.');
                if(!var.modVersion.startsWith("v"))
                    sb.append('v');
                String fullName = sb.append(var.modVersion).append('.' ).append(var.varType).toString();
                if (!builtModules.contains(fullName) && !fullName.equals(new StringBuilder(currModule.libNickName).append('.').append(currModule.moduleName).append(".v").append(currModule.versionName).append('.' ).append(currModule.moduleName).toString()))
                {
                    String xmlPath="";
                    try
                    {
                        String srcPath = SCSUtility.getSrcPathUsingNick(var.modLibNickName, var.varType, var.modVersion);
                        String sifPath=SCSUtility.getFullPathToSif(srcPath, var.varType);
                        xmlPath=SCSUtility.getFullPathToXml(srcPath, var.varType);
                        if((new File(xmlPath)).exists() || (new File(sifPath)).exists())
                        {
                            Module child = SCSUtility.openModule(srcPath, var.varType);
                            recursiveBuild(child, parent);
                        }
                    }
                    catch (FileNotFoundException e)
                    {
                        String errstr = "recursiveBuild: FileNotFoundException " + xmlPath;
                        JOptionPane.showMessageDialog(parent, errstr, "NslJavaInterface Error", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (ClassNotFoundException e)
                    {
                        String errstr = "recursiveBuild: ClassNotFoundException " + xmlPath;
                        JOptionPane.showMessageDialog(parent, errstr, "NslJavaInterface Error", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IOException e)
                    {
                        String errstr = "recursiveBuild: IOException " + xmlPath;
                        JOptionPane.showMessageDialog(parent, errstr, "NslJavaInterface Error", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(!builtModules.contains(currModule.libNickName+'.'+currModule.moduleName+'.'+currModule.versionName+'.'+currModule.moduleName))
        {
            try
            {
                String srcPath=SCSUtility.getSrcPathUsingNick(currModule.libNickName, currModule.moduleName, currModule.versionName);
                currModule.save(srcPath,SCSUtility.getFullPathToXml(srcPath, currModule.moduleName));
                buildJavaVersion(currModule.libNickName, currModule.moduleName, currModule.versionName, parent);
                builtModules.add(currModule.libNickName+'.'+currModule.moduleName+'.'+currModule.versionName+'.'+currModule.moduleName);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Builds the Java version of the current module
     * @param currentModuleLibraryName - Name of current module library
     * @param currentModuleName - Name of the current module
     * @param currentModuleVersionName - Current module version
     * @param parent - parent frame 
     */
    protected static void buildJavaVersion(String currentModuleLibraryName, String currentModuleName,
                                        String currentModuleVersionName, SchEditorFrame parent)
    {
        try
        {
            NslCompiler.main(new String[]{"-libraryDir", (new File(SCSUtility.getLibPath(currentModuleLibraryName))).getParent(),
                                          currentModuleLibraryName + '/' + currentModuleName + "/v" +
                                          currentModuleVersionName + "/src/" + currentModuleName});
        }
        catch(Exception e)
        {
            String errstr="Build Java: current module IOException while looking for: " + currentModuleName;
            JOptionPane.showMessageDialog(parent, errstr, "NslJavaInterface Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Runs the Java NSL simulator on the current model
     *
     * @param currentModelLibraryName - Name of the library the current model is in
     * @param currentModelName        - Name of the current model
     * @param currentModelVersionName - Version of the current model
     * @param currentDir              - Directory the current model is located in
     * @param debugLevel              - Debug level to use
     */
    public static void simulateUsingJava(String currentModelLibraryName, String currentModelName,
                                         String currentModelVersionName, String currentDir, int debugLevel)
    {
        String path = currentDir.substring(0, currentDir.lastIndexOf(currentModelLibraryName +
                File.separatorChar + currentModelName));
        String modelFullName = currentModelLibraryName + '.' + currentModelName + ".v" + currentModelVersionName +
                ".src." + currentModelName;
        NslMain.standAlone = false;
        NslMain.main(new String[]{modelFullName, "-classpath", path, "-debug", Integer.toString(debugLevel)});
    }
}
