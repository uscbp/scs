/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)DoubleSaccade.mod	1.8 --- 08/05/99 -- 13:56:15 */
package CrowleyModel.SaccadeTargetLocations.v1_1_1.src;

import java.awt.*;
import java.awt.event.*;
import java.lang.*;

// panel that plots and records target locations
public class SaccadeTargetLocations extends java.awt.Panel {
  // enums for reading parameter values
  public static final int FX = 0;
  public static final int FY = 1;
  public static final int X1 = 2;
  public static final int Y1 = 3;
  public static final int X2 = 4;
  public static final int Y2 = 5;
  // enums for setting mode
  public static final int M_F = 0;
  public static final int M_1 = 1;
  public static final int M_2 = 2;
  // internal variables
  private Rectangle r_border;
  private int fix_x, fix_y, t1_x, t1_y, t2_x, t2_y;
  private int mode;

  public SaccadeTargetLocations() {
    setBackground(Color.white);
    fix_x = fix_y = 4;
    t1_y = t2_y = 1;
    t1_x = 5;
    t2_x = 1;
    mode = M_F;

    addMouseListener( new  MouseAdapter(){
      public void mousePressed(java.awt.event.MouseEvent e){
	int rx, ry;
	
	rx = e.getX()*9/r_border.width;
	ry = e.getY()*9/r_border.height;
	switch(mode){
	case M_F:
	  fix_x = rx;
	  fix_y = ry;
	  break;
	case M_1:
	  t1_x = rx;
	  t1_y = ry;
	  break;
	case M_2:
	  t2_x = rx;
	  t2_y = ry;
	  break;
	}
	repaint();
      }
    });

  }

  public int GetParam(int what){
    switch(what){
    case FX:
      return fix_x;
    case FY:
      return fix_y;
    case X1:
      return t1_x;
    case Y1:
      return t1_y;
    case X2:
      return t2_x;
    case Y2:
      return t2_y;
    default:
      return 0;
    }
  }
  
  public void SetMode(int new_mode){
    mode = new_mode;
  }

  public void paint(Graphics g) {
    float sx,sy;
    int i;
    int x0,y0,x1,y1;

    r_border = getBounds();
    // Compute scaling constants: dimx = dimy = 9
    sx = (float)(r_border.width/9.);
    sy = (float)(r_border.height/9.);
    // Draw box
    g.setColor(Color.black);
    g.drawRect(0, 0, r_border.width-1, r_border.height-1);
    // Draw Grid
    g.setColor(Color.lightGray);
    // Horiz lines
    x0 = 1;
    x1 = r_border.width-2;
    for(i=1;i<9;i++){
      y0 = y1 = r_border.height*i/9;
      g.drawLine(x0,y0,x1,y1);
    }
    // Vert lines
    y0 = 1;
    y1 = r_border.height-2;
    for(i=1;i<9;i++){
      x0 = x1 = r_border.width*i/9;
      g.drawLine(x0,y0,x1,y1);
    }
    // Draw targets and fixation point
    // Fixation
    g.setColor(Color.red);
    g.fillOval((int)(fix_x*sx+sx/2.)-3 , (int)(fix_y*sy+sy/2.)-3 , 7, 7);
    // First Target
    g.setColor(Color.green);
    g.fillRect((int)(t1_x*sx+sx/2.)-3 , (int)(t1_y*sy+sy/2.)-3 , 7, 7);
    // Second Target
    g.setColor(Color.blue);
    g.fillRect((int)(t2_x*sx+sx/2.)-3 , (int)(t2_y*sy+sy/2.)-3 , 7, 7);
  }

}

