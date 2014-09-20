/** 
 STNlatburst class
 Represents the Subthalamic Nucleus Burst Cells Layer
 see    STNlatburst
 version 0.1 96-11-19
 author  Michael Crowley
 -var public GPElatburst_in - input coming from GPElatburst module (of type NslDouble2)
 -var public STNlatburst_out - output going to SNRlatburst module (of type NslDouble2)
*/
package CrowleyModel.STNlatburst.v1_1_1.src;

nslModule STNlatburst(int CorticalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  STNlatburst
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 GPElatburst_in(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 STNlatburst_out(CorticalArraySize,CorticalArraySize); // 
private NslDouble2 stnlatburst(CorticalArraySize,CorticalArraySize); // 
private double stnlatbursttm; // 
private double STNlatburstTONIC; // 
private double STNlbsigma1; // 
private double STNlbsigma2; // 
private double STNlbsigma3; // 
private double STNlbsigma4; // 

//methods 
public void initRun () {
    stnlatbursttm = 0.01;
    STNlatburstTONIC = 60;
    STNlbsigma1 = 10; //20; see lc30.nsl
    STNlbsigma2 = 60;
    STNlbsigma3 = 0;
    STNlbsigma4 = 60;
    stnlatburst=0;
    STNlatburst_out=0;
  }
  public void simRun () {
  // System.err.println("@@@@ STNlatburst simRun entered @@@@");
    stnlatburst = nslDiff (stnlatburst,stnlatbursttm, 
                                    - stnlatburst + STNlatburstTONIC 
                                                  - GPElatburst_in);
    STNlatburst_out = Nsl2Sigmoid.eval (stnlatburst,STNlbsigma1, STNlbsigma2,
                                           STNlbsigma3, STNlbsigma4);
  }
public void makeConn(){
}
}//end STNlatburst

