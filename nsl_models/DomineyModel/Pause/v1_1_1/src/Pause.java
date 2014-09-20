package DomineyModel.Pause.v1_1_1.src;

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

public class Pause extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  Pause
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat2 supcol; // 
public  NslDinFloat2 fefsac; // 
public  NslDinFloat2 stm; // 
public  NslDinFloat2 weights; // 
public  NslDinFloat2 mlbn; // 
public  NslDinFloat0 ebn; // 
public  NslDoutFloat0 pause; // 
private NslFloat0 pausePot; // pausePot cells are inhibited by Trig and are reactivated when RI = DELTA
private  NslFloat2 arrTrig; // 
private NslFloat0 dataTrig; // 
private NslFloat0 dataTrigN; // 
private NslFloat0 dataIdelta; // 
private NslFloat0 datadelta; // 
private  NslFloat2 arrdelta; // 
private  NslFloat2 arrMask; // 
private NslFloat0 alldirectionpause_k1; // 
private NslFloat0 arrTrig_tm; // 
private NslFloat0 arrTrig_k1; // 
private NslFloat0 pausePot_tm; // 
private NslFloat0 pausePot_k1; // 
private NslFloat0 dataTrigN_k1; // 
private NslFloat0 dataTrigN_k2; // 
private NslFloat0 dataRTrigN_k3; // 
private NslFloat0 arrMask_k1; // 
private NslFloat0 arrMask_k2; // 
private NslFloat0 arrMask_k3; // 
private NslFloat0 dataIdelta_k1; // 
private NslFloat0 datadelta_k1; // 
private NslFloat0 datadelta_k2; // 
private NslFloat0 datadelta_k3; // 
private NslFloat0 pause_k1; // 
private NslFloat0 pause_k2; // 
private NslFloat0 pause_k3; // 
private NslFloat0 resetInteg; // 
private NslFloat0 resetIntegPot; // 
private NslFloat0 resetIntegPot_k1; // 
private NslFloat0 resetInteg_k1; // 
private NslFloat0 resetInteg_k2; // 
private NslFloat0 resetInteg_k3; // 
private float maxdelta; // 

//methods 
public void initRun() 
{
	resetInteg.set(0);

	 pause.set( 0);
	pausePot.set( 0);
	arrTrig.set( 0);
	dataTrig.set(0);
	dataTrigN.set( 0);
	dataIdelta.set(0);
	datadelta.set(0);
	arrdelta.set( 0);
	arrMask.set(0);
	maxdelta=0;

	alldirectionpause_k1.set(1.1);
	pausePot_tm.set(0.006);

 	arrTrig_tm.set(0.006);
	arrTrig_k1.set(50);
	// aa: 2.1.7 model had the left pausePot with a k1 of 1 while the rest were 0.1.
	pausePot_k1.set(0.1);
	dataTrigN_k1.set(0);
	dataTrigN_k2.set(0);
	dataRTrigN_k3.set(0);
	arrMask_k1.set(240);
	arrMask_k2.set(0);
	arrMask_k3.set(40);
	dataIdelta_k1.set(3);
	datadelta_k1.set(0);
	datadelta_k2.set(0);
	datadelta_k3.set(0);
	pause_k1.set(8);
	pause_k2.set(300);
	pause_k3.set(0);
	resetIntegPot_k1.set(0.031);
	resetInteg_k1.set(0);
	resetInteg_k2.set(0);
	resetInteg_k3.set(0);


}

