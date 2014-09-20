package CrowleyModel.DoubleSaccade.v1_1_1.src;
/*nslAllImports*/

import java.awt.*;
import java.awt.event.*;
import CrowleyModel.SaccadeTargetLocations.v1_1_1.src.SaccadeTargetLocations;
import CrowleyModel.SaccadeTargetDurations.v1_1_1.src.SaccadeTargetDurations;
import CrowleyModel.VISINPUT.v1_1_1.src.VISINPUT;
import CrowleyModel.Med.v1_1_1.src.Med;

/*********************************/
/*                               */
/*   Importing all Nsl classes   */
/*                               */
/*********************************/

import nslj.src.system.*;
import nslj.src.cmd.*;
import nslj.src.lang.*;
import nslj.src.math.*;
import nslj.src.display.*;

/*********************************/

public class DoubleSaccade extends NslModule {

    // Private variables

    Panel panel;
    Button b_acc;  // Apply button
    Checkbox cb_fix, cb_t1, cb_t2;
    Scrollbar sb_fix_s, sb_fix_e, sb_t1_s, sb_t1_e, sb_t2_s, sb_t2_e;
    boolean initialized=false;
    SaccadeTargetLocations stl;
    SaccadeTargetDurations std;

    // NslDoubleSaccadeCanvas canvas; // For automatic compilation

    public void initModule(){
    if(!initialized)
    {
    initialized=true;
	nslRemoveFromLocalProtocols("manual");
	nslRemoveFromLocalProtocols("gap");

	panel =  new  Panel();
	panel.setLayout( new  GridLayout(0,1));
	createSaccadeTargetLocations(panel);
	createSaccadeTargetDurations(panel);
	nslAddUserComponent(panel);
    }
    }

    public void createSaccadeTargetLocations(Panel panel) {
        Panel p1,p2;
        CheckboxGroup cbg;

        // Panel for selecting targets
        cbg =  new  CheckboxGroup();

        cb_fix =  new  Checkbox("Fixation",cbg,true);
        cb_fix.addItemListener( new  ItemListener(){
            public void itemStateChanged(ItemEvent e){
	        stl.SetMode(SaccadeTargetLocations.M_F);
	        b_acc.setEnabled(true);
            }
        });

        cb_t1 =  new  Checkbox("Target 1",cbg,false);
    	cb_t1.addItemListener( new  ItemListener(){
      	    public void itemStateChanged(ItemEvent e){
		stl.SetMode(SaccadeTargetLocations.M_1);
		b_acc.setEnabled(true);
      	    }
    	});

	cb_t2 =  new  Checkbox("Target 2",cbg,false);
	cb_t2.addItemListener( new  ItemListener(){
      	    public void itemStateChanged(ItemEvent e){
		stl.SetMode(SaccadeTargetLocations.M_2);
		b_acc.setEnabled(true);
            }
	});

    	p1 =  new  Panel();
	p1.setLayout( new  GridLayout(0,1));
    	p1.add(cb_fix);
    	p1.add(cb_t1);
    	p1.add(cb_t2);

    	// Composite target panel
    	stl =  new  SaccadeTargetLocations();
    	p2 =  new  Panel();
    	p2.setLayout( new  BorderLayout());
    	p2.add("North",  new  Label("Target Locations",Label.CENTER));
    	p2.add("Center", stl);
    	p2.add("West", p1);

	panel.add(p2);
    }

    public void createSaccadeTargetDurations(Panel panel) {
        Panel p1,p2;

    	// Panel for setting times
    	sb_fix_s =  new  Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, 60);
    	sb_fix_s.addAdjustmentListener( new  AdjustmentListener() {
      	    public void adjustmentValueChanged(AdjustmentEvent e){
		std.SetParam(SaccadeTargetDurations.FS, 
		     (float)(sb_fix_s.getValue()/100.));
		b_acc.setEnabled(true);
		std.repaint();
      	    }
    	});

    	sb_fix_e =  new  Scrollbar(Scrollbar.HORIZONTAL, 20, 1, 0, 60);
    	sb_fix_e.addAdjustmentListener( new  AdjustmentListener() {
      	    public void adjustmentValueChanged(AdjustmentEvent e){
		std.SetParam(SaccadeTargetDurations.FE, 
		     (float)(sb_fix_e.getValue()/100.));
		std.repaint();
      	    }
    	});

	sb_t1_s =  new  Scrollbar(Scrollbar.HORIZONTAL, 5, 1, 0, 60);
    	sb_t1_s.addAdjustmentListener( new  AdjustmentListener() {
      	    public void adjustmentValueChanged(AdjustmentEvent e){
		std.SetParam(SaccadeTargetDurations.T1S, 
		     (float)(sb_t1_s.getValue()/100.));
		b_acc.setEnabled(true);
		std.repaint();
      	    }
    	});

