package CrowleyModel.Thal.v1_1_1.src;
nslImport CrowleyModel.ThPFCmem.v1_1_1.src.*;
nslImport CrowleyModel.ThFEFmem.v1_1_1.src.*;
nslImport CrowleyModel.ThLIPmem.v1_1_1.src.*;
nslImport CrowleyModel.ThMEDlcn.v1_1_1.src.*;
nslImport CrowleyModel.RNMEDinh.v1_1_1.src.*;

nslJavaModule Thal(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  Thal
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 SNRmedburst_in(array_size,array_size); // 
public NslDinDouble2 PFCmem_in(array_size,array_size); // 
public NslDinDouble2 FEFmem_in(array_size,array_size); // 
public NslDinDouble2 LIPmem_in(array_size,array_size); // 
public NslDoutDouble2 ThPFCmem_out(array_size,array_size); // 
public NslDoutDouble2 ThFEFmem_out(array_size,array_size); // 
public NslDoutDouble2 ThLIPmem_out(array_size,array_size); // 
public NslDoutDouble2 ThMEDlcn_out(array_size,array_size); // 
public NslDoutDouble2 RNMEDinh_out(array_size,array_size); // 
private int THBurstRate=60; // 
private int THBurstLevel=15; // 
private double DecayRate=0.9; // 
private NslDouble2 THNewActivation(array_size,array_size); // 
public ThPFCmem thpfc(array_size); // 
public ThFEFmem thfef(array_size); // 
public ThLIPmem thlip(array_size); // 
public ThMEDlcn thmed(array_size); // 
public RNMEDinh rnmed(array_size); // 

//methods 
public void initModule()
{
	nslAddAreaCanvas("output", "thna", RNMEDinh_out, 0, 10);
}


public void initRun(){
    THNewActivation = 0;
  }

  public void simRun(){
    //    THNewActivation = THCheckBurst(SNRmedburst);
    // System.err.println("@@@@ Thal simRun entered @@@@");
     THCheckBurst(THNewActivation, SNRmedburst_in);
  }

  /**
    Uses  bursting constant to casue activation in neuorns
    undergoing remapping. This is indicated by a decrease in 
    inhibition below a certain threshold.
    */

public  void  THCheckBurst(NslDouble2 THNewActivation, NslDouble2 SNRmedburst_in) {
    double[][] inmat = SNRmedburst_in.get();
    double[][] outmat = THNewActivation.get();
    int size1 = inmat.length;
    int size2 = inmat[0].length;

    if(size1!=outmat.length || size2!=outmat[0].length) {
      System.err.println("THCheckBurst: array size not match\n"+
			 "THNewActivation ("+size1+"x"+size2+
			 ") SNRmedburst_in ("+inmat.length+"x"+
			 inmat[0].length+")");
    }
    for (int i=0; i<size1; i++) {
      for(int j=0; j<size2; j++) {
	if (inmat[i][j]<THBurstLevel)
	  outmat[i][j]=THBurstRate;
	else 
          outmat[i][j]=outmat[i][j]*DecayRate;
      }
    }

  }
public void makeConn(){
    nslRelabel(SNRmedburst_in,thpfc.SNRmedburst_in);
    nslRelabel(SNRmedburst_in,thfef.SNRmedburst_in);
    nslRelabel(SNRmedburst_in,thlip.SNRmedburst_in);
    nslRelabel(SNRmedburst_in,rnmed.SNRmedburst_in);
    nslRelabel(PFCmem_in,thpfc.PFCmem_in);
    nslRelabel(FEFmem_in,thfef.FEFmem_in);
    nslRelabel(LIPmem_in,thlip.LIPmem_in);
    nslRelabel(thpfc.ThPFCmem_out,ThPFCmem_out);
    nslRelabel(thfef.ThFEFmem_out,ThFEFmem_out);
    nslConnect(thmed.ThMEDlcn_out,thpfc.ThMEDlcn_in);
    nslConnect(thmed.ThMEDlcn_out,thfef.ThMEDlcn_in);
    nslConnect(thmed.ThMEDlcn_out,thlip.ThMEDlcn_in);
    nslRelabel(thmed.ThMEDlcn_out,ThMEDlcn_out);
    nslRelabel(thlip.ThLIPmem_out,ThLIPmem_out);
    nslConnect(rnmed.RNMEDinh_out,thmed.RNMEDinh_in);
    nslRelabel(rnmed.RNMEDinh_out,RNMEDinh_out);
}
}//end Thal

