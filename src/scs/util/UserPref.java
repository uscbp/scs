package scs.util;/* SCCS  %W%---%G%--%U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import scs.util.SCSUtility;

import java.io.*;
import java.util.Properties;
import java.util.Vector;
import java.util.StringTokenizer;
import java.awt.*;


public class UserPref
{
    public static String keymapType; //Word or Emacs keystrokes
    public static String installPath;
    public static Color grid_col;
    public static Color drawBack_col;
    public static Color inPin_col;
    public static Color outPin_col;
    public static Color inPortFill_col;
    public static Color outPortFill_col;
    public static Color connection_col;
    public static Integer connectionWidth;
    public static Color line_col;
    public static Color poly_col;
    public static Color rect_col;
    public static Color oval_col;
    public static Color freeText_col;
    public static String freeTextFontName;
    public static int freeTextSize;
    public static Font freeTextFont;
    public static Color moduleText_col;
    public static String moduleTextFontName;
    public static int moduleTextSize;
    public static Font moduleTextFont;
    public static String moduleTextLocation; //options: ABOVE, BELOW, RIGHT, LEFT of instance icon
    public static Color instanceText_col;
    public static int instanceTextSize;
    public static String instanceTextFontName;
    public static Font instanceTextFont;
    public static String instanceTextLocation; //options: CENTER, ABOVE, BELOW, RIGHT, LEFT of instance icon
    public static Color highlight_col;
    public static String input_port_shape;
    public static String output_port_shape;
    public static int schematicGridSize;
    public static int iconGridSize;
    static int maxRecentlyOpened=5;
    public static Vector<String> recentlyOpened;

    protected static Properties scsprop;

    public static void init()
    {
        String lib_pref = SCSUtility.scs_preferences_path;
        try
        {
            DataInputStream in = new DataInputStream(new FileInputStream(new File(lib_pref)));
            scsprop = new Properties();
            scsprop.load(in);
            in.close();

            keymapType = scsprop.getProperty("scs_keymapType", "Word"); //or Emacs
            grid_col = SCSUtility.returnCol(scsprop.getProperty("grid_col", "red").toUpperCase());
            drawBack_col =  SCSUtility.returnCol(scsprop.getProperty("drawBack_col", "black").toUpperCase());
            inPin_col =  SCSUtility.returnCol(scsprop.getProperty("inPin_col", "white").toUpperCase());
            outPin_col =  SCSUtility.returnCol(scsprop.getProperty("outPin_col", "white").toUpperCase());
            inPortFill_col =  SCSUtility.returnCol(scsprop.getProperty("inPortFill_col", "cyan").toUpperCase());
            outPortFill_col =  SCSUtility.returnCol(scsprop.getProperty("outPortFill_col", "cyan").toUpperCase());
            connection_col =  SCSUtility.returnCol(scsprop.getProperty("connection_col", "green").toUpperCase());
            connectionWidth = Integer.parseInt(scsprop.getProperty("connectionWidth", "2"));
            line_col =  SCSUtility.returnCol(scsprop.getProperty("line_col", "green").toUpperCase());
            poly_col =  SCSUtility.returnCol(scsprop.getProperty("poly_col", "red").toUpperCase());
            rect_col =  SCSUtility.returnCol(scsprop.getProperty("rect_col", "green").toUpperCase());
            oval_col =  SCSUtility.returnCol(scsprop.getProperty("oval_col", "orange").toUpperCase());
            freeText_col =  SCSUtility.returnCol(scsprop.getProperty("freeText_col", "green").toUpperCase());
            freeTextFontName = scsprop.getProperty("freeTextFontName", "Monospaced");
            freeTextSize = Integer.parseInt(scsprop.getProperty("freeTextSize", "10"));
            freeTextFont = new Font(freeTextFontName, Font.BOLD, freeTextSize);
            moduleText_col =  SCSUtility.returnCol(scsprop.getProperty("moduleText_col", "red").toUpperCase());
            moduleTextFontName = scsprop.getProperty("moduleTextFontName", "Monospaced");
            moduleTextSize = Integer.parseInt(scsprop.getProperty("moduleTextSize", "10"));
            moduleTextFont = new Font(moduleTextFontName, Font.BOLD, moduleTextSize);
            moduleTextLocation = scsprop.getProperty("moduleTextLocation", "CENTER"); //options: ABOVE, BELOW, RIGHT, LEFT of instance icon
            instanceText_col =  SCSUtility.returnCol(scsprop.getProperty("instanceText_col", "green").toUpperCase());
            instanceTextSize = Integer.parseInt(scsprop.getProperty("instanceTextSize", "10"));
            instanceTextFontName = scsprop.getProperty("instanceTextFontName", "Monospaced");
            instanceTextFont = new Font(instanceTextFontName, Font.BOLD, instanceTextSize);
            instanceTextLocation = scsprop.getProperty("instanceTextLocation", "BELOW"); //options: CENTER, ABOVE, BELOW, RIGHT, LEFT of instance icon
            highlight_col =  SCSUtility.returnCol(scsprop.getProperty("highlight_col", "red").toUpperCase());
            installPath = scsprop.getProperty("installPath");
            input_port_shape = scsprop.getProperty("input_port_shape", "default").toUpperCase();
            output_port_shape = scsprop.getProperty("output_port_shape", "default").toUpperCase();
            schematicGridSize = Integer.parseInt(scsprop.getProperty("schematicGridSize","8"));
            iconGridSize = Integer.parseInt(scsprop.getProperty("iconGridSize","8"));

            recentlyOpened=new Vector<String>();
            String openedString=scsprop.getProperty("recentlyOpened");
            if(openedString.length()>0)
            {
                StringTokenizer tokenizer=new StringTokenizer(openedString,",");
                while(tokenizer.hasMoreTokens())
                {
                    recentlyOpened.add(tokenizer.nextToken());
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error: UserPref(): FileNotFoundException: " + lib_pref);
        }
        catch (IOException e)
        {
            System.err.println("Error: UserPref(): IOException: " + lib_pref);
        }
    }

    public static void save()
    {
        updateProperties();
        String lib_pref=SCSUtility.scs_preferences_path;
        try
        {
            PrintWriter out=new PrintWriter(new FileOutputStream(new File(lib_pref)));
            scsprop.store(out,"");
            out.flush();
            out.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error: UserPref.save(): FileNotFoundException: " + lib_pref);
        }
        catch (IOException e)
        {
            System.err.println("Error: UserPref.save(): IOException: " + lib_pref);
        }
    }

    protected static void updateProperties()
    {
        scsprop.setProperty("scs_keymapType", keymapType);
        scsprop.setProperty("installPath", installPath);
        scsprop.setProperty("grid_col", SCSUtility.returnColorNameString(grid_col));
        scsprop.setProperty("drawBack_col", SCSUtility.returnColorNameString(drawBack_col));
        //scsprop.setProperty("noActionTaken_col", SCSUtility.returnColorNameString(noActionTaken_col));
        scsprop.setProperty("inPin_col", SCSUtility.returnColorNameString(inPin_col));
        scsprop.setProperty("outPin_col", SCSUtility.returnColorNameString(outPin_col));
        scsprop.setProperty("inPortFill_col", SCSUtility.returnColorNameString(inPortFill_col));
        scsprop.setProperty("outPortFill_col", SCSUtility.returnColorNameString(outPortFill_col));
        scsprop.setProperty("connection_col", SCSUtility.returnColorNameString(connection_col));
        scsprop.setProperty("connectionWidth", (new Integer(connectionWidth)).toString());
        scsprop.setProperty("line_col", SCSUtility.returnColorNameString(line_col));
        scsprop.setProperty("poly_col", SCSUtility.returnColorNameString(poly_col));
        scsprop.setProperty("rect_col", SCSUtility.returnColorNameString(rect_col));
        scsprop.setProperty("oval_col", SCSUtility.returnColorNameString(oval_col));
        scsprop.setProperty("freeText_col", SCSUtility.returnColorNameString(freeText_col));
        scsprop.setProperty("freeTextFontName", freeTextFontName);
        scsprop.setProperty("freeTextSize", (new Integer(freeTextSize)).toString());
        scsprop.setProperty("moduleText_col", SCSUtility.returnColorNameString(moduleText_col));
        scsprop.setProperty("moduleTextFontName", moduleTextFontName);
        scsprop.setProperty("moduleTextSize", (new Integer(moduleTextSize)).toString());
        scsprop.setProperty("moduleTextLocation", moduleTextLocation);
        scsprop.setProperty("instanceText_col", SCSUtility.returnColorNameString(instanceText_col));
        scsprop.setProperty("instanceTextSize", (new Integer(instanceTextSize)).toString());
        scsprop.setProperty("instanceTextFontName", instanceTextFontName);
        scsprop.setProperty("instanceTextLocation", instanceTextLocation);
        scsprop.setProperty("highlight_col", SCSUtility.returnColorNameString(highlight_col));
        scsprop.setProperty("input_port_shape", input_port_shape);
        scsprop.setProperty("output_port_shape", output_port_shape);
        scsprop.setProperty("schematicGridSize", (new Integer(schematicGridSize)).toString());
        scsprop.setProperty("iconGridSize", (new Integer(iconGridSize)).toString());
        String recentStr="";
        for(int i=0; i<recentlyOpened.size(); i++)
        {
            if(i>0)
                recentStr+=",";
            recentStr+=recentlyOpened.get(i);
        }
        scsprop.setProperty("recentlyOpened", recentStr);
    }

    public static void addToRecentlyOpened(String library, String module, String version)
    {
        String modString=library+"."+module+"."+version;
        if(recentlyOpened==null)
            recentlyOpened=new Vector<String>();
        if(recentlyOpened.contains(modString))
            recentlyOpened.remove(modString);
        if(recentlyOpened.size()<maxRecentlyOpened)
            recentlyOpened.add("");
        for(int i=recentlyOpened.size()-1; i>0; i--)
        {
            recentlyOpened.set(i, recentlyOpened.get(i-1));
        }
        recentlyOpened.set(0, modString);
        save();
    }
}
