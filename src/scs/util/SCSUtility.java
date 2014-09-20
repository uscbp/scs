package scs.util;/* SCCS  %W%---%G%--%U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.
/**
 * SCSUtility - A collection of useful utilities - some for graphics some for
 *         file handeling.
 *
 * @author Amanda Alexander
 * @version     %I%, %G%
 *
 * @since JDK1.2
 */

import scs.Module;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.nio.channels.FileChannel;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Node;

/**
 * ---------------------------------------------------------
 */
// static class
public class SCSUtility
{
    public static int gridD2 = 4; //the number of pixels in the grid divided by 2
    public static int grid = 8; //the number of pixels in the grid
    public static int gridT2 = 16; //the number of pixels in the grid time 2
    public static int sifVersionNum = 9; //the version number for this sif format
    public static String nslmVersionNum = "3_0_1"; //the version number for this mod format

    public static String user_home = System.getProperty("user.home");
    public static String scs_library_paths_file = "SCS_LIBRARY_PATHS";
    public static String scs_preferences_file = "SCS_PREFERENCES";

    public static String scs_library_paths_path = (new File(user_home + File.separator + ".scs" + File.separator +
            scs_library_paths_file)).exists() ? user_home + File.separator + ".scs" + File.separator +
            scs_library_paths_file : user_home + File.separator + scs_library_paths_file;
    public static String scs_preferences_path = (new File(user_home + File.separator + ".scs" + File.separator +
            scs_preferences_file)).exists() ? user_home + File.separator + ".scs" + File.separator +
            scs_preferences_file : user_home + File.separator + scs_preferences_file;
    // Suffix applied to the key used in resource file lookups for an image.
    public static final String imageSuffix = "Image";
    // Suffix applied to the key used in resource file lookups for a label.
    public static final String labelSuffix = "Label";
    // Suffix applied to the key used in resource file
    public static final String actionSuffix = "Action";
    // Suffix applied to the key used in resource file lookups for a key shortcut.
    public static final String keyShortcutSuffix = "Key";
    // Suffix applied to the key used in resource file lookups for tooltip text.
    public static final String tipSuffix = "Tooltip";

	//Methods that could be iconized. If searching through this list,
	//remember to add "public void " before the function name!
	public static String[] methods = {
        "initSys",
        "callFromConstructorTop",
        "callFromConstructorBottom",
        "makeConn",
        "initModule",
        "initTrainEpochs",
        "initTrain",
        "simTrain",
        "endTrain",
        "endTrainEpochs",
        "initRunEpochs",
        "initRun",
        "simRun",
        "endRun",
        "endRunEpochs",
        "endModule",
        "endSys"
	};
    
    /**
     *  Return color from name
     * @param colstring - name of color
     * @return Color
     */
    public static Color returnCol(String colstring)
    {
        Color resultCol;

        if (colstring.equals("BLUE"))
        {
            resultCol = Color.blue;
        }
        else if(colstring.equals("RED"))
        {
            resultCol = Color.red;
        }
        else if(colstring.equals("GRAY"))
        {
            resultCol = Color.gray;
        }
        else if(colstring.equals("LIGHTGRAY"))
        {
            resultCol = Color.lightGray;
        }
        else if(colstring.equals("DARKGRAY"))
        {
            resultCol = Color.darkGray;
        }
        else if(colstring.equals("YELLOW"))
        {
            resultCol = Color.yellow;
        }
        else if(colstring.equals("GREEN"))
        {
            resultCol = Color.green;
        }
        else if(colstring.equals("BLACK"))
        {
            resultCol = Color.black;
        }
        else if(colstring.equals("CYAN"))
        {
            resultCol = Color.cyan;
        }
        else if(colstring.equals("MAGENTA"))
        {
            resultCol = Color.magenta;
        }
        else if(colstring.equals("ORANGE"))
        {
            resultCol = Color.orange;
        }
        else if(colstring.equals("PINK"))
        {
            resultCol = Color.pink;
        }
        else if(colstring.equals("WHITE"))
        {
            resultCol = Color.white;
        }
        else
        {
            System.err.println("Error:SCSUtility:returnCol:unknown color " + colstring);
            resultCol = Color.red;
        }

        return resultCol;
    }


