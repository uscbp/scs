package HopfieldModel.HopfieldNetwork.v1_1_1.src;

nslModule HopfieldNetwork(int size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: HopfieldModel
//moduleName:  HopfieldNetwork
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinInt2 input(size, size); // 
public NslDoutDouble0 energy(); // 
public NslDoutInt2 output(size, size); // 
private NslDouble2 mp(size,size); // 
private NslDouble4 weight(size,size,size,size); // 

//methods 
public void initModule() 
{
	weight=0.0;
}

public void simTrain() 
{
	for(int k=0;k<size; k++) 
        {
		for(int l=0; l<size; l++) 
		{
			for(int i=0; i<size; i++)
			{
				for(int j=0; j<size; j++)
				{
					if (i==k && j==l)
						weight[k][l][i][j]= 0.0;
					else
						weight[k][l][i][j]=weight[k][l][i][j]+(double)input[k][l]*(double)input[i][j]/(size*size);
				}
			}
		}
	}
}

public void initRun()
{
	output=input;
}

public void simRun() 
{
	int k = nslRandom(0,size-1);
	int l = nslRandom(0,size-1);
	mp[k][l]=nslSum(weight[k][l]^(NslDouble2)output);
	output[k][l]=nslStep(mp[k][l],0,-1,1);

	energy = 0.0;
	for (k=0; k<size; k++)
	{
		for (l=0; l<size; l++)
		{
			for (int i=0; i<size; i++)
			{
				for (int j=0; j<size; j++)
				{
					energy=energy+(double)(weight[k][l][i][j]*mp[k][l]*mp[i][j]);
				}
			}
		}
	}
	energy = -0.5*energy;
}
public void makeConn(){
}
}//end HopfieldNetwork

