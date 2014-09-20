package DomineyModel.FEF.v1_1_1.src;

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

public class FEF extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  FEF
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat2 ppqv; // 
public  NslDinFloat2 thmem; // 
public  NslDinFloat2 fon; // 
public  NslDinFloat2 stimulation; // 
public  NslDoutFloat2 fefvis; // 
public  NslDoutFloat2 fefmem; // 
public  NslDoutFloat2 fefsac; // 
private NslFloat0 fefmemPot_tm; // 
private NslFloat0 fefvisPot_tm; // 
private NslFloat0 fefsacPot_tm; // 
private NslFloat0 fefmemPot_k1; // 
private NslFloat0 fefmemPot_k2; // 
private NslFloat0 fefmemPot_k4; // 
private NslFloat0 fefsacPot_k1; // 
private NslFloat0 fefsacPot_k2; // 
private NslFloat0 fefsacPot_k3; // 
private NslFloat0 fefvisPot_k1; // 
private NslFloat0 fefvisPot_k2; // 
private NslFloat0 fefvis_x1; // 
private NslFloat0 fefvis_x2; // 
private NslFloat0 fefvis_y1; // 
private NslFloat0 fefvis_y2; // 
private NslFloat0 fefmem_x1; // 
private NslFloat0 fefmem_x2; // 
private NslFloat0 fefmem_y1; // 
private NslFloat0 fefmem_y2; // 
private NslFloat0 fefsac_x1; // 
private NslFloat0 fefsac_x2; // 
private NslFloat0 fefsac_y1; // 
private NslFloat0 fefsac_y2; // 
private NslFloat0 fefsac_k1; // 
private  NslFloat2 fefvisPot; // 
private  NslFloat2 fefmemPot; // 
private  NslFloat2 fefsacPot; // 
private  NslFloat2 fefsactmp; // 
private NslInt0 protocolNum; // 
private int center; // 

//methods 
public void initModule() 
{	
	fefsac.nslSetAccess('W');
	fefmemPot_k2.nslSetAccess('W');
}

public void initRun() 
{
	center = (int)stdsz/2;

	protocolNum.set(  (NslInt0)nslGetValue("domineyModel.protocolNum"));

	fefvis.set(0);
	fefmem.set(0);
	fefsac.set(0);
	fefvisPot.set(0);
	fefmemPot.set(0);
	fefsacPot.set(0);
	fefsactmp.set(0);

	fefmemPot_tm.set( .008) ;
	fefvisPot_tm.set( .006) ; //xxx???AA
	fefsacPot_tm.set(  .008);
	fefmemPot_k1.set( 0.2) ;
	fefmemPot_k2.set( 0) ;
	//98/12/10 aa: not in 2.1.7  :doing a memory protocolNum
	if ((protocolNum.get()==2)||(protocolNum.get()==3)||(protocolNum.get()==8)||(protocolNum.get()==15)) 
	{	
		fefmemPot_k2.set( 1) ;  //was lost in NSL2.1.7 nsl file
	}
	fefmemPot_k4.set( 8) ;

	fefsacPot_k1.set(  1);
	fefsacPot_k2.set(  2);
	fefsacPot_k3.set(  3);
	fefvisPot_k1.set(  0);
	fefvisPot_k2.set(  1);
	fefvis_x1.set(  0);
	fefvis_x2.set( 90) ;
	fefvis_y1.set(  0);
	fefvis_y2.set(90)  ;
	fefmem_x1.set( 0) ;
	fefmem_x2.set(  90);
	fefmem_y1.set( 0) ;
	fefmem_y2.set(90)  ;
	fefsac_x1.set(  80); //AA: in 2.1.7 but whynot 0?
	fefsac_x2.set( 90) ;
	fefsac_y1.set(  0);
	fefsac_y2.set( 90) ;
	
 	//XX 98/11/18 aa: fefsac_k1 depeneds on protocolNum 
	// fefsac_k1*stimulation:
	// simple,double = 0
	// collision  = 4.8 why?
	// compensatoryI = 2.5 why?  Published doc says: 1.58
	// compensatoryII = nothing stated. Published doc says: 1.58
	// If we look at the SC equations: supcol_k3 then this
	// fefsac_k1 for compII should be stronger than for compI case.
	// For this reason we will set it to 3.5
	fefsac_k1.set(0);  // if stimulation is not used on FEF in most protocolNums
	// if stimulation used
	if ((protocolNum.get()==11)||(protocolNum.get()==13)) 
	{
		fefsac_k1.set(2.5);  // 
		// Thesis says fefsac_k1= 1.58 at 175 hz for 40 msec
		// for compensatory protocolNums but in the potential equation
	}
	if (protocolNum.get()==12) 
	{
		fefsac_k1.set(3.5);
	}
}

