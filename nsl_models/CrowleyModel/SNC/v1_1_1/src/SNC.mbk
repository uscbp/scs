/** 
 SNC class
 Represents the Substantia Nigra pars Compacta Layer
 @see    SNC
 @version 0.1 96/11/19
 @author  Michael Crowley
 -var private SNCdop_out - output going to CDlatburst and CDmedburst modules (of type NslDouble2)<p>
*/
package CrowleyModel.SNC.v1_1_1.src;

nslModule SNC(int CorticalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  SNC
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 LimbicCortex_in(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 SNCdop_out(CorticalArraySize,CorticalArraySize); // 
private NslDouble2 sncdop(CorticalArraySize,CorticalArraySize); // 
private double sncdoptm; // 
private double SNCdopsigma1; // 
private double SNCdopsigma2; // 
private double SNCdopsigma3; // 
private double SNCdopsigma4; // 

//methods 
public void initRun () {
    sncdoptm = 0.01;
    SNCdopsigma1 = 0;
    SNCdopsigma2 = 60;
    SNCdopsigma3 = 0;
    SNCdopsigma4 = 20; //10;
  }

  public void simRun () {
  // System.err.println("@@@@ SNC simRun entered @@@@");
    sncdop = nslDiff (sncdop,sncdoptm, 
                          - sncdop + LimbicCortex_in);
    SNCdop_out = Nsl2Sigmoid.eval (sncdop,SNCdopsigma1, SNCdopsigma2,
                                 SNCdopsigma3, SNCdopsigma4);
  }
public void makeConn(){
}
}//end SNC

