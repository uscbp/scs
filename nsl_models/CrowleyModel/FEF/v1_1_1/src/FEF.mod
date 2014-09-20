/** 
 Here is the class representing the Frontal Eye Fields (FEF) module.
 FEF is modeled to have two type of cells: FEFvis, visual response cells and
 FEFmem, memory response cells. FEFvis only responds to visual stimuli that 
 are targets of impending saccades and do not fire before saccades made 
 without visual input, nor they project to the SC.
*/
package CrowleyModel.FEF.v1_1_1.src;

nslJavaModule FEF(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  FEF
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 ThFEFmem_in(array_size,array_size); // 
public NslDinDouble2 LIPmem_in(array_size,array_size); // 
public NslDinDouble2 PFCgo_in(array_size,array_size); // 
public NslDinDouble2 PFCnovelty(array_size,array_size); // 
public NslDinDouble2 PFCmem_in(array_size,array_size); // 
public NslDoutDouble2 FEFmem_out(array_size,array_size); // 
public NslDoutDouble2 FEFsac_out(array_size,array_size); // 
private NslDouble2 fefmem(array_size,array_size); // 
private NslDouble2 fefsac(array_size,array_size); // 
private NslInt0 FOVEAX; // 
private NslInt0 FOVEAY; // 
private final int NINE=9; // 
private final double CorticalSlowdown=1.0; // 
private final double basefefsactm=0.008; // 
private double FEFmemsigma1; // 
private double FEFmemsigma2; // 
private double FEFmemsigma3; // 
private double FEFmemsigma4; // 
private double FEFsacsigma1; // 
private double FEFsacsigma2; // 
private double FEFsacsigma3; // 
private double FEFsacsigma4; // 
private double FEFSaccadeVector; // 
private double fefmemtm; // 
private double fefsactm; // 
private double fefmemK1; // 
private double fefmemK2; // 
private double fefmemK3; // 
private double fefsacK1; // 
private double fefsacK2; // 

//methods 
public void initModule() 
{
	FOVEAX=(NslInt0)nslGetValue("crowleyModel.FOVEAX");
	FOVEAY=(NslInt0)nslGetValue("crowleyModel.FOVEAY");
	nslAddAreaCanvas("output", "fef", FEFsac_out, 0, 100);
}

  public void initRun(){
    
    FEFmem_out = 0;
    FEFsac_out = 0;
    fefmem       = 0;
    fefsac       = 0;

    FEFmemsigma1 =   0;
    FEFmemsigma2 =  90;
    FEFmemsigma3 =   0;
    FEFmemsigma4 =  90;
    FEFsacsigma1 =  40;
    FEFsacsigma2 =  90;
    FEFsacsigma3 =   0;
    FEFsacsigma4 =  90;
    

    FEFSaccadeVector = 0;
    fefmemtm = 0.008;
    fefsactm = 0.006;
    fefmemK1 = 0.5;
    fefmemK2 = 1;
    fefmemK3 = 0.5;

    fefsacK1 = 0.3;
    fefsacK2 = 1;
  }
public void simRun(){

  // System.err.println("@@@@ FEF simRun entered @@@@");


    //LNK_FEF2
    /**
     * A memory loop is established between FEFmem and mediodorsal thalamus
     *(ThFEFmem_in) to maintain the saccadic target memory.
     */

    // 1-2-97 isaac:  fefmemK3 * PFCmem is missing from the original code.
    // redefine the inport interface.

    fefmem=nslDiff(fefmem,fefmemtm,-fefmem+(fefmemK1*ThFEFmem_in)+(fefmemK2*LIPmem_in)+(fefmemK3*PFCmem_in));
    
    //    fefsactm = basefefsactm * CorticalSlowdown;
    
    fefsac=nslDiff(fefsac,fefsactm,-fefsac + ( fefsacK1 * FEFmem_out )
		       + ( fefsacK2 * PFCgo_in ));// + PFCnovelty);
    fefsac[(int)FOVEAX][(int)FOVEAY] = 0;
    FEFmem_out=Nsl2Sigmoid.eval(fefmem,FEFmemsigma1,FEFmemsigma2,FEFmemsigma3,FEFmemsigma4);
    FEFsac_out=Nsl2Sigmoid.eval(fefsac,FEFsacsigma1,FEFsacsigma2,FEFsacsigma3,FEFsacsigma4);
	//96/12/20 aa
    //System.out.println("FEFsac_out: " + FEFsac_out);
  }
public void makeConn(){
}
}//end FEF

