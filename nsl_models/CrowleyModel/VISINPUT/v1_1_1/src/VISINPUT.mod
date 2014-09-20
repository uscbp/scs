/** 
 Here is the class representing the input module from s.C.
 In old NSL it is an input array, but since NSLJ does not have
 input arrays yet we make them static arrays.
*/
package CrowleyModel.VISINPUT.v1_1_1.src;
nslImport CrowleyModel.Illuminations.v1_1_1.src.*;
nslImport CrowleyModel.Illumination.v1_1_1.src.*;

nslJavaModule VISINPUT(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  VISINPUT
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutDouble2 visinput_out(array_size,array_size); // 
private final int NINE=9; // 
private final double retinatm=0.004; // 
private int cycle=0; // 
private NslDouble2 retina(array_size, array_size); // 
public Illuminations InputIlluminations(); // 
public Illumination dummy(); // 

//methods 
public void initModule()
{
	InputIlluminations.initIlluminations();
	nslAddAreaCanvas("output", "input", visinput_out, 0.0, 100.0);
}

public void initRun(){
	visinput_out = 0;
	retina = 0;
	cycle  = 0;
	
	/** for demo purpose only */
	/* Commented out Illuminations in initRun() (JS 10/14/97) */
	/*
	    Illumination ve;
	    Illumination (4,4,0,0.0,0.2) ve  ;
	    InputIlluminations.getIlluminationList().addElement(ve);
	    Illumination (0,0,60,0.050,0.150) ve  ;
	    InputIlluminations.getIlluminationList().addElement(ve);
	    Illumination (2,1,60,0.100,0.200) ve  ;
	    InputIlluminations.getIlluminationList().addElement(ve);
	*/

	// 96/12/20 aa
	// nslPrintln("InputIlluminations:\n" + InputIlluminations+"\n");
    }

    public void simRun(){
	// System.err.println("@@@@ VISINPUT simRun entered @@@@");

	/*
	    CurrentTime = rint( ( SACCADE.get_time() * 10000.0 ));
	    CurrentTime = CurrentTime / 10000.0;

	    nslPrintln("Time: "+ setprecision(4) + setw(8) + CurrentTime);
	*/

	CheckInput( visinput_out, InputIlluminations );
	retina = nslDiff(retina,retinatm, -retina + visinput_out);

	// 96/12/20 aa
	cycle=cycle+1;
	// nslPrintln("cycle " + cycle);  took out Jan 21 1998 - aa
	// nslPrintln("visinput_out " + visinput_out);
    }
  
    // Allow manipulation of events (JS 10/9/97)

public void resetIlluminations() 
{
	InputIlluminations.RemoveIlluminations();
}

public void addIllumination(int x, int y, double act, double ts, double te)
{
	InputIlluminations.addIllumination(new Illumination(x,y,act,ts,te));
}

// This function processes the visual events established for the current
// experiment at the start of each time step.  If a new visual event has
// started, the appropriate matrix element in visinput is activated.  If a
// visual event has expired, the corresponding visinput element is set to 0.

public NslDouble2 CheckInput(NslDouble2 visinput, Illuminations events) {
	double cur_time = 0;
	Illumination cur;
	int i;
	java.util.Enumeration e;

	cur_time = system.getCurTime();
	if(events != null && events.getIlluminationList()!=null)
	{
		e  = ((java.util.Vector)events.getIlluminationList()).elements();

		while (e.hasMoreElements()) {
		    // Check start time for an event
		    cur = (Illumination)e.nextElement();
		    if (cur_time >= cur.getStart()) {
			// Set visinput array element to event activation value
			visinput[cur.getX()][cur.getY()] = 0+cur.getValue();
		    }
		    if (cur_time >=cur.getStop()) {
			// Illumination is done, set visinput element to 0
			visinput[cur.getX()][cur.getY()] = 0.0;
		    }
		}
	}

	return visinput;
    }
public void makeConn(){
}
}//end VISINPUT

