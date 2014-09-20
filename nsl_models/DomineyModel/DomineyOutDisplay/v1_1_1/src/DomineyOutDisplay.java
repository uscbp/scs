package DomineyModel.DomineyOutDisplay.v1_1_1.src;

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

public class DomineyOutDisplay extends NslJavaModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  DomineyOutDisplay
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat2 visualinputSub; // 
public  NslDinFloat2 stimSC; // 
public  NslDinFloat2 stimFEF; // 
public  NslDinFloat0 posteriorParietalCenter; // 
public  NslDinFloat2 ppqv; // 
public  NslDinFloat2 scqv; // 
public  NslDinFloat2 supcol; // 
public  NslDinFloat2 scsac; // 
public  NslDinFloat2 fefsac; // 
public  NslDinFloat2 fefvis; // 
public  NslDinFloat2 fefmem; // 
public  NslDinFloat2 thmem; // 
public  NslDinFloat0 horizontalTheta; // 
public  NslDinFloat0 verticalTheta; // 
private boolean goodstatus=false; // false=fail; true=success
private int center; // 
private int centerP3; // 
private int centerP2; // 
private int centerM2; // 
private int centerM3; // 
private NslFloat0 fixation; // 
private NslFloat0 visinP3M3; // 
private NslFloat0 visinM3P3; // 
private NslFloat0 visinM3P0; // 
private NslFloat0 visinP0M2; // 
private NslFloat0 visinM2P0; // 
private NslFloat0 visinM2P2; // 
private NslFloat0 visinM2M3; // 
private NslFloat0 stimfefM3P0; // 
private NslFloat0 stimscM3P0; // 
private NslFloat0 stimfefP0M2; // 
private NslFloat0 stimscP0M2; // 

//methods 
public void initModule() 
{
	// note this is different than that declared in the visualinput.mod
	center = (int)stdsz/2;
	centerP3 = center+3 ;
	centerP2 = center+2 ;
	centerM3 = center-3 ;
	centerM2 = center-2 ;


	// For time plots
	if (!(system.getNoDisplay())) 
	{		
		nslAddTemporalCanvas("output", "fixation", fixation,0,100, NslColor.getColor("BLACK"));
		nslAddTemporalCanvas("output", "ppCenter", posteriorParietalCenter,0,100, NslColor.getColor("BLACK"));
		nslAddTemporalCanvas("output", "horizTheta", horizontalTheta,-5.5,5.5,NslColor.getColor("BLACK"));
		nslAddTemporalCanvas("output", "vertTheta", verticalTheta,-5.5,5.5,NslColor.getColor("BLACK"));
		nslAddAreaCanvas("output", "thMem", thmem,0,20);
	        nslSetColumns(1, "output");	
	}//if not noDisplay
}

public void initRun()
{
	fixation.set(0);
	visinP3M3.set(0);
	visinM3P3.set(0);
	visinM3P0.set(0);
	visinP0M2.set(0);
	visinM2P2.set(0);
	visinM2M3.set(0);
	stimfefM3P0.set(0);
	stimscM3P0.set(0);
	stimfefP0M2.set(0);
	stimscP0M2.set(0);

	if (system.debug>27) 
	{
		system.nslPrintln("DomOutDisplay:initRun");
	}
	getValues();

}

public void simRun()
{
	getValues();	           
}

public void getValues()
{	
	fixation.set(visualinputSub.get(center,center));
	visinP3M3.set(visualinputSub.get(centerP3,centerM3));
	visinM3P3.set(visualinputSub.get(centerM3,centerP3));
	visinM3P0.set(visualinputSub.get(centerM3,center));
	visinP0M2.set(visualinputSub.get(center,centerM2));
	visinM2P2.set(visualinputSub.get(centerM2,centerP2));
	visinM2M3.set(visualinputSub.get(centerM2,centerM3));

	stimfefM3P0.set(stimFEF.get(centerM3,center));
	stimscM3P0.set(stimSC.get(centerM3,center));
	stimfefP0M2.set(stimFEF.get(center,centerM2));
	stimscP0M2.set(stimSC.get(center,centerM2));
}  //end getValues
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
	int stdsz;
	int bigsz;

	/* Temporary variables */

	/* GENERIC CONSTRUCTOR: */
	public DomineyOutDisplay(String nslName, NslModule nslParent, int stdsz, int bigsz)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		this.bigsz=bigsz;
		initSys();
		makeInstDomineyOutDisplay(nslName, nslParent, stdsz, bigsz);
	}

	public void makeInstDomineyOutDisplay(String nslName, NslModule nslParent, int stdsz, int bigsz)
{ 
		Object[] nslArgs=new Object[]{stdsz, bigsz};
		callFromConstructorTop(nslArgs);
		visualinputSub = new NslDinFloat2("visualinputSub", this, stdsz, stdsz);
		stimSC = new NslDinFloat2("stimSC", this, stdsz, stdsz);
		stimFEF = new NslDinFloat2("stimFEF", this, stdsz, stdsz);
		posteriorParietalCenter = new NslDinFloat0("posteriorParietalCenter", this);
		ppqv = new NslDinFloat2("ppqv", this, stdsz, stdsz);
		scqv = new NslDinFloat2("scqv", this, stdsz, stdsz);
		supcol = new NslDinFloat2("supcol", this, stdsz, stdsz);
		scsac = new NslDinFloat2("scsac", this, stdsz, stdsz);
		fefsac = new NslDinFloat2("fefsac", this, stdsz, stdsz);
		fefvis = new NslDinFloat2("fefvis", this, stdsz, stdsz);
		fefmem = new NslDinFloat2("fefmem", this, stdsz, stdsz);
		thmem = new NslDinFloat2("thmem", this, stdsz, stdsz);
		horizontalTheta = new NslDinFloat0("horizontalTheta", this);
		verticalTheta = new NslDinFloat0("verticalTheta", this);
		fixation = new NslFloat0("fixation", this);
		visinP3M3 = new NslFloat0("visinP3M3", this);
		visinM3P3 = new NslFloat0("visinM3P3", this);
		visinM3P0 = new NslFloat0("visinM3P0", this);
		visinP0M2 = new NslFloat0("visinP0M2", this);
		visinM2P0 = new NslFloat0("visinM2P0", this);
		visinM2P2 = new NslFloat0("visinM2P2", this);
		visinM2M3 = new NslFloat0("visinM2M3", this);
		stimfefM3P0 = new NslFloat0("stimfefM3P0", this);
		stimscM3P0 = new NslFloat0("stimscM3P0", this);
		stimfefP0M2 = new NslFloat0("stimfefP0M2", this);
		stimscP0M2 = new NslFloat0("stimscP0M2", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end DomineyOutDisplay
