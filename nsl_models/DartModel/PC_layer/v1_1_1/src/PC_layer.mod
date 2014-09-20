package DartModel.PC_layer.v1_1_1.src;

nslModule PC_layer(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  PC_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 gc_in(30,30); // GC input
public NslDinDouble1 io_in(2); // IO input
public NslDoutDouble2 pc_out(2,5); // 
private double f_max=100; // 
private double offset=750; // output +- 5 for 0 input
private double slope=.005; // 
private NslDouble1 w(3000); // 
private NslDouble2 pc_mp(2,5); // 
private NslDouble0 alpha; // 

//methods 
public void initModule() 
{
	alpha = (NslDouble0)nslGetValue("dartModel.alpha");
     
	// Initialize weights
	w = (20./((30.*30.)/3.)) + (nslRandom(w,-.5,.5)/2.0);
	pc_out = 5.;
	pc_mp = 0.;
}

public void simTrain() 
{
	simRun();
}
 
public void endTrain() 
{
	endRun();
}

public void simRun()
{
	processGCInputs(false);   
	// Update PC cells
	pc_out = f_max * nslSigmoid(pc_mp,slope,offset);
}

public void endRun() 
{
	// Learning
	processGCInputs(true);
	w = nslBound(w,0, 1, 0, 1);
}

private void processGCInputs(boolean endEpoch) 
{
	int px,py,gx,gy,y,wc;
	int beam_start;
	int i,j;
	// GC inputs
	pc_mp = 0.;
	wc = 0;
	for(px=0;px<2;px++)
	{
		for(py=0;py<5;py++)
		{
			beam_start = py*30/5;
			for(gx=0;gx<30;gx++)
			{
				for(y=0;y<10;y++)
				{
					gy = (beam_start + y)%30;
					if (!endEpoch) 
					{
						pc_mp[px][py] = pc_mp[px][py] + w[wc] * gc_in[gx][gy];
					}
					else 
					{
						w[wc] = w[wc] + alpha * (gc_in[gx][gy]*.01) * (io_in[px] - 2.);
					}
					wc++;
				}
			}
		}
	}
 }
public void makeConn(){
}
}//end PC_layer

