package scs;
/**
 * NslDebugManager - Provides additional output functionality for debugging Nsl circuits in SCS.
 *	The idea is to provide the ability to view any state of a NSL simulation
 *  in the context of the SCS schematic view (as opposed to the NSL post-simulation result
 *  view). Helps the user to quickly pinpoint erroneous data in the schematic context to 
 *  allow for faster fixes. 
 * @author Matt Mehne - mmehne@gmail.com
 */

import java.util.Enumeration;
import java.util.Vector;

import scs.ui.*;
import nslj.src.system.*;
import nslj.src.lang.*;
import nslj.src.main.NslMain;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NslDebugManager
{
    private static SchematicPanel schPanel;
    private static JDialog listDialog;
    private static Vector watchList = new Vector();
    private static Vector<NslData> selectedData;
    private static JList varList;

    public static void inputNewWatch(SchematicPanel p)
    {
        schPanel=p;
        SchEditorFrame parentFrame = SchEditorFrame.getSingleton();

        //Vector<String> mvec = new Vector<String>();

        //String temp = "";
        //String buffer1 = "";
        //System.out.println("STEP");
        // NslModule nm = NslHierarchy.nslGetSystem().nslGetModuleRef("out");
        //NslSystem ns = NslMain.system;
        //ns.nslPrintAllVariables();
        NslModule model = NslHierarchy.system.nslGetModelRef();
        NslModule tmod = getModuleRef(parentFrame.getCurrentInstanceName(), parentFrame.currModule.moduleName, model);
        if (tmod == null)
            tmod = model;
        Vector mvec2 = getRecursiveNames(tmod, "");


        varList = new JList(mvec2.toArray());
        listDialog = new JDialog(parentFrame);
        int maxWidth=0;
        for(int i=0; i<mvec2.size(); i++)
        {
            int sizex = listDialog.getFontMetrics(listDialog.getFont()).stringWidth(mvec2.get(i).toString());
            if(sizex>maxWidth)
                maxWidth=sizex;
        }
        int height=listDialog.getFontMetrics(listDialog.getFont()).getHeight()*(mvec2.size()+3);

        Container c = listDialog.getContentPane();
        c.setLayout(new FlowLayout());
        c.setBackground(java.awt.Color.white);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(varList);
        c.add(scrollPane);
        JButton okButton = new JButton("Ok");
        c.add(okButton);
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Object[] vals=varList.getSelectedValues();
                listDialog.setVisible(false);
                selectedData=selectData(vals);
                schPanel.addWatchIndicators(selectedData);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        c.add(cancelButton);
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                listDialog.setVisible(false);
            }
        });

        listDialog.setSize(Math.min(maxWidth+50,300), Math.min(height+75,300));
        varList.setPreferredSize(new Dimension(maxWidth+20,height+75));
        listDialog.setLocation(parentFrame.getX() + parentFrame.getWidth()/2, parentFrame.getY() + parentFrame.getHeight()/3);
        listDialog.setVisible(true);

    }

    public static void step()
    {
        for (int i = 0; i < watchList.size(); i++)
        {
            Object tempObj = watchList.elementAt(i);
            if (!(tempObj instanceof NslData))
            {
                return;
                //watchList.removeElement(tempObj);
            }

            //NslData dat = (NslData) tempObj;

        }
    }

    public static NslModule getModuleRef(String name, String type, NslModule nm)
    {
        Enumeration E = nm.nslGetModuleChildrenVector().elements();
        NslModule child;// = null;
        NslModule tempref;
        if (E.hasMoreElements())
        {
            while (E.hasMoreElements())
            {
                child = (NslModule) E.nextElement();
                if (type.equals(child.getClass().getSimpleName()) && name.equals(child.nslGetName()))
                {
                    return child;
                }
                else if ((tempref = getModuleRef(name, type, child)) != null)
                    return tempref;
            }
        }
        return null;
    }

    public static NslModule getModuleRef(String name, NslModule nm)
    {
        Enumeration E = nm.nslGetModuleChildrenVector().elements();
        NslModule child;// = null;
        NslModule tempref;
        if (E.hasMoreElements())
        {
            while (E.hasMoreElements())
            {
                child = (NslModule) E.nextElement();
                if (name.equals(child.nslGetName()))
                {
                    return child;
                }
                else if ((tempref = getModuleRef(name, child)) != null)
                    return tempref;
            }
        }
        return null;
    }

    private static Vector<NslData> selectData(Object[] fullNames)
    {
        Vector<NslData> data=new Vector<NslData>();
        SchEditorFrame parentFrame = SchEditorFrame.getSingleton();

        for(int j=0; j<fullNames.length; j++)
        {
            NslModule module = NslHierarchy.system.nslGetModelRef();
            NslModule tmod = getModuleRef(parentFrame.getCurrentInstanceName(), module);
            if (tmod != null)
                module = tmod;

            String[] levels = fullNames[j].toString().split("[.]");
            if (levels.length == 0)
                continue;
            for (int i = 0; i < levels.length; i++)
            {
                NslModule temp;
                temp = getModuleRef(levels[i], module, false);

                if (temp == null)
                {
                    break;
                }
                else
                {
                    module = temp;
                }
            }
            Enumeration d = module.nslGetDataVarsVector().elements();

            while (d.hasMoreElements())
            {
                NslData tempdata = (NslData) d.nextElement();
                String tempname = tempdata.nslGetName();
                if (tempname.equals(levels[levels.length - 1]))
                {
                    data.add(tempdata);
                    break;
                }
            }
        }

        return data;
    }

    public static NslModule getModuleRef(String name, NslModule nm, boolean recursive)
    {
        Enumeration E = nm.nslGetModuleChildrenVector().elements();
        NslModule child;// = null;
        NslModule tempref;
        if (E.hasMoreElements())
        {
            while (E.hasMoreElements())
            {
                child = (NslModule) E.nextElement();
                if (name.equals(child.nslGetName()))
                {
                    return child;
                }
                else if (recursive && (tempref = getModuleRef(name, child)) != null)
                    return tempref;
            }
        }
        return null;
    }

    private static Vector getRecursiveNames(NslModule module, String context)
    {
        String dot = "";
        if (context.length() > 0)
        {
            dot = ".";
        }
        //String temp = "";
        Vector<String> vec = new Vector<String>();

        Enumeration moduleList = module.nslGetModuleChildrenVector().elements();
        Enumeration dataList = module.nslGetDataVarsVector().elements();
        //int count = 0;
        while (moduleList.hasMoreElements())
        {
            NslModule submodule = (NslModule) moduleList.nextElement();
            vec.addAll(getRecursiveNames(submodule, context + dot + submodule.nslGetName()));
        }
        while (dataList.hasMoreElements())
        {

            vec.addElement(context + dot + ((NslData) dataList.nextElement()).nslGetName());
        }
        return vec;
    }

} // END OF CLASS - NslDebugManager