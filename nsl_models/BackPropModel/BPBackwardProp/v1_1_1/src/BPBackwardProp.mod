package BackPropModel.BPBackwardProp.v1_1_1.src;

nslModule BPBackwardProp(int hidSize, int outSize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: BackPropModel
//moduleName:  BPBackwardProp
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat1 bInput(outSize); // 
public NslDinFloat1 fInput(hidSize); // 
public NslDinFloat1 mf(outSize); // 
public NslDinFloat2 w(hidSize, outSize); // 
public NslDoutFloat1 dh(outSize); // 
public NslDoutFloat2 dw(hidSize, outSize); // 
private NslFloat1 delta(outSize); // 
private NslFloat0 lrate; // 
public NslDoutFloat1 bOutput(hidSize); // 

//methods 
public void initModule() 
{
	lrate.nslSetAccess('W');
	lrate=0.8f;
}

public void simTrain() 
{
	for(int i=0; i<hidSize; i++)
	{
		for(int j=0; j<outSize; j++)
		{
			delta[j]=mf[j]*(1.0f-mf[j])*bInput[j];
			dh[j]=lrate*delta[j];
			dw[i][j]=lrate*delta[j]*fInput[i];
		}
		bOutput[i] = nslProd(w[i], delta);
	}
}
public void makeConn(){
}
}//end BPBackwardProp

