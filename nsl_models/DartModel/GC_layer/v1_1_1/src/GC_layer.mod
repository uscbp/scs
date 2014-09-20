package DartModel.GC_layer.v1_1_1.src;

nslModule GC_layer(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  GC_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 pp_in(10,10); // 
public NslDinDouble2 fcx_in(10,4); // 
public NslDoutDouble2 gc_out(30,30); // 
private double f_max=100; // 
private NslDouble0 w; // 
private NslDouble2 gc_mp(30,30); // 
private NslDouble0 gc_offset; // 
private NslDouble0 gc_slope; // 
private NslDouble0 gc_dist; // 
private NslInt0 gc_nd; // 
private int src[3600]; // 
private int Xo[3600]; // 
private int Yo[3600]; // 
private int Xd[3600]; // 
private int Yd[3600]; // 
private int NC; // 

//methods 
public void initModule() 
{
	gc_offset = (NslDouble0)nslGetValue("dartModel.gc_offset");
	gc_slope  = (NslDouble0)nslGetValue("dartModel.gc_slope");
	gc_dist   = (NslDouble0)nslGetValue("dartModel.gc_dist");
	gc_nd     = (NslInt0)nslGetValue("dartModel.gc_nd");

	w = 1./(double)gc_nd;

	int gx,gy,i,x,y;
     
	gc_out = 50.;
	gc_mp = 0.;
	// Create mapping function
	NC = 0;
	for(gx=0;gx<30;gx++)
	{
		for(gy=0;gy<30;gy++)
		{
			for(i=0;i<gc_nd;i++)
			{
				Xd[NC] = gx;
				Yd[NC] = gy;
				// PP input
				if(nslRandom() < gc_dist)
				{ 
					src[NC] = 0;
					Xo[NC] = (int)nslRandom(3,8);
					Yo[NC] = (int)nslRandom(0,10);
		 		} 
				// FCX input
				else 
				{ 
					src[NC] = 1;
					Xo[NC] = (int)nslRandom(0,10);
					Yo[NC] = (int)nslRandom(1,3);
				}
				NC++;
			}
		}
	}
}

public void simTrain() 
{
	simRun();
}

public void simRun()
{
	int i,j;
	int mx,my,ix,iy;

	// Map inputs onto 30x30 array using mapping function
          
	gc_mp = 0.;
	for(i=0;i<NC;i++)
	{
		mx = Xd[i];
		my = Yd[i];
		ix = Xo[i];
		iy = Yo[i];
		if(src[i]==0)
			gc_mp[mx][my] = gc_mp[mx][my] + pp_in[ix][iy];
		else
			gc_mp[mx][my] = gc_mp[mx][my] + fcx_in[ix][iy];
	}
     
	gc_mp = w * gc_mp;

	gc_out = f_max * nslSigmoid(gc_mp,gc_slope, gc_offset);
}
public void makeConn(){
}
}//end GC_layer

