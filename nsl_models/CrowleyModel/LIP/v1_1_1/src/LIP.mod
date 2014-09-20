/** 
A class representing the lateral Intraparital Cortex layer of Crowley Model.
@see Michael Crowley Model
@version   Fall 96
@ author   HBP
 -var public lipvistm used in LIPvis to calculate the membrane potential at
 lipvistm.<p>
 -var public lipvistm used in LIPmem to calculate the membrane potential at
 lipmemtm.<p>
*/
package CrowleyModel.LIP.v1_1_1.src;

nslJavaModule LIP(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  LIP
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 ThLIPmem_in(array_size,array_size); // 
public NslDinDouble2 SLIPvis_in(array_size,array_size); // 
public NslDoutDouble2 LIPvis_out(array_size,array_size); // 
public NslDoutDouble2 LIPmem_out(array_size,array_size); // 
private double lipvistm; // 
private double lipmemtm; // 
private double LIPmemK; // 
private double LIPvissigma1; // 
private double LIPvissigma2; // 
private double LIPvissigma3; // 
private double LIPvissigma4; // 
private double LIPmemsigma1; // 
private double LIPmemsigma2; // 
private double LIPmemsigma3; // 
private double LIPmemsigma4; // 
private NslDouble2 lipvis(array_size,array_size); // 
private NslDouble2 lipmem(array_size,array_size); // 
private NslInt0 FOVEAX; // 
private NslInt0 FOVEAY; // 

//methods 
public void initModule()
{
	FOVEAX = (NslInt0)nslGetValue("crowleyModel.FOVEAX")  ;
	FOVEAY = (NslInt0)nslGetValue("crowleyModel.FOVEAX")  ;
	nslAddAreaCanvas("output", "lip", LIPmem_out, 0, 100);
}

public void initRun(){
    lipvis = 0;
    lipmem = 0;

    LIPvis_out = 0;
    LIPmem_out=0;
    LIPmemK=0.9;

    lipvistm=0.006;
    lipmemtm=0.008;
    LIPvissigma1=0;
    LIPvissigma2=90;
    LIPvissigma3=0;
    LIPvissigma4=90;

    LIPmemsigma1=0;
    LIPmemsigma2=90;
    LIPmemsigma3=0;
    LIPmemsigma4=90;
}

public void simRun(){
  // System.err.println("@@@@ LIP simRun entered @@@@");
  /* <Q> RETINA? LIPmemK ThLIPmem_in */

/* Note: the order of the following lines is very important */
/* The membrain potentials are calculated first, then the firing rates */

    lipvis=nslDiff(lipvis,lipvistm,-lipvis+ SLIPvis_in); //RETINA;
    lipmem=nslDiff(lipmem,lipmemtm,-lipmem+(LIPmemK*ThLIPmem_in)+LIPvis_out);

    lipmem[(int)FOVEAX][(int)FOVEAY]=LIPvis_out[(int)FOVEAX][(int)FOVEAY];                        

    LIPvis_out=Nsl2Sigmoid.eval(lipvis,LIPvissigma1, LIPvissigma2,
                              LIPvissigma3, LIPvissigma4);
    LIPmem_out=Nsl2Sigmoid.eval(lipmem,LIPmemsigma1, LIPmemsigma2,
                              LIPmemsigma3, LIPmemsigma4);
  }
public void makeConn(){
}
}//end LIP

