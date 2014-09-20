/** 
 CDmedinh class
 Represents the Caudate Non-dopaminergic Interneuron Cells Layer
 see     CDmedinh
 version 0.1 96-11-19
 author  Michael Crowley
 -var public FEFsac_in - input coming from FEF module (of type NslDouble2)
 -var public LIPmem_in - input coming from LIP module (of type NslDouble2)
 -var public CDmedinh_out - output going to CDmedtan module (of type NslDouble2)
*/
package CrowleyModel.CDmedinh.v1_1_1.src;
nslImport CrowleyModel.Func.v1_1_1.src.*;

nslModule CDmedinh(int CorticalArraySize,int StriatalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  CDmedinh
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinInt3 FEFxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 FEFymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 LIPxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 LIPymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 FEFsac_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 LIPmem_in(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 CDmedinh_out(StriatalArraySize,StriatalArraySize); // 
private double cdmedinhtm; // 
private double CDlisigma1; // 
private double CDlisigma2; // 
private double CDlisigma3; // 
private double CDlisigma4; // 
private NslDouble2 CDfefinput(StriatalArraySize,StriatalArraySize); // 
private NslDouble2 CDlipinput(StriatalArraySize,StriatalArraySize); // 
private NslDouble2 cdmedinh(StriatalArraySize,StriatalArraySize); // 
public Func func(CorticalArraySize); // 

//methods 
public void initRun () 
{
	CDmedinh_out =0;
	cdmedinhtm = 0.01;
	CDlisigma1 = 45;
	CDlisigma2 = 90;
	CDlisigma3 = 0;
	CDlisigma4 = 60;
}

public void simRun () 
{
	int tempint;

	// System.err.println("@@@@ CDmedinh simRun entered @@@@");

	tempint = func.SetCD (CDfefinput, FEFxmap_bulk , FEFymap_bulk , FEFsac_in);
	tempint = func.SetCD (CDlipinput, LIPxmap_bulk , LIPymap_bulk , LIPmem_in);

	cdmedinh = nslDiff (cdmedinh,cdmedinhtm, - cdmedinh + CDfefinput + CDlipinput);

	CDmedinh_out = Nsl2Sigmoid.eval(cdmedinh,CDlisigma1, CDlisigma2,
                                     CDlisigma3, CDlisigma4);
}
public void makeConn(){
}
}//end CDmedinh

