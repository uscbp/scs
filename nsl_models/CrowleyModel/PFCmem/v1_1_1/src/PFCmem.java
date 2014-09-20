package CrowleyModel.PFCmem.v1_1_1.src;

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

public class PFCmem extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  PFCmem
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 ThPFCmem_in; // 
public  NslDinDouble2 LIPmem_in; // 
public  NslDinDouble2 pfcseq_in; // 
public  NslDoutDouble2 PFCmem_out; // 
private  NslDouble2 pfcmem; // 
private double pfcmemtm; // 
private double pfcmemK1; // 
private double pfcmemK2; // 
private double pfcseqK; // 
private double PFCmemsigma1; // 
private double PFCmemsigma2; // 
private double PFCmemsigma3; // 
private double PFCmemsigma4; // 

//methods 
public void initRun(){
    PFCmem_out.set(  0.0);
    pfcmem.set(  0.0);

    pfcmemtm = 0.008;
    pfcmemK1 = 1.5;
    pfcmemK2 = 0.5;
    pfcseqK = 2.0;
    PFCmemsigma1 = 0.0;
    PFCmemsigma2 = 180.0;
    PFCmemsigma3 = 0.0;
    PFCmemsigma4 = 90.0;
}

public void simRun(){
	//ThPFCmem_in = pfcmemK1* ThPFCmem_in;

  // System.err.println("@@@@ PFCmem simRun entered @@@@");
    pfcmem.set(  system.nsldiff.eval(pfcmem,pfcmemtm,
			 __tempPFCmem7.setReference(NslAdd.eval(__tempPFCmem7.get(), __tempPFCmem6.setReference(NslAdd.eval(__tempPFCmem6.get(), __tempPFCmem5.setReference(NslAdd.eval(__tempPFCmem5.get(), __tempPFCmem0.setReference(NslSub.eval(__tempPFCmem0.get(), 0, pfcmem)), (__tempPFCmem1.setReference(NslElemMult.eval(__tempPFCmem1.get(), pfcmemK1, ThPFCmem_in))))), (__tempPFCmem2.setReference(NslElemMult.eval(__tempPFCmem2.get(), pfcmemK2, LIPmem_in))))), (__tempPFCmem4.setReference(NslElemMult.eval(__tempPFCmem4.get(), __tempPFCmem3.setReference(NslElemMult.eval(__tempPFCmem3.get(), pfcseqK, pfcseq_in)), LIPmem_in)))))));
   
    pfcmem.set(4, 4,   0.0);
    PFCmem_out.set(  Nsl2Sigmoid.eval(pfcmem,PFCmemsigma1, PFCmemsigma2,
			      PFCmemsigma3, PFCmemsigma4));
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
		NslDouble2 __tempPFCmem0 = new NslDouble2(1, 1);
		NslDouble2 __tempPFCmem1 = new NslDouble2(1, 1);
		NslDouble2 __tempPFCmem2 = new NslDouble2(1, 1);
		NslDouble2 __tempPFCmem3 = new NslDouble2(1, 1);
		NslDouble2 __tempPFCmem4 = new NslDouble2(1, 1);
		NslDouble2 __tempPFCmem5 = new NslDouble2(1, 1);
		NslDouble2 __tempPFCmem6 = new NslDouble2(1, 1);
		NslDouble2 __tempPFCmem7 = new NslDouble2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public PFCmem(String nslName, NslModule nslParent, int array_size)
{
		super(nslName, nslParent);
		this.array_size=array_size;
		initSys();
		makeInstPFCmem(nslName, nslParent, array_size);
	}

	public void makeInstPFCmem(String nslName, NslModule nslParent, int array_size)
{ 
		Object[] nslArgs=new Object[]{array_size};
		callFromConstructorTop(nslArgs);
		ThPFCmem_in = new NslDinDouble2("ThPFCmem_in", this, array_size, array_size);
		LIPmem_in = new NslDinDouble2("LIPmem_in", this, array_size, array_size);
		pfcseq_in = new NslDinDouble2("pfcseq_in", this, array_size, array_size);
		PFCmem_out = new NslDoutDouble2("PFCmem_out", this, array_size, array_size);
		pfcmem = new NslDouble2("pfcmem", this, array_size, array_size);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end PFCmem
