package scs.ui;
/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * IconPanel - A class representing the panel in the icon editor which is used
 * to draw icons.
 *
 * @author Weifang Xie, Nitin Gupta, Amanda Alexander
 * @version     %I%, %G%
 *
 * @*var       c		the color of the current paint brush
 * @*var       old_x		the variable to keep the old value of the 
 *				x-coordinate of the mouse 
 * @*var       old_y		the variable to keep the old value of the 
 *                                              y-coordinate of the mouse 
 * @*var       resizingOrMoving		a flag indicating whether the current 
 *				highlighted component is selected as a whole (so
 *				that it's in the mode of moving) or only one
 *				point is selected (so that it's in the mode of
 *				resizing)
 * @*var       parentFrame		pointing to the parentFrame--IconEditorFrame
 * @*var       grid		the measure of the current grid
 * @*var       mousedown	a flag indicating whether the mouse is in the 
 *				button pressed mode
 *
 * @since JDK1.1
 */

import scs.Declaration;
import scs.util.UserPref;
import scs.graphics.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//------------------------------------------------------------------

public class IconPanel extends JPanel implements MouseMotionListener, MouseListener, KeyListener
{
    Color c = Color.green;
    int old_x = 0;
    int old_y = 0;
    int resizingOrMoving; //resizing=0, moving=1
    IconEditorFrame parentFrame;
    public boolean snapToGrid=true;
    public boolean showGrid=false;
    boolean mousedown = false;
    Declaration var = null;

    static public String text_string = "DUMMY";
    public static Color currBackgroundCol = UserPref.drawBack_col;

    //-------------------------------------------------------------------------
    /**
     * Constructor of this class with frame parameter
     */
    public IconPanel(IconEditorFrame parentF)
    {
        parentFrame = parentF;
        setBackground(currBackgroundCol); //why not noActionTakenBack_col
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
    }

    //-------------------------------
    /**
     * Return the preferred size of this icon panel.
     */
    public Dimension getPreferredSize()
    {
        return new Dimension(400, 400);
    }

    //-------------------------------
    /**
     * paint this icon panel.
     */
    public void paintChildren(Graphics g)
    {
        if(showGrid)
            drawGrid((Graphics2D)g);
        if (parentFrame.parentFrame.currModule.myIcon != null)
        {
            parentFrame.parentFrame.currModule.myIcon.paint(g);
        }
    }
    //-------------------------------

    protected void drawGrid(Graphics2D g)
    {
        g.setColor(UserPref.grid_col);
        for(int x=0; x<getWidth(); x+=UserPref.iconGridSize)
        {
            g.drawLine(x, 0, x, getHeight());
        }
        for(int y=0; y<getHeight(); y+=UserPref.iconGridSize)
        {
            g.drawLine(0, y, getWidth(), y);
        }
    }

    /**
     * When status of the icon panel as to what component will be inserted next
     * is changed, this function is called to clear the select flags of those
     * components on this icon panel.
     */

    public void newstatus()
    {
        int ix;
        GraphicPart tempComp;

        if (parentFrame == null)
        {
            return;
        }
        if (parentFrame.parentFrame.currModule.myIcon == null)
        {
            return;
        }

        if (parentFrame.parentFrame.currModule.myIcon.drawableParts != null)
        {
            for (ix = 0; ix < parentFrame.parentFrame.currModule.myIcon.drawableParts.size(); ix++)
            {
                tempComp = (GraphicPart) parentFrame.parentFrame.currModule.myIcon.drawableParts.elementAt(ix);
                tempComp.select = 0;
            }
        }
        setBackground(currBackgroundCol);
        repaint();
    }
//--------------------------------------------


    /**
     * Event handler for mouseMoved event.
     */
    public void mouseMoved(MouseEvent e)
    {
    }
//--------------------------------------------

    /**
     * Event handler for mousePressed event.
     */
//--------------------------------------------
    public void mousePressed(MouseEvent e)
    {
        GraphicPart tempComp;
        int ix;
        String status = parentFrame.myToolBox.status;

        int x = e.getX();
        int y = e.getY();
        if(snapToGrid)
        {
            x = (e.getX() + (UserPref.iconGridSize >> 1)) / UserPref.iconGridSize * UserPref.iconGridSize;
            y = (e.getY() + (UserPref.iconGridSize >> 1)) / UserPref.iconGridSize * UserPref.iconGridSize;
        }
        mousedown = true;
        requestFocus();
        if (parentFrame.parentFrame.currModule.myIcon == null)
        {
            return;
        }
        if (parentFrame.parentFrame.currModule.myIcon.drawableParts == null)
        {
            return;
        }

        for (ix = 0; ix < parentFrame.parentFrame.currModule.myIcon.drawableParts.size(); ix++)
        {
            tempComp = (GraphicPart) parentFrame.parentFrame.currModule.myIcon.drawableParts.elementAt(ix);

            if (tempComp.select != 0 && status!=null && status.equals("insert_poly"))
            {
                tempComp.addpoint(x, y);
                old_x = x;
                old_y = y;
                parentFrame.parentFrame.currModule.myIcon.setminmax();
                repaint();
                return;
            }
        }
        resizingOrMoving = 0; //resizing=0, moving=1
        if (status==null)
        {
            for (ix = parentFrame.parentFrame.currModule.myIcon.drawableParts.size() - 1; ix >= 0; ix--)
            {
                tempComp = (GraphicPart) parentFrame.parentFrame.currModule.myIcon.drawableParts.elementAt(ix);

                if (tempComp.selectpoint(e.getX(), e.getY()))
                {
                    resizingOrMoving = 0; // resizing mode
                    parentFrame.cut.setEnabled(true);
                    parentFrame.copy.setEnabled(true);
                    parentFrame.delete.setEnabled(true);
                }
                else if (tempComp.selectobj(e.getX(), e.getY()))
                {
                    resizingOrMoving = 1;   // moving mode
                    parentFrame.cut.setEnabled(true);
                    parentFrame.copy.setEnabled(true);
                    parentFrame.delete.setEnabled(true);
                }
                else
                    tempComp.select=0;
            }
        }
        else if (status.equals("insert_line"))
        {
            parentFrame.parentFrame.currModule.myIcon.addDrawablePart(new GraphicPart_line(x, y, UserPref.line_col));
            parentFrame.parentFrame.currModule.setDirty(true);
            parentFrame.myToolBox.status=null;
        }
        else if (status.equals("insert_text"))
        {
            GraphicPart_text tempGOT = new GraphicPart_text(x, y, text_string, UserPref.freeTextFont, UserPref.freeText_col, UserPref.freeTextSize);
            parentFrame.parentFrame.currModule.myIcon.addDrawablePart(tempGOT);
            parentFrame.parentFrame.currModule.setDirty(true);
            parentFrame.myToolBox.status=null;
        }
        else if (status.equals("insert_oval"))
        {
            parentFrame.parentFrame.currModule.myIcon.addDrawablePart(new GraphicPart_oval(x, y, UserPref.oval_col));
            parentFrame.parentFrame.currModule.setDirty(true);
            parentFrame.myToolBox.status=null;
        }
        else if (status.equals("insert_rect"))
        {
            parentFrame.parentFrame.currModule.myIcon.addDrawablePart(new GraphicPart_rect(x, y, UserPref.rect_col));
            parentFrame.parentFrame.currModule.setDirty(true);
            parentFrame.myToolBox.status=null;
        }
        else if (status.equals("insert_poly"))
        {
            parentFrame.parentFrame.currModule.myIcon.addDrawablePart(new GraphicPart_poly(x, y, UserPref.poly_col));
            parentFrame.parentFrame.currModule.setDirty(true);
            parentFrame.myToolBox.status=null;
        }
        else if (status.equals("insert_inport"))
        {
            addInport(x, y);
            parentFrame.myToolBox.status=null;
        }
        else if (status.equals("insert_outport"))
        {
            addOutport(x, y);
            parentFrame.myToolBox.status=null;
        }

        old_x = x;
        old_y = y;
        parentFrame.parentFrame.currModule.myIcon.setminmax();
        repaint();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }  //end mousedPressed


    //-------------------------------
    /**
     * get pointer to the var variable for adding a new port
     */
    public void setPortInfo(Declaration var)
    {
        this.var = var;
    }

    //--------------------------------------
    public void addPort(Declaration var, int x, int y, GraphicPart port)
    {
        //left to right
        if ((var.portIconDirection == 'L') && (x < scs.graphics.Icon.pinLength))
        {
            String errstr = "addPort:x less than Icon.pinLength";
            JOptionPane.showMessageDialog(parentFrame, errstr, "IconPanel Error", JOptionPane.ERROR_MESSAGE);
            //top to bottom
        }
        else if ((var.portIconDirection == 'T') && (y < scs.graphics.Icon.pinLength))
        {
            String errstr = "addPort:y less than Icon.pinLength";
            JOptionPane.showMessageDialog(parentFrame, errstr, "IconPanel Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            if (parentFrame.parentFrame.currModule != null)
            {
                parentFrame.parentFrame.currModule.myIcon.addDrawablePart(port);
                //we modified the old variable already
                int index = parentFrame.parentFrame.currModule.findVarIndex(var.varName);
                if (index == -1)
                {
                    parentFrame.parentFrame.currModule.addVariable(var);
                    if(port instanceof IconInport)
                    {
                        parentFrame.parentFrame.currModule.mySchematic.addDrawableObj(new SchematicInport(var));
                    }
                    else if(port instanceof IconOutport)
                    {
                        parentFrame.parentFrame.currModule.mySchematic.addDrawableObj(new SchematicOutport(var));
                    }
                    parentFrame.parentFrame.mySchematicPanel.repaint();
                    parentFrame.parentFrame.repaint();

                    // Add variable to NslmEditor if module is open in it
                    if(parentFrame.parentFrame.nslmEditor!=null && parentFrame.parentFrame.nslmEditor.isVisible())
                        parentFrame.parentFrame.nslmEditor.addVariable(var);
                }
            }
        }
    } // end addPort

    //--------------------------------------
    public void addInport(int x, int y)
    {
        GraphicPart port = new IconInport(var, x, y, c);
        addPort(var, x, y, port);
    } // end addInports

    //--------------------------------------
    public void addOutport(int x, int y)
    {
        GraphicPart port = new IconOutport(var, x, y, c);
        addPort(var, x, y, port);
    }

    //------------------------------------------------------------
    /**
     * Event handler for mouseDragged event.
     */
    public void mouseDragged(MouseEvent e)
    {
        GraphicPart tempComp;
        int ix;

        int x = e.getX();
        int y = e.getY();
        if(snapToGrid)
        {
            x = (e.getX() + (UserPref.iconGridSize >> 1)) / UserPref.iconGridSize * UserPref.iconGridSize;
            y = (e.getY() + (UserPref.iconGridSize >> 1)) / UserPref.iconGridSize * UserPref.iconGridSize;
        }

        if (parentFrame.parentFrame.currModule.myIcon == null)
        {
            return;
        }

        for (ix = 0; ix < parentFrame.parentFrame.currModule.myIcon.drawableParts.size(); ix++)
        {
            tempComp = (GraphicPart) parentFrame.parentFrame.currModule.myIcon.drawableParts.elementAt(ix);

            if (tempComp.select > 1 && resizingOrMoving == 0)
            {
                tempComp.movepoint(x - old_x, y - old_y);  //sending in delta
                parentFrame.parentFrame.currModule.setDirty(true);
                break;
            }
            if (tempComp.select == 1 && resizingOrMoving == 1)
            {
                tempComp.moveobj(x - old_x, y - old_y);
                parentFrame.parentFrame.currModule.setDirty(true);
                break;
            }
        }

        old_x = x;
        old_y = y;
        parentFrame.parentFrame.currModule.myIcon.setminmax();
        repaint();
    }

    //----------------------------------------------------------
    /**
     * Event handler for mouseReleased event.
     */
    public void mouseReleased(MouseEvent e)
    {
        String status = parentFrame.myToolBox.status;
        GraphicPart tempComp;
        int ix;

        mousedown = false;

        int x=e.getX();
        int y=e.getY();
        if(snapToGrid)
        {
            x = (e.getX() + (UserPref.iconGridSize >> 1)) / UserPref.iconGridSize * UserPref.iconGridSize;
            y = (e.getY() + (UserPref.iconGridSize >> 1)) / UserPref.iconGridSize * UserPref.iconGridSize;
        }
        if (parentFrame.parentFrame.currModule.myIcon == null)
        {
            return;
        }
        else
            parentFrame.parentFrame.currModule.myIcon.resetLabel();

        boolean itemSelected=false;
        for (ix = 0; ix < parentFrame.parentFrame.currModule.myIcon.drawableParts.size(); ix++)
        {
            tempComp = (GraphicPart) parentFrame.parentFrame.currModule.myIcon.drawableParts.elementAt(ix);

            // unselect the other objects first
            tempComp.select = 0; // unselect this component.
            // if it is selected.. it will be done again below

            // select the whole object
            if(tempComp.selectobj(x, y))
                itemSelected=true;
        }
        if(!itemSelected)
        {
            parentFrame.cut.setEnabled(false);
            parentFrame.copy.setEnabled(false);
            parentFrame.delete.setEnabled(false);
        }
        if (status != null && (status.equals("insert_inport") || status.equals("insert_outport")))
        {
            parentFrame.myToolBox.status = null;
        }

        repaint();
    }

    /**
     * Event handler for mouseEntered event.
     */
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * Event handler for mouseExited event.
     */
    public void mouseExited(MouseEvent e)
    {
    }

    /**
     * Event handler for mouseClicked event.
     */
    public void mouseClicked(MouseEvent e)
    {
    }

    /**
     * Event handler for keyPressed event.
     */
    public void keyPressed(KeyEvent e)
    {
        if (mousedown)
            return;

        if (e.getKeyCode() == KeyEvent.VK_DELETE)
        {
            parentFrame.removeDrawablePart();
            parentFrame.parentFrame.currModule.setDirty(true);
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


    //----------------------------------------------
    /**
     * method to set the background color of the IconPanel
     * @param drawBack_col - background color
     **/
    public static void setBackgroundColor(Color drawBack_col)
    {
        currBackgroundCol = drawBack_col;
    }
}//end class IconPanel