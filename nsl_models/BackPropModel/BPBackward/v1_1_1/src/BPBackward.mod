package BackPropModel.BPBackward.v1_1_1.src;

nslModule BPBackward(int inSize, int hidSize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: BackPropModel
//moduleName:  BPBackward
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat1 bInput(hidSize); // 
public NslDinFloat1 fInput(inSize); // 
public NslDinFloat1 mf(hidSize); // 
public NslDoutFloat1 dh(hidSize); // 
public NslDoutFloat2 dw(inSize, hidSize); // 
private NslFloat1 delta(hidSize); // 
private NslFloat0 lrate; // 

//methods 
public void initModule() 
{
	lrate.nslSetAccess('W');
	lrate=0.8f;
}

public void simTrain() 
{
	backwardPass();
}

public void backwardPass()
{
	for(int i=0; i<hidSize; i++)
	{
		for(int j=0; j<inSize; j++)
		{
			delta[i]=mf[i] * (1.0f-mf[i]) * bInput[i];
			dh[i]=lrate*delta[i];
			dw[j][i]=dh[i]*fInput[j];
		}
	}
}
public void makeConn(){
}
}//end BPBackward