public void simRun() 
{
	// The trigger cells get input from FEF and supcol.  Once the saccade begins,
	// these cells are inhibited so that residual activity in FEF and supcol won't
	// prevent short saccades from ending
	arrTrig.set(system.nsldiff.eval(arrTrig,arrTrig_tm,__tempPause6.setReference(NslSub.eval(__tempPause6.get(), __tempPause5.setReference(NslAdd.eval(__tempPause5.get(), __tempPause4.setReference(NslAdd.eval(__tempPause4.get(), __tempPause0.setReference(NslSub.eval(__tempPause0.get(), 0, arrTrig)), (__tempPause1.setReference(NslElemMult.eval(__tempPause1.get(), stm, supcol))))), (__tempPause2.setReference(NslElemMult.eval(__tempPause2.get(), stm, fefsac))))), __tempPause3.setReference(NslElemMult.eval(__tempPause3.get(), arrTrig_k1, arrMask))))));

	pausePot.set(system.nsldiff.eval(pausePot,pausePot_tm,__tempPause12.setReference(__tempPause11.setReference(__tempPause10.setReference(__tempPause7.setReference(-pausePot.get()).get()+__tempPause8.setReference(pausePot_k1.get()*dataTrigN.get()).get()).get()+datadelta.get()).get()-__tempPause9.setReference(alldirectionpause_k1.get()*resetInteg.get()).get())));

	resetIntegPot.set(  __tempPause15.setReference(__tempPause14.setReference(resetInteg.get()+__tempPause13.setReference(resetIntegPot_k1.get()*ebn.get()).get()).get()-pause.get()));
		// this thresholding allows us to manipulate the accuracy
		// of the saccade by specifying how close DELTA and RI
		// must be to terminate the saccade (triger the pausePot cells)


	arrdelta.set(  __tempPause16.setReference(NslElemMult.eval(__tempPause16.get(), arrMask, weights)));  // weights has values that increase
		// as you move to the periphery.  arrMask has a constant
		// value once the inputs are above threshold.  The result
		// is that the spatial FEF and supcol signal gets converted into
		// delatA which is used to produce datadelta which is compared
		// to the resetable integrator to terminate the saccade
	//-------------------
	dataTrig.set(  NslMaxValue.eval(arrTrig));
	dataTrigN.set(  NslRamp.eval(dataTrig,dataTrigN_k1,dataTrigN_k2,dataRTrigN_k3));
		// this thresholding allows us to manipulate the accuracy
		// of the saccade by specifying how close DELTA and RI
		// must be to terminate the saccade (triger the pausePot cells)
	resetInteg.set(  NslRamp.eval(resetIntegPot, resetInteg_k1, resetInteg_k2, resetInteg_k3));

	arrMask.set(  NslStep.eval(mlbn,arrMask_k1,arrMask_k2,arrMask_k3));
		// fires at a fixed rate when llbns above threshold.
		// used to get the Delta

	maxdelta = NslMaxValue.eval(arrdelta);
	dataIdelta.set(  __tempPause18.setReference(maxdelta-__tempPause17.setReference(dataIdelta_k1.get()*pause.get()).get()));
	datadelta.set(  NslRamp.eval(dataIdelta,datadelta_k1,datadelta_k2,datadelta_k3));
	pause.set(  NslStep.eval(pausePot,pause_k1,pause_k2,pause_k3));

	if (system.debug>=9) 
	{
		system.nslPrintln("debug: Pause: dataTrigN");
		system.nslPrintln(dataTrigN);
		system.nslPrintln("debug: Pause: pause");
		system.nslPrintln(pause);
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
		NslFloat2 __tempPause0 = new NslFloat2(1, 1);
		NslFloat2 __tempPause1 = new NslFloat2(1, 1);
		NslFloat2 __tempPause2 = new NslFloat2(1, 1);
		NslFloat2 __tempPause3 = new NslFloat2(1, 1);
		NslFloat2 __tempPause4 = new NslFloat2(1, 1);
		NslFloat2 __tempPause5 = new NslFloat2(1, 1);
		NslFloat2 __tempPause6 = new NslFloat2(1, 1);
		NslFloat0 __tempPause7 = new NslFloat0();
		NslFloat0 __tempPause8 = new NslFloat0();
		NslFloat0 __tempPause9 = new NslFloat0();
		NslFloat0 __tempPause10 = new NslFloat0();
		NslFloat0 __tempPause11 = new NslFloat0();
		NslFloat0 __tempPause12 = new NslFloat0();
		NslFloat0 __tempPause13 = new NslFloat0();
		NslFloat0 __tempPause14 = new NslFloat0();
		NslFloat0 __tempPause15 = new NslFloat0();
		NslFloat2 __tempPause16 = new NslFloat2(1, 1);
		NslFloat0 __tempPause17 = new NslFloat0();
		NslFloat0 __tempPause18 = new NslFloat0();

	/* GENERIC CONSTRUCTOR: */
	public Pause(String nslName, NslModule nslParent, int stdsz)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		initSys();
		makeInstPause(nslName, nslParent, stdsz);
	}

	public void makeInstPause(String nslName, NslModule nslParent, int stdsz)
{ 
		Object[] nslArgs=new Object[]{stdsz};
		callFromConstructorTop(nslArgs);
		supcol = new NslDinFloat2("supcol", this, stdsz, stdsz);
		fefsac = new NslDinFloat2("fefsac", this, stdsz, stdsz);
		stm = new NslDinFloat2("stm", this, stdsz, stdsz);
		weights = new NslDinFloat2("weights", this, stdsz, stdsz);
		mlbn = new NslDinFloat2("mlbn", this, stdsz, stdsz);
		ebn = new NslDinFloat0("ebn", this);
		pause = new NslDoutFloat0("pause", this);
		pausePot = new NslFloat0("pausePot", this);
		arrTrig = new NslFloat2("arrTrig", this, stdsz, stdsz);
		dataTrig = new NslFloat0("dataTrig", this);
		dataTrigN = new NslFloat0("dataTrigN", this);
		dataIdelta = new NslFloat0("dataIdelta", this);
		datadelta = new NslFloat0("datadelta", this);
		arrdelta = new NslFloat2("arrdelta", this, stdsz, stdsz);
		arrMask = new NslFloat2("arrMask", this, stdsz, stdsz);
		alldirectionpause_k1 = new NslFloat0("alldirectionpause_k1", this);
		arrTrig_tm = new NslFloat0("arrTrig_tm", this);
		arrTrig_k1 = new NslFloat0("arrTrig_k1", this);
		pausePot_tm = new NslFloat0("pausePot_tm", this);
		pausePot_k1 = new NslFloat0("pausePot_k1", this);
		dataTrigN_k1 = new NslFloat0("dataTrigN_k1", this);
		dataTrigN_k2 = new NslFloat0("dataTrigN_k2", this);
		dataRTrigN_k3 = new NslFloat0("dataRTrigN_k3", this);
		arrMask_k1 = new NslFloat0("arrMask_k1", this);
		arrMask_k2 = new NslFloat0("arrMask_k2", this);
		arrMask_k3 = new NslFloat0("arrMask_k3", this);
		dataIdelta_k1 = new NslFloat0("dataIdelta_k1", this);
		datadelta_k1 = new NslFloat0("datadelta_k1", this);
		datadelta_k2 = new NslFloat0("datadelta_k2", this);
		datadelta_k3 = new NslFloat0("datadelta_k3", this);
		pause_k1 = new NslFloat0("pause_k1", this);
		pause_k2 = new NslFloat0("pause_k2", this);
		pause_k3 = new NslFloat0("pause_k3", this);
		resetInteg = new NslFloat0("resetInteg", this);
		resetIntegPot = new NslFloat0("resetIntegPot", this);
		resetIntegPot_k1 = new NslFloat0("resetIntegPot_k1", this);
		resetInteg_k1 = new NslFloat0("resetInteg_k1", this);
		resetInteg_k2 = new NslFloat0("resetInteg_k2", this);
		resetInteg_k3 = new NslFloat0("resetInteg_k3", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end Pause