    /**
     *  Return the color opposite from the given color
     * @param colstring - name of the color
     * @return Color
     */
    public static Color returnOppositeCol(String colstring)
    {
        Color resultCol;

        if (colstring.equals("BLUE"))
        {
            resultCol = Color.orange;
        }
        else if(colstring.equals("RED"))
        {
            resultCol = Color.green;
        }
        else if(colstring.equals("GRAY"))
        {
            resultCol = Color.black;
        }
        else if(colstring.equals("LIGHTGRAY"))
        {
            resultCol = Color.black;
        }
        else if(colstring.equals("DARKGRAY"))
        {
            resultCol = Color.white;
        }
        else if(colstring.equals("YELLOW"))
        {
            resultCol = Color.magenta;
        }
        else if(colstring.equals("GREEN"))
        {
            resultCol = Color.red;
        }
        else if(colstring.equals("BLACK"))
        {
            resultCol = Color.white;
        }
        else if(colstring.equals("CYAN"))
        {
            resultCol = Color.orange;
        }
        else if(colstring.equals("MAGENTA"))
        {
            resultCol = Color.yellow;
        }
        else if(colstring.equals("ORANGE"))
        {
            resultCol = Color.blue;
        }
        else if(colstring.equals("PINK"))
        {
            resultCol = Color.green;
        }
        else if(colstring.equals("WHITE"))
        {
            resultCol = Color.black;
        }
        else
        {
            System.err.println("Error:SCSUtility:returnOppositeCol:unknown color " + colstring);
            resultCol = Color.red;
        }

        return resultCol;
    }

    /**
     *  Return color name
     * @param p_color - color
     * @return String
     */

    public static String returnColorNameString(Color p_color)
    {
        String result = "CYAN";

        if (p_color.equals(Color.blue))
        {
            result = "BLUE";
        }
        else if (p_color.equals(Color.white))
        {
            result = "WHITE";
        }
        else if (p_color.equals(Color.red))
        {
            result = "RED";
        }
        else if (p_color.equals(Color.gray))
        {
            result = "GRAY";
        }
        else if (p_color.equals(Color.lightGray))
        {
            result = "LIGHTGRAY";
        }
        else if (p_color.equals(Color.darkGray))
        {
            result = "DARKGRAY";
        }
        else if (p_color.equals(Color.yellow))
        {
            result = "YELLOW";
        }
        else if (p_color.equals(Color.green))
        {
            result = "GREEN";
        }
        else if (p_color.equals(Color.black))
        {
            result = "BLACK";
        }
        else if (p_color.equals(Color.cyan))
        {
            result = "CYAN";
        }
        else if (p_color.equals(Color.magenta))
        {
            result = "MAGENTA";
        }
        else if (p_color.equals(Color.orange))
        {
            result = "ORANGE";
        }
        else if (p_color.equals(Color.pink))
        {
            result = "PINK";
        }
        else
        {
            System.err.println("Error:SCSUtility:returnColorNameString:unknown input " + p_color.toString());
        }
        return result;
    }

    //---------------------------------------------------------
    public static void setColorMenu(JComboBox choice)
    {
        choice.addItem("BLACK");
        choice.addItem("WHITE");
        choice.addItem("RED");
        choice.addItem("YELLOW");
        choice.addItem("GREEN");
        choice.addItem("CYAN");
        choice.addItem("DARKGRAY");
        choice.addItem("LIGHTGRAY");
        choice.addItem("GRAY");
        choice.addItem("MAGENTA");
        choice.addItem("ORANGE");
        choice.addItem("PINK");
        choice.addItem("BLUE");
    }

    //---------------------------------------------------------
    public static void setTextSizeMenu(JComboBox choice)
    {
        choice.addItem("10");
        choice.addItem("12");
        choice.addItem("14");
        choice.addItem("16");
        choice.addItem("18");
        choice.addItem("20");
        choice.addItem("22");
    }

