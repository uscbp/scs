/** 
 CDmedtan class
 Represents the Caudate Tonically Active Cells Layer
 see     CDmedtan
 version 0.1 96-11-19
 author  Michael Crowley
 -var private SNCdop_in - input coming from SNC module (of type NslDouble2)
 -var private CDmedinh_in - input coming from CDmedinh module (of type NslDouble2)
 -var private CDmedtan_out - output going to CDmedburst module (of type NslDouble2)
*/
package CrowleyModel.CDmedtan.v1_1_1.src;

nslModule CDmedtan(int CorticalArraySize, int StriatalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  CDmedtan
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 SNCdop_in(StriatalArraySize,StriatalArraySize); // 
public NslDinDouble2 CDmedinh_in(StriatalArraySize,StriatalArraySize); // 
public NslDoutDouble2 CDmedtan_out(StriatalArraySize,StriatalArraySize); // 
private double cdmedtantm; // 
private double cdmedtanTONIC; // 
private double CDSNCdopK; // 
private double CDltsigma1; // 
private double CDltsigma2; // 
private double CDltsigma3; // 
private double CDltsigma4; // 
private NslDouble2 cdmedtan(StriatalArraySize,StriatalArraySize); // 

//methods 
public void initRun () 
{
	CDmedtan_out=0;

	cdmedtantm = 0.01;
	cdmedtanTONIC = 10;
	CDSNCdopK = 0.5;
	CDltsigma1 = 0;
	CDltsigma2 = 10;
	CDltsigma3 = 0;
	CDltsigma4 = 10;
}

public void simRun () 
{
	// System.err.println("@@@@ CDmedtanh simRun entered @@@@");
	cdmedtan = nslDiff(cdmedtan,cdmedtantm, 
                              - cdmedtan + cdmedtanTONIC - CDmedinh_in
                              - (CDSNCdopK * nslMaxValue(SNCdop_in)));
	CDmedtan_out = Nsl2Sigmoid.eval( cdmedtan,CDltsigma1, CDltsigma2,
                                     CDltsigma3, CDltsigma4); 
}
public void makeConn(){
}
}//end CDmedtan

