/** 
 Lat class
 Represents the Lateral Circuit
 @see    Lat
 @version 0.1 96/11/19
 @author  Michael Crowley
 -var public PFCgo_in - input coming from PFC module (of type NslDouble2)<p>
 -var public SNCdop_in - input coming from SNC module (of type NslDouble2)<p>
 -var public PFCfovea_in - input coming from PFC module (of type NslDouble2)<p>
 -var public FEFsac_in - input coming from FEF module (of type NslDouble2)<p>
 -var public SNRlatburst_out - output going to SC module (of type NslDouble2)<p>
*/
package CrowleyModel.Lat.v1_1_1.src;
nslImport CrowleyModel.CDlatinh.v1_1_1.src.*;
nslImport CrowleyModel.CDlattan.v1_1_1.src.*;
nslImport CrowleyModel.CDlatburst.v1_1_1.src.*;
nslImport CrowleyModel.GPElatburst.v1_1_1.src.*;
nslImport CrowleyModel.STNlatburst.v1_1_1.src.*;
nslImport CrowleyModel.SNRlatburst.v1_1_1.src.*;

nslModule Lat(int CorticalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  Lat
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 FEFsac_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 SNCdop_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 PFCgo_in(CorticalArraySize,CorticalArraySize); // 
public NslDinDouble2 PFCfovea_in(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble2 SNRlatburst_out(CorticalArraySize,CorticalArraySize); // 
public CDlatinh cdlatinh(CorticalArraySize); // 
public CDlattan cdlattan(CorticalArraySize); // 
public CDlatburst cdlatburst(CorticalArraySize); // 
public GPElatburst gpelatburst(CorticalArraySize); // 
public STNlatburst stnlatburst(CorticalArraySize); // 
public SNRlatburst snrlatburst(CorticalArraySize); // 

//methods 

public void makeConn(){
    nslRelabel(FEFsac_in,cdlatinh.FEFsac_in);
    nslRelabel(FEFsac_in,cdlatburst.FEFsac_in);
    nslRelabel(PFCgo_in,cdlatinh.PFCgo_in);
    nslRelabel(PFCgo_in,cdlatburst.PFCgo_in);
    nslRelabel(SNCdop_in,cdlattan.SNCdop_in);
    nslRelabel(SNCdop_in,cdlatburst.SNCdop_in);
    nslRelabel(PFCfovea_in,cdlatburst.PFCfovea_in);
    nslConnect(cdlatinh.CDlatinh_out,cdlattan.CDlatinh_in);
    nslConnect(cdlattan.CDlattan_out,cdlatburst.CDlattan_in);
    nslConnect(cdlatburst.CDindlatburst_out,gpelatburst.CDindlatburst_in);
    nslConnect(cdlatburst.CDdirlatburst_out,snrlatburst.CDdirlatburst_in);
    nslConnect(gpelatburst.GPElatburst_out,stnlatburst.GPElatburst_in);
    nslConnect(stnlatburst.STNlatburst_out,snrlatburst.STNlatburst_in);
    nslRelabel(snrlatburst.SNRlatburst_out,SNRlatburst_out);
}
}//end Lat

