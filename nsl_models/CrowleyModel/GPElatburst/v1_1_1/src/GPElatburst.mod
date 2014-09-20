/** 
 GPElatburst class
 Represents the Globus Pallidus External Burst Cells Layer
 see    GPElatburst
 version 0.1 96-11-19
 author  Michael Crowley
 -var private CDindlatburst_in - input coming from CDlatburst module (of type NslDouble2)
 -var private GPElatburst_out - output going to STNlatburst module (of type NslDouble2)
*/
package CrowleyModel.GPElatburst.v1_1_1.src;

nslModule GPElatburst(int CorticalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  GPElatburst
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 CDindlatburst_in(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 GPElatburst_out(CorticalArraySize,CorticalArraySize); // 
private double gpelatbursttm; // 
private double GPElatburstTONIC; // 
private double GPElatburstK; // 
private double GPElbsigma1; // 
private double GPElbsigma2; // 
private double GPElbsigma3; // 
private double GPElbsigma4; // 
private NslDouble2 gpelatburst(CorticalArraySize,CorticalArraySize); // 

//methods 
public void initRun () 
{
	GPElatburst_out =0;

	//System.err.println("GPElatburst:=1=");
	gpelatbursttm = 0.01;
	//System.err.println("GPElatburst:=2=");
	GPElatburstTONIC = 30;
	//System.err.println("GPElatburst:=3=");
	GPElatburstK = 2;
	//System.err.println("GPElatburst:=4=");
	GPElbsigma1 = 0;
	//System.err.println("GPElatburst:=5=");
	GPElbsigma2 = 60;
	//System.err.println("GPElatburst:=6=");
	GPElbsigma3 = 0;
	//System.err.println("GPElatburst:=7=");
	GPElbsigma4 = 60;
	//System.err.println("GPElatburst:=8=");
}

public void simRun () 
{
	// System.err.println("@@@@ GPElatburst simRun entered @@@@");
	gpelatburst = nslDiff (gpelatburst,gpelatbursttm, - gpelatburst + GPElatburstTONIC - (GPElatburstK * CDindlatburst_in));
	GPElatburst_out = Nsl2Sigmoid.eval (gpelatburst,GPElbsigma1, GPElbsigma2, GPElbsigma3, GPElbsigma4);
}
public void makeConn(){
}
}//end GPElatburst

