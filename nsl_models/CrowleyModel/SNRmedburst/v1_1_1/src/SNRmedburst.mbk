/** 
 SNRmedburst class
 Represents the Substantia Nigra pars Reticulata Burst Cells Layer
 @see     SNRmedburst
 @version 0.1 96/11/19
 @author  Michael Crowley
 -var private CDdirmedburst_in - input coming from 
 CDmedburst module (of type NslDouble2)<p>
 -var private STNmedburst_in - input coming from 
 STNmedburst module (of type NslDouble2)<p>
 -var private SNRmedburst_out - output going to 
 Thal module (of type NslDouble2)<p>
*/
package CrowleyModel.SNRmedburst.v1_1_1.src;

nslModule SNRmedburst(int CorticalArraySize,int StriatalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  SNRmedburst
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinInt2 SNRMapCount_bulk(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble3 SNRweights_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 SNRxmap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinInt3 SNRymap_bulk(CorticalArraySize,CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 STNmedburst_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 CDdirmedburst_in(StriatalArraySize,StriatalArraySize); // 
public NslDoutDouble2 SNRmedburst_out(CorticalArraySize,CorticalArraySize); // 
private double snrmedbursttm; // 
private double SNRmedburstTONIC; // 
private double SNRcdlbK; // 
private double SNRstnlbK; // 
private double SNRlbsigma1; // 
private double SNRlbsigma2; // 
private double SNRlbsigma3; // 
private double SNRlbsigma4; // 
private NslDouble2 snrmedburst(CorticalArraySize,CorticalArraySize); // 
private NslDouble2 SNRcdinput(9,9); // 

//methods 
// This function is also called in the lib.h module by:
  // TestConnections, TestFoveaMapping

public void initRun () {
    snrmedbursttm = 0.01;
    SNRmedburstTONIC = 30;
    SNRcdlbK = 1;
    SNRstnlbK = 0.5;
    SNRlbsigma1 = 15;
    SNRlbsigma2 = 60;
    SNRlbsigma3 = 0;
    SNRlbsigma4 = 60;
    snrmedburst = 30;
    SNRmedburst_out = Nsl2Sigmoid.eval(snrmedburst,SNRlbsigma1, SNRlbsigma2,
                                           SNRlbsigma3, SNRlbsigma4);

  }
  public void simRun () {
    int tempint;
    /* <Q> SNRcdinput? */
  // System.err.println("@@@@ SNRmedburst simRun entered @@@@");

//System.out.println("SNRxmap "+new NslInt3(SNRxmap));

    tempint = SumCDtoSNR (CDdirmedburst_in, SNRcdinput);
//System.out.println("SNRweights:"+SNRweights_bulk);

//    System.out.println("CD.max "+CDdirmedburst_in.max() + "\nSNR "+SNRcdinput);
    snrmedburst = nslDiff (snrmedburst,snrmedbursttm, 
                                    - snrmedburst + SNRmedburstTONIC 
                                    - (SNRcdlbK * SNRcdinput)
                                    + (SNRstnlbK * STNmedburst_in));
    SNRmedburst_out = Nsl2Sigmoid.eval(snrmedburst,SNRlbsigma1, SNRlbsigma2,
                                           SNRlbsigma3, SNRlbsigma4);
 }


public int SumCDtoSNR (NslDouble2 CD, NslDouble2 SNR) 
  {
  //  This function sums the activity in the medial CD circuit onto 
  //  the medial SNR circuit through SNRweights, SNRxmap and SNRymap.

    int i, j, k;
    NslInt0 xmaploc=new NslInt0("xmaploc",this), ymaploc=new NslInt0("ymaploc",this);

    //if (CD==null) System.err.println("CD null!!!!");
    //if (SNR==null) System.err.println("SNR null!!!!");
     // System.err.println("SNRmedburst.SumCDtoSNR: entered....");
    SNR = 0;  // Ensure new mapping only
     // System.err.println("SNRmedburst.SumCDtoSNR: A");
     //System.err.println("SNRMapCount:"+SNRMapCount);
    for (i = 0; i < CorticalArraySize; i ++)
      for (j = 0; j < CorticalArraySize; j ++) {
        for (k = 0; k < SNRMapCount_bulk[i][j]; k ++) {
    //System.err.println("SNRmedburst.SumCDtoSNR: loop: ("+i+","+j+","+k+")");
          xmaploc = SNRxmap_bulk[i][j][k];
          ymaploc = SNRymap_bulk[i][j][k];
          SNR [i][j] = SNR[i][j] + CD [xmaploc.get()][ymaploc.get()] * SNRweights_bulk[i][j][k];
        }
      }
    return 0;
}
public void makeConn(){
}
}//end SNRmedburst

