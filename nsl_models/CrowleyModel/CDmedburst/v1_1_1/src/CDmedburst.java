/** 
 CDmedburst

        Dopamine is modelled as an inhibitory effect on the indirect
        pathway projecting through GPe and STN and as an excitatory
        effect on the direct pathway projecting to GPi/SNr.

        Also, assume that the effect of dopamine on the projection neurons in 
        the caudate is to manipulate the time constant of the neurons.  More
        dopamine shortens the time constant of the neurons and less dopamine
        increases the time constant.  This makes the CD more or less responsive
        the inputs from the cortex.
*/
package CrowleyModel.CDmedburst.v1_1_1.src;
import CrowleyModel.Func.v1_1_1.src.*;

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

public class CDmedburst extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  CDmedburst
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 PFCgo_in; // 
public  NslDinDouble2 FEFsac_in; // 
public  NslDinDouble2 LIPmem_in; // 
public  NslDinDouble2 SNCdop_in; // 
public  NslDinDouble2 CDmedtan_in; // 
public  NslDinInt2 SNRMapCount_bulk; // 
public  NslDinInt3 FEFxmap_bulk; // 
public  NslDinInt3 FEFymap_bulk; // 
public  NslDinInt3 LIPxmap_bulk; // 
public  NslDinInt3 LIPymap_bulk; // 
public  NslDinInt3 PFCxmap_bulk; // 
public  NslDinInt3 PFCymap_bulk; // 
public  NslDinDouble3 SNRweights_bulk; // 
public  NslDoutDouble2 CDindmedburst_out; // 
public  NslDoutDouble2 CDdirmedburst_out; // 
private NslDouble0 LearnRate; // 
private  NslDouble2 cdindmedburst; // 
private  NslDouble2 cddirmedburst; // 
private  NslDouble2 SNCdopmed; // 
private  NslDouble2 CDfefinput; // 
private  NslDouble2 CDlipinput; // 
private  NslDouble2 CDpfcinput; // 
private double SNCdopmax; // 
private double CorticalSlowDown; // 
private double basecdmedbursttm; // 
private double cdmedbursttm; // 
private double CDfefinputK; // 
private double CDlipinputK; // 
private double CDpfcinputK; // 
private double CDmedtanK; // 
private double CDmedsncK; // 
private double CDmedfefsacK; // 
private double CDmedpfcgoK; // 
private double CDmbsigma1; // 
private double CDmbsigma2; // 
private double CDmbsigma3; // 
private double CDmbsigma4; // 
public  Func func; // 

//methods 
public void initSys() 
{
	//System.err.println("CDmedburst:initSys");
}

public void initModule() 
{
	LearnRate.set(0.05);
}

public void initRun () 
{
	CDindmedburst_out.set(0);
	CDdirmedburst_out.set(0);
	CorticalSlowDown = 1.;
	basecdmedbursttm = 0.01;
	cdmedbursttm = basecdmedbursttm*CorticalSlowDown ;
	CDfefinputK = 1.0;
	CDlipinputK = 1.0;
	CDpfcinputK = 1.0;
	CDmedtanK = 2;
	CDmedsncK = 1;
	CDmedfefsacK = 0.35;
	CDmedpfcgoK = 0.5;
	CDmbsigma1 = 25;
	CDmbsigma2 = 90;
	CDmbsigma3 = 0;
	CDmbsigma4 = 60;
}

