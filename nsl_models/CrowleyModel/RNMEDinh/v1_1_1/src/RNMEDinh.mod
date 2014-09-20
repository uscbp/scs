/** 
Module RNMEDinh - part of thalamus
*/
package CrowleyModel.RNMEDinh.v1_1_1.src;

nslModule RNMEDinh(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  RNMEDinh
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 SNRmedburst_in(array_size,array_size); // 
public NslDoutDouble2 RNMEDinh_out(array_size,array_size); // 
private NslDouble2 RNmedinh(array_size,array_size); // 
private double RNmedinhtm; // 
private double RNmedinhTONIC; // 
private double RNmedinhK; // 
private double RNmedinhsigma1; // 
private double RNmedinhsigma2; // 
private double RNmedinhsigma3; // 
private double RNmedinhsigma4; // 

//methods 
public void initRun(){
    RNmedinh = 0;
    RNMEDinh_out = 0;
     RNmedinhtm=0.006;
     RNmedinhTONIC=10;
     RNmedinhK=0.16;
     RNmedinhsigma1=-10;
     RNmedinhsigma2=10;
     RNmedinhsigma3=0;
     RNmedinhsigma4=10;
}

public void simRun() {
	  /* <Q> SNRmedburst_in */
  // System.err.println("@@@@ RNMEDinh simRun entered @@@@");
    RNmedinh=nslDiff(RNmedinh,RNmedinhtm,-RNmedinh
                     +RNmedinhTONIC -(RNmedinhK*SNRmedburst_in));

    RNMEDinh_out=Nsl2Sigmoid.eval(RNmedinh,RNmedinhsigma1,
                            RNmedinhsigma2,
                            RNmedinhsigma3,
                            RNmedinhsigma4);
  }
public void makeConn(){
}
}//end RNMEDinh