    /**
     * Returns the path to the module source directory
     * @param lib - library path
     * @param mod - module name
     * @param version - module version
     * @return Strign
     */
    static public String getFullPathToSrc(String lib, String mod, String version)
    {
        StringBuilder templateWFullPath;
        if ((lib == null) || (mod == null) || (version == null))
        {
            return (null);
        }
        else if (lib.length() == 0 || mod.length() == 0 || version.length() == 0)
        {
            return (null);
        }
        else
        {
            templateWFullPath = new StringBuilder(lib);
            if (!lib.endsWith(File.separator))
            {
                templateWFullPath.append(File.separator);
            }
            templateWFullPath.append(mod);
            if (!mod.endsWith(File.separator))
            {
                templateWFullPath.append(File.separator);
            }
            if (version.charAt(0)!='v')
            {
                templateWFullPath.append('v');
            }
            templateWFullPath.append(version);
            if (!version.endsWith(File.separator))
            {
                templateWFullPath.append(File.separator);
            }
            templateWFullPath.append("src");
            return templateWFullPath.toString();
        }
    }

    /**
     * Returns the path to the module matlab source directory
     * @param lib - path to library
     * @param mod - module name
     * @return String
     */
    static public String getFullPathToMatlab(String lib, String mod)
    {
        StringBuilder templateWFullPath;
        if ((lib == null) || (mod == null))
        {
            return (null);
        }
        else if (lib.length() == 0 || mod.length() == 0)
        {
            return (null);
        }
        else
        {
            templateWFullPath = new StringBuilder(lib);
            if (!lib.endsWith(File.separator))
            {
                templateWFullPath.append(File.separator);
            }
            templateWFullPath.append('@');
            templateWFullPath.append(mod);
            return templateWFullPath.toString();
        }
    }

    /**
     * Returns the path to the module sif file (for compatibility with modules created with older SCS versions)
     * @param path - module source path
     * @param moduleName - module name
     * @return String
     */
    static public String getFullPathToSif(String path, String moduleName)
    {
        if ((path == null) || (moduleName == null))
        {
            return (null);
        }
        else if (path.length()==0 || moduleName.length()==0)
        {
            return (null);
        }
        else
        {
            return path + File.separator + moduleName + ".sif";
        }
    }

    static public String getFullPathToXml(String path, String moduleName)
    {
        if ((path == null) || (moduleName == null))
        {
            return (null);
        }
        else if (path.length()==0 || moduleName.length()==0)
        {
            return (null);
        }
        else
        {
            return path + File.separator + moduleName + ".xml";
        }
    }

