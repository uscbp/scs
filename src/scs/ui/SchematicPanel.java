package scs.ui;
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * SchematicPanel - A class representing the panel in the schematic editor which
 * is used to draw schematics.
 *
 * @author Xie, Gupta, Alexander
 * @version     %I%,%G%
 *
 * @param       c               the color of the current paint brush
 * @param       old_x           the variable to keep the old value of the
 *                                               x-coordinate of the mouse
 * @param       old_y           the variable to keep the old value of the
 *                                        x-coordinate of the mouse
 * @param       parentFrame          pointing to the parentFrame--SchEditorFrame
 * @param       grid            the measure of the current grid
 *
 * @since JDK1.1
 */

import scs.*;
import scs.util.UserPref;
import scs.graphics.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.StringTokenizer;
import java.text.DecimalFormat;

import nslj.src.lang.NslData;

public class SchematicPanel extends JPanel implements MouseMotionListener, MouseListener, KeyListener
{

    public static int linewidth = 2;

    private int numCycles = 1; //during debug, number of cycles to run for each step 
    int old_x, old_y;
    SchEditorFrame parentFrame;
    public boolean snapToGrid=true;
    public boolean showGrid=false;
    int preferredWidth=800, preferredHeight=600;

    public static Color currBackgroundCol = UserPref.drawBack_col;

    public static String freeTextString = "DUMMY";
    public static String instanceTextString = "DUMMY";
    public Vector<GraphicPart_text> dataBoxList = new Vector<GraphicPart_text>();
    public Vector<NslData> dataList = new Vector<NslData>();

    /**
     * Constructor of this class with parentFrame set to fm.
     * @param fm - parent SchEditorFrame
     */
    SchematicPanel(SchEditorFrame fm)
    {
        parentFrame = fm;
        setBackground(currBackgroundCol);
        addMouseMotionListener(this);
        addMouseListener(this);

        addKeyListener(this);
    }

    /**
     * Return the preferred size of this schematic panel.
     */
    public Dimension getPreferredSize()
    {
        return new Dimension(preferredWidth, preferredHeight);
    }

    public void setPreferredSize(int w, int h)
    {
        preferredWidth=w;
        preferredHeight=h;
    }

    /**
     * Repaint this schematic panel.
     * @param g - Graphics object to paint to 
     */
    public void paintChildren(Graphics g)
    { //use to be paint
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHints(GraphicsUtil.DEFAULT_RENDERING_HINTS);

        if (parentFrame == null)
        {
            System.err.println("Error:SchematicPanel:paint:null Schematic frame");
            return;
        }
        if (parentFrame.currModule == null)
        {
            return;
        }
        if (parentFrame.currModule.mySchematic == null)
        {
            return;
        }

        if(showGrid)
            drawGrid(g2);

        parentFrame.currModule.mySchematic.paint(g2);

        Component selComponent = parentFrame.currModule.mySchematic.selComponent;

        if (selComponent != null && selComponent instanceof IconInst)
        {
            IconInst iconInst = (IconInst) selComponent;
            if (iconInst.selPort != null && iconInst.selPort instanceof IconInport)
            {
                parentFrame.myStatusPanel.setStatusMessage("The type of this inport: " +
                                                           ((IconInport) iconInst.selPort).Type);
                return;
            }
            if (iconInst.selPort != null && iconInst.selPort instanceof IconOutport)
            {
                parentFrame.myStatusPanel.setStatusMessage("The type of this outport: " +
                                                           ((IconOutport) iconInst.selPort).Type);
                return;
            }
        }
        else if(selComponent instanceof SchematicInport)
        {
            parentFrame.myStatusPanel.setStatusMessage("The type of this inport: " +
                                                       ((SchematicInport) selComponent).Type);
            return;
        }
        else if(selComponent instanceof SchematicOutport)
        {
            parentFrame.myStatusPanel.setStatusMessage("The type of this outport: " +
                                                       ((SchematicOutport) selComponent).Type);
            return;
        }
        else if(selComponent instanceof Connection)
        {
            return;
        }
        parentFrame.myStatusPanel.setStatusMessage("");
    }

