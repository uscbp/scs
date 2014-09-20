package CrowleyModel.PFCseq.v1_1_1.src;
nslImport CrowleyModel.IJpair.v1_1_1.src.*;

nslModule PFCseq(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  PFCseq
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 LIPvis_in(array_size,array_size); // 
public NslDinDouble2 PFCmem_in(array_size,array_size); // 
public NslDinDouble2 PFCfovea_in(array_size,array_size); // 
public NslDoutDouble2 pfcseq_out(array_size,array_size); // 
public NslDoutDouble2 PFCseq_out(array_size,array_size); // 
private NslDouble2 pfcsel(array_size,array_size); // 
private NslDouble2 pfcseq(array_size,array_size); // 
private NslInt0 FOVEAX; // 
private NslInt0 FOVEAY; // 
public IJpair tij(); // 
private double pfcseqtm; // 
private double pfcselK; // 
private double pfcfoveaK; // 
private double Refractory; // 
private double SEQmax; // 
private double seqmax; // 

//methods 
public void initModule() {
     tij.init(); //initialize user class
     FOVEAX=(NslInt0)nslGetValue("crowleyModel.FOVEAX");
     FOVEAY=(NslInt0)nslGetValue("crowleyModel.FOVEAY");
}
  
public void initRun(){
    pfcseq_out = 0.0;
    PFCseq_out = 0.0;

    SEQmax=0;
    seqmax=0;
    
    pfcseqtm = 0.008;
    pfcselK = 1.5;
    pfcfoveaK = 2.0;
    Refractory = 0.025 / nslGetRunDelta();
}
public void simRun(){
    int tempint;

  /// System.err.println("@@@@ PFCseq simRun entered @@@@");

    pfcseq_out=SetTargetSequence(LIPvis_in, pfcseq_out);

    SEQmax=nslMaxValue(PFCseq_out);
    seqmax=nslMaxValue(pfcseq_out);

//    if ((SEQmax < Refractory) && (seqmax > 0.0)){
    if ((nslMaxValue(PFCseq_out) < Refractory) && (nslMaxValue(pfcseq_out) > 0.0)){
      tempint = tij.MaxIJ(PFCmem_in);
      pfcsel = 0.0;
      pfcsel[tij.getI()][tij.getJ()] = PFCmem_in[tij.getI()][tij.getJ()];
    }

    PFCseq_out = nslDiff(PFCseq_out,pfcseqtm,
				 -PFCseq_out
				 + (pfcselK * pfcsel)
				 - (pfcfoveaK * PFCfovea_in));

    pfcseq_out = pfcseq_out * 0.95;
    //pfcseq = pfcseq * 0.95;
    pfcsel = pfcsel * 0.95;
}

  // private methods
private NslDouble2 SetTargetSequence(NslDouble2 inmat, NslDouble2 outmat) {
    // This function set the sequence order for
    // sequentially appearing targets.
    int i, j, savei, savej, imax, jmax;
    int newtarget;
    /*
    imax = (int)inmat.get_imax();
    jmax = (int)inmat.get_jmax();
    */

    imax = (int)inmat.getSize1();
    jmax = (int)inmat.getSize2();

    savei = -1;
    savej = -1;

    newtarget = 0;             //Set to 1 when a saccade target first appears

    for ( i=0; i<imax; i++ ){
      for ( j=0; j<jmax; j++ ){
	if ( ( inmat[i][j] > 0.5 )  && ( outmat[i][j] == 0 ) ){
	  if ( ( i!=FOVEAX ) || ( j!=FOVEAY ) ){

	    // Found a new target that is not on the fovea.  My assumption
	    // is that there are no saccadic fovea projections from cortex.
	    // Save element location so value can be set to 1 after
	    // all other target memories have been incremented

	    savei = i;
	    savej = j;
	  }
	}
      }
    }
    if ( ( savei >= 0 ) && ( savej >= 0 ) ){
      // New target exists.  Increment any existing target memories.

      for ( i=0; i<imax; i++ ){
	for ( j=0; j<jmax; j++ ){
	  if ( outmat[i][j] > 0 ){
	    outmat[i][j] = outmat[i][j] + 1;
	  }
	}
      }
      outmat[savei][savej] = 1;    //Set new target memory to 1

    }
    return outmat;
}
public void makeConn(){
}
}//end PFCseq