    /**
     * Deletes the given file
     * @param filepath - path of file to delete
     * @param parent - parent component
     * @return boolean
     */
    public static boolean deleteFile(String filepath, JFrame parent)
    {
        File tempFile = new File(filepath);
        File trashFile = new File(ClassLoader.getSystemResource("trash").getPath(), new File(filepath).getName());
        if(trashFile.exists())
            recursiveDeleteFile(trashFile);

        if (tempFile.exists() && !tempFile.renameTo(trashFile))
        {
            String errstr="Unable to delete module.. Please check if trash path exists...";
            JOptionPane.showMessageDialog(parent, errstr, "File Delete Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else
            return true;
    }

    public static void recursiveDeleteFile(File f)
    {
        File[] children = f.listFiles();
        if(children!=null)
        {
            for(int i=0; i<children.length; i++)
                recursiveDeleteFile(children[i]);
        }
        f.delete();
    }

    /**
     * Append string to file in a new line
     * @param filepath - path to file to append to
     * @param newLine - new line to append
     * @throws IOException - if there is an error opening the file
     */
    public static void appendToFile(String filepath, String newLine) throws IOException
    {
        FileWriter fstream = new FileWriter(filepath,true);
        PrintWriter out = new PrintWriter(fstream);
        out.println(newLine);
        //Close the output stream
        out.close();
    }
    
    /**
     * readLibraryNickAndPathsFile
     * todo: change to java's Property class
     * @throws IOException - if error reading library path file
     * @return Vector
     */
    static public Vector readLibraryNickAndPathsFile() throws IOException
    {
        boolean done = false;
        String line;
        FileInputStream fis;

        Vector<String> result = new Vector<String>();
        try
        {
            fis = new FileInputStream(scs_library_paths_path);
            BufferedReader in_br = new BufferedReader(new InputStreamReader(fis));
            while (!done)
            {
                line = in_br.readLine();
                if (line == null)
                {
                    done = true;
                }
                else if (line.length()>0)
                { //if blank skip
                    result.addElement(line);
                }
            }
            fis.close();
            return (result);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error:SCSUtility: readLibraryNickAndPathsFile: FileNotFoundException: " + scs_library_paths_path);
            throw new FileNotFoundException("SCSUtility:readLibraryNickAndPathsFile");
        }
        catch (IOException e)
        {
            System.err.println("Error:SCSUtility: readLibraryNickAndPathsFile: IOException " + scs_library_paths_path);
            throw new IOException("SCSUtility:readLibraryNickAndPathsFile");
        }
    }

    /**
     * parseOutLibNickName
     * @param line - line to parse nickname from
     * @return  String
     * todo: change to java's Property class
     */
    public static String parseOutLibNickName(String line)
    {
        int spacenum;
        String resultBuffer;

        // first element in string is nick name
        spacenum = line.indexOf("=");
        if (spacenum <= 0)
        {
            return (null);
        }
        else
        {
            resultBuffer = (line.substring(0, spacenum)).trim();
        }
        return (resultBuffer);
    }

    /**
     * parseOutLibPathName
     * @param line - line to parse path from
     * @return  String
     * todo: change to java's Property class
     */
    public static String parseOutLibPathName(String line)
    {
        //note: please trim the line before calling this proceedure
        // and check that it is not null.
        // second element in string is path
        int spacenum;
        String resultBuffer;

        spacenum = line.indexOf("=");
        if (spacenum <= 0)
        {
            return (null);
        }
        else
        {
            //todo: change to getToken
            spacenum = line.lastIndexOf("=") + 1; // assumes no spaces after path name
            if (spacenum >= line.length())
            {
                return (null);
            }
            else
            {
                resultBuffer = (line.substring(spacenum)).trim();
            }
        }
        return (resultBuffer).replace("/", File.separator);
    }

    /**
     * vectorToPathArray
     * @param nicksAndPaths - vector of nickname and path lines
     * @return  an array of strings
     * todo: change to java's Property class
     */
    public static String[] vectorToPathArray(Vector nicksAndPaths)
    {
        int i, j;
        if (nicksAndPaths == null)
        {
            System.err.println("Error:SCSUtility:vectorToPathArray: vector of nicknames and pathnames are null");
            return (null);
        }
        int sz = nicksAndPaths.size();
        String [] result_buffer = new String[sz];
        String temp;
        i = 0;
        j = 0;
        while (i < sz)
        {
            String line = (String) nicksAndPaths.elementAt(i);
            line = line.trim();
            if (line.length()>0)
            {
                temp = parseOutLibPathName(line);
                if (temp == null || temp.length()==0)
                {
                    return null;
                }
                else
                {
                    result_buffer[j] = temp;
                    j++;
                }
                i++;
            }
        }
        return (result_buffer);
    }

    /**
     * vectorToNickNameArray
     * @param nicksAndPaths - vector of library name and path lines
     * @return - an array of string
     * todo: change to java's Property class
     */
    public static String[] vectorToNickNameArray(Vector nicksAndPaths)
    {
        int i, j;
        String line;

        i = 0;
        j = 0;
        int sz = nicksAndPaths.size();
        String [] result_buffer = new String[sz];
        String temp;

        while (i < sz)
        {
            // first element in string is nick name
            line = ((String) nicksAndPaths.elementAt(i)).trim();
            if (line.length()>0)
            {
                temp = parseOutLibNickName(line);

                if (temp == null || temp.length()==0)
                {
                    return (null);
                }
                else
                {
                    result_buffer[j] = temp;
                    j++;
                }
            }
            i++;
        }
        return (result_buffer);
    }

    /**
     * getLibNickName
     * @param findMeLibPathStr - library path string to find
     * @return the library nick name given a library path name, if not found, return "NOTFOUND" string
     * if error, return "null" string
     * @throws IOException - if error reading ilbrary path file
     * todo: change to java's Property class
     */
    static public String getLibNickName(String findMeLibPathStr) throws IOException
    {
        boolean done = false;
        String line;
        String lib_path;
        String lib_nick;

        try
        {
            // read  the file of library path names
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(scs_library_paths_path)));
            while (!done)
            { // while nick and paths in file
                line = in.readLine(); // throws IOException
                if (line == null)
                {
                    done = true;  //eof
                }
                else
                {
                    line = line.trim();
                    if (line.length()==0)
                    {
                        System.err.println("Warning:SCSUtility:getLibNickName blank line in file " + scs_library_paths_file);
                    }
                    else
                    {
                        lib_path = parseOutLibPathName(line);
                        if (lib_path == null)
                        {
                            System.err.println("Error:SCSUtility:getLibNickName no equal sign in:" + line);
                            return (null);
                        }
                        else if (lib_path.equals(findMeLibPathStr))
                        {
                            lib_nick = parseOutLibNickName(line);
                            if (lib_nick == null)
                            {
                                System.err.println("Error:SCSUtility:getLibNickName no equal sign in:" + line);
                                return (null);
                            }
                            else
                            {
                                return (lib_nick);
                            }
                        }
                    }// if found
                } // if not blank
            } //end while
        }  //end try
        catch (FileNotFoundException e)
        {
            System.err.println("Error:SCSUtility:getLibNickName:FileNotFoundException " + scs_library_paths_path);
            throw (new FileNotFoundException());
        }
        catch (IOException e)
        {
            System.err.println("Error:SCSUtility:IOException:getLibNickName: " + scs_library_paths_path);
            throw (new IOException());
        }
        // if not other error and nick name not found return NOTFOUND string
        return ("NOTFOUND");
    }

