/** 
 SNRlatburst class
 Represents the Substantia Nigra pars Reticulata Burst Cells Layer
 see    SNRlatburst
 version 0.1 96-11-19
 author  Michael Crowley
 -var private CDdirlatburst_in - input coming from CDlatburst module (of type NslDouble2)
 -var private STNlatburst_in - input coming from STNlatburst module (of type NslDouble2)
 -var private SNRlatburst_out - output going to SC module (of type NslDouble2)
*/
package CrowleyModel.SNRlatburst.v1_1_1.src;

nslModule SNRlatburst(int CorticalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  SNRlatburst
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 CDdirlatburst_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 STNlatburst_in(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 SNRlatburst_out(CorticalArraySize,CorticalArraySize); // 
private double snrlatbursttm; // 
private double SNRlatburstTONIC; // 
private double SNRcdlbK; // 
private double SNRstnlbK; // 
private double SNRlbsigma1; // 
private double SNRlbsigma2; // 
private double SNRlbsigma3; // 
private double SNRlbsigma4; // 
private NslDouble2 snrlatburst(CorticalArraySize,CorticalArraySize); // 

//methods 
public void initRun () {

    snrlatbursttm = 0.01;
    SNRlatburstTONIC = 30;
    SNRcdlbK = 1;
    SNRstnlbK = 0.5;
    SNRlbsigma1 = 15;
    SNRlbsigma2 = 60;
    SNRlbsigma3 = 0;
    SNRlbsigma4 = 60;
    snrlatburst = 30;

 	// 99/8/2 aa check on the following
    SNRlatburst_out = Nsl2Sigmoid.eval (snrlatburst,SNRlbsigma1, SNRlbsigma2,
                                           SNRlbsigma3, SNRlbsigma4);
  }
public void simRun () {
  // System.err.println("@@@@ SNRlatburst simRun entered @@@@");
    snrlatburst = nslDiff (snrlatburst,snrlatbursttm, 
                                    - snrlatburst + SNRlatburstTONIC 
                                                  - (SNRcdlbK * CDdirlatburst_in)
                                                  + (SNRstnlbK * STNlatburst_in));
    SNRlatburst_out = Nsl2Sigmoid.eval (snrlatburst,SNRlbsigma1, SNRlbsigma2,
                                           SNRlbsigma3, SNRlbsigma4);

  }
public void makeConn(){
}
}//end SNRlatburst

