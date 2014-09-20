/** 
Module ThMEDlcn - part of Thalamus
*/
package CrowleyModel.ThMEDlcn.v1_1_1.src;

nslModule ThMEDlcn(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  ThMEDlcn
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 RNMEDinh_in(array_size,array_size); // 
public NslDoutDouble2 ThMEDlcn_out(array_size,array_size); // 
private double Thmedlcntm; // 
private double ThmedlcnTONIC; // 
private double ThmedlcnK; // 
private double ThMEDlcnsigma1; // 
private double ThMEDlcnsigma2; // 
private double ThMEDlcnsigma3; // 
private double ThMEDlcnsigma4; // 
private NslDouble2 Thmedlcn(array_size,array_size); // 

//methods 
public void initRun(){
    Thmedlcn = 0;
    ThMEDlcn_out = 0;

     Thmedlcntm=0.006;
     ThmedlcnTONIC=10; ThmedlcnK=1;
     ThMEDlcnsigma1=-10;
     ThMEDlcnsigma2=10;
     ThMEDlcnsigma3=0;
     ThMEDlcnsigma4=10;
}

public void simRun(){
	  /* <Q> RNMEDinh_in */
      // System.err.println("@@@@ ThMEDlcn simRun entered @@@@");
    Thmedlcn=nslDiff(Thmedlcn,Thmedlcntm,-Thmedlcn
                     +ThmedlcnTONIC-(ThmedlcnK*RNMEDinh_in));

    ThMEDlcn_out=Nsl2Sigmoid.eval(Thmedlcn,ThMEDlcnsigma1,
                            ThMEDlcnsigma2,
                            ThMEDlcnsigma3,
                            ThMEDlcnsigma4);
  }
public void makeConn(){
}
}//end ThMEDlcn

