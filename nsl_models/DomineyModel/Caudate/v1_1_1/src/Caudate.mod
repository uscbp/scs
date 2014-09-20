package DomineyModel.Caudate.v1_1_1.src;

nslModule Caudate(int stdsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  Caudate
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat2 fefmem(stdsz,stdsz); // 
public NslDinFloat2 fefsac(stdsz,stdsz); // 
public NslDoutFloat2 cdsac(stdsz,stdsz); // 
public NslDoutFloat2 cdmem(stdsz,stdsz); // 
private NslFloat0 cdmemPot_tm; // 
private NslFloat0 cdsacPot_tm; // 
private NslFloat0 cdmemPot_k1; // 
private NslFloat0 cdsacPot_k1; // 
private NslFloat0 cdmem_x1; // 
private NslFloat0 cdmem_x2; // 
private NslFloat0 cdmem_y1; // 
private NslFloat0 cdmem_y2; // 
private NslFloat0 cdsac_x1; // 
private NslFloat0 cdsac_x2; // 
private NslFloat0 cdsac_y1; // 
private NslFloat0 cdsac_y2; // 
private NslFloat2 cdmemPot(stdsz,stdsz); // 
private NslFloat2 cdsacPot(stdsz,stdsz); // 

//methods 
public void initRun() 
{
	cdmem=0;
	cdsac=0;
	cdmemPot_tm= .01 ;
	cdsacPot_tm=  .008;
	cdmemPot_k1= 1 ;
	cdsacPot_k1=  1;	
	cdmem_x1=50 ;
	cdmem_x2=  90;
	cdmem_y1= 0 ;
	cdmem_y2=60  ;
	cdsac_x1=  0;
	cdsac_x2= 50 ;
	cdsac_y1=  0;
	cdsac_y2= 60 ;	
}

public void simRun() 
{
	cdmemPot=nslDiff(cdmemPot,cdmemPot_tm, - cdmemPot + cdmemPot_k1*fefmem);
	cdsacPot=nslDiff(cdsacPot,cdsacPot_tm,- cdsacPot + cdsacPot_k1*fefsac);
	cdmem = nslSigmoid(cdmemPot,cdmem_x1,cdmem_x2,cdmem_y1,cdmem_y2);
	cdsac = nslSigmoid(cdsacPot,cdsac_x1,cdsac_x2,cdsac_y1,cdsac_y2);

	if (system.debug>22) 
	{
		nslPrintln("Caudate: cdmem");
		nslPrintln( cdmem);
		nslPrintln("Caudate: cdsac");
		nslPrintln(cdsac);

	}
}
public void makeConn(){
}
}//end Caudate

