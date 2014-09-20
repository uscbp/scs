package DomineyModel.DomineyModel.v1_1_1.src;
import DomineyModel.BrainStem.v1_1_1.src.*;
import DomineyModel.Retina.v1_1_1.src.*;
import DomineyModel.VisCortex.v1_1_1.src.*;
import DomineyModel.Erasure2.v1_1_1.src.*;
import DomineyModel.PPQV.v1_1_1.src.*;
import DomineyModel.FEF.v1_1_1.src.*;
import DomineyModel.FON.v1_1_1.src.*;
import DomineyModel.BasalGanglia.v1_1_1.src.*;
import DomineyModel.SC.v1_1_1.src.*;
import DomineyModel.Thalamus.v1_1_1.src.*;
import DomineyModel.DomineyOutDisplay.v1_1_1.src.*;
import DomineyModel.VisualInput.v1_1_1.src.*;
import DomineyModel.StimFEF.v1_1_1.src.*;
import DomineyModel.StimSC.v1_1_1.src.*;

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

public class DomineyModel extends NslModel{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  DomineyModel
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  BrainStem brainSteml; // 
public  Retina retinal; // 
public  VisCortex viscortexl; // 
public  Erasure2 erasure2_l; // 
public  PPQV ppqvl; // 
public  FEF fefl; // 
public  FON fonl; // 
public  BasalGanglia bgl; // 
public  SC supcoll; // 
public  Thalamus thall; // 
public  DomineyOutDisplay outl; // 
public  VisualInput visualinputl; // 
public  StimFEF stimfefl; // 
public  StimSC stimscl; // 
private int stdsz=9; // 
private int bigsz=27; // 
private NslFloat0 nWTAThreshold; // 
private NslInt0 protocolNum; // 
private String protocolString; // 15 protocolNums
private int numOfDirections=4; // 4 directions, left, right, up, down
private int curcycle=0; // 
private double curtime=0; // 
private double stepsize=0; // 

//methods 
public void initSys()
{
	system.setRunEndTime(0.7); // most protocols do not need this long
	system.nslSetRunDelta(0.005); // from NSL2.1.7 compensatory_new.nsl
	system.nslSetBuffering(false); // NSL2.1.7 way
}

public void callFromConstructorTop() 
{
	if (system.debug>=1) 
	{
		system.nslPrintln("DomineyModel:callFromConstructorTop");
	}
}

public void callFromConstructorBottom() 
{
	if (system.debug>=1) 
	{
		system.nslPrintln("DomineyModel:callFromConstructorBottom");
	}
}

public void singleProtocol()
{
	protocolNum.set(1);
}

public void memorySingle1Protocol()
{
	protocolNum.set(2);
}

public void memorySingle2Protocol()
{
	protocolNum.set(3);
}

public void double1Protocol()
{
	protocolNum.set(4);
}

public void double2Protocol()
{
	protocolNum.set(5);
}

public void lesionscProtocol()
{
	protocolNum.set(6);
}

public void lesionfefProtocol()
{
	protocolNum.set(7);
}

public void memoryDoubleProtocol()
{
	protocolNum.set(8);
}

public void stimulatedsc1Protocol()
{
	protocolNum.set(9);
}

public void stimulatedsc2Protocol()
{
	protocolNum.set(10);
}

public void stimulatedfef1Protocol()
{
	protocolNum.set(11);
}

public void stimulatedfef2Protocol()
{
	protocolNum.set(12);
}

public void stimulatedfef_lesionscProtocol()
{
	protocolNum.set(13);
}

public void stimulatedsc_lesionfefProtocol()
{
	protocolNum.set(14);
}

public void memoryDouble2Protocol()
{	
	protocolNum.set(15);
}

public void initModule()
{
	if (system.debug>=1) 
	{
		system.nslPrintln("DomineyModel:initModule: current time: "+curtime+" step "+stepsize);
	}

	nWTAThreshold.set(70); //From Dominey Lib.C file
	nslSetRunDelta(0.005); //98/8/4 aa

	// 98/10/27 aa: This should be selectable from the executive interface
	// See initRun code below.
	protocolNum.set(0);//"manual";

	nslDeclareProtocol("single","single");  //1
	nslDeclareProtocol("memorySingle1","memorySingle1");//fixation on until after targets
	nslDeclareProtocol("memorySingle2","memorySingle2");//fixation on until after targets
	nslDeclareProtocol("double1","double1");  //4
	nslDeclareProtocol("double2","double2");  //5
	nslDeclareProtocol("lesionsc","lesionsc"); //6
	nslDeclareProtocol("lesionfef","lesionfef"); //7
	nslDeclareProtocol("memoryDouble","memoryDouble"); //8
	nslDeclareProtocol("stimulatedsc1","stimulatedsc1"); //9
	nslDeclareProtocol("stimulatedsc2","stimulatedsc2"); //10
	nslDeclareProtocol("stimulatedfef1","stimulatedfef1"); //11
	nslDeclareProtocol("stimulatedfef2","stimulatedfef2"); //12
	nslDeclareProtocol("stimulatedfef_lesionsc","stimulatedfef_lesionsc"); //13
	nslDeclareProtocol("stimulatedsc_lesionfef","stimulatedsc_lesionfef"); //14
	nslDeclareProtocol("memoryDouble2","memoryDouble2"); //15

	system.addProtocolToAll("single");  //1
	system.addProtocolToAll("memorySingle1");//fixation on until after targets
	system.addProtocolToAll("memorySingle2");//fixation on until after targets
	system.addProtocolToAll("double1");  //4
	system.addProtocolToAll("double2");  //5
	system.addProtocolToAll("lesionsc"); //6
	system.addProtocolToAll("lesionfef"); //7
	system.addProtocolToAll("memoryDouble"); //8
	system.addProtocolToAll("stimulatedsc1"); //9
	system.addProtocolToAll("stimulatedsc2"); //10
	system.addProtocolToAll("stimulatedfef1"); //11
	system.addProtocolToAll("stimulatedfef2"); //12
	system.addProtocolToAll("stimulatedfef_lesionsc"); //13
	system.addProtocolToAll("stimulatedsc_lesionfef"); //14
	system.addProtocolToAll("memoryDouble2"); //15
}

public void initRun() 
{
	curtime=system.getCurrentTime();

	stepsize=system.nslGetRunDelta();

	if (system.debug>=1) 
	{
		system.nslPrintln("debug DomineyModel: initRun: current time: "+curtime+" step "+stepsize);
	}
}
public void makeConn(){
    nslConnect(erasure2_l.erasure2,thall.erasure2);
    nslConnect(erasure2_l.erasure2,ppqvl.erasure2);
    nslConnect(stimscl.stimSC,supcoll.stimulation);
    nslConnect(stimscl.stimSC,outl.stimSC);
    nslConnect(visualinputl.visualinput,retinal.visualinput);
    nslConnect(visualinputl.visualinputSub,outl.visualinputSub);
    nslConnect(viscortexl.posteriorParietal,fonl.posteriorParietal);
    nslConnect(viscortexl.posteriorParietal,ppqvl.posteriorParietal);
    nslConnect(fonl.fon,fefl.fon);
    nslConnect(fonl.fon,supcoll.fon);
    nslConnect(fonl.fovElem,ppqvl.fovElem);
    nslConnect(fonl.fonCenter,outl.posteriorParietalCenter);
    nslConnect(bgl.snrmem,thall.snrmem);
    nslConnect(bgl.snrsac,supcoll.snrsac);
    nslConnect(ppqvl.ppqv,fefl.ppqv);
    nslConnect(ppqvl.ppqv,supcoll.ppqv);
    nslConnect(ppqvl.ppqv,outl.ppqv);
    nslConnect(retinal.retina,viscortexl.retina);
    nslConnect(retinal.retina,supcoll.retina);
    nslConnect(supcoll.scqv,outl.scqv);
    nslConnect(supcoll.supcol,brainSteml.supcol);
    nslConnect(supcoll.supcol,outl.supcol);
    nslConnect(supcoll.scsac,outl.scsac);
    nslConnect(supcoll.scDelay,thall.scDelay);
    nslConnect(supcoll.scDelay,ppqvl.scDelay);
    nslConnect(fefl.fefvis,outl.fefvis);
    nslConnect(fefl.fefmem,thall.fefmem);
    nslConnect(fefl.fefmem,bgl.fefmem);
    nslConnect(fefl.fefmem,outl.fefmem);
    nslConnect(fefl.fefsac,bgl.fefsac);
    nslConnect(fefl.fefsac,supcoll.fefsac);
    nslConnect(fefl.fefsac,brainSteml.fefsac);
    nslConnect(fefl.fefsac,outl.fefsac);
    nslConnect(thall.thmem,fefl.thmem);
    nslConnect(thall.thmem,outl.thmem);
    nslConnect(brainSteml.horizontalVelocity,ppqvl.horizontalVelocity);
    nslConnect(brainSteml.verticalVelocity,ppqvl.verticalVelocity);
    nslConnect(brainSteml.horizontalTheta,retinal.horizontalTheta);
    nslConnect(brainSteml.horizontalTheta,outl.horizontalTheta);
    nslConnect(brainSteml.verticalTheta,retinal.verticalTheta);
    nslConnect(brainSteml.verticalTheta,outl.verticalTheta);
    nslConnect(brainSteml.saccademask,retinal.saccademask);
    nslConnect(brainSteml.saccademask,supcoll.saccademask);
    nslConnect(stimfefl.stimFEF,fefl.stimulation);
    nslConnect(stimfefl.stimFEF,outl.stimFEF);
}

