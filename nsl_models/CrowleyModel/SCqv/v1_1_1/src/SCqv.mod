/** 
Here is the class representing the target locations for SRBNs (saccade
related burst neurons). This layer is called in the model quasi visual
layer (SCqv) layer. The SCqv cells receive their input from LIP.
*/
package CrowleyModel.SCqv.v1_1_1.src;

nslModule SCqv(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  SCqv
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 LIPmem_in(array_size, array_size); // 
public NslDoutDouble2 SCqv_out(array_size, array_size); // 
private NslDouble2 scqv(array_size,array_size); // 
private double SCqvsigma1; // 
private double SCqvsigma2; // 
private double SCqvsigma3; // 
private double SCqvsigma4; // 
private double scqvtm; // 

//methods 
public void initRun()
{
	scqv = 0;
	SCqv_out = 0;

	SCqvsigma1 =   0;
	SCqvsigma2 =  90;
	SCqvsigma3 =   0;
	SCqvsigma4 =  90;
	scqvtm = 0.01;
}

public void simRun()
{
	// System.err.println("@@@@ SCqv simRun entered @@@@");
	scqv=nslDiff(scqv,scqvtm, -scqv + LIPmem_in);
	SCqv_out=Nsl2Sigmoid.eval(scqv,SCqvsigma1,SCqvsigma2,SCqvsigma3,SCqvsigma4);
}
public void makeConn(){
}
}//end SCqv

