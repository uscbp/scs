package BackPropModel.BPForward.v1_1_1.src;

nslModule BPForward(int inSize, int hidSize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: BackPropModel
//moduleName:  BPForward
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat1 fInput(inSize); // 
public NslDinFloat1 dh(hidSize); // 
public NslDinFloat2 dw(inSize, hidSize); // 
public NslDoutFloat1 mf(hidSize); // 
public NslDoutFloat2 w(inSize, hidSize); // 
private NslFloat1 mp(hidSize); // 
private NslFloat1 h(hidSize); // 

//methods 
public void initModule() 
{
	for(int i=0; i<hidSize; i++)
	{
		h[i]=nslRandom(-0.5f,0.5f);
		for(int j=0; j<inSize; j++)
		{
			w[j][i]=nslRandom(-0.5f,0.5f);
		}
	}
	dw = 0.0;
	dh = 0.0;
}

public void forward()
{
	mp = fInput*w;
	mf = nslSigmoid(mp + h);
}

public void simTrain() 
{
	w = w + dw;
	h = h + dh;
	forward();
}

public void simRun()
{
	forward();
}
public void makeConn(){
}
}//end BPForward

