package CrowleyModel.Thal.v1_1_1.src;
import CrowleyModel.ThPFCmem.v1_1_1.src.*;
import CrowleyModel.ThFEFmem.v1_1_1.src.*;
import CrowleyModel.ThLIPmem.v1_1_1.src.*;
import CrowleyModel.ThMEDlcn.v1_1_1.src.*;
import CrowleyModel.RNMEDinh.v1_1_1.src.*;

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

public class Thal extends NslJavaModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  Thal
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 SNRmedburst_in; // 
public  NslDinDouble2 PFCmem_in; // 
public  NslDinDouble2 FEFmem_in; // 
public  NslDinDouble2 LIPmem_in; // 
public  NslDoutDouble2 ThPFCmem_out; // 
public  NslDoutDouble2 ThFEFmem_out; // 
public  NslDoutDouble2 ThLIPmem_out; // 
public  NslDoutDouble2 ThMEDlcn_out; // 
public  NslDoutDouble2 RNMEDinh_out; // 
private int THBurstRate=60; // 
private int THBurstLevel=15; // 
private double DecayRate=0.9; // 
private  NslDouble2 THNewActivation; // 
public  ThPFCmem thpfc; // 
public  ThFEFmem thfef; // 
public  ThLIPmem thlip; // 
public  ThMEDlcn thmed; // 
public  RNMEDinh rnmed; // 

//methods 
public void initModule()
{
	nslAddAreaCanvas("output", "thna", RNMEDinh_out, 0, 10);
}


public void initRun(){
    THNewActivation.set(  0);
  }

  public void simRun(){
    //    THNewActivation = THCheckBurst(SNRmedburst);
    // System.err.println("@@@@ Thal simRun entered @@@@");
     THCheckBurst(THNewActivation, SNRmedburst_in);
  }

  /**
    Uses  bursting constant to casue activation in neuorns
    undergoing remapping. This is indicated by a decrease in 
    inhibition below a certain threshold.
    */

public  void  THCheckBurst(NslDouble2 THNewActivation, NslDouble2 SNRmedburst_in) {
    double[][] inmat = SNRmedburst_in.get();
    double[][] outmat = THNewActivation.get();
    int size1 = inmat.length;
    int size2 = inmat[0].length;

    if(size1!=outmat.length||size2!=outmat[0].length) {
      System.err.println("THCheckBurst: array size not match\n"+"THNewActivation ("+size1+"x"+size2+") SNRmedburst_in ("+inmat.length+"x"+inmat[0].length+")");
    }
    for (int i=0; i<size1; i++) {
      for(int j=0; j<size2; j++) {
	if (inmat[i][j]<THBurstLevel)
	  outmat[i][j]=THBurstRate;
	else 
          outmat[i][j]=outmat[i][j]*DecayRate;
      }
    }

  }
public void makeConn(){
    nslConnect(SNRmedburst_in,thpfc.SNRmedburst_in);
    nslConnect(SNRmedburst_in,thfef.SNRmedburst_in);
    nslConnect(SNRmedburst_in,thlip.SNRmedburst_in);
    nslConnect(SNRmedburst_in,rnmed.SNRmedburst_in);
    nslConnect(PFCmem_in,thpfc.PFCmem_in);
    nslConnect(FEFmem_in,thfef.FEFmem_in);
    nslConnect(LIPmem_in,thlip.LIPmem_in);
    nslConnect(thpfc.ThPFCmem_out,ThPFCmem_out);
    nslConnect(thfef.ThFEFmem_out,ThFEFmem_out);
    nslConnect(thmed.ThMEDlcn_out,thpfc.ThMEDlcn_in);
    nslConnect(thmed.ThMEDlcn_out,thfef.ThMEDlcn_in);
    nslConnect(thmed.ThMEDlcn_out,thlip.ThMEDlcn_in);
    nslConnect(thmed.ThMEDlcn_out,ThMEDlcn_out);
    nslConnect(thlip.ThLIPmem_out,ThLIPmem_out);
    nslConnect(rnmed.RNMEDinh_out,thmed.RNMEDinh_in);
    nslConnect(rnmed.RNMEDinh_out,RNMEDinh_out);
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

	/* GENERIC CONSTRUCTOR: */
	public Thal(String nslName, NslModule nslParent, int array_size)
{
		super(nslName, nslParent);
		this.array_size=array_size;
		initSys();
		makeInstThal(nslName, nslParent, array_size);
	}

	public void makeInstThal(String nslName, NslModule nslParent, int array_size)
{ 
		Object[] nslArgs=new Object[]{array_size};
		callFromConstructorTop(nslArgs);
		SNRmedburst_in = new NslDinDouble2("SNRmedburst_in", this, array_size, array_size);
		PFCmem_in = new NslDinDouble2("PFCmem_in", this, array_size, array_size);
		FEFmem_in = new NslDinDouble2("FEFmem_in", this, array_size, array_size);
		LIPmem_in = new NslDinDouble2("LIPmem_in", this, array_size, array_size);
		ThPFCmem_out = new NslDoutDouble2("ThPFCmem_out", this, array_size, array_size);
		ThFEFmem_out = new NslDoutDouble2("ThFEFmem_out", this, array_size, array_size);
		ThLIPmem_out = new NslDoutDouble2("ThLIPmem_out", this, array_size, array_size);
		ThMEDlcn_out = new NslDoutDouble2("ThMEDlcn_out", this, array_size, array_size);
		RNMEDinh_out = new NslDoutDouble2("RNMEDinh_out", this, array_size, array_size);
		THNewActivation = new NslDouble2("THNewActivation", this, array_size, array_size);
		thpfc = new ThPFCmem("thpfc", this, array_size);
		thfef = new ThFEFmem("thfef", this, array_size);
		thlip = new ThLIPmem("thlip", this, array_size);
		thmed = new ThMEDlcn("thmed", this, array_size);
		rnmed = new RNMEDinh("rnmed", this, array_size);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end Thal

