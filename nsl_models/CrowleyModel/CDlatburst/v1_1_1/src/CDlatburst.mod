/** 
Dopamine is modelled as an inhibitory effect on the indirect
pathway projecting through GPe and STN and as an excitatory
effect on the direct pathway projecting to GPi/SNr.

Assume that the effect of dopamine on the projection neurons 
in the caudate is to manipulate the time constant of the neurons.  
More dopamine shortens the time constant of the neurons and 
less dopamine increases the time constant.
*/
package CrowleyModel.CDlatburst.v1_1_1.src;

nslModule CDlatburst(int CorticalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  CDlatburst
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 FEFsac_in(CorticalArraySize, CorticalArraySize); // 
public NslDinDouble2 SNCdop_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 PFCgo_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 PFCfovea_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 CDlattan_in(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 CDindlatburst_out(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 CDdirlatburst_out(CorticalArraySize,CorticalArraySize); // 
private NslDouble2 cdindlatburst(CorticalArraySize,CorticalArraySize); // 
private NslDouble2 cddirlatburst(CorticalArraySize,CorticalArraySize); // 
private double CorticalSlowDown; // 
private double basecdlatbursttm; // 
private double cdlatbursttm; // 
private double CDlatfefsacK; // 
private double CDlattanK; // 
private double CDlatsncK; // 
private double CDlatpfcK; // 
private double CDlatpfcgoK; // 
private double CDlbsigma1; // 
private double CDlbsigma2; // 
private double CDlbsigma3; // 
private double CDlbsigma4; // 

//methods 
public void initRun () 
{
	CDindlatburst_out=0;
	CDdirlatburst_out=0;

	CorticalSlowDown = 1.;
	basecdlatbursttm = 0.01;
	cdlatbursttm = basecdlatbursttm * CorticalSlowDown;
	CDlatfefsacK = 0.85;
	CDlattanK = 2;
	CDlatsncK = 1;
	CDlatpfcK = 0.25;
	CDlatpfcgoK = 0.25;
	CDlbsigma1 = 0;
	CDlbsigma2 = 60; // 90; //99/8/2 aa ???
	CDlbsigma3 = 0;
	CDlbsigma4 = 60;
}

public void simRun () 
{
	cdindlatburst = nslDiff (cdindlatburst,cdlatbursttm, 
		- cdindlatburst + (CDlatfefsacK * FEFsac_in)
		- (CDlattanK * CDlattan_in)
		- (CDlatsncK * SNCdop_in)
		+ (CDlatpfcK * PFCfovea_in));
	CDindlatburst_out = Nsl2Sigmoid.eval(cdindlatburst,CDlbsigma1, CDlbsigma2, CDlbsigma3, CDlbsigma4);

	cddirlatburst = nslDiff (cddirlatburst,cdlatbursttm, 
		- cddirlatburst + (CDlatfefsacK * FEFsac_in)
		+ (CDlatpfcgoK * PFCgo_in)
		- (CDlattanK * CDlattan_in)
		+ (CDlatsncK * SNCdop_in));

	CDdirlatburst_out = Nsl2Sigmoid.eval(cddirlatburst, CDlbsigma1, CDlbsigma2, CDlbsigma3, CDlbsigma4);
}
public void makeConn(){
}
}//end CDlatburst

