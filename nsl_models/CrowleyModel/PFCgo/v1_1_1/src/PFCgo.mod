package CrowleyModel.PFCgo.v1_1_1.src;

nslModule PFCgo(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  PFCgo
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 LIPmem_in(array_size,array_size); // 
public NslDinDouble2 PFCseq_in(array_size,array_size); // 
public NslDinDouble2 PFCfovea_in(array_size,array_size); // 
public NslDoutDouble2 PFCgo_out(array_size,array_size); // 
private NslDouble2 pfcgo(array_size,array_size); // 
private double pfcgotm; // 
private double basepfcgotm; // 
private double CorticalSlowdown; // 
private double PFCgosigma1; // 
private double PFCgosigma2; // 
private double PFCgosigma3; // 
private double PFCgosigma4; // 
private double PFCseqK=0.9; // 
private double PFClipmem=0.35; // 

//methods 
public void initRun(){
//    PFClipmem      =  0.35; 
//    PFCseqK        =  0.9;

    pfcgo = 0.0;
    PFCgo_out = 0.0;
    basepfcgotm = 0.008;
    CorticalSlowdown = 1.0;
    pfcgotm = basepfcgotm * CorticalSlowdown;
    PFCgosigma1 = 20.0;
    PFCgosigma2 = 60.0;
    PFCgosigma3 = 0.0;
    PFCgosigma4 = 60.0;
}

public void simRun(){

  // System.err.println("@@@@ PFCgo simRun entered @@@@");
    /* <Q> PFClipmem PFCseqK */
    pfcgo = nslDiff(pfcgo,pfcgotm,
		       -pfcgo
		       + (PFClipmem * LIPmem_in)
		       + (PFCseqK * PFCseq_in)
		       - PFCfovea_in);
    PFCgo_out = Nsl2Sigmoid.eval(pfcgo,PFCgosigma1, PFCgosigma2,
				  PFCgosigma3, PFCgosigma4);
	// 96/12/20 aa
	//System.out.println("PFCgo_out: " + PFCgo_out);
  }
public void makeConn(){
}
}//end PFCgo

