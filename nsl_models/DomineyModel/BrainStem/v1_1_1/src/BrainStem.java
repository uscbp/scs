package DomineyModel.BrainStem.v1_1_1.src;
import DomineyModel.LLBN.v1_1_1.src.*;
import DomineyModel.Motor.v1_1_1.src.*;
import DomineyModel.EyePositionAndVelocity.v1_1_1.src.*;
import DomineyModel.TNChange.v1_1_1.src.*;
import DomineyModel.Bursters.v1_1_1.src.*;

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

public class BrainStem extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  BrainStem
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat2 supcol; // 
public  NslDinFloat2 fefsac; // 
public  NslDoutFloat0 horizontalVelocity; // 
public  NslDoutFloat0 verticalVelocity; // 
public  NslDoutFloat0 horizontalTheta; // 
public  NslDoutFloat0 verticalTheta; // 
public  NslDoutFloat2 saccademask; // 
public  LLBN llbnl; // 
public  Motor motorl; // 
public  Motor motorr; // 
public  Motor motoru; // 
public  Motor motord; // 
public  EyePositionAndVelocity eyePosAndVell; // 
public  TNChange tnChangel; // 
public  TNChange tnChanger; // 
public  TNChange tnChangeu; // 
public  TNChange tnChanged; // 
public  Bursters burstersl; // 

//methods 
public void simRun() 
{
	if (system.debug>=2) 
	{
		system.nslPrintln("BrainStem:simRun");
	}
}
public void makeConn(){
    nslConnect(supcol,llbnl.supcol);
    nslConnect(supcol,motorl.supcol);
    nslConnect(supcol,motorr.supcol);
    nslConnect(supcol,motoru.supcol);
    nslConnect(supcol,motord.supcol);
    nslConnect(fefsac,llbnl.fefsac);
    nslConnect(fefsac,motorl.fefsac);
    nslConnect(fefsac,motorr.fefsac);
    nslConnect(fefsac,motoru.fefsac);
    nslConnect(fefsac,motord.fefsac);
    nslConnect(motord.ebn,burstersl.debn);
    nslConnect(motord.ebn,tnChanged.ebn);
    nslConnect(motord.ebn,tnChangeu.opposite_ebn);
    nslConnect(motord.ebn,eyePosAndVell.debn);
    nslConnect(motord.fefgate,tnChanged.fefgate);
    nslConnect(motord.tnDelta,tnChanged.tnDelta);
    nslConnect(burstersl.saccademask,saccademask);
    nslConnect(tnChangeu.tn,motoru.tn);
    nslConnect(tnChangeu.tn,eyePosAndVell.utn);
    nslConnect(tnChangeu.tnChange,eyePosAndVell.utnChange);
    nslConnect(motoru.ebn,burstersl.uebn);
    nslConnect(motoru.ebn,tnChangeu.ebn);
    nslConnect(motoru.ebn,tnChanged.opposite_ebn);
    nslConnect(motoru.ebn,eyePosAndVell.uebn);
    nslConnect(motoru.fefgate,tnChangeu.fefgate);
    nslConnect(motoru.tnDelta,tnChangeu.tnDelta);
    nslConnect(tnChanged.tn,motord.tn);
    nslConnect(tnChanged.tn,eyePosAndVell.dtn);
    nslConnect(tnChanged.tnChange,eyePosAndVell.dtnChange);
    nslConnect(tnChanger.tn,motorr.tn);
    nslConnect(tnChanger.tn,eyePosAndVell.rtn);
    nslConnect(tnChanger.tnChange,eyePosAndVell.rtnChange);
    nslConnect(tnChangel.tn,motorl.tn);
    nslConnect(tnChangel.tn,eyePosAndVell.ltn);
    nslConnect(tnChangel.tnChange,eyePosAndVell.ltnChange);
    nslConnect(motorr.ebn,burstersl.rebn);
    nslConnect(motorr.ebn,tnChanger.ebn);
    nslConnect(motorr.ebn,tnChangel.opposite_ebn);
    nslConnect(motorr.ebn,eyePosAndVell.rebn);
    nslConnect(motorr.fefgate,tnChanger.fefgate);
    nslConnect(motorr.tnDelta,tnChanger.tnDelta);
    nslConnect(eyePosAndVell.horizontalVelocity,horizontalVelocity);
    nslConnect(eyePosAndVell.verticalVelocity,verticalVelocity);
    nslConnect(eyePosAndVell.horizontalTheta,horizontalTheta);
    nslConnect(eyePosAndVell.verticalTheta,verticalTheta);
    nslConnect(llbnl.llbn,motorl.llbn);
    nslConnect(llbnl.llbn,motord.llbn);
    nslConnect(llbnl.llbn,motorr.llbn);
    nslConnect(llbnl.llbn,motoru.llbn);
    nslConnect(motorl.ebn,burstersl.lebn);
    nslConnect(motorl.ebn,tnChangel.ebn);
    nslConnect(motorl.ebn,eyePosAndVell.lebn);
    nslConnect(motorl.ebn,tnChanger.opposite_ebn);
    nslConnect(motorl.fefgate,tnChangel.fefgate);
    nslConnect(motorl.tnDelta,tnChangel.tnDelta);
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
	int numOfDirections;
	NslFloat0 nWTAThreshold;

	/* Temporary variables */

	/* GENERIC CONSTRUCTOR: */
	public BrainStem(String nslName, NslModule nslParent, int stdsz, int numOfDirections, NslFloat0 nWTAThreshold)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		this.numOfDirections=numOfDirections;
		this.nWTAThreshold=nWTAThreshold;
		initSys();
		makeInstBrainStem(nslName, nslParent, stdsz, numOfDirections, nWTAThreshold);
	}

	public void makeInstBrainStem(String nslName, NslModule nslParent, int stdsz, int numOfDirections, NslFloat0 nWTAThreshold)
{ 
		Object[] nslArgs=new Object[]{stdsz, numOfDirections, nWTAThreshold};
		callFromConstructorTop(nslArgs);
		nWTAThreshold = new NslFloat0("nWTAThreshold", this);
		supcol = new NslDinFloat2("supcol", this, stdsz, stdsz);
		fefsac = new NslDinFloat2("fefsac", this, stdsz, stdsz);
		horizontalVelocity = new NslDoutFloat0("horizontalVelocity", this);
		verticalVelocity = new NslDoutFloat0("verticalVelocity", this);
		horizontalTheta = new NslDoutFloat0("horizontalTheta", this);
		verticalTheta = new NslDoutFloat0("verticalTheta", this);
		saccademask = new NslDoutFloat2("saccademask", this, stdsz, stdsz);
		llbnl = new LLBN("llbnl", this, stdsz);
		motorl = new Motor("motorl", this, stdsz, 1);
		motorr = new Motor("motorr", this, stdsz, 2);
		motoru = new Motor("motoru", this, stdsz, 3);
		motord = new Motor("motord", this, stdsz, 4);
		eyePosAndVell = new EyePositionAndVelocity("eyePosAndVell", this);
		tnChangel = new TNChange("tnChangel", this);
		tnChanger = new TNChange("tnChanger", this);
		tnChangeu = new TNChange("tnChangeu", this);
		tnChanged = new TNChange("tnChanged", this);
		burstersl = new Bursters("burstersl", this, stdsz);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end BrainStem

