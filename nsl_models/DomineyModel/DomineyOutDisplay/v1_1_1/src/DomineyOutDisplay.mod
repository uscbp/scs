package DomineyModel.DomineyOutDisplay.v1_1_1.src;

nslJavaModule DomineyOutDisplay(int stdsz, int bigsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  DomineyOutDisplay
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat2 visualinputSub(stdsz,stdsz); // 
public NslDinFloat2 stimSC(stdsz,stdsz); // 
public NslDinFloat2 stimFEF(stdsz,stdsz); // 
public NslDinFloat0 posteriorParietalCenter(); // 
public NslDinFloat2 ppqv(stdsz, stdsz); // 
public NslDinFloat2 scqv(stdsz, stdsz); // 
public NslDinFloat2 supcol(stdsz, stdsz); // 
public NslDinFloat2 scsac(stdsz, stdsz); // 
public NslDinFloat2 fefsac(stdsz, stdsz); // 
public NslDinFloat2 fefvis(stdsz, stdsz); // 
public NslDinFloat2 fefmem(stdsz, stdsz); // 
public NslDinFloat2 thmem(stdsz, stdsz); // 
public NslDinFloat0 horizontalTheta(); // 
public NslDinFloat0 verticalTheta(); // 
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
	centerP3 = center + 3;
	centerP2 = center + 2;
	centerM3 = center - 3;
	centerM2 = center - 2;


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
	fixation=0;
	visinP3M3=0;
	visinM3P3=0;
	visinM3P0=0;
	visinP0M2=0;
	visinM2P2=0;
	visinM2M3=0;
	stimfefM3P0=0;
	stimscM3P0=0;
	stimfefP0M2=0;
	stimscP0M2=0;

	if (system.debug>27) 
	{
		nslPrintln("DomOutDisplay:initRun");
	}
	getValues();

}

public void simRun()
{
	getValues();	           
}

public void getValues()
{	
	fixation=visualinputSub.get(center,center);
	visinP3M3=visualinputSub.get(centerP3,centerM3);
	visinM3P3=visualinputSub.get(centerM3,centerP3);
	visinM3P0=visualinputSub.get(centerM3,center);
	visinP0M2=visualinputSub.get(center,centerM2);
	visinM2P2=visualinputSub.get(centerM2,centerP2);
	visinM2M3=visualinputSub.get(centerM2,centerM3);

	stimfefM3P0=stimFEF.get(centerM3,center);
	stimscM3P0=stimSC.get(centerM3,center);
	stimfefP0M2=stimFEF.get(center,centerM2);
	stimscP0M2=stimSC.get(center,centerM2);
}  //end getValues
public void makeConn(){
}
}//end DomineyOutDisplay

