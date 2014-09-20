package CrowleyModel.MedBulk.v1_1_1.src;

nslModule MedBulk(int CorticalArraySize, int MaxConnections){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  MedBulk
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutInt2 SNRMapCount_bulk(CorticalArraySize,CorticalArraySize); // 
public NslDoutDouble3 SNRweights_bulk(CorticalArraySize,CorticalArraySize,MaxConnections); // 
public NslDoutInt3 SNRxmap_bulk(CorticalArraySize,CorticalArraySize,MaxConnections); // 
public NslDoutInt3 SNRymap_bulk(CorticalArraySize,CorticalArraySize,MaxConnections); // 
public NslDoutInt3 FEFxmap_bulk(CorticalArraySize,CorticalArraySize,MaxConnections); // 
public NslDoutInt3 FEFymap_bulk(CorticalArraySize,CorticalArraySize,MaxConnections); // 
public NslDoutInt3 LIPxmap_bulk(CorticalArraySize,CorticalArraySize,MaxConnections); // 
public NslDoutInt3 LIPymap_bulk(CorticalArraySize,CorticalArraySize,MaxConnections); // 
public NslDoutInt3 PFCxmap_bulk(CorticalArraySize,CorticalArraySize,MaxConnections); // 
public NslDoutInt3 PFCymap_bulk(CorticalArraySize,CorticalArraySize,MaxConnections); // 

//methods 
public void initModule()
  {
    SNRMapCount_bulk=0;
    SNRweights_bulk=0;
    SNRxmap_bulk=0;
    SNRymap_bulk=0;
//nslPrintln("Bulk FEFxmap_bulk:"+FEFxmap_bulk);
    FEFxmap_bulk=0;
    FEFymap_bulk=0;
//nslPrintln("Bulk FEFxmap_bulk:"+medbulk1.FEFxmap_bulk);
    LIPxmap_bulk=0;
    LIPymap_bulk=0;
    PFCxmap_bulk=0;
    PFCymap_bulk=0;
  }
  public void initRun()
  {
//System.out.println("MedBulk SNRxmap:"+SNRxmap_bulk);
  }
public void makeConn(){
}
}//end MedBulk

