package CrowleyModel.GapSaccade.v1_1_1.src;

nslImport nslAllImports;

import java.awt.*;
import java.awt.event.*;
nslImport CrowleyModel/VISINPUT/v1_1_1/src/VISINPUT;
nslImport CrowleyModel/SaccadeParameterPlot/v1_1_1/src/SaccadeParameterPlot;

nslModule GapSaccade(VISINPUT visinput) {

    // Private variables

    Panel p_amp, p_time, panel;
    Scrollbar sb_time, sb_amp, sb_dur;
    Button b_acc;
    boolean initialized=false;

    SaccadeParameterPlot p_spp;

    // Nsl mehtods

    public void initModule(){
    if(!initialized)
    {
    initialized=true;
	nslRemoveFromLocalProtocols("manual");
	nslRemoveFromLocalProtocols("double");

	panel = new Panel();
	panel.setLayout(new GridLayout(0,1));
	createGapSaccadePanel(panel);
	nslAddUserComponent(panel);
	}
    }

    public void createGapSaccadePanel(Panel panel) {

    	sb_amp = new Scrollbar(Scrollbar.VERTICAL, 1, 1, 0, 4);
    	sb_amp.addAdjustmentListener(new AdjustmentListener() {
      	    public void adjustmentValueChanged(AdjustmentEvent e){
	    	p_spp.setAmp(4-sb_amp.getValue());
		b_acc.setEnabled(true);
		p_spp.repaint();
      	    }
    	});
        sb_time = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, 28);
    	sb_time.addAdjustmentListener(new AdjustmentListener() {
      	    public void adjustmentValueChanged(AdjustmentEvent e){
		p_spp.setTime(sb_time.getValue()*25);
		b_acc.setEnabled(true);
		p_spp.repaint();
      	    }
    	});
    
    	sb_dur = new Scrollbar(Scrollbar.HORIZONTAL, 4, 1, 0, 16);
    	sb_dur.addAdjustmentListener(new AdjustmentListener() {
	    public void adjustmentValueChanged(AdjustmentEvent e){
		p_spp.setDuration(sb_dur.getValue()*25);
		b_acc.setEnabled(true);
		p_spp.repaint();
      	    }
    	});
    
    	b_acc = new Button("Apply");
    	b_acc.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e){
		String s = e.getActionCommand();
		if("Apply".equals(s)){
      		    Finalize();
      		    // Disable button
     		    b_acc.setEnabled(false);
    		}
  	    }
	});

	p_amp = new Panel();
        p_amp.setLayout(new FlowLayout());
        p_amp.add(sb_amp);
	p_amp.add(new Label("Amp"));
	
	p_time = new Panel();
    	p_time.setLayout(new FlowLayout(FlowLayout.CENTER));
    	p_time.add(sb_time);
    	p_time.add(new Label("Gap time"));
    	p_time.add(sb_dur);
    	p_time.add(new Label("Target duration"));
    	p_time.add(b_acc);

    	p_spp = new SaccadeParameterPlot();

    	panel.setLayout(new BorderLayout());
    	panel.add("North", new Label("General Saccade Controls",Label.CENTER));
    	panel.add("Center",p_spp);
    	panel.add("East",p_amp);
    	panel.add("South",p_time);
    }

    public void Finalize(){
	int x,y;
	double ts, te;

	// Clear events
	visinput.resetIlluminations();

	// fixation
    	x = 4;
    	y = 4;
    	ts = 0.;
    	te = (double)(sb_time.getValue()*25./1000.);
    	visinput.addIllumination(y,x,60.0,ts,te);
    	nslPrintln("Params: ts: "+ts+" te: "+te);

    	// Target
    	x = 4+4-sb_amp.getValue();
    	y = 4;
    	ts = 0.2;
    	te = (double)(.2+sb_dur.getValue()*25./1000.);
    	visinput.addIllumination(y,x,60.0,ts,te);
    	nslPrintln("Params: ts: "+ts+" te: "+te);
    }
}
