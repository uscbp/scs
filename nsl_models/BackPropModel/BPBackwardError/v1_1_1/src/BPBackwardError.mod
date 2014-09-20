package BackPropModel.BPBackwardError.v1_1_1.src;

nslModule BPBackwardError(int outSize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: BackPropModel
//moduleName:  BPBackwardError
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat1 mf(outSize); // 
public NslDinFloat1 desired(outSize); // 
public NslDoutFloat1 eOutput(outSize); // 
private NslFloat0 stopError; // 
public NslDoutDouble0 tss(); // 

//methods 
public void initModule() 
{
	stopError.nslSetAccess('W');
	stopError=0.0001f;
}

public void initTrainEpochs() 
{
	eOutput=0;
}

public void simTrain() 
{
	eOutput = desired - mf;
	tss = tss + nslSum(eOutput ^ eOutput);
}

public void initTrain()
{
	tss=0;
}

public void endTrain() 
{
	tss=tss/system.getEndTime();
	if (tss < stopError) 
	{
		nslPrintln("Convergence");
		system.breakEpochs();
		return;
	}
}
public void makeConn(){
}
}//end BPBackwardError

