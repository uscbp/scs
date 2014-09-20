/** 
Module: ThPFCmem - Part of the Thalamus
*/
package CrowleyModel.ThPFCmem.v1_1_1.src;

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

public class ThPFCmem extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  ThPFCmem
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 SNRmedburst_in; // 
public  NslDinDouble2 ThMEDlcn_in; // 
public  NslDinDouble2 PFCmem_in; // 
public  NslDoutDouble2 ThPFCmem_out; // 
private double Thpfcmemtm; // 
private double ThpfcmemK1; // 
private double ThpfcmemK2; // 
private double ThpfcmemK3; // 
private double ThPFCmemsigma1; // 
private double ThPFCmemsigma2; // 
private double ThPFCmemsigma3; // 
private double ThPFCmemsigma4; // 
private  NslDouble2 Thpfcmem; // 
private  NslDouble2 THNewActivation; // 

//methods 
public void initModule()
  {
    THNewActivation.set(  (NslDouble2)nslGetValue ("crowleyModel.thal1.THNewActivation"));
  }

public void initRun(){
    Thpfcmem.set(  0);
    ThPFCmem_out.set(  0);
    Thpfcmemtm=0.006;
    ThpfcmemK1=1.5;
    ThpfcmemK2=0.5;
    ThpfcmemK3=0.5;
    ThPFCmemsigma1=30;
    ThPFCmemsigma2=65;
    ThPFCmemsigma3=0;
    ThPFCmemsigma4=60;
}

public void simRun(){
  /* <Q> PFCmem_in SNRmedburst_in ThMEDlcn_in THNewActivation */
    THNewActivation.set(  (NslDouble2)nslGetValue("crowleyModel.thal1.THNewActivation"));
  // System.err.println("@@@@ ThPFCmem_in simRun entered @@@@");
    Thpfcmem.set(system.nsldiff.eval(Thpfcmem,Thpfcmemtm,__tempThPFCmem7.setReference(NslAdd.eval(__tempThPFCmem7.get(), __tempThPFCmem6.setReference(NslSub.eval(__tempThPFCmem6.get(), __tempThPFCmem5.setReference(NslSub.eval(__tempThPFCmem5.get(), __tempThPFCmem4.setReference(NslAdd.eval(__tempThPFCmem4.get(), __tempThPFCmem0.setReference(NslSub.eval(__tempThPFCmem0.get(), 0, Thpfcmem)), (__tempThPFCmem1.setReference(NslElemMult.eval(__tempThPFCmem1.get(), ThpfcmemK1, PFCmem_in))))), (__tempThPFCmem2.setReference(NslElemMult.eval(__tempThPFCmem2.get(), ThpfcmemK2, SNRmedburst_in))))), (__tempThPFCmem3.setReference(NslElemMult.eval(__tempThPFCmem3.get(), ThpfcmemK3, ThMEDlcn_in))))), THNewActivation))));
			   
    ThPFCmem_out.set(Nsl2Sigmoid.eval(Thpfcmem,ThPFCmemsigma1,
                            ThPFCmemsigma2,
                            ThPFCmemsigma3,
                            ThPFCmemsigma4));
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
		NslDouble2 __tempThPFCmem0 = new NslDouble2(1, 1);
		NslDouble2 __tempThPFCmem1 = new NslDouble2(1, 1);
		NslDouble2 __tempThPFCmem2 = new NslDouble2(1, 1);
		NslDouble2 __tempThPFCmem3 = new NslDouble2(1, 1);
		NslDouble2 __tempThPFCmem4 = new NslDouble2(1, 1);
		NslDouble2 __tempThPFCmem5 = new NslDouble2(1, 1);
		NslDouble2 __tempThPFCmem6 = new NslDouble2(1, 1);
		NslDouble2 __tempThPFCmem7 = new NslDouble2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public ThPFCmem(String nslName, NslModule nslParent, int array_size)
{
		super(nslName, nslParent);
		this.array_size=array_size;
		initSys();
		makeInstThPFCmem(nslName, nslParent, array_size);
	}

	public void makeInstThPFCmem(String nslName, NslModule nslParent, int array_size)
{ 
		Object[] nslArgs=new Object[]{array_size};
		callFromConstructorTop(nslArgs);
		SNRmedburst_in = new NslDinDouble2("SNRmedburst_in", this, array_size, array_size);
		ThMEDlcn_in = new NslDinDouble2("ThMEDlcn_in", this, array_size, array_size);
		PFCmem_in = new NslDinDouble2("PFCmem_in", this, array_size, array_size);
		ThPFCmem_out = new NslDoutDouble2("ThPFCmem_out", this, array_size, array_size);
		Thpfcmem = new NslDouble2("Thpfcmem", this, array_size, array_size);
		THNewActivation = new NslDouble2("THNewActivation", this, array_size, array_size);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end ThPFCmem
