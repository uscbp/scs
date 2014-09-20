/** 
Module ThFEFmem - Part of the Thalamus
*/
package CrowleyModel.ThFEFmem.v1_1_1.src;

nslModule ThFEFmem(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  ThFEFmem
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 FEFmem_in(array_size,array_size); // 
public NslDinDouble2 SNRmedburst_in(array_size,array_size); // 
public NslDinDouble2 ThMEDlcn_in(array_size,array_size); // 
public NslDoutDouble2 ThFEFmem_out(array_size,array_size); // 
private double Thfefmemtm; // 
private double ThfefmemK1; // 
private double ThfefmemK2; // 
private double ThfefmemK3; // 
private double ThFEFmemsigma1; // 
private double ThFEFmemsigma2; // 
private double ThFEFmemsigma3; // 
private double ThFEFmemsigma4; // 
private NslDouble2 THNewActivation(array_size,array_size); // 
private NslDouble2 Thfefmem(array_size,array_size); // 

//methods 
public void initModule()
 {
    THNewActivation= (NslDouble2)nslGetValue("crowleyModel.thal1.THNewActivation") ;
  }

public void initRun(){

    ThFEFmem_out = 0;
    Thfefmem = 0;
    Thfefmemtm=0.006;
    ThfefmemK1=1.5;
    ThfefmemK2=0.5;
    ThfefmemK3=0.5;
    ThFEFmemsigma1=30;
    ThFEFmemsigma2=65;
    ThFEFmemsigma3=0;
    ThFEFmemsigma4=60;
  }

public void simRun(){
  /* <Q> FEFmem_in  SNRmedburst_in  ThMEDlcn_in  THNewActivation*/
    THNewActivation= (NslDouble2)nslGetValue("crowleyModel.thal1.THNewActivation") ;

  // System.err.println("@@@@ ThFEFmem simRun entered @@@@");
    Thfefmem=nslDiff(Thfefmem,Thfefmemtm,-Thfefmem
                     +(ThfefmemK1*FEFmem_in)
                     -(ThfefmemK2*SNRmedburst_in)
                     -(ThfefmemK3*ThMEDlcn_in)
                     +THNewActivation);


   ThFEFmem_out=Nsl2Sigmoid.eval(Thfefmem,ThFEFmemsigma1,
                            ThFEFmemsigma2,
                            ThFEFmemsigma3,
                            ThFEFmemsigma4);

	// 96/12/20
	//System.out.println("ThFEFmem_out: " + ThFEFmem_out);
  }
public void makeConn(){
}
}//end ThFEFmem

