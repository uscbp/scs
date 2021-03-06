/** 
Module ThFEFmem - Part of the Thalamus
*/
package CrowleyModel.ThFEFmem.v1_1_1.src;

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

public class ThFEFmem extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  ThFEFmem
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 FEFmem_in; // 
public  NslDinDouble2 SNRmedburst_in; // 
public  NslDinDouble2 ThMEDlcn_in; // 
public  NslDoutDouble2 ThFEFmem_out; // 
private double Thfefmemtm; // 
private double ThfefmemK1; // 
private double ThfefmemK2; // 
private double ThfefmemK3; // 
private double ThFEFmemsigma1; // 
private double ThFEFmemsigma2; // 
private double ThFEFmemsigma3; // 
private double ThFEFmemsigma4; // 
private  NslDouble2 THNewActivation; // 
private  NslDouble2 Thfefmem; // 

//methods 
public void initModule()
 {
    THNewActivation.set( (NslDouble2)nslGetValue("crowleyModel.thal1.THNewActivation")) ;
  }

public void initRun(){

    ThFEFmem_out.set(  0);
    Thfefmem.set(  0);
    Thfefmemtm=0.006;
    ThfefmemK1=1.5;
    ThfefmemK2=0.5;
    ThfefmemK3=0.5;
    ThFEFmemsigma1=30;
    ThFEFmemsigma2=65;
    ThFEFmemsigma3=0;
    ThFEFmemsigma4=60;
  }

public void simRun(){
  /* <Q> FEFmem_in  SNRmedburst_in  ThMEDlcn_in  THNewActivation*/
    THNewActivation.set( (NslDouble2)nslGetValue("crowleyModel.thal1.THNewActivation")) ;

  // System.err.println("@@@@ ThFEFmem simRun entered @@@@");
    Thfefmem.set(system.nsldiff.eval(Thfefmem,Thfefmemtm,__tempThFEFmem7.setReference(NslAdd.eval(__tempThFEFmem7.get(), __tempThFEFmem6.setReference(NslSub.eval(__tempThFEFmem6.get(), __tempThFEFmem5.setReference(NslSub.eval(__tempThFEFmem5.get(), __tempThFEFmem4.setReference(NslAdd.eval(__tempThFEFmem4.get(), __tempThFEFmem0.setReference(NslSub.eval(__tempThFEFmem0.get(), 0, Thfefmem)), (__tempThFEFmem1.setReference(NslElemMult.eval(__tempThFEFmem1.get(), ThfefmemK1, FEFmem_in))))), (__tempThFEFmem2.setReference(NslElemMult.eval(__tempThFEFmem2.get(), ThfefmemK2, SNRmedburst_in))))), (__tempThFEFmem3.setReference(NslElemMult.eval(__tempThFEFmem3.get(), ThfefmemK3, ThMEDlcn_in))))), THNewActivation))));


   ThFEFmem_out.set(Nsl2Sigmoid.eval(Thfefmem,ThFEFmemsigma1,
                            ThFEFmemsigma2,
                            ThFEFmemsigma3,
                            ThFEFmemsigma4));

	// 96/12/20
	//System.out.println("ThFEFmem_out: " + ThFEFmem_out);
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
		NslDouble2 __tempThFEFmem0 = new NslDouble2(1, 1);
		NslDouble2 __tempThFEFmem1 = new NslDouble2(1, 1);
		NslDouble2 __tempThFEFmem2 = new NslDouble2(1, 1);
		NslDouble2 __tempThFEFmem3 = new NslDouble2(1, 1);
		NslDouble2 __tempThFEFmem4 = new NslDouble2(1, 1);
		NslDouble2 __tempThFEFmem5 = new NslDouble2(1, 1);
		NslDouble2 __tempThFEFmem6 = new NslDouble2(1, 1);
		NslDouble2 __tempThFEFmem7 = new NslDouble2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public ThFEFmem(String nslName, NslModule nslParent, int array_size)
{
		super(nslName, nslParent);
		this.array_size=array_size;
		initSys();
		makeInstThFEFmem(nslName, nslParent, array_size);
	}

	public void makeInstThFEFmem(String nslName, NslModule nslParent, int array_size)
{ 
		Object[] nslArgs=new Object[]{array_size};
		callFromConstructorTop(nslArgs);
		FEFmem_in = new NslDinDouble2("FEFmem_in", this, array_size, array_size);
		SNRmedburst_in = new NslDinDouble2("SNRmedburst_in", this, array_size, array_size);
		ThMEDlcn_in = new NslDinDouble2("ThMEDlcn_in", this, array_size, array_size);
		ThFEFmem_out = new NslDoutDouble2("ThFEFmem_out", this, array_size, array_size);
		THNewActivation = new NslDouble2("THNewActivation", this, array_size, array_size);
		Thfefmem = new NslDouble2("Thfefmem", this, array_size, array_size);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end ThFEFmem

