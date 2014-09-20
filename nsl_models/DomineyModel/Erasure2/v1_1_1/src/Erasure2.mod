package DomineyModel.Erasure2.v1_1_1.src;

nslModule Erasure2(int stdsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  Erasure2
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutFloat2 erasure2(stdsz, stdsz); // 
private int halfStdsz=0; // 
private int oneThirdStdsz=0; // 
private int twoThirdsStdszMin1=0; // 

//methods 
public void initModule() 
{	
	halfStdsz=(int)stdsz/2;  // if stdsz=9, then this is 4
	oneThirdStdsz=(int)stdsz/3;  //if stdsz=9, then this is 3
	twoThirdsStdszMin1=oneThirdStdsz+ oneThirdStdsz-1; //if, then 5
}

public void initRun() 
{
	// the following draws a square outlining the fovea
	// with 1 everywhere except the outline which is 0.5
	erasure2=1;       // set everything to 1 then set some to 0.5                    
	erasure2[oneThirdStdsz][oneThirdStdsz] = .5;
	erasure2[oneThirdStdsz][halfStdsz] = 0.5; 
	erasure2[oneThirdStdsz][twoThirdsStdszMin1] = 0.5;

	erasure2[halfStdsz][oneThirdStdsz] = 0.5;
	erasure2[halfStdsz][halfStdsz] = 0.5; 
	erasure2[halfStdsz][twoThirdsStdszMin1] = 0.5;

	erasure2[twoThirdsStdszMin1][oneThirdStdsz] = 0.5;
	erasure2[twoThirdsStdszMin1][halfStdsz] = 0.5; 
	erasure2[twoThirdsStdszMin1][twoThirdsStdszMin1] = 0.5;

	if (system.debug>1) 
	{
		nslPrintln("Erasure2:initRun: erasure2");
		nslPrintln(erasure2);
	}
}
public void makeConn(){
}
}//end Erasure2