	sb_t1_e =  new  Scrollbar(Scrollbar.HORIZONTAL, 10, 1, 0, 60);
	sb_t1_e.addAdjustmentListener( new  AdjustmentListener() {
      	    public void adjustmentValueChanged(AdjustmentEvent e){
		std.SetParam(SaccadeTargetDurations.T1E, 
		     (float)(sb_t1_e.getValue()/100.));
		b_acc.setEnabled(true);
		std.repaint();
            }
    	});

	sb_t2_s =  new  Scrollbar(Scrollbar.HORIZONTAL, 10, 1, 0, 60);
    	sb_t2_s.addAdjustmentListener( new  AdjustmentListener() {
      	    public void adjustmentValueChanged(AdjustmentEvent e){
		std.SetParam(SaccadeTargetDurations.T2S, 
		     (float)(sb_t2_s.getValue()/100.));
		b_acc.setEnabled(true);
		std.repaint();
      	    }	
    	});

    	sb_t2_e =  new  Scrollbar(Scrollbar.HORIZONTAL, 15, 1, 0, 60);
    	sb_t2_e.addAdjustmentListener( new  AdjustmentListener() {
      	    public void adjustmentValueChanged(AdjustmentEvent e){
		std.SetParam(SaccadeTargetDurations.T2E, 
		     (float)(sb_t2_e.getValue()/100.));
		b_acc.setEnabled(true);
		std.repaint();
      	    }
    	});

    	p1 =  new  Panel();
    	p1.setLayout( new  GridLayout(0,1));
    	p1.add( new  Label("Fixation", Label.CENTER));
    	p1.add(sb_fix_s);
    	p1.add(sb_fix_e);
    	p1.add( new  Label("Target 1", Label.CENTER));
    	p1.add(sb_t1_s);
    	p1.add(sb_t1_e);
    	p1.add( new  Label("Target 2", Label.CENTER));
    	p1.add(sb_t2_s);
    	p1.add(sb_t2_e);

    	// Apply button
    	p1.add(b_acc =  new  Button("Apply"));
    	b_acc.addActionListener( new  ActionListener(){
	    public void actionPerformed(ActionEvent e){
		String s = e.getActionCommand();
    		if("Apply".equals(s)){
      		    Finalize();
      		    // Set button
      		    b_acc.setEnabled(false);
    		}
  	    }
	});

    	// Composite duration + scrollbar panel
    	std =  new  SaccadeTargetDurations();
    	p2 =  new  Panel();
    	p2.setLayout( new  BorderLayout());
    	p2.add("North",  new  Label("Target Durations",Label.CENTER));
    	p2.add("Center", std);

	panel.add(p2);
	panel.add(p1);

    }

    public void Finalize(){
    	int xf,yf,x1,y1,x2,y2;
   	double ts, te;

   	// Clear events
    	visinput.resetIlluminations();

	// fixation
    	xf = stl.GetParam(SaccadeTargetLocations.FX);
    	yf = stl.GetParam(SaccadeTargetLocations.FY);
    	ts = (double)(sb_fix_s.getValue()/100.);
    	te = (double)(sb_fix_e.getValue()/100.);
    	visinput.addIllumination(yf,xf,60.0,ts,te);

    	// target 1
    	x1 = stl.GetParam(SaccadeTargetLocations.X1);
    	y1 = stl.GetParam(SaccadeTargetLocations.Y1);
    	ts = (double)(sb_t1_s.getValue()/100.);
    	te = (double)(sb_t1_e.getValue()/100.);
    	visinput.addIllumination(y1,x1,60.0,ts,te);

    	// target 2
    	x2 = stl.GetParam(SaccadeTargetLocations.X2);
    	y2 = stl.GetParam(SaccadeTargetLocations.Y2);
    	ts = (double)(sb_t2_s.getValue()/100.);
    	te = (double)(sb_t2_e.getValue()/100.);
    	visinput.addIllumination(y2,x2,60.0,ts,te);
    
    	// Learn saccade
    	med.startNewElementList();
	    med.addNewSpecifiedElementsToLearn(y1,x1,y2,x2);
    }



	/******************************************************/
	/*                                                    */
	/* Generated by nslc.src.NslCompiler. Do not edit these lines! */
	/*                                                    */
	/******************************************************/

	/* Constructor and related methods */
	/* makeinst() declared variables */

	/* Formal parameters */
	VISINPUT visinput;
	Med med;

	/* Temporary variables */

	/* GENERIC CONSTRUCTOR: */
	public DoubleSaccade(String nslName, NslModule nslParent, VISINPUT visinput, Med med) {
		super(nslName, nslParent);
		this.visinput=visinput;
		this.med=med;
		initSys();
		makeInstDoubleSaccade(nslName, nslParent, visinput, med);
	}

	public void makeInstDoubleSaccade(String nslName, NslModule nslParent, VISINPUT visinput, Med med) { 
		callFromConstructorTop(new Object[]{visinput, med});
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/




}
