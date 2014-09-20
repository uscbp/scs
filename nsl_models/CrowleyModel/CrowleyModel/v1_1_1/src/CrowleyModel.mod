package CrowleyModel.CrowleyModel.v1_1_1.src;
nslImport CrowleyModel.VISINPUT.v1_1_1.src.*;
nslImport CrowleyModel.LC.v1_1_1.src.*;
nslImport CrowleyModel.LIP.v1_1_1.src.*;
nslImport CrowleyModel.Thal.v1_1_1.src.*;
nslImport CrowleyModel.Med.v1_1_1.src.*;
nslImport CrowleyModel.Lat.v1_1_1.src.*;
nslImport CrowleyModel.SNC.v1_1_1.src.*;
nslImport CrowleyModel.PFC.v1_1_1.src.*;
nslImport CrowleyModel.SC.v1_1_1.src.*;
nslImport CrowleyModel.FEF.v1_1_1.src.*;
nslImport CrowleyModel.BSG.v1_1_1.src.*;
nslImport CrowleyModel.DoubleSaccade.v1_1_1.src.*;
nslImport CrowleyModel.GapSaccade.v1_1_1.src.*;

nslModel CrowleyModel(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  CrowleyModel
//versionName: 1_1_1
//floatSubModules: true


//variables 
public VISINPUT visinput(CorticalArraySize); // 
public LC lc(CorticalArraySize); // 
public LIP lip(CorticalArraySize); // 
public Thal thal1(CorticalArraySize); // 
public Med med(CorticalArraySize,StriatalArraySize); // 
public Lat lat(CorticalArraySize); // 
public SNC snc(CorticalArraySize); // 
public PFC pfc(CorticalArraySize); // 
public SC sc(CorticalArraySize); // 
public FEF fef(CorticalArraySize); // 
public BSG bsg(CorticalArraySize); // 
private final int CorticalArraySize=9; // 
private final int StriatalArraySize=90; // 
private int half_CorticalArraySize=(int)(CorticalArraySize / 2); // 
private NslInt0 FOVEAX(half_CorticalArraySize); // 
private NslInt0 FOVEAY(half_CorticalArraySize); // 
public DoubleSaccade doubleSaccade(visinput, med); // 
public GapSaccade gapSaccade(visinput); // 

//methods 
public void initSys()
{
	system.setEndTime(0.55);
	system.nslSetBuffering(true);  //all output ports will be double buffered
}

public void initModule()
{
	FOVEAX=half_CorticalArraySize;
	FOVEAY=half_CorticalArraySize;
	system.nslSetRunDelta(0.001);
	nslSetRunDelta(0.001);
	system.nslSetBuffering(true);  //all output ports will be double buffered

	nslDeclareProtocol("gap", "Gap Saccade");
	nslDeclareProtocol("double", "Double Saccade");
          
	system.addProtocolToAll("gap");
	system.addProtocolToAll("double");
}

public void gapProtocol() 
{
	doubleSaccade.hide("Independent");
	gapSaccade.show("Independent");
}

public void doubleProtocol() 
{
	gapSaccade.hide("Independent");
	doubleSaccade.show("Independent");
}
public void makeConn(){
    nslConnect(lip.LIPvis_out,pfc.LIPvis_in);
    nslConnect(lip.LIPmem_out,thal1.LIPmem_in);
    nslConnect(lip.LIPmem_out,med.LIPmem_in);
    nslConnect(lip.LIPmem_out,pfc.LIPmem_in);
    nslConnect(lip.LIPmem_out,fef.LIPmem_in);
    nslConnect(lip.LIPmem_out,sc.LIPmem_in);
    nslConnect(lc.LimbicCortex_out,snc.LimbicCortex_in);
    nslConnect(snc.SNCdop_out,med.SNCdop_in);
    nslConnect(snc.SNCdop_out,lat.SNCdop_in);
    nslConnect(med.SNRmedburst_out,thal1.SNRmedburst_in);
    nslConnect(fef.FEFmem_out,thal1.FEFmem_in);
    nslConnect(fef.FEFsac_out,med.FEFsac_in);
    nslConnect(fef.FEFsac_out,lat.FEFsac_in);
    nslConnect(fef.FEFsac_out,sc.FEFsac_in);
    nslConnect(thal1.ThPFCmem_out,pfc.ThPFCmem_in);
    nslConnect(thal1.ThFEFmem_out,fef.ThFEFmem_in);
    nslConnect(thal1.ThLIPmem_out,lip.ThLIPmem_in);
    nslConnect(pfc.PFCfovea_out,lat.PFCfovea_in);
    nslConnect(pfc.PFCfovea_out,sc.PFCfovea_in);
    nslConnect(pfc.PFCgo_out,med.PFCgo_in);
    nslConnect(pfc.PFCgo_out,lat.PFCgo_in);
    nslConnect(pfc.PFCgo_out,fef.PFCgo_in);
    nslConnect(pfc.PFCmem_out,thal1.PFCmem_in);
    nslConnect(pfc.PFCmem_out,fef.PFCmem_in);
    nslConnect(lat.SNRlatburst_out,sc.SNRlatburst_in);
    nslConnect(sc.SCsac_out,bsg.SCsac_in);
    nslConnect(sc.SCbu_out,bsg.SCbu_in);
    nslConnect(bsg.BSGsaccade_out,sc.BSGsaccade_in);
    nslConnect(bsg.BSGEyeMovement_out,sc.BSGEyeMovement_in);
    nslConnect(visinput.visinput_out,lip.SLIPvis_in);
}
}//end CrowleyModel