    //------------------------------------ 

    /**
     * getLibPathName
     * @param libNickName - libray name
     * @return the library path given a library NickName, if not found, return "NOTFOUND" string
     * if error, return "null" string
     * @throws IOException - if error reading library path file
     * todo: change to java's Property class
     */
    // --------------------------------------------------
    static public String getLibPath(String libNickName) throws IOException
    {
        boolean done = false;
        String line;
        String lib_path;
        String lib_nick;
        try
        {
            // read  the file of library path names
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(scs_library_paths_path)));
            while (!done)
            { // while nick and paths in file
                line = in.readLine(); //throws IO exception
                if (line == null)
                {
                    done = true;  //eof
                }
                else
                {
                    line = line.trim();
                    if (line.length()>0)
                    {
                        lib_nick = parseOutLibNickName(line);
                        if (lib_nick == null)
                        {
                            System.err.println("Error:SCSUtility:getLibPath no equal sign in :" + line);
                            return (null);
                        }
                        else if (lib_nick.equals(libNickName))
                        {
                            lib_path = parseOutLibPathName(line);

                            if (lib_path == null)
                            {
                                System.err.println("Error:SCSUtility:getLibPath no equal sign in :" + line);
                                return (null);
                            }
                            else
                            {
                                return (lib_path);
                            }
                        }// else if
                    } ////if not blank line
                } ////if not null
            } //end while
        }  //end try

        catch (FileNotFoundException e)
        {
            System.err.println("Error:SCSUtility:FileNotFoundException:getLibPath: " + scs_library_paths_path);
            throw (new FileNotFoundException());
        }
        catch (IOException e)
        {
            System.err.println("Error:SCSUtility:IOException:getLibPath: " + scs_library_paths_path);
            throw (new IOException());
        }
        // if not other error and nick name not found return NOTFOUND string
        return ("NOTFOUND");
    }

    /**
     * getLibPathLast 
     * @param libPath String
     * @return String
     */
    static public String getLibPathLast(String libPath)
    {
        String lastPart = "";
        if (libPath == null)
            return null;
        if (libPath.length()==0)
            return null;
        int len = libPath.length(); //remove end slashes
        //tried to use File.seperator but it only took the one direction
        // and sometime the paths use both Unix and MS notation for
        // directory seperation
        // what happend to using dots??
        //boolean found = false;
        while (libPath.length()>0 && (libPath.charAt(libPath.length()-1)=='/' || libPath.charAt(libPath.length()-1)=='\\'))
        {
            libPath = libPath.substring(0, (len - 1));
            len = libPath.length();
        }
        int lastForwardSlash = libPath.lastIndexOf("/");
        int lastBackwardSlash = libPath.lastIndexOf("\\");
        if (lastBackwardSlash > lastForwardSlash)
        {
            lastPart = libPath.substring(lastBackwardSlash + 1, len);
        }
        if (lastForwardSlash >= lastBackwardSlash)
        {
            lastPart = libPath.substring(lastForwardSlash + 1, len);
        }
        return (lastPart);
    }

    /**
     * getSrcPathUsingNick 
     * return the path name string given library NickName, module name and version number 
     * @param libNickName - library name
     * @param modulename - module name
     * @param version - version
     * @throws IOException - if error reading library path file
     * @return String that start with the libpath and ends with "/src".
     */
    public static String getSrcPathUsingNick(String libNickName, String modulename, String version)
            throws IOException
    {
        String lib_path;
        String returnStr;

        try
        {
            lib_path = getLibPath(libNickName);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error:SCSUtility:getSrcPathUsingNick: FileNotFoundException with " + scs_library_paths_path);
            throw (new FileNotFoundException());
        }
        catch (IOException e)
        {
            System.err.println("Error:SCSUtility:getSrcPathUsingNick: IOException with " + scs_library_paths_path);
            throw (new IOException());
        }
        if (lib_path == null)
        {
            System.err.println("Error:SCSUtility:getSrcPathUsingNick:something wrong with " + scs_library_paths_file);
            return (null);
        }
        if (lib_path.equals("NOTFOUND"))
        {
            System.err.println("ERROR:SCSUtility:getSrcPathUsingNick: no match for alias " + libNickName);
            return (null);
        }
        // otherwise use lib_path
        returnStr = getFullPathToSrc(lib_path, modulename, version);
        if (returnStr == null)
        {
            System.err.println("ERROR:SCSUtility:getSrcPathUsingNick: either lib_path, modulename, or version is null");
            return (null);
        }

        return (returnStr);
    }

    /**
     * getMatlabPathUsingNick
     * return the path name string given library NickName and module name
     * @param libNickName - library name
     * @param moduleName - module name
     * @throws IOException - if error reading library path file
     * @return String that start with the libpath and ends with "@"moduleName.
     */
    public static String getMatlabPathUsingNick(String libNickName, String moduleName)
            throws IOException
    {
        String lib_path;
        String returnStr;

        try
        {
            lib_path = getLibPath(libNickName);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error:SCSUtility:getSrcPathUsingNick: FileNotFoundException with " + scs_library_paths_path);
            throw (new FileNotFoundException());
        }
        catch (IOException e)
        {
            System.err.println("Error:SCSUtility:getSrcPathUsingNick: IOException with " + scs_library_paths_path);
            throw (new IOException());
        }
        if (lib_path == null)
        {
            System.err.println("Error:SCSUtility:getSrcPathUsingNick:something wrong with " + scs_library_paths_file);
            return (null);
        }
        if (lib_path.equals("NOTFOUND"))
        {
            System.err.println("ERROR:SCSUtility:getSrcPathUsingNick: no match for alias " + libNickName);
            return (null);
        }
        // otherwise use lib_path
        returnStr = getFullPathToMatlab(lib_path, moduleName);
        if (returnStr == null)
        {
            System.err.println("ERROR:SCSUtility:getSrcPathUsingNick: either lib_path, modulename, or version is null");
            return (null);
        }

        return (returnStr);
    }

    //--------------------------------------------------------------------
    public static void setFirstColumn(GridBagConstraints gbc, int rows)
    {
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 100;
        gbc.weighty = 100;
        gbc.gridx = 0; //first column
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 1;
        gbc.gridheight = rows;
    }

    //--------------------------------------------------------------------
    public static void setSecondColumn(GridBagConstraints gbc, int rows)
    {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 100;
        gbc.weighty = 100;
        gbc.gridx = 1; //second column
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = rows;
    }

    //--------------------------------------------------------------------
    //note: I believe this method is having trouble setting the end of
    //the row.
    public static void setSecondColumn(GridBagConstraints gbc, int rows, int columns)
    {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 100;
        gbc.weighty = 100;
        gbc.gridx = 1;//second column
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = columns;
        gbc.gridheight = rows;
    }

    /**
     * -------------------------------------------------------------
     * addRadioButton
     *
     * @param buttonPanel JPanel
     * @param group       ButtonGroup
     * @param buttonName  String
     * @param selected    boolean - is the button selected
     * @param listener    ActionListener - class in which the actionPerformed methods is found.
     *                    default is that buttons will be centered
     *                    must set the alignment in the calling buttonPanel
     * @return JRadioButton
     */
    public static JRadioButton addRadioButton(JPanel buttonPanel, ButtonGroup group, String buttonName, boolean selected,
                                              ActionListener listener)
    {
        JRadioButton button = new JRadioButton(buttonName, selected);
        button.addActionListener(listener);
        //set the name of the button in the model as well since the ButtonModel
        // is all we can get from the button group.
        (button.getModel()).setActionCommand(buttonName);

        group.add(button);
        buttonPanel.add(button);
        return button;
    }

    public static ImageIcon getImageIcon(String filename)
    {

        return new ImageIcon(ClassLoader.getSystemResource("resources/"+filename));
    }

    public static void copyFolder(File fin, File fout) throws IOException
    {
        fout.mkdirs();
        String[] children = fin.list();
        if (children == null)
        {
            // Either dir does not exist or is not a directory
        }
        else
        {
            for(int p=0;p<children.length;p++)
            {
                File f = new File(fin+"/"+children[p]);
                File f1 = new File(fout+"/"+children[p]);
                if(f.isDirectory())
                    copyFolder(f,f1);
                else
                    copyFile(f,f1);
            }
        }
    }

    public static void copyFile(File source, File target) throws IOException
    {
       FileChannel sourceChannel = new FileInputStream(source).getChannel();
       FileChannel targetChannel = new FileOutputStream(target).getChannel();
       sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
       sourceChannel.close();
       targetChannel.close();
    }

    public static Module openModule(String libraryName, String moduleName, String versionName) throws IOException,
            ClassNotFoundException, ParserConfigurationException, SAXException
    {
        return openModule(getSrcPathUsingNick(libraryName, moduleName, versionName), moduleName);
    }

    public static Module openModule(String srcPath, String moduleName) throws IOException, ClassNotFoundException,
            ParserConfigurationException, SAXException
    {
        Module returnModule = new Module();
        String sifFilePath= getFullPathToSif(srcPath, moduleName);
        String xmlFilePath= getFullPathToXml(srcPath, moduleName);
        File fii = new File(xmlFilePath);
        if(fii.exists())
        {
            FileInputStream fis = new FileInputStream(fii);
            DocumentBuilderFactory domFactory=DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);

            DocumentBuilder builder=domFactory.newDocumentBuilder();
            try
            {
                org.w3c.dom.Document doc=builder.parse(fis);
                returnModule.readXML(doc);   // read the module info
            }
            catch(SAXParseException e)
            {
                System.err.println("Error opening file "+xmlFilePath);
                e.printStackTrace();
            }
            fis.close();
        }
        else
        {
            fii=new File(sifFilePath);
            FileInputStream fis = new FileInputStream(fii);
            ObjectInputStream ois = new ObjectInputStream(fis);
            returnModule.read(ois);   // read the module info
            fis.close();
        }
        return returnModule;
    }

    public static String getNodeValue(org.w3c.dom.Node node)
    {
        for(int i=0; i<node.getChildNodes().getLength(); i++)
        {
            if(node.getChildNodes().item(i).getNodeType()== Node.TEXT_NODE)
                return node.getChildNodes().item(i).getNodeValue();
        }
        return "";
    }

    public static String getXMLFormattedString(String str)
    {
        if(str!=null)
            return str.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
        return "";        
    }
} //end Class SCSUtility.java

