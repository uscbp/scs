/** 
 Here is the class representing the Frontal Eye Fields (FEF) module.
 FEF is modeled to have two type of cells: FEFvis, visual response cells and
 FEFmem, memory response cells. FEFvis only responds to visual stimuli that 
 are targets of impending saccades and do not fire before saccades made 
 without visual input, nor they project to the SC.
*/
package CrowleyModel.FEF.v1_1_1.src;

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

public class FEF extends NslJavaModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  FEF
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 ThFEFmem_in; // 
public  NslDinDouble2 LIPmem_in; // 
public  NslDinDouble2 PFCgo_in; // 
public  NslDinDouble2 PFCnovelty; // 
public  NslDinDouble2 PFCmem_in; // 
public  NslDoutDouble2 FEFmem_out; // 
public  NslDoutDouble2 FEFsac_out; // 
private  NslDouble2 fefmem; // 
private  NslDouble2 fefsac; // 
private NslInt0 FOVEAX; // 
private NslInt0 FOVEAY; // 
private final int NINE=9; // 
private final double CorticalSlowdown=1.0; // 
private final double basefefsactm=0.008; // 
private double FEFmemsigma1; // 
private double FEFmemsigma2; // 
private double FEFmemsigma3; // 
private double FEFmemsigma4; // 
private double FEFsacsigma1; // 
private double FEFsacsigma2; // 
private double FEFsacsigma3; // 
private double FEFsacsigma4; // 
private double FEFSaccadeVector; // 
private double fefmemtm; // 
private double fefsactm; // 
private double fefmemK1; // 
private double fefmemK2; // 
private double fefmemK3; // 
private double fefsacK1; // 
private double fefsacK2; // 

//methods 
public void initModule() 
{
	FOVEAX.set((NslInt0)nslGetValue("crowleyModel.FOVEAX"));
	FOVEAY.set((NslInt0)nslGetValue("crowleyModel.FOVEAY"));
	nslAddAreaCanvas("output", "fef", FEFsac_out, 0, 100);
}

  public void initRun(){
    
    FEFmem_out.set(  0);
    FEFsac_out.set(  0);
    fefmem.set(        0);
    fefsac.set(        0);

    FEFmemsigma1 =   0;
    FEFmemsigma2 =  90;
    FEFmemsigma3 =   0;
    FEFmemsigma4 =  90;
    FEFsacsigma1 =  40;
    FEFsacsigma2 =  90;
    FEFsacsigma3 =   0;
    FEFsacsigma4 =  90;
    

    FEFSaccadeVector = 0;
    fefmemtm = 0.008;
    fefsactm = 0.006;
    fefmemK1 = 0.5;
    fefmemK2 = 1;
    fefmemK3 = 0.5;

    fefsacK1 = 0.3;
    fefsacK2 = 1;
  }
public void simRun(){

  // System.err.println("@@@@ FEF simRun entered @@@@");


    //LNK_FEF2
    /**
     * A memory loop is established between FEFmem and mediodorsal thalamus
     *(ThFEFmem_in) to maintain the saccadic target memory.
     */

    // 1-2-97 isaac:  fefmemK3 * PFCmem is missing from the original code.
    // redefine the inport interface.

    fefmem.set(system.nsldiff.eval(fefmem,fefmemtm,__tempFEF6.setReference(NslAdd.eval(__tempFEF6.get(), __tempFEF5.setReference(NslAdd.eval(__tempFEF5.get(), __tempFEF4.setReference(NslAdd.eval(__tempFEF4.get(), __tempFEF0.setReference(NslSub.eval(__tempFEF0.get(), 0, fefmem)), (__tempFEF1.setReference(NslElemMult.eval(__tempFEF1.get(), fefmemK1, ThFEFmem_in))))), (__tempFEF2.setReference(NslElemMult.eval(__tempFEF2.get(), fefmemK2, LIPmem_in))))), (__tempFEF3.setReference(NslElemMult.eval(__tempFEF3.get(), fefmemK3, PFCmem_in)))))));
    
    //    fefsactm = basefefsactm * CorticalSlowdown;
    
    fefsac.set(system.nsldiff.eval(fefsac,fefsactm,__tempFEF11.setReference(NslAdd.eval(__tempFEF11.get(), __tempFEF10.setReference(NslAdd.eval(__tempFEF10.get(), __tempFEF7.setReference(NslSub.eval(__tempFEF7.get(), 0, fefsac)), (__tempFEF8.setReference(NslElemMult.eval(__tempFEF8.get(), fefsacK1, FEFmem_out))))), (__tempFEF9.setReference(NslElemMult.eval(__tempFEF9.get(), fefsacK2, PFCgo_in))))) ));// + PFCnovelty);
    fefsac.set((int)FOVEAX.get(), (int)FOVEAY.get(),   0);
    FEFmem_out.set(Nsl2Sigmoid.eval(fefmem,FEFmemsigma1,FEFmemsigma2,FEFmemsigma3,FEFmemsigma4));
    FEFsac_out.set(Nsl2Sigmoid.eval(fefsac,FEFsacsigma1,FEFsacsigma2,FEFsacsigma3,FEFsacsigma4));
	//96/12/20 aa
    //System.out.println("FEFsac_out: " + FEFsac_out);
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
		NslDouble2 __tempFEF0 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF1 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF2 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF3 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF4 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF5 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF6 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF7 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF8 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF9 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF10 = new NslDouble2(1, 1);
		NslDouble2 __tempFEF11 = new NslDouble2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public FEF(String nslName, NslModule nslParent, int array_size)
{
		super(nslName, nslParent);
		this.array_size=array_size;
		initSys();
		makeInstFEF(nslName, nslParent, array_size);
	}

	public void makeInstFEF(String nslName, NslModule nslParent, int array_size)
{ 
		Object[] nslArgs=new Object[]{array_size};
		callFromConstructorTop(nslArgs);
		ThFEFmem_in = new NslDinDouble2("ThFEFmem_in", this, array_size, array_size);
		LIPmem_in = new NslDinDouble2("LIPmem_in", this, array_size, array_size);
		PFCgo_in = new NslDinDouble2("PFCgo_in", this, array_size, array_size);
		PFCnovelty = new NslDinDouble2("PFCnovelty", this, array_size, array_size);
		PFCmem_in = new NslDinDouble2("PFCmem_in", this, array_size, array_size);
		FEFmem_out = new NslDoutDouble2("FEFmem_out", this, array_size, array_size);
		FEFsac_out = new NslDoutDouble2("FEFsac_out", this, array_size, array_size);
		fefmem = new NslDouble2("fefmem", this, array_size, array_size);
		fefsac = new NslDouble2("fefsac", this, array_size, array_size);
		FOVEAX = new NslInt0("FOVEAX", this);
		FOVEAY = new NslInt0("FOVEAY", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end FEF

