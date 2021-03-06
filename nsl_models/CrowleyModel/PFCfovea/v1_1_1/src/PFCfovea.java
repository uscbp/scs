package CrowleyModel.PFCfovea.v1_1_1.src;

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
import nslj.src.display.j3d.*;

/*********************************/

public class PFCfovea extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  PFCfovea
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 LIPvis_in; // 
public  NslDoutDouble2 PFCfovea_out; // 
private double pfcfoveatm; // 
private double Fixation; // 
private double DisFixation; // 
private double PFCfoveasigma1; // 
private double PFCfoveasigma2; // 
private double PFCfoveasigma3; // 
private double PFCfoveasigma4; // 
private  NslDouble2 pfcfovea; // 
private  NslInt0 FOVEAX; // 
private  NslInt0 FOVEAY; // 

//methods 
public void initModule(){

    FOVEAX.set(  (NslInt0)nslGetValue("crowleyModel.FOVEAX"));
    FOVEAY.set(  (NslInt0)nslGetValue("crowleyModel.FOVEAY"));  
}



public void initRun(){
    pfcfovea.set(  0.0);
    PFCfovea_out.set(  0.0);
    pfcfoveatm = 0.008;
    PFCfoveasigma1 = 0.0;
    PFCfoveasigma2 = 60.0;
    PFCfoveasigma3 = 0.0;
    PFCfoveasigma4 = 90.0;

    Fixation = 1.0;
    DisFixation = 0.0;
}

public void simRun(){
    // System.err.println("@@@@ PFCfovea simRun entered @@@@");
// 99/8/3 aa:in acutallity x should map with j, and y should map with i.
    pfcfovea.set(  system.nsldiff.eval(pfcfovea,pfcfoveatm, __tempPFCfovea3.setReference(NslAdd.eval(__tempPFCfovea3.get(), __tempPFCfovea0.setReference(NslSub.eval(__tempPFCfovea0.get(), 0, pfcfovea)), (__tempPFCfovea2.setReference(__tempPFCfovea1.setReference(LIPvis_in.get((int)FOVEAX.get(), (int)FOVEAY.get())).get()*(Fixation-DisFixation)))))));
    PFCfovea_out.set( Nsl2Sigmoid.eval(pfcfovea,PFCfoveasigma1, PFCfoveasigma2,
				  PFCfoveasigma3, PFCfoveasigma4));
}
public void makeConn(){
}

	/******************************************************/
	/*                                                    */
	/* Generated by nslc.src.NslCompiler. Do not edit these lines! */
	/*                                                    */
	/******************************************************/

	/* Constructor and related methods */
	/* makeinst() declared variables */

	/* Formal parameters */
	int array_size;

	/* Temporary variables */
		NslDouble2 __tempPFCfovea0 = new NslDouble2(1, 1);
		NslDouble0 __tempPFCfovea1 = new NslDouble0();
		NslDouble0 __tempPFCfovea2 = new NslDouble0();
		NslDouble2 __tempPFCfovea3 = new NslDouble2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public PFCfovea(String nslName, NslModule nslParent, int array_size)
{
		super(nslName, nslParent);
		this.array_size=array_size;
		initSys();
		makeInstPFCfovea(nslName, nslParent, array_size);
	}

	public void makeInstPFCfovea(String nslName, NslModule nslParent, int array_size)
{ 
		Object[] nslArgs=new Object[]{array_size};
		callFromConstructorTop(nslArgs);
		LIPvis_in = new NslDinDouble2("LIPvis_in", this, array_size, array_size);
		PFCfovea_out = new NslDoutDouble2("PFCfovea_out", this, array_size, array_size);
		pfcfovea = new NslDouble2("pfcfovea", this, array_size, array_size);
		FOVEAX = new NslInt0("FOVEAX", this, 4);
		FOVEAY = new NslInt0("FOVEAY", this, 4);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end PFCfovea

