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
nslImport CrowleyModel.Func.v1_1_1.src.*;

nslModule CDmedburst(int CorticalArraySize,int StriatalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  CDmedburst
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 PFCgo_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 FEFsac_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 LIPmem_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 SNCdop_in(StriatalArraySize,StriatalArraySize); // 
public NslDinDouble2 CDmedtan_in(StriatalArraySize,StriatalArraySize); // 
public NslDinInt2 SNRMapCount_bulk(CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 FEFxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 FEFymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 LIPxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 LIPymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 PFCxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 PFCymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinDouble3 SNRweights_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 CDindmedburst_out(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 CDdirmedburst_out(StriatalArraySize,StriatalArraySize); // 
private NslDouble0 LearnRate; // 
private NslDouble2 cdindmedburst(CorticalArraySize,CorticalArraySize); // 
private NslDouble2 cddirmedburst(StriatalArraySize,StriatalArraySize); // 
private NslDouble2 SNCdopmed(StriatalArraySize,StriatalArraySize); // 
private NslDouble2 CDfefinput(StriatalArraySize,StriatalArraySize); // 
private NslDouble2 CDlipinput(StriatalArraySize,StriatalArraySize); // 
private NslDouble2 CDpfcinput(StriatalArraySize,StriatalArraySize); // 
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
public Func func(CorticalArraySize); // 

//methods 
public void initSys() 
{
	//System.err.println("CDmedburst:initSys");
}

public void initModule() 
{
	LearnRate=0.05;
}

public void initRun () 
{
	CDindmedburst_out=0;
	CDdirmedburst_out=0;
	CorticalSlowDown = 1.;
	basecdmedbursttm = 0.01;
	cdmedbursttm = basecdmedbursttm * CorticalSlowDown;
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

	SNCdopmax = nslMaxValue(SNCdop_in);
	SNCdopmed = SNCdopmax;
	/* <Q> where do FEFsac_in, LIPmem_in come from? */
	//System.err.println("===== CDmedburst[1] Calling SetCD");

	tempint = func.SetCD (CDfefinput, FEFxmap_bulk, FEFymap_bulk, FEFsac_in);
	//    tempint = func.SetCD (CDlipinput, LIPxmap, LIPymap, LIPmem_in);
	//System.err.println("===== CDmedburst[2] Calling SetCD");
	tempint = func.SetCD (CDlipinput, LIPxmap_bulk, LIPymap_bulk, LIPmem_in);
	//nslprintln("\tCDlip "+nslMax(CDlipinput));
	//System.err.println("===== CDmedburst[3] Calling SetCD");
	tempint = func.SetCD (CDpfcinput, PFCxmap_bulk, PFCymap_bulk, PFCgo_in);

	cdindmedburst = nslDiff (cdindmedburst,cdmedbursttm, 
                                        - cdindmedburst 
					+ (CDmedfefsacK * LIPmem_in)
                                        - (CDmedsncK * SNCdop_in)
                                        + (CDmedpfcgoK * nslMaxValue(PFCgo_in)));
	CDindmedburst_out = Nsl2Sigmoid.eval (cdindmedburst, CDmbsigma1, CDmbsigma2,
                                               CDmbsigma3, CDmbsigma4);

	cddirmedburst = nslDiff (cddirmedburst,cdmedbursttm, 
                                        - cddirmedburst 
					+ (CDfefinputK * CDfefinput)
                                        + (CDlipinputK * CDlipinput)
                                        + (CDpfcinputK * CDpfcinput)
                                        - (CDmedtanK * CDmedtan_in)
                                        + (CDmedsncK * SNCdopmed));
	//nslPrintln("CDfefinput:\n"+CDfefinput);
	//nslPrintln("CDlipinput:\n"+CDlipinput);
	//nslPrintln("CDpfcinput:\n"+CDpfcinput);
	//nslPrintln("SNCdopmed:\n"+SNCdopmed);
	CDdirmedburst_out = Nsl2Sigmoid.eval(cddirmedburst, CDmbsigma1, CDmbsigma2,
                                               CDmbsigma3, CDmbsigma4);
	//nslPrintln("CDdirmedburst_out:\n"+CDdirmedburst_out);
}
public void makeConn(){
}
}//end CDmedburst