public void simRun () 
{
	int tempint;
	// System.err.println("@@@@ CDmedburst simRun entered @@@@");

	SNCdopmax = NslMaxValue.eval(SNCdop_in);
	SNCdopmed.set(  SNCdopmax);
	/* <Q> where do FEFsac_in, LIPmem_in come from? */
	//System.err.println("===== CDmedburst[1] Calling SetCD");

	tempint = func.SetCD (CDfefinput, FEFxmap_bulk, FEFymap_bulk, FEFsac_in);
	//    tempint = func.SetCD (CDlipinput, LIPxmap, LIPymap, LIPmem_in);
	//System.err.println("===== CDmedburst[2] Calling SetCD");
	tempint = func.SetCD (CDlipinput, LIPxmap_bulk, LIPymap_bulk, LIPmem_in);
	//nslprintln("\tCDlip "+nslMax(CDlipinput));
	//System.err.println("===== CDmedburst[3] Calling SetCD");
	tempint = func.SetCD (CDpfcinput, PFCxmap_bulk, PFCymap_bulk, PFCgo_in);

	cdindmedburst.set(  system.nsldiff.eval (cdindmedburst,cdmedbursttm, 
                                        __tempCDmedburst5.setReference(NslAdd.eval(__tempCDmedburst5.get(), __tempCDmedburst4.setReference(NslSub.eval(__tempCDmedburst4.get(), __tempCDmedburst3.setReference(NslAdd.eval(__tempCDmedburst3.get(), __tempCDmedburst0.setReference(NslSub.eval(__tempCDmedburst0.get(), 0, cdindmedburst)), (__tempCDmedburst1.setReference(NslElemMult.eval(__tempCDmedburst1.get(), CDmedfefsacK, LIPmem_in))))), (__tempCDmedburst2.setReference(NslElemMult.eval(__tempCDmedburst2.get(), CDmedsncK, SNCdop_in))))), (CDmedpfcgoK*NslMaxValue.eval(PFCgo_in))))));
	CDindmedburst_out.set(  Nsl2Sigmoid.eval (cdindmedburst, CDmbsigma1, CDmbsigma2,
                                               CDmbsigma3, CDmbsigma4));

	cddirmedburst.set(  system.nsldiff.eval (cddirmedburst,cdmedbursttm, 
                                        __tempCDmedburst16.setReference(NslAdd.eval(__tempCDmedburst16.get(), __tempCDmedburst15.setReference(NslSub.eval(__tempCDmedburst15.get(), __tempCDmedburst14.setReference(NslAdd.eval(__tempCDmedburst14.get(), __tempCDmedburst13.setReference(NslAdd.eval(__tempCDmedburst13.get(), __tempCDmedburst12.setReference(NslAdd.eval(__tempCDmedburst12.get(), __tempCDmedburst6.setReference(NslSub.eval(__tempCDmedburst6.get(), 0, cddirmedburst)), (__tempCDmedburst7.setReference(NslElemMult.eval(__tempCDmedburst7.get(), CDfefinputK, CDfefinput))))), (__tempCDmedburst8.setReference(NslElemMult.eval(__tempCDmedburst8.get(), CDlipinputK, CDlipinput))))), (__tempCDmedburst9.setReference(NslElemMult.eval(__tempCDmedburst9.get(), CDpfcinputK, CDpfcinput))))), (__tempCDmedburst10.setReference(NslElemMult.eval(__tempCDmedburst10.get(), CDmedtanK, CDmedtan_in))))), (__tempCDmedburst11.setReference(NslElemMult.eval(__tempCDmedburst11.get(), CDmedsncK, SNCdopmed)))))));
	//nslPrintln("CDfefinput:\n"+CDfefinput);
	//nslPrintln("CDlipinput:\n"+CDlipinput);
	//nslPrintln("CDpfcinput:\n"+CDpfcinput);
	//nslPrintln("SNCdopmed:\n"+SNCdopmed);
	CDdirmedburst_out.set(  Nsl2Sigmoid.eval(cddirmedburst, CDmbsigma1, CDmbsigma2,
                                               CDmbsigma3, CDmbsigma4));
	//nslPrintln("CDdirmedburst_out:\n"+CDdirmedburst_out);
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
	int CorticalArraySize;
	int StriatalArraySize;

	/* Temporary variables */
		NslDouble2 __tempCDmedburst0 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst1 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst2 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst3 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst4 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst5 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst6 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst7 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst8 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst9 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst10 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst11 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst12 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst13 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst14 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst15 = new NslDouble2(1, 1);
		NslDouble2 __tempCDmedburst16 = new NslDouble2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public CDmedburst(String nslName, NslModule nslParent, int CorticalArraySize, int StriatalArraySize)
{
		super(nslName, nslParent);
		this.CorticalArraySize=CorticalArraySize;
		this.StriatalArraySize=StriatalArraySize;
		initSys();
		makeInstCDmedburst(nslName, nslParent, CorticalArraySize, StriatalArraySize);
	}

	public void makeInstCDmedburst(String nslName, NslModule nslParent, int CorticalArraySize, int StriatalArraySize)
{ 
		Object[] nslArgs=new Object[]{CorticalArraySize, StriatalArraySize};
		callFromConstructorTop(nslArgs);
		PFCgo_in = new NslDinDouble2("PFCgo_in", this, CorticalArraySize, CorticalArraySize);
		FEFsac_in = new NslDinDouble2("FEFsac_in", this, CorticalArraySize, CorticalArraySize);
		LIPmem_in = new NslDinDouble2("LIPmem_in", this, CorticalArraySize, CorticalArraySize);
		SNCdop_in = new NslDinDouble2("SNCdop_in", this, StriatalArraySize, StriatalArraySize);
		CDmedtan_in = new NslDinDouble2("CDmedtan_in", this, StriatalArraySize, StriatalArraySize);
		SNRMapCount_bulk = new NslDinInt2("SNRMapCount_bulk", this, CorticalArraySize, CorticalArraySize);
		FEFxmap_bulk = new NslDinInt3("FEFxmap_bulk", this, CorticalArraySize, CorticalArraySize, CorticalArraySize);
		FEFymap_bulk = new NslDinInt3("FEFymap_bulk", this, CorticalArraySize, CorticalArraySize, CorticalArraySize);
		LIPxmap_bulk = new NslDinInt3("LIPxmap_bulk", this, CorticalArraySize, CorticalArraySize, CorticalArraySize);
		LIPymap_bulk = new NslDinInt3("LIPymap_bulk", this, CorticalArraySize, CorticalArraySize, CorticalArraySize);
		PFCxmap_bulk = new NslDinInt3("PFCxmap_bulk", this, CorticalArraySize, CorticalArraySize, CorticalArraySize);
		PFCymap_bulk = new NslDinInt3("PFCymap_bulk", this, CorticalArraySize, CorticalArraySize, CorticalArraySize);
		SNRweights_bulk = new NslDinDouble3("SNRweights_bulk", this, CorticalArraySize, CorticalArraySize, CorticalArraySize);
		CDindmedburst_out = new NslDoutDouble2("CDindmedburst_out", this, CorticalArraySize, CorticalArraySize);
		CDdirmedburst_out = new NslDoutDouble2("CDdirmedburst_out", this, StriatalArraySize, StriatalArraySize);
		LearnRate = new NslDouble0("LearnRate", this);
		cdindmedburst = new NslDouble2("cdindmedburst", this, CorticalArraySize, CorticalArraySize);
		cddirmedburst = new NslDouble2("cddirmedburst", this, StriatalArraySize, StriatalArraySize);
		SNCdopmed = new NslDouble2("SNCdopmed", this, StriatalArraySize, StriatalArraySize);
		CDfefinput = new NslDouble2("CDfefinput", this, StriatalArraySize, StriatalArraySize);
		CDlipinput = new NslDouble2("CDlipinput", this, StriatalArraySize, StriatalArraySize);
		CDpfcinput = new NslDouble2("CDpfcinput", this, StriatalArraySize, StriatalArraySize);
		func = new Func("func", this, CorticalArraySize);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end CDmedburst

