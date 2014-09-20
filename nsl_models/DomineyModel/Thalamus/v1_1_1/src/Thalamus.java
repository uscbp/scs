package DomineyModel.Thalamus.v1_1_1.src;

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

public class Thalamus extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  Thalamus
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat2 fefmem; // 
public  NslDinFloat2 scDelay; // 
public  NslDinFloat2 snrmem; // 
public  NslDinFloat2 erasure2; // 
public  NslDoutFloat2 thmem; // 
private NslFloat0 thmemPot_tm; // 
private NslFloat0 thmemPot_k1; // 
private NslFloat0 thmemPot_k2; // 
private NslFloat0 thmemPot_k3; // 
private NslFloat0 thmem_x1; // 
private NslFloat0 thmem_x2; // 
private NslFloat0 thmem_y1; // 
private NslFloat0 thmem_y2; // 
private  NslFloat2 thmemPot; // 
private  NslFloat2 erasureConvSCDelay; // 
private int center; // 

//methods 
public void initRun() 
{
       thmem.set(0);
       thmemPot.set(0);
       erasureConvSCDelay.set( 0);

	center = (int)stdsz/2;

	thmemPot_tm.set( .006) ;
	thmemPot_k1.set( 1) ;	
	thmemPot_k2.set( 4) ;
	thmemPot_k3.set( 1) ;// aa: diff from 98
	thmem_x1.set(0) ;
	thmem_x2.set(  45);
	thmem_y1.set( 0) ;
	thmem_y2.set(10)  ;
}

public void simRun() 
{
	erasureConvSCDelay.set(__tempThalamus0.setReference(NslConv.eval(__tempThalamus0.get(), erasure2, scDelay)));

	thmemPot.set(system.nsldiff.eval(thmemPot, thmemPot_tm, __tempThalamus7.setReference(NslSub.eval(__tempThalamus7.get(), __tempThalamus6.setReference(NslSub.eval(__tempThalamus6.get(), __tempThalamus5.setReference(NslAdd.eval(__tempThalamus5.get(), __tempThalamus1.setReference(NslSub.eval(__tempThalamus1.get(), 0, thmemPot)), __tempThalamus2.setReference(NslElemMult.eval(__tempThalamus2.get(), thmemPot_k3, fefmem)))), __tempThalamus3.setReference(NslElemMult.eval(__tempThalamus3.get(), thmemPot_k1, snrmem)))), __tempThalamus4.setReference(NslElemMult.eval(__tempThalamus4.get(), thmemPot_k2, erasureConvSCDelay))))));
	thmemPot.set(center, center,   0); // the " - FOVEA" term - so we don't 	 "rememPotber" the fixation point
	thmem.set(  NslSigmoid.eval(thmemPot,thmem_x1,thmem_x2,thmem_y1,thmem_y2));

	if (system.debug>25) 
	{
		system.nslPrintln("Thalamus: simRun: thmem");
		system.nslPrintln(thmem);
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
		NslFloat2 __tempThalamus0 = new NslFloat2(1, 1);
		NslFloat2 __tempThalamus1 = new NslFloat2(1, 1);
		NslFloat2 __tempThalamus2 = new NslFloat2(1, 1);
		NslFloat2 __tempThalamus3 = new NslFloat2(1, 1);
		NslFloat2 __tempThalamus4 = new NslFloat2(1, 1);
		NslFloat2 __tempThalamus5 = new NslFloat2(1, 1);
		NslFloat2 __tempThalamus6 = new NslFloat2(1, 1);
		NslFloat2 __tempThalamus7 = new NslFloat2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public Thalamus(String nslName, NslModule nslParent, int stdsz)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		initSys();
		makeInstThalamus(nslName, nslParent, stdsz);
	}

	public void makeInstThalamus(String nslName, NslModule nslParent, int stdsz)
{ 
		Object[] nslArgs=new Object[]{stdsz};
		callFromConstructorTop(nslArgs);
		fefmem = new NslDinFloat2("fefmem", this, stdsz, stdsz);
		scDelay = new NslDinFloat2("scDelay", this, stdsz, stdsz);
		snrmem = new NslDinFloat2("snrmem", this, stdsz, stdsz);
		erasure2 = new NslDinFloat2("erasure2", this, stdsz, stdsz);
		thmem = new NslDoutFloat2("thmem", this, stdsz, stdsz);
		thmemPot_tm = new NslFloat0("thmemPot_tm", this);
		thmemPot_k1 = new NslFloat0("thmemPot_k1", this);
		thmemPot_k2 = new NslFloat0("thmemPot_k2", this);
		thmemPot_k3 = new NslFloat0("thmemPot_k3", this);
		thmem_x1 = new NslFloat0("thmem_x1", this);
		thmem_x2 = new NslFloat0("thmem_x2", this);
		thmem_y1 = new NslFloat0("thmem_y1", this);
		thmem_y2 = new NslFloat0("thmem_y2", this);
		thmemPot = new NslFloat2("thmemPot", this, stdsz, stdsz);
		erasureConvSCDelay = new NslFloat2("erasureConvSCDelay", this, stdsz, stdsz);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end Thalamus

