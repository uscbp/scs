package DomineyModel.TNDelta.v1_1_1.src;

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

public class TNDelta extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  TNDelta
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat2 fefsac; // 
public  NslDinFloat0 tn; // 
public  NslDoutFloat0 fefgate; // 
public  NslDoutFloat0 tnDelta; // 
private NslFloat0 fefswitchPot_k1; // 
private NslFloat0 fefgatePot_tm; // 
private NslFloat0 fefswitch_k1; // 
private NslFloat0 fefswitch_k2; // 
private NslFloat0 fefswitch_k3; // 
private NslFloat0 fefgate_k1; // 
private NslFloat0 fefgate_k2; // 
private NslFloat0 fefgate_k3; // 
private NslFloat0 fefswitchPot; // 
private NslFloat0 fefgatePot; // 
private NslFloat0 fefswitch; // 
private NslFloat0 tnDelay2_tm; // 
private NslInt0 protocolNum; // 
private NslFloat0 tnDelay3_tm; // 
private NslFloat0 tnDelta_tm; // 
private NslFloat0 tnDelay2; // 
private NslFloat0 tnDelay3; // 

//methods 
public void initRun() 
{
	if (system.debug>=10) 
	{
		system.nslPrintln("TNDelta:initRun");
	}

// 	only need if doing collision experiments
//	protocolNum=(NslInt0)nslGetValue("domineyModel.protocolNum");
	
	fefgate.set( 0);
	fefswitch.set(0);
	fefswitchPot.set(0);
	fefgatePot.set(0);

	fefswitchPot_k1.set( 0.21);
	fefgatePot_tm.set(0.04);
	fefswitch_k1.set(89);
	fefswitch_k2.set(0);
	fefswitch_k3.set(90);
	fefgate_k1.set(40);  // aa: 2.1.7 also mentions 45
	fefgate_k2.set( 0);

	//98/11/20 aa: fefgate_k3 = 1 for collisions experiments
	// is what the 2.1.7 file had as the default
	fefgate_k3.set(0);  // for non-collision experiments
	// fefgate_k3=1;  // for collision experiments

	tnDelta.set(0);  //use to be TNCHANGE2 in NSL2.1.7
	tnDelay2.set(154);
	tnDelay3.set(154);

	tnDelay2_tm.set(  0.02);
	tnDelay3_tm.set(  0.04);  // aa: 2.1.7 had up direction as 0.02
	tnDelta_tm.set(  0.006);
}

public void simRun() 
{
	fefswitchPot.set(  fefswitchPot_k1.get()*NslMaxValue.eval(fefsac));
	fefgatePot.set(system.nsldiff.eval(fefgatePot,fefgatePot_tm,__tempTNDelta1.setReference(__tempTNDelta0.setReference(-fefgatePot.get()).get()+fefswitch.get()) ));
	fefswitch.set(  NslStep.eval(fefswitchPot,fefswitch_k1,fefswitch_k2,fefswitch_k3));
	fefgate.set(  NslStep.eval(fefgatePot,fefgate_k1,fefgate_k2,fefgate_k3));

	tnDelay2.set(system.nsldiff.eval(tnDelay2,tnDelay2_tm, __tempTNDelta3.setReference(__tempTNDelta2.setReference(-tnDelay2.get()).get()+tn.get()) ));
	tnDelay3.set(system.nsldiff.eval(tnDelay3,tnDelay3_tm, __tempTNDelta5.setReference(__tempTNDelta4.setReference(-tnDelay3.get()).get()+tn.get()) ));
	tnDelta.set(system.nsldiff.eval(tnDelta,tnDelta_tm, __tempTNDelta8.setReference(__tempTNDelta7.setReference(__tempTNDelta6.setReference(-tnDelta.get()).get()+tnDelay2.get()).get()-tnDelay3.get()) ));

	if (system.debug>=10) 
	{
		system.nslPrintln("TNDelta:fefgate:"+fefgate.toString());
		system.nslPrintln("TNDelta:tnDelta:"+tnDelta.toString());
	}
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
	int stdsz;

	/* Temporary variables */
		NslFloat0 __tempTNDelta0 = new NslFloat0();
		NslFloat0 __tempTNDelta1 = new NslFloat0();
		NslFloat0 __tempTNDelta2 = new NslFloat0();
		NslFloat0 __tempTNDelta3 = new NslFloat0();
		NslFloat0 __tempTNDelta4 = new NslFloat0();
		NslFloat0 __tempTNDelta5 = new NslFloat0();
		NslFloat0 __tempTNDelta6 = new NslFloat0();
		NslFloat0 __tempTNDelta7 = new NslFloat0();
		NslFloat0 __tempTNDelta8 = new NslFloat0();

	/* GENERIC CONSTRUCTOR: */
	public TNDelta(String nslName, NslModule nslParent, int stdsz)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		initSys();
		makeInstTNDelta(nslName, nslParent, stdsz);
	}

	public void makeInstTNDelta(String nslName, NslModule nslParent, int stdsz)
{ 
		Object[] nslArgs=new Object[]{stdsz};
		callFromConstructorTop(nslArgs);
		fefsac = new NslDinFloat2("fefsac", this, stdsz, stdsz);
		tn = new NslDinFloat0("tn", this);
		fefgate = new NslDoutFloat0("fefgate", this);
		tnDelta = new NslDoutFloat0("tnDelta", this);
		fefswitchPot_k1 = new NslFloat0("fefswitchPot_k1", this);
		fefgatePot_tm = new NslFloat0("fefgatePot_tm", this);
		fefswitch_k1 = new NslFloat0("fefswitch_k1", this);
		fefswitch_k2 = new NslFloat0("fefswitch_k2", this);
		fefswitch_k3 = new NslFloat0("fefswitch_k3", this);
		fefgate_k1 = new NslFloat0("fefgate_k1", this);
		fefgate_k2 = new NslFloat0("fefgate_k2", this);
		fefgate_k3 = new NslFloat0("fefgate_k3", this);
		fefswitchPot = new NslFloat0("fefswitchPot", this);
		fefgatePot = new NslFloat0("fefgatePot", this);
		fefswitch = new NslFloat0("fefswitch", this);
		tnDelay2_tm = new NslFloat0("tnDelay2_tm", this);
		protocolNum = new NslInt0("protocolNum", this);
		tnDelay3_tm = new NslFloat0("tnDelay3_tm", this);
		tnDelta_tm = new NslFloat0("tnDelta_tm", this);
		tnDelay2 = new NslFloat0("tnDelay2", this);
		tnDelay3 = new NslFloat0("tnDelay3", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end TNDelta
