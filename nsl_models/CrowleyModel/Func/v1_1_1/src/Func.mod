/** 
This class contains the SetCD function from lib.h file
*/
package CrowleyModel.Func.v1_1_1.src;

nslModule Func(int CorticalArraySize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  Func
//versionName: 1_1_1
//floatSubModules: true


//variables 
private final int MaxConnections=50; // 

//methods 
// This function is also called by other functions in the lib.h file:
// MapToFovea, MapToOffset, TestConnections, TestFoveaMapping
public int SetCD (NslDouble2 CD, NslInt3 xmap, NslInt3 ymap, NslDouble2 mat) 
{
	//  This function maps the cortical excitation onto the CD based on the
	//  nslConnectections established in the xmap and ymap arrays

	int i, j, k, loc;
	NslInt0 xmaploc=new NslInt0("xmaploc", this), ymaploc=new NslInt0("ymaploc",this);

	verbatim_NSLJ; 
		if (CD==null)
		{ 
			System.err.println("********** CD NULL!!!!");
			System.exit(0);
		}
		if (xmap==null) 
		{
			System.err.println("********** xmap NULL!!!!");
			System.exit(0);
		}
		if (ymap==null)
		{
			System.err.println("********** ymap NULL!!!!");
			System.exit(0);
		}
		if (mat==null)
		{
			System.err.println("********** mat NULL!!!!");
			System.exit(0);
		}
	verbatim_off; 
 
	for (i = 0; i < CorticalArraySize; i ++)
	{
		for (j = 0; j < CorticalArraySize; j ++) 
		{
			if (mat [i][j] != 0)
			{
				// Found an active cortical neuron
				for (k = 0; k < MaxConnections; k ++) 
				{
					// loc = ( MaxConnections * j ) + k + 
					// ( MaxConnections * CorticalArraySize * i );
					xmaploc = xmap[i][j][k]; ymaploc = ymap[i][j][k];
					// xmaploc = *(xmap+loc); ymaploc = *(ymap+loc);
					if ((xmaploc != 0) || (ymaploc != 0)) 
					{
						CD [xmaploc.get()][ymaploc.get()] = mat [i][j];
					}
				}
			}
		}
	}
	return 0;
}
public void makeConn(){}
}//end Func