public void simRun() 
{
	fefvisPot.set(system.nsldiff.eval(fefvisPot,fefvisPot_tm, (__tempFEF1.setReference(NslAdd.eval(__tempFEF1.get(), __tempFEF0.setReference(NslSub.eval(__tempFEF0.get(), 0, fefvisPot)), ppqv)) )));
	fefmemPot.set(system.nsldiff.eval(fefmemPot,fefmemPot_tm,( __tempFEF8.setReference(NslSub.eval(__tempFEF8.get(), __tempFEF7.setReference(NslAdd.eval(__tempFEF7.get(), __tempFEF6.setReference(NslAdd.eval(__tempFEF6.get(), __tempFEF2.setReference(NslSub.eval(__tempFEF2.get(), 0, fefmemPot)), __tempFEF3.setReference(NslElemMult.eval(__tempFEF3.get(), fefmemPot_k4, thmem)))), __tempFEF4.setReference(NslElemMult.eval(__tempFEF4.get(), fefmemPot_k2, fefvis)))), __tempFEF5.setReference(NslElemMult.eval(__tempFEF5.get(), fefmemPot_k1, fon)))))));
	fefsacPot.set(system.nsldiff.eval(fefsacPot,fefsacPot_tm,( __tempFEF15.setReference(NslSub.eval(__tempFEF15.get(), __tempFEF14.setReference(NslAdd.eval(__tempFEF14.get(), __tempFEF13.setReference(NslAdd.eval(__tempFEF13.get(), __tempFEF9.setReference(NslSub.eval(__tempFEF9.get(), 0, fefsacPot)), __tempFEF10.setReference(NslElemMult.eval(__tempFEF10.get(), fefsacPot_k1, fefvis)))), __tempFEF11.setReference(NslElemMult.eval(__tempFEF11.get(), fefsacPot_k2, fefmem)))), __tempFEF12.setReference(NslElemMult.eval(__tempFEF12.get(), fefsacPot_k3, fon)))))));
	// aa: note: the published doc addes the fefsac_k1*stimulation 
	// in this equations (fefsacPot) instead of the fefsac equation below.

	fefsacPot.set(center, center,   0);
	fefvis.set(  NslSigmoid.eval(fefvisPot,fefvis_x1,fefvis_x2,fefvis_y1,fefvis_y2));

	fefmem.set(  NslSigmoid.eval(fefmemPot,fefmem_x1,fefmem_x2,fefmem_y1,fefmem_y2));

	fefsactmp.set(  NslSigmoid.eval(fefsacPot,fefsac_x1,fefsac_x2,fefsac_y1,fefsac_y2)); 
	fefsac.set(  __tempFEF17.setReference(NslAdd.eval(__tempFEF17.get(), fefsactmp, (__tempFEF16.setReference(NslElemMult.eval(__tempFEF16.get(), fefsac_k1, stimulation))))));

	//98/12/8 aa: from the 92 paper, set fefsac to 0 in equation 13 for
	// lesioning of fef
	//lesion fef
	if ((protocolNum.get()==7)||(protocolNum.get()==14)) 
	{
		fefsac.set(0);  
	}

	if (system.debug>=18) 
	{
		system.nslPrintln("simRun: FEF: fefsac:");
		system.nslPrintln(fefsac);
		system.nslPrintln("simRun: FEF: fefmem:");
		system.nslPrintln(fefmem);
		system.nslPrintln("simRun: FEF: fefvis:");
		system.nslPrintln(fefvis);
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
		NslFloat2 __tempFEF0 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF1 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF2 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF3 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF4 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF5 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF6 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF7 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF8 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF9 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF10 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF11 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF12 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF13 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF14 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF15 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF16 = new NslFloat2(1, 1);
		NslFloat2 __tempFEF17 = new NslFloat2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public FEF(String nslName, NslModule nslParent, int stdsz)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		initSys();
		makeInstFEF(nslName, nslParent, stdsz);
	}

	public void makeInstFEF(String nslName, NslModule nslParent, int stdsz)
{ 
		Object[] nslArgs=new Object[]{stdsz};
		callFromConstructorTop(nslArgs);
		ppqv = new NslDinFloat2("ppqv", this, stdsz, stdsz);
		thmem = new NslDinFloat2("thmem", this, stdsz, stdsz);
		fon = new NslDinFloat2("fon", this, stdsz, stdsz);
		stimulation = new NslDinFloat2("stimulation", this, stdsz, stdsz);
		fefvis = new NslDoutFloat2("fefvis", this, stdsz, stdsz);
		fefmem = new NslDoutFloat2("fefmem", this, stdsz, stdsz);
		fefsac = new NslDoutFloat2("fefsac", this, stdsz, stdsz);
		fefmemPot_tm = new NslFloat0("fefmemPot_tm", this);
		fefvisPot_tm = new NslFloat0("fefvisPot_tm", this);
		fefsacPot_tm = new NslFloat0("fefsacPot_tm", this);
		fefmemPot_k1 = new NslFloat0("fefmemPot_k1", this);
		fefmemPot_k2 = new NslFloat0("fefmemPot_k2", this);
		fefmemPot_k4 = new NslFloat0("fefmemPot_k4", this);
		fefsacPot_k1 = new NslFloat0("fefsacPot_k1", this);
		fefsacPot_k2 = new NslFloat0("fefsacPot_k2", this);
		fefsacPot_k3 = new NslFloat0("fefsacPot_k3", this);
		fefvisPot_k1 = new NslFloat0("fefvisPot_k1", this);
		fefvisPot_k2 = new NslFloat0("fefvisPot_k2", this);
		fefvis_x1 = new NslFloat0("fefvis_x1", this);
		fefvis_x2 = new NslFloat0("fefvis_x2", this);
		fefvis_y1 = new NslFloat0("fefvis_y1", this);
		fefvis_y2 = new NslFloat0("fefvis_y2", this);
		fefmem_x1 = new NslFloat0("fefmem_x1", this);
		fefmem_x2 = new NslFloat0("fefmem_x2", this);
		fefmem_y1 = new NslFloat0("fefmem_y1", this);
		fefmem_y2 = new NslFloat0("fefmem_y2", this);
		fefsac_x1 = new NslFloat0("fefsac_x1", this);
		fefsac_x2 = new NslFloat0("fefsac_x2", this);
		fefsac_y1 = new NslFloat0("fefsac_y1", this);
		fefsac_y2 = new NslFloat0("fefsac_y2", this);
		fefsac_k1 = new NslFloat0("fefsac_k1", this);
		fefvisPot = new NslFloat2("fefvisPot", this, stdsz, stdsz);
		fefmemPot = new NslFloat2("fefmemPot", this, stdsz, stdsz);
		fefsacPot = new NslFloat2("fefsacPot", this, stdsz, stdsz);
		fefsactmp = new NslFloat2("fefsactmp", this, stdsz, stdsz);
		protocolNum = new NslInt0("protocolNum", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end FEF

