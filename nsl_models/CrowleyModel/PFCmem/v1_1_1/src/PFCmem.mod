package CrowleyModel.PFCmem.v1_1_1.src;

nslModule PFCmem(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  PFCmem
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 ThPFCmem_in(array_size,array_size); // 
public NslDinDouble2 LIPmem_in(array_size,array_size); // 
public NslDinDouble2 pfcseq_in(array_size,array_size); // 
public NslDoutDouble2 PFCmem_out(array_size,array_size); // 
private NslDouble2 pfcmem(array_size,array_size); // 
private double pfcmemtm; // 
private double pfcmemK1; // 
private double pfcmemK2; // 
private double pfcseqK; // 
private double PFCmemsigma1; // 
private double PFCmemsigma2; // 
private double PFCmemsigma3; // 
private double PFCmemsigma4; // 

//methods 
public void initRun(){
    PFCmem_out = 0.0;
    pfcmem = 0.0;

    pfcmemtm = 0.008;
    pfcmemK1 = 1.5;
    pfcmemK2 = 0.5;
    pfcseqK = 2.0;
    PFCmemsigma1 = 0.0;
    PFCmemsigma2 = 180.0;
    PFCmemsigma3 = 0.0;
    PFCmemsigma4 = 90.0;
}

public void simRun(){
	//ThPFCmem_in = pfcmemK1* ThPFCmem_in;

  // System.err.println("@@@@ PFCmem simRun entered @@@@");
    pfcmem = nslDiff(pfcmem,pfcmemtm,
			 -pfcmem
			 + (pfcmemK1 * ThPFCmem_in)
			 + (pfcmemK2 * LIPmem_in)
			 + (pfcseqK * pfcseq_in ^ LIPmem_in));
   
    pfcmem[4][4] = 0.0;
    PFCmem_out = Nsl2Sigmoid.eval(pfcmem,PFCmemsigma1, PFCmemsigma2,
			      PFCmemsigma3, PFCmemsigma4);
  }
public void makeConn(){
}
}//end PFCmem

