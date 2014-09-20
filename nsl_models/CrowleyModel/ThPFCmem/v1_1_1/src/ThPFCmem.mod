/** 
Module: ThPFCmem - Part of the Thalamus
*/
package CrowleyModel.ThPFCmem.v1_1_1.src;

nslModule ThPFCmem(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  ThPFCmem
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 SNRmedburst_in(array_size,array_size); // 
public NslDinDouble2 ThMEDlcn_in(array_size,array_size); // 
public NslDinDouble2 PFCmem_in(array_size,array_size); // 
public NslDoutDouble2 ThPFCmem_out(array_size,array_size); // 
private double Thpfcmemtm; // 
private double ThpfcmemK1; // 
private double ThpfcmemK2; // 
private double ThpfcmemK3; // 
private double ThPFCmemsigma1; // 
private double ThPFCmemsigma2; // 
private double ThPFCmemsigma3; // 
private double ThPFCmemsigma4; // 
private NslDouble2 Thpfcmem(array_size,array_size); // 
private NslDouble2 THNewActivation(array_size,array_size); // 

//methods 
public void initModule()
  {
    THNewActivation = (NslDouble2)nslGetValue ("crowleyModel.thal1.THNewActivation");
  }

public void initRun(){
    Thpfcmem = 0;
    ThPFCmem_out = 0;
    Thpfcmemtm=0.006;
    ThpfcmemK1=1.5;
    ThpfcmemK2=0.5;
    ThpfcmemK3=0.5;
    ThPFCmemsigma1=30;
    ThPFCmemsigma2=65;
    ThPFCmemsigma3=0;
    ThPFCmemsigma4=60;
}

public void simRun(){
  /* <Q> PFCmem_in SNRmedburst_in ThMEDlcn_in THNewActivation */
    THNewActivation = (NslDouble2)nslGetValue("crowleyModel.thal1.THNewActivation");
  // System.err.println("@@@@ ThPFCmem_in simRun entered @@@@");
    Thpfcmem=nslDiff(Thpfcmem,Thpfcmemtm,-Thpfcmem
                     +(ThpfcmemK1*PFCmem_in)
                     -(ThpfcmemK2*SNRmedburst_in)
                     -(ThpfcmemK3*ThMEDlcn_in)
                     +THNewActivation);
			   
    ThPFCmem_out=Nsl2Sigmoid.eval(Thpfcmem,ThPFCmemsigma1,
                            ThPFCmemsigma2,
                            ThPFCmemsigma3,
                            ThPFCmemsigma4);
  }
public void makeConn(){
}
}//end ThPFCmem