    protected void drawGrid(Graphics2D g)
    {
        g.setColor(UserPref.grid_col);
        for(int x=0; x<getWidth(); x+=UserPref.schematicGridSize)
        {
            g.drawLine(x, 0, x, getHeight());
        }
        for(int y=0; y<getHeight(); y+=UserPref.schematicGridSize)
        {
            g.drawLine(0, y, getWidth(), y);
        }
    }
    /**
     * Event handler for keyPressed event.
     */
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_DELETE)
        {
            deleteComp();
        }
        else if(e.getKeyCode() == KeyEvent.VK_ENTER) // push into selected module
        {
            if(parentFrame.currModule.mySchematic.selComponent instanceof IconInst)
                parentFrame.descend();
        }
        else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) // pop out of current module context
        {

            parentFrame.ascend();
        }
        else if(e.getKeyCode() == KeyEvent.VK_N) // PRESSING 'N' ~ next in debug mode
        {
            if(parentFrame.debugtoolbar!=null && parentFrame.debugtoolbar.cycleflag)
            {
                try
                {
                    setNumCycles(Integer.parseInt(parentFrame.debugtoolbar.numCycleField.getText()));
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            debugDoNext();
        }
        else if(e.getKeyCode() == KeyEvent.VK_W) // 'W' key- add watch
        {
            debugAddWatch();
        }
        else if(e.getKeyCode() == KeyEvent.VK_C) // clear all the debug boxes
        {
            debugClearAll();
        }
    }

    /**
     * Event handler for keyReleased event.
     */
    public void keyReleased(KeyEvent e)
    {
    }

    /**
     * Event handler for keyTyped event.
     */
    public void keyTyped(KeyEvent e)
    {
    }

    /**
     * Event handler for mouseMoved event.
     */
    public void mouseMoved(MouseEvent e)
    {
    }

    /**
     * Event handler for mousePressed event.
     */
    public void mousePressed(MouseEvent e)
    {
        requestFocus();
        Module currModule = parentFrame.currModule;
        if (currModule == null)
        {
            return;
        }

        Component tempComponent;
        int x = e.getX();
        int y = e.getY();
        int ix;
        int found = -1;

        if (parentFrame.status.equals("insert_text"))
        {
            currModule.mySchematic.addDrawableObj(new GraphicPart_text(x, y, freeTextString, UserPref.freeTextFont, UserPref.freeText_col, UserPref.freeTextSize));
            parentFrame.status = "nothing";
        }

        //go thru all drawables and find new object selected
        for (ix = currModule.mySchematic.drawableObjs.size() - 1; ix >= 0; ix--)
        {
            tempComponent = currModule.mySchematic.drawableObjs.elementAt(ix);

            // check if iconInst selected
            if (tempComponent instanceof IconInst)
            {
                IconInst iconInst = (IconInst) tempComponent;

                //go thru drawables to see if pin on icon selected
                if (iconInst.selectport(x, y))
                {
                    if (iconInst.selPort instanceof IconOutport)
                    {
                        if (parentFrame.status.equals("Connection"))
                        {
                            //add new connection
                            Connection conn = new Connection(iconInst, (IconOutport) iconInst.selPort,
                                                             UserPref.connection_col);
                            currModule.mySchematic.drawableObjs.addElement(conn);
                            parentFrame.status = "";
                            currModule.mySchematic.selComponent = conn;
                            parentFrame.setCutEnabled(true);
                            parentFrame.setCopyEnabled(true);
                            parentFrame.setDescendEnabled(false);
                            found = currModule.mySchematic.drawableObjs.size() - 1;
                            break;
                        }
                        //this is a IconOutport but not in connection mode
                        else
                        {
                            currModule.mySchematic.selComponent = tempComponent;
                            parentFrame.setCutEnabled(true);
                            parentFrame.setCopyEnabled(true);
                            parentFrame.setDescendEnabled(false);
                            found = ix;
                            break;
                        }
                    }
                    // if inport selected give warning
                    if (iconInst.selPort instanceof IconInport)
                    {
                        if (parentFrame.status.equals("Connection"))
                        {
                            String errstr = "SchematicPanel:Cannot select inport for start of a connection. \b"; //rings bell
                            JOptionPane.showMessageDialog(this, errstr, "SchematicPanel Warning", JOptionPane.WARNING_MESSAGE);

                            parentFrame.myStatusPanel.setWarningMessage("Warning:" + errstr);
                            currModule.mySchematic.selComponent = null;
                            parentFrame.setCutEnabled(false);
                            parentFrame.setCopyEnabled(false);
                            parentFrame.setDescendEnabled(false);
                            found = -1;
                            break;
                        }
                        else
                        {
                            currModule.mySchematic.selComponent = tempComponent;
                            parentFrame.setCutEnabled(true);
                            parentFrame.setCopyEnabled(true);
                            parentFrame.setDescendEnabled(false);
                            found = ix;
                            break;
                        }
                    }
                    //should not get here
                    System.out.println("Debug:SchematicPanel:should not get here.");
                    currModule.mySchematic.selComponent = tempComponent;
                    parentFrame.setCutEnabled(true);
                    parentFrame.setCopyEnabled(true);
                    found = ix;
                    break;
                } //end of - a pin has been selected

                //is icon selected
                if (iconInst.selectobj(x, y))
                {
                    currModule.mySchematic.selComponent = tempComponent;
                    parentFrame.setCutEnabled(true);
                    parentFrame.setCopyEnabled(true);
                    parentFrame.setDescendEnabled(true);
                    found = ix;
                    break;
                }
            } //end if instanceof IconInst

            // check if iconMethod selected
            if (tempComponent instanceof IconMethod)
            {
                IconMethod iconInst = (IconMethod) tempComponent;

                //go thru drawables to see if pin on icon selected
                    //is icon selected
                    if (iconInst.selectobj(x, y))
                    {
                        currModule.mySchematic.selComponent = tempComponent;
                        parentFrame.setCutEnabled(true);
                        parentFrame.setCopyEnabled(true);
                        parentFrame.setDescendEnabled(true);
                        found = ix;
                        break;
                    }
            } //end if instanceof IconInst

            
            // check if SchematicInport selected
            if (tempComponent instanceof SchematicInport)
            {
                SchematicInport sip = (SchematicInport) tempComponent;
                if (sip.selectport(x, y))
                {
                    if (parentFrame.status.equals("Connection"))
                    {
                        //add new connection
                        Connection conn;
                        conn = new Connection(sip, UserPref.connection_col);
                        currModule.mySchematic.drawableObjs.addElement(conn);
                        parentFrame.status = "";
                        currModule.mySchematic.selComponent = conn;
                        parentFrame.setCutEnabled(true);
                        parentFrame.setCopyEnabled(true);
                        found = currModule.mySchematic.drawableObjs.size() - 1;
                        parentFrame.setDescendEnabled(false);
                        break;
                    }
                    currModule.mySchematic.selComponent = tempComponent;
                    parentFrame.setCutEnabled(true);
                    parentFrame.setCopyEnabled(true);
                    parentFrame.setDescendEnabled(false);
                    found = ix;
                    break;
                }

                if (sip.selectobj(x, y))
                {
                    currModule.mySchematic.selComponent = tempComponent;
                    parentFrame.setCutEnabled(true);
                    parentFrame.setCopyEnabled(true);
                    parentFrame.setDescendEnabled(false);
                    found = ix;
                    break;
                }
            }

            // check if sch outport selected
            if (tempComponent instanceof SchematicOutport)
            {
                SchematicOutport sop = (SchematicOutport) tempComponent;
                //is the pin on the SchematicOutport selected?
                if (sop.selectport(x, y))
                {
                    currModule.mySchematic.selComponent = tempComponent;
                    parentFrame.setCutEnabled(true);
                    parentFrame.setCopyEnabled(true);
                    parentFrame.setDescendEnabled(false);
                    found = ix;
                    break;
                }
                if (sop.selectobj(x, y))
                {
                    currModule.mySchematic.selComponent = tempComponent;
                    parentFrame.setCutEnabled(true);
                    parentFrame.setCopyEnabled(true);
                    parentFrame.setDescendEnabled(false);
                    found = ix;
                    break;
                }
            }

            // check if connection selected
            if (tempComponent instanceof Connection && !parentFrame.status.equals("Connection"))
            {
                //If not first point in connection mode or
                //if not in connection mode at all
                Connection conn = (Connection) tempComponent;
                //if connection not done
                if (conn.selectpoint(x, y))
                {
                    currModule.mySchematic.selComponent = tempComponent;
                    parentFrame.setCutEnabled(true);
                    parentFrame.setCopyEnabled(true);
                    parentFrame.setDescendEnabled(false);
                    found = ix;
                    break;
                }
                //if you just want to select a connection
                if (conn.selectobj(x, y))
                {
                    if(snapToGrid)
                        conn.insertpoint(x, y, UserPref.schematicGridSize);
                    else
                        conn.insertpoint(x, y, 1);
                    currModule.mySchematic.selComponent = tempComponent;
                    parentFrame.setCutEnabled(true);
                    parentFrame.setCopyEnabled(true);
                    parentFrame.setDescendEnabled(false);
                    found = ix;
                    break;
                }
            }

            // check if  free text selected
            if (tempComponent instanceof GraphicPart_text)
            {
                GraphicPart_text got = (GraphicPart_text) tempComponent;

                if (got.selectobj(x, y))
                {
                    currModule.mySchematic.selComponent = tempComponent;
                    parentFrame.setCutEnabled(true);
                    parentFrame.setCopyEnabled(true);
                    parentFrame.setDescendEnabled(false);
                    found = ix;
                    break;
                }
            }
        } //end of schDrawablesLoop which is a down to

        //not found can be -1 meaning not found
        //now go and unhighlight anything previously selected
        //todo: should just know what the previously selected comp was
        //and only unhighlight it.
        if (found != -1)
        {
            for (ix = currModule.mySchematic.drawableObjs.size() - 1; ix >= 0; ix--)
            {
                if (found != ix)
                {
                    tempComponent = currModule.mySchematic.drawableObjs.elementAt(ix);

                    if (tempComponent instanceof IconInst)
                    {
                        ((IconInst) tempComponent).unselect();
                    }
                    if (tempComponent instanceof IconMethod)
                    {
                        ((IconMethod) tempComponent).unselect();
                    }
                    if (tempComponent instanceof SchematicInport)
                    {
                        ((SchematicInport) tempComponent).unselect();
                    }
                    if (tempComponent instanceof SchematicOutport)
                    {
                        ((SchematicOutport) tempComponent).unselect();
                    }
                    if (tempComponent instanceof Connection)
                    {
                        ((Connection) tempComponent).unselect();
                    }
                    if (tempComponent instanceof GraphicPart_text)
                    {
                        ((GraphicPart_text) tempComponent).unselect();
                    }
                }
            } //end for (ix=schDrawables.size()-1

            currModule.mySchematic.pushtop(found);
        }  //if found
        else
        {
            if (currModule.mySchematic.selComponent instanceof IconInst)
            {
                ((IconInst) currModule.mySchematic.selComponent).unselect();
            }
            if (currModule.mySchematic.selComponent instanceof IconMethod)
            {
                ((IconMethod) currModule.mySchematic.selComponent).unselect();
            }
            if (currModule.mySchematic.selComponent instanceof SchematicInport)
            {
                ((SchematicInport) currModule.mySchematic.selComponent).unselect();
            }
            if (currModule.mySchematic.selComponent instanceof SchematicOutport)
            {
                ((SchematicOutport) currModule.mySchematic.selComponent).unselect();
            }
            if (currModule.mySchematic.selComponent instanceof Connection)
            {
                ((Connection) currModule.mySchematic.selComponent).unselect();
            }
            if (currModule.mySchematic.selComponent instanceof GraphicPart_text)
            {
                ((GraphicPart_text) currModule.mySchematic.selComponent).unselect();
            }
            currModule.mySchematic.selComponent = null;
            parentFrame.setCutEnabled(false);
            parentFrame.setCopyEnabled(false);
            parentFrame.setDescendEnabled(false);
        }
        old_x=x;
        old_y=y;
        if(snapToGrid)
        {
            old_x = (x + (UserPref.schematicGridSize >> 1)) / UserPref.schematicGridSize * UserPref.schematicGridSize;
            old_y = (y + (UserPref.schematicGridSize >> 1)) / UserPref.schematicGridSize * UserPref.schematicGridSize;
        }
        repaint();
    }

    /**
     * Event handler for mouseDragged event.
     * @param e - MouseEvent
     */
    public void mouseDragged(MouseEvent e)
    {
        if (parentFrame.currModule == null)
        {
            return;
        }
        Module currModule = parentFrame.currModule;
        Component selComponent = currModule.mySchematic.selComponent;
        if (selComponent == null)
            return;

        int x=e.getX();
        int y=e.getY();
        if(snapToGrid)
        {
            x = (e.getX() + (UserPref.schematicGridSize >> 1)) / UserPref.schematicGridSize * UserPref.schematicGridSize;
            y = (e.getY() + (UserPref.schematicGridSize >> 1)) / UserPref.schematicGridSize * UserPref.schematicGridSize;
        }

        if (selComponent instanceof IconInst)
        {
            IconInst iconInst = (IconInst) selComponent;
            iconInst.selPort = null;
            iconInst.moveobj(x - old_x, y - old_y);
            parentFrame.currModule.setDirty(true);
        }
        if (selComponent instanceof IconMethod)
        {
            IconMethod iconInst = (IconMethod) selComponent;
            iconInst.moveobj(x - old_x, y - old_y);
            parentFrame.currModule.setDirty(true);
        }
        else if (selComponent instanceof SchematicInport)
        {
            SchematicInport sip = (SchematicInport) selComponent;
            sip.moveobj(x - old_x, y - old_y);
            parentFrame.currModule.setDirty(true);
        }
        else if (selComponent instanceof SchematicOutport)
        {
            SchematicOutport sop = (SchematicOutport) selComponent;
            sop.moveobj(x - old_x, y - old_y);
            parentFrame.currModule.setDirty(true);
        }
        else if (selComponent instanceof Connection)
        {
            Connection conn = (Connection) selComponent;
            if (conn.src_iconOrSchPort instanceof IconInst)
            {
                ((IconInst) conn.src_iconOrSchPort).selPort = null;
            }
            conn.movepoint(x - old_x, y - old_y);

            Vector schDrawables = currModule.mySchematic.drawableObjs;
            Component tempComponent;
            int ix;
            boolean setStatus=false;
            for (ix = schDrawables.size() - 1; ix >= 0; ix--)
            {
                tempComponent = (Component) schDrawables.elementAt(ix);

                if (tempComponent instanceof IconInst)
                {
                    IconInst iconInst = (IconInst) tempComponent;

                    //go thru drawables to see if pin on icon selected
                    if (iconInst.selectport(x, y))
                    {
                        if(iconInst.selPort instanceof IconInport)
                        {
                            parentFrame.myStatusPanel.setStatusMessage("The type of this inport: " + ((IconInport) iconInst.selPort).Type);
                            setStatus = true;
                        }
                        else if(iconInst.selPort instanceof IconOutport)
                        {
                            parentFrame.myStatusPanel.setStatusMessage("The type of this outport: " + ((IconOutport) iconInst.selPort).Type);
                            setStatus = true;
                        }
                    }
                    else
                    {
                        iconInst.unselect();
                    }
                }
                // check if SchematicInport selected
                else if (tempComponent instanceof SchematicInport)
                {
                    SchematicInport sip = (SchematicInport) tempComponent;
                    if (sip.selectport(x, y))
                    {
                        parentFrame.myStatusPanel.setStatusMessage("The type of this inport: " + ((SchematicInport) tempComponent).Type);
                        setStatus = true;
                    }
                    else
                        sip.unselect();
                }
                // check if sch outport selected
                else if (tempComponent instanceof SchematicOutport)
                {
                    SchematicOutport sop = (SchematicOutport) tempComponent;
                    //is the pin on the SchematicOutport selected?
                    if (sop.selectport(x, y))
                    {
                        parentFrame.myStatusPanel.setStatusMessage("The type of this outport: " + ((SchematicOutport) tempComponent).Type);
                        setStatus = true;
                    }
                    else
                        sop.unselect();
                }
            }
            if(!setStatus)
                parentFrame.myStatusPanel.setStatusMessage("");
            parentFrame.currModule.setDirty(true);
        }
        else if (selComponent instanceof GraphicPart_text)
        {
            GraphicPart_text got = (GraphicPart_text) selComponent;
            got.moveobj(x - old_x, y - old_y);
            parentFrame.currModule.setDirty(true);
        }

        old_x = x;
        old_y = y;

        repaint();
    }

    /**
     * Event handler for mouseReleased event.
     * @param e - MouseEvent
     */
    public void mouseReleased(MouseEvent e)
    {
        Module currModule = parentFrame.currModule;
        if (currModule == null)
            return;
        Component selComponent = parentFrame.currModule.mySchematic.selComponent;
        if (selComponent == null)
        {
            return;
        }

        if(e.getClickCount()==2)
        {
            if(selComponent instanceof IconInst)
                parentFrame.descend();
            if(selComponent instanceof IconMethod)
            {
            	parentFrame.nslmEditor = new NslmEditorFrame(parentFrame, ((IconMethod)selComponent).methodName);
            	parentFrame.nslmEditor.setVisible(true);
            }
        }
        else
        {
            if (selComponent instanceof IconInst)
            {
                IconInst iconInst = (IconInst) selComponent;
                iconInst.selPort = null;

                for (int i = 0; i < iconInst.drawableParts.size(); i++)
                {
                    GraphicPart gobj = (GraphicPart) iconInst.drawableParts.elementAt(i);
                    Connection conn;

                    if (gobj instanceof IconInport)
                    {
                        conn = ((IconInport) gobj).link;  //todo:00/5/11 aa: this is the problem
                        if (conn != null)
                        {
                            conn.mergedest();
                        }
                    }
                    if (gobj instanceof IconOutport)
                    {
                        IconOutport op = (IconOutport) gobj;

                        for (int j = 0; j < op.links.size(); j++)
                        {
                            conn = op.links.elementAt(j);
                            conn.mergesrc();
                        }
                    }
                }
            }
            else if (selComponent instanceof SchematicInport)
            {
                SchematicInport sip = (SchematicInport) selComponent;

                for (int i = 0; i < sip.links.size(); i++)
                {
                    Connection conn = sip.links.elementAt(i);
                    conn.mergesrc();
                }
            }
            else if (selComponent instanceof SchematicOutport)
            {
                SchematicOutport sop = (SchematicOutport) selComponent;
                if (sop.link != null)
                {
                    sop.link.mergedest();
                }
            }
            else if(selComponent instanceof Connection)
            {
                Vector schDrawables = currModule.mySchematic.drawableObjs;
                Component tempComponent;

                int x = e.getX();
                int y = e.getY();
                Connection conn = (Connection) selComponent;

                if (conn.src_iconOrSchPort instanceof IconInst)
                {
                    ((IconInst) conn.src_iconOrSchPort).selPort = null;
                }

                String reqType="";
                if(conn.src_iconOrSchPort!=null && conn.src_iconOrSchPort instanceof SchematicInport)
                {
                    reqType=((SchematicInport)conn.src_iconOrSchPort).Type;
                    if(reqType.startsWith("NslDin"))
                        reqType=reqType.substring(6);
                }
                else if(conn.src_port instanceof IconOutport)
                {
                    reqType=conn.src_port.Type;
                    if(reqType.startsWith("NslDout"))
                        reqType=reqType.substring(7);
                }

                for (int ix = schDrawables.size() - 1; ix >= 0; ix--)
                {
                    tempComponent = (Component) schDrawables.elementAt(ix);

                    if (tempComponent instanceof IconInst)
                    {
                        IconInst iconInst = (IconInst) tempComponent;
                        if (iconInst.selectport(x, y))
                        {
                            if (iconInst.selPort instanceof IconInport)
                            {
                                IconInport iip=(IconInport)iconInst.selPort;
                                String givenType=iip.Type;
                                if(givenType.startsWith("NslDin"))
                                    givenType=givenType.substring(6);
                                if(givenType.equals(reqType))
                                {
                                    conn.connectdest(iconInst, (IconInport) iconInst.selPort);
                                    iconInst.unselect();
                                    parentFrame.myStatusPanel.setStatusMessage("");
                                    iconInst.selPort = null;
                                    parentFrame.currModule.setDirty(true);
                                }
                                else
                                {
                                    String errstr="Connection src port is of type "+reqType+
                                            " and dest port is of type "+givenType+
                                            ". The connection has not been made.";
                                    JOptionPane.showMessageDialog(this, errstr, "SchematicPanel Error",
                                                                  JOptionPane.ERROR_MESSAGE);
                                }
                                break;
                            }
                        }
                    }
                    if (tempComponent instanceof SchematicOutport)
                    {
                        SchematicOutport sop = (SchematicOutport) tempComponent;
                        if (sop.selectport(x, y))
                        {
                            String givenType=sop.Type;
                            if(givenType.startsWith("NslDout"))
                                givenType=givenType.substring(7);
                            if(givenType.equals(reqType))
                            {
                                conn.connectdest(sop);
                                parentFrame.myStatusPanel.setStatusMessage("");
                                sop.unselect();
                                parentFrame.currModule.setDirty(true);
                            }
                            else
                            {
                                String errstr="Connection src port is of type "+reqType+" and dest port is of type "+
                                        givenType+". The connection has not been made.";
                                JOptionPane.showMessageDialog(this, errstr, "SchematicPanel Error",
                                                              JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        }
                    }
                }
                conn.merge();
                if (conn.numVerticies == 1)
                {
                    if (conn.src_iconOrSchPort instanceof IconInst)
                    {
                        conn.src_port.disconnect(conn);
                        parentFrame.currModule.setDirty(true);
                    }
                    if (conn.src_iconOrSchPort instanceof SchematicInport)
                    {
                        ((SchematicInport) conn.src_iconOrSchPort).disconnect(conn);
                        parentFrame.currModule.setDirty(true);
                    }
                    if (conn.dest_iconOrSchPort instanceof IconInst)
                    {
                        conn.dest_port.disconnect();
                        parentFrame.currModule.setDirty(true);
                    }
                    if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                    {
                        ((SchematicOutport) conn.dest_iconOrSchPort).disconnect();
                        parentFrame.currModule.setDirty(true);
                    }
                    schDrawables.removeElement(conn);
                    parentFrame.currModule.setDirty(true);
                }
            }

            parentFrame.resetScrollbarLimits();
        }
    }

    /**
     * Event handler for mouseEntered event.
     * @param e - MouseEvent
     */
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * Event handler for mouseExited event.
     * @param e - MouseEvent
     */
    public void mouseExited(MouseEvent e)
    {
    }

    /**
     * Event handler for mouseClicked event.
     * @param e - MouseEvent
     */
    public void mouseClicked(MouseEvent e)
    {
    }

    /**
     * deleteComp
     * deletes the selected component or interconnect from
     * the list of variables and drawables and returns it 
     * @return deleted component
     */
    public Component deleteComp()
    {

        if (parentFrame.currModule == null)
        {
            System.err.println("Error:SchematicPanel: deleteComp:1 No Module Open");
            return (null);
        }
        if (parentFrame.currModule.mySchematic == null)
        {
            System.err.println("Error:SchematicPanel: deleteComp:2 No Schematic Open");
            return (null);
        }
        if (parentFrame.currModule.mySchematic.selComponent == null)
        {
            System.err.println("Error:SchematicPanel: deleteComp:3 No component selected");
            return (null);
        }
        if (parentFrame.currModule.variables == null)
        {
            System.err.println("Error:SchematicPanel: deleteComp:4 No variables to delete");
            return (null);
        }
        //now we unview and delete the object from variables lists
        Component comp = parentFrame.currModule.mySchematic.selComponent;
        String name;
        String errstr="Are you sure you want to delete the component or connection icon?";
        int selected = JOptionPane.showConfirmDialog(null, errstr, "Warning", JOptionPane.OK_CANCEL_OPTION,
                                                     JOptionPane.WARNING_MESSAGE);
        if (selected!=JOptionPane.OK_OPTION)
        {
            return (null);
        }
        if (comp instanceof SchematicInport)
        {
            SchematicInport sip = (SchematicInport) comp;
            name = sip.Name;

            // Remove port from NslmEditor if module is open in it
            if(parentFrame.nslmEditor!=null && parentFrame.nslmEditor.isVisible())
                parentFrame.nslmEditor.removeVariable(parentFrame.currModule.getVariable(name));

            if ((parentFrame.currModule.variables != null))
            {
                parentFrame.currModule.deleteVariable(name);
            }
        }
        else if (comp instanceof SchematicOutport)
        {
            SchematicOutport sop = (SchematicOutport) comp;
            name = sop.Name;

            // Remove port from NslmEditor if module is open in it
            if(parentFrame.nslmEditor!=null && parentFrame.nslmEditor.isVisible())
                parentFrame.nslmEditor.removeVariable(parentFrame.currModule.getVariable(name));

            if ((parentFrame.currModule.variables != null))
            {
                parentFrame.currModule.deleteVariable(name);
            }
        }
        else if (comp instanceof IconInst)
        {
            name = ((IconInst)comp).instanceName;

            // Remove submodule from NslmEditor if module is open in it
            if(parentFrame.nslmEditor!=null && parentFrame.nslmEditor.isVisible())
                parentFrame.nslmEditor.removeVariable(parentFrame.currModule.getVariable(name));

            if(parentFrame.currModule.variables!=null)
            {
                parentFrame.currModule.deleteVariable(name);
            }
        }
        //now delete the iconinst, connection, text, or whatever
        parentFrame.currModule.mySchematic.deleteDrawableObj(comp);
        if(parentFrame.currModule.mySchematic.selComponent!=null && parentFrame.currModule.mySchematic.selComponent.equals(comp))
            parentFrame.currModule.mySchematic.selComponent = null;
        parentFrame.currModule.setDirty(true);

        // Repaint IconEditor if module is open in it
        if(parentFrame.iconEditor!=null && parentFrame.iconEditor.isVisible())
            parentFrame.iconEditor.myIconPanel.repaint();

        repaint();
        return (comp);
    }

    /**
     * method to set the background color of the SchematicPanel
     * @param drawBack_col - background color
     */
    public static void setBackgroundColor(Color drawBack_col)
    {
        currBackgroundCol = drawBack_col;
    }

    public void debugDoNext()
    {
        if(SchEditorFrame.debugMode)
        {
            //NslDebugManager.step();
            parentFrame.step(numCycles);
            parentFrame.repaint();

            for(int i = 0; i < dataBoxList.size(); i++)
            {
                NslData tmpND = dataList.elementAt(i);

                //String s = shortNum(tmpND.toString());
                String s = convertNumericalString(tmpND.toString().replaceAll("} ","}\n"),3);

                String temptxt = tmpND.nslGetName() + ": " + s;
                dataBoxList.elementAt(i).settext(temptxt);
            }

        }
    }

    public void debugAddWatch()
    {
        if(SchEditorFrame.debugMode)
        {
            NslDebugManager.inputNewWatch(this);
        }
    }

    public void addWatchIndicators(Vector<NslData> nds)
    {
        for(int i=0; i<nds.size(); i++)
        {
            Font tempfont = new Font("Courier", Font.BOLD, 12);
            GraphicPart_text temp;

            //String s = shortNum(nd.toString());
            String s = convertNumericalString(nds.get(i).toString().replaceAll("} ","}\n"),3);

            temp = new GraphicPart_text(50, 50+i*30, nds.get(i).nslGetName() + ": " + s, tempfont, UserPref.freeText_col,
                                        UserPref.freeTextSize);
            temp.setBackground(Color.WHITE);
            temp.setcolor(Color.BLUE);
            temp.hasBackground = true;

            dataBoxList.addElement(temp);
            dataList.addElement(nds.get(i));

            parentFrame.currModule.mySchematic.addDrawableObj(temp);
            parentFrame.repaint();
        }
    }

    protected String convertNumericalString(String s, int sigFigs)
    {
        StringBuilder n=new StringBuilder();
        StringTokenizer t1=new StringTokenizer(s,"\n");
        int numLine=0;
        while(t1.hasMoreTokens())
        {
            if(numLine>0)
                n.append("\n");
            String line=t1.nextToken().trim();
            if(line.startsWith("{"))
                line=line.substring(1).trim();
            if(line.endsWith("}"))
                line=line.substring(0,line.length()-2).trim();
            StringTokenizer t2=new StringTokenizer(line," ");
            boolean writeBraces=false;
            if(t1.countTokens()>1 || t2.countTokens()>1)
            {
                n.append("{");
                writeBraces=true;
            }
            int numidx=0;
            while(t2.hasMoreTokens())
            {
                Double d=Double.parseDouble(t2.nextToken().trim());
                DecimalFormat format=new DecimalFormat("0.000");
                if(d<0.001 && d>0.0)
                        format.applyPattern("0.000E0");
                if(numidx>0)
                    n.append(" ");
                n.append(format.format(d));
                numidx++;
            }
            if(writeBraces)
                n.append("}");
            numLine++;
        }
        return n.toString();
    }

    public void debugClearAll()
    {
        if(dataBoxList == null)
            return;

        Enumeration el = dataBoxList.elements();

        if(!el.hasMoreElements())
            return;

        while(el.hasMoreElements())
        {
            Component temp = (Component)el.nextElement();
            if(parentFrame.currModule!=null)
            {
                parentFrame.currModule.mySchematic.deleteDrawableObj(temp);
                parentFrame.repaint();
            }
        }
    }

    public void setNumCycles(int x)
    {
        this.numCycles = x;
    }
}//end class SchematicPanel