	/******************************************************/
	/*                                                    */
	/* Generated by nslc.src.NslCompiler. Do not edit these lines! */
	/*                                                    */
	/******************************************************/

	/* Constructor and related methods */
	/* makeinst() declared variables */

	/* EMPTY CONSTRUCTOR: Called only for the top level module */
	public DomineyModel() {
		super("domineyModel",(NslModel)null);
		initSys();
		makeInstDomineyModel("domineyModel",null);
	}

	/* Formal parameters */

	/* Temporary variables */

	/* GENERIC CONSTRUCTOR: */
	public DomineyModel(String nslName, NslModule nslParent)
{
		super(nslName, nslParent);
		initSys();
		makeInstDomineyModel(nslName, nslParent);
	}

	public void makeInstDomineyModel(String nslName, NslModule nslParent)
{ 
		Object[] nslArgs=new Object[]{};
		callFromConstructorTop(nslArgs);
		brainSteml = new BrainStem("brainSteml", this, stdsz, numOfDirections, nWTAThreshold);
		retinal = new Retina("retinal", this, stdsz, bigsz);
		viscortexl = new VisCortex("viscortexl", this, stdsz);
		erasure2_l = new Erasure2("erasure2_l", this, stdsz);
		ppqvl = new PPQV("ppqvl", this, stdsz);
		fefl = new FEF("fefl", this, stdsz);
		fonl = new FON("fonl", this, stdsz);
		bgl = new BasalGanglia("bgl", this, stdsz);
		supcoll = new SC("supcoll", this, stdsz);
		thall = new Thalamus("thall", this, stdsz);
		outl = new DomineyOutDisplay("outl", this, stdsz, bigsz);
		visualinputl = new VisualInput("visualinputl", this, stdsz, bigsz);
		stimfefl = new StimFEF("stimfefl", this, stdsz);
		stimscl = new StimSC("stimscl", this, stdsz);
		nWTAThreshold = new NslFloat0("nWTAThreshold", this);
		protocolNum = new NslInt0("protocolNum", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end DomineyModel

