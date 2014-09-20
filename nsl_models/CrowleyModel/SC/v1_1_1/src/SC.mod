/** 
 Here is the class representing the Superior Colliculus (SC) module. The
 module contains three layers SCsac, SCqv, SCbu. This is converted
 as a parent module and 4 children module (the extra module is for
 non-neural stuff).

 The Execute Saccade schema is implemented in the SC module and
 the "brainstem saccade generator".
 Once PFC has issued a GO signal, the combination of increased activity from
 PFC and decreased inhibitory activity from BG allow activation to grow in
 the SC. This activation is projected to the brainstem where motor neurons
 are excited and cause the eye muscles to move the eyes to the new target
 location.
*/
package CrowleyModel.SC.v1_1_1.src;
nslImport CrowleyModel.SCsac.v1_1_1.src.*;
nslImport CrowleyModel.SCqv.v1_1_1.src.*;
nslImport CrowleyModel.SCbu.v1_1_1.src.*;

nslJavaModule SC(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  SC
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 SNRlatburst_in(array_size,array_size); // 
public NslDinDouble2 FEFsac_in(array_size,array_size); // 
public NslDinDouble2 LIPmem_in(array_size,array_size); // 
public NslDinDouble2 PFCfovea_in(array_size,array_size); // 
public NslDinDouble0 BSGsaccade_in(); // 
public NslDinDouble1 BSGEyeMovement_in(array_size); // 
public NslDoutDouble2 SCsac_out(array_size,array_size); // 
public NslDoutDouble2 SCbu_out(array_size,array_size); // 
public SCsac SCsac1(array_size); // 
public SCqv SCqv1(array_size); // 
public SCbu SCbu1(array_size); // 
private NslInt0 FOVEAX; // 
private NslInt0 FOVEAY; // 

//methods 
public void initModule() 
{
	FOVEAX = (NslInt0)nslGetValue("crowleyModel.FOVEAX");
	FOVEAY = (NslInt0)nslGetValue("crowleyModel.FOVEAY");  
	nslAddAreaCanvas("output", "sc_sac", SCsac_out, 0, 100);
	nslAddSpatialCanvas("output", "sc_bu", SCbu_out, 0, 100);
}
public void makeConn(){
    nslRelabel(SNRlatburst_in,SCsac1.SNRlatburst_in);
    nslRelabel(FEFsac_in,SCsac1.FEFsac_in);
    nslRelabel(LIPmem_in,SCqv1.LIPmem_in);
    nslRelabel(PFCfovea_in,SCbu1.PFCfovea_in);
    nslRelabel(BSGsaccade_in,SCbu1.BSGsaccade_in);
    nslRelabel(BSGEyeMovement_in,SCbu1.BSGEyeMovement_in);
    nslConnect(SCqv1.SCqv_out,SCsac1.SCqv_in);
    nslConnect(SCsac1.SCsac_out,SCbu1.SCsac_in);
    nslRelabel(SCsac1.SCsac_out,SCsac_out);
    nslConnect(SCbu1.SCbu_out,SCsac1.SCbu_in);
    nslRelabel(SCbu1.SCbu_out,SCbu_out);
}
}//end SC

