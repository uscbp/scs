/** 
 STNmedburst class
 Represents the Subthalamic Nucleus Burst Cells Layer
 see     STNmedburst
 version 0.1 96-11-19
 author Michael Crowley
 -var private GPEmedburst_in - input coming from GPEmedburst module (of type NslDouble2)
 -var private STNmedburst_out - output going to SNRmedburst module (of type NslDouble2)
*/
package CrowleyModel.STNmedburst.v1_1_1.src;

nslModule STNmedburst(int CorticalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  STNmedburst
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 GPEmedburst_in(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 STNmedburst_out(CorticalArraySize,CorticalArraySize); // 
private double stnmedbursttm; // 
private double STNmedburstTONIC; // 
private double STNlbsigma1; // 
private double STNlbsigma2; // 
private double STNlbsigma3; // 
private double STNlbsigma4; // 
private NslDouble2 stnmedburst(CorticalArraySize,CorticalArraySize); // 

//methods 
public void initRun () {
    STNmedburst_out=0;
    stnmedbursttm = 0.01;
    STNmedburstTONIC = 60;
    STNlbsigma1 = 10; //20;
    STNlbsigma2 = 60;
    STNlbsigma3 = 0;
    STNlbsigma4 = 60;
  }
  public void simRun () {
	  // System.err.println("@@@@ STNmedburst simRun entered @@@@");
    stnmedburst = nslDiff (stnmedburst,stnmedbursttm, 
                                    - stnmedburst + STNmedburstTONIC - GPEmedburst_in);
    STNmedburst_out = Nsl2Sigmoid.eval(stnmedburst,STNlbsigma1, STNlbsigma2,
                                           STNlbsigma3, STNlbsigma4);
  }
public void makeConn(){
}
}//end STNmedburst

