/** 
 CDlattan class
 Represents the Caudate Tonically Active Cells Layer
 see     CDlattan
 version 0.1 96-11-19
 author  Michael Crowley
 var private CDlatinh_in - input coming from CDlatinh module (of type NslDouble2)
 var private SNCdop_in - input coming from SNC module (of type NslDouble2)
 var private CDlattan_out - output going to CDlatburst module (of type NslDouble2)
*/
package CrowleyModel.CDlattan.v1_1_1.src;

nslModule CDlattan(int CorticalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  CDlattan
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 SNCdop_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 CDlatinh_in(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 CDlattan_out(CorticalArraySize,CorticalArraySize); // 
private double cdlattantm; // 
private double cdlattanTONIC; // 
private double CDSNCdopK; // 
private double CDltsigma1; // 
private double CDltsigma2; // 
private double CDltsigma3; // 
private double CDltsigma4; // 
private NslDouble2 cdlattan(CorticalArraySize,CorticalArraySize); // 

//methods 
public void initRun () 
{
	cdlattantm = 0.01;
	cdlattanTONIC = 10;
	CDSNCdopK = 0.5;
	CDltsigma1 = 0;
	CDltsigma2 = 10;
	CDltsigma3 = 0;
	CDltsigma4 = 10;
}

public void simRun () 
{
	// System.err.println("@@@@ CDlattan simRun entered @@@@");
	cdlattan = nslDiff (cdlattan,cdlattantm, -cdlattan + cdlattanTONIC - CDlatinh_in - (CDSNCdopK * SNCdop_in));
	CDlattan_out = Nsl2Sigmoid.eval(cdlattan,CDltsigma1, CDltsigma2, CDltsigma3, CDltsigma4);
}
public void makeConn(){
}
}//end CDlattan

