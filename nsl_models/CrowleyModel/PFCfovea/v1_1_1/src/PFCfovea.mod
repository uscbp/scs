package CrowleyModel.PFCfovea.v1_1_1.src;

nslModule PFCfovea(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  PFCfovea
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 LIPvis_in(array_size,array_size); // 
public NslDoutDouble2 PFCfovea_out(array_size,array_size); // 
private double pfcfoveatm; // 
private double Fixation; // 
private double DisFixation; // 
private double PFCfoveasigma1; // 
private double PFCfoveasigma2; // 
private double PFCfoveasigma3; // 
private double PFCfoveasigma4; // 
private NslDouble2 pfcfovea(array_size,array_size); // 
private NslInt0 FOVEAX(4); // 
private NslInt0 FOVEAY(4); // 

//methods 
public void initModule(){

    FOVEAX = (NslInt0)nslGetValue("crowleyModel.FOVEAX");
    FOVEAY = (NslInt0)nslGetValue("crowleyModel.FOVEAY");  
}



public void initRun(){
    pfcfovea = 0.0;
    PFCfovea_out = 0.0;
    pfcfoveatm = 0.008;
    PFCfoveasigma1 = 0.0;
    PFCfoveasigma2 = 60.0;
    PFCfoveasigma3 = 0.0;
    PFCfoveasigma4 = 90.0;

    Fixation = 1.0;
    DisFixation = 0.0;
}

public void simRun(){
    // System.err.println("@@@@ PFCfovea simRun entered @@@@");
// 99/8/3 aa:in acutallity x should map with j, and y should map with i.
    pfcfovea = nslDiff(pfcfovea,pfcfoveatm, -pfcfovea + (LIPvis_in[(int)FOVEAX][(int)FOVEAY]* (Fixation - DisFixation)));
    PFCfovea_out =Nsl2Sigmoid.eval(pfcfovea,PFCfoveasigma1, PFCfoveasigma2,
				  PFCfoveasigma3, PFCfoveasigma4);
}
public void makeConn(){
}
}//end PFCfovea

