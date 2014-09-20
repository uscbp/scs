/** 
 Med class
 Represents the Medial Circuit
 @see     Med
 @version 0.1 96/11/19
 @author 
 -var public FEFsac_in - input coming from FEF module (of type NslDouble2)<p>
 -var public LIPmem_in - input coming from LIP module (of type NslDouble2)<p>
 -var public SNCdop_in - input coming from SNC module (of type NslDouble2)<p>
 -var public PFCgo_in - input coming from PFC module (of type NslDouble2)<p>
 -var public SNRmedburst_out - output going to Thal module (of type NslDouble2)<p>
*/
package CrowleyModel.Med.v1_1_1.src;
import CrowleyModel.CDmedinh.v1_1_1.src.*;
import CrowleyModel.CDmedtan.v1_1_1.src.*;
import CrowleyModel.CDmedburst.v1_1_1.src.*;
import CrowleyModel.GPEmedburst.v1_1_1.src.*;
import CrowleyModel.STNmedburst.v1_1_1.src.*;
import CrowleyModel.SNRmedburst.v1_1_1.src.*;
import CrowleyModel.MedBulk.v1_1_1.src.*;
import CrowleyModel.Func.v1_1_1.src.*;
import CrowleyModel._Element.v1_1_1.src.*;

/*********************************/
/*                               */
/*   Importing all Nsl classes   */
/*                               */
/*********************************/

import nslj.src.system.*;
import nslj.src.cmd.*;
import nslj.src.lang.*;
import nslj.src.math.*;
import nslj.src.display.*;
import nslj.src.display.j3d.*;

/*********************************/

public class Med extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  Med
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 FEFsac_in; // 
public  NslDinDouble2 SNCdop_in; // 
public  NslDinDouble2 PFCgo_in; // 
public  NslDinDouble2 LIPmem_in; // 
public  NslDoutDouble2 CDdirmedburst_out; // 
public  NslDoutDouble2 SNRmedburst_out; // 
public  NslDoutDouble2 CDmedinh_out; // 
public  NslDoutDouble2 CDmedtan_out; // 
public  NslDoutDouble2 CDindmedburst_out; // 
public  NslDoutDouble2 GPEmedburst_out; // 
public  NslDoutDouble2 STNmedburst_out; // 
public  CDmedinh cdmedinh; // 
public  CDmedtan cdmedtan; // 
public  CDmedburst cdmedburst; // 
public  GPEmedburst gpemedburst; // 
public  STNmedburst stnmedburst; // 
public  SNRmedburst snrmedburst; // 
public  MedBulk medbulk1; // 
private boolean CONSOLE_IO=false; // 
private int NumberIterations=10; // 
private int FEFPatchCount; // 
private int LIPPatchCount; // 
private int PFCPatchCount; // 
private int FEFPatchSize; // 
private int LIPPatchSize; // 
private int PFCPatchSize; // 
private double FEFfill; // 
private double LIPfill; // 
private double PFCfill; // 
private double ConnectionChance; // 
private  NslDouble0 LearnRate; // 
private int MaxConnections=50; // 
private int MaxCenters=16; // 
private  NslDouble2 FEFsac_mirror; // 
private  NslDouble2 PFCgo_mirror; // 
private  NslDouble2 LIPmem_mirror; // 
private NslInt0 FOVEAX; // 
private NslInt0 FOVEAY; // 
public  Func func; // 
public  _Element LearnedElements; // 
public  _Element UnlearnedElements; // 
public  _Element Teacher; // 

//methods 
public void initModule() 
{
	FOVEAX.set((NslInt0)nslGetValue("crowleyModel.FOVEAX"));
	FOVEAY.set((NslInt0)nslGetValue("crowleyModel.FOVEAY"));
	
	Teacher=new _Element("Teacher",this);
	Teacher.initElement();
	LearnedElements= new _Element("LearnedElements",this) ;
	LearnedElements.initElement();
	UnlearnedElements= new _Element("UnlearnedElements",this) ;
	UnlearnedElements.initElement();

	
}

public void initRun () 
{
	MakeMapping(medbulk1);
	TestConnections();
	TestFoveaMapping();
	learnNewElements();
}

public void MakeMapping(MedBulk medbulk1) 
{
	int i, j, temp;
    
	int[][] FEFCenters =  new  int[MaxCenters][2];
	int[][] LIPCenters =  new  int[MaxCenters][2];
	int[][] PFCCenters =  new  int[MaxCenters][2];
	int FEFoffset, LIPoffset, PFCoffset;
 
	int iMinCell, jMinCell, MapSize;
 
	int overcrowded = 0;
	int FEFcount    = 0;
	int LIPcount    = 0;
	int PFCcount    = 0;

	MappingParameters();

	FEFoffset = FEFPatchSize/2;
	LIPoffset = LIPPatchSize/2;
	PFCoffset = PFCPatchSize/2;

	MapSize = StriatalArraySize/3;
	for(i=0; i<CorticalArraySize; i++) 
	{
		iMinCell = (i/3)*MapSize ;
		for(j=0; j<CorticalArraySize; j++) 
		{
			jMinCell = (j/3)*MapSize ;
			CalcCentroids(FEFPatchCount, FEFCenters, iMinCell, jMinCell, MapSize);
			CalcCentroids(LIPPatchCount, LIPCenters, iMinCell, jMinCell, MapSize);
			CalcCentroids(PFCPatchCount, PFCCenters, iMinCell, jMinCell, MapSize);
			/* Establish the nslConnectectivity between each cortical
			   region and the caudate using the region centers
			   and the calculated offsets.
			*/
			PFCcount += MakeConnections(PFCCenters, PFCoffset, medbulk1.PFCxmap_bulk.get(), medbulk1.PFCymap_bulk.get(),
							PFCfill, i, j);
			FEFcount += MakeConnections(FEFCenters, FEFoffset, medbulk1.FEFxmap_bulk.get(), medbulk1.FEFymap_bulk.get(),
			  FEFfill, i, j);
			LIPcount += MakeConnections(LIPCenters, LIPoffset, medbulk1.LIPxmap_bulk.get(), medbulk1.LIPymap_bulk.get(), 
			  LIPfill, i, j);
		}
	}

	/* Establish the direct path mapping from CD to SNr */
	SNRMapping(medbulk1,medbulk1.FEFxmap_bulk.get(), medbulk1.FEFymap_bulk.get(), FEFPatchCount, MapSize);
	SNRMapping(medbulk1,medbulk1.LIPxmap_bulk.get(), medbulk1.LIPymap_bulk.get(), LIPPatchCount, MapSize);
	SNRMapping(medbulk1,medbulk1.PFCxmap_bulk.get(), medbulk1.PFCymap_bulk.get(), PFCPatchCount, MapSize);

	// Output each cortical nslConnectection array for comparison purposes
	OutputMapping(medbulk1.PFCxmap_bulk.get(), medbulk1.PFCymap_bulk.get());
	OutputMapping(medbulk1.FEFxmap_bulk.get(), medbulk1.FEFymap_bulk.get());
	OutputMapping(medbulk1.LIPxmap_bulk.get(), medbulk1.LIPymap_bulk.get());
}

// See FUNC(MappingParameters)
public void MappingParameters() 
{
	if (!CONSOLE_IO) 
	{
		MappingParameters(9, 3, 0.5, 0.5, 0.05);
		return;
	}
	LIPPatchCount = FEFPatchCount = PFCPatchCount = nslj.src.system.Console.readInt("Enter the number of patches (max 16) > ");

	LIPPatchSize = FEFPatchSize = PFCPatchSize = nslj.src.system.Console.readInt("Enter patch size > ");

	LIPfill = FEFfill = PFCfill = nslj.src.system.Console.readDouble("Enter fill ratio > ");

	ConnectionChance = nslj.src.system.Console.readDouble("Enter nslConnectection percentage (0-1) > ");
	LearnRate.set(nslj.src.system.Console.readDouble("Enter learning rate (~0.05) >"));
	//99/8/8 aa: added line above : a value of 0.05 does not agree
	// with what is in Element.mod. Element.mod has .005.
}  

public void MappingParameters(int count, int size, double fill,double nslConnectection, double learn) 
{
	LIPPatchCount = FEFPatchCount = PFCPatchCount = count;
	LIPPatchSize = FEFPatchSize = PFCPatchSize = size;
	LIPfill = FEFfill = PFCfill = fill;
	ConnectionChance = nslConnectection;
	LearnRate.set(learn);
}

public void CalcCentroids(int count, int[][] arr, int imin, int jmin, int size) 
{
	int i, j, area, side;

	side = (int)java.lang.Math.sqrt(count);
	area = size/side ;

	for (i=0; i<side; i++ ) 
	{
		for (j=0; j<side; j++) 
		{
			arr[i*side+j][0] = (int)(NslRandom.eval()*area+(area*i)+imin );
			arr[i*side+j][1] = (int)(NslRandom.eval()*area+(area*j)+jmin);
		}
	}
}

public int MakeConnections(int[][] centers, int offset, int[][][] xmap, int[][][] ymap, double fill, int iindex, int jindex) 
{
	int count, xstart, ystart, xend, yend;
	int i, j;
	int[] _savei;
	int[] _savej;
	int ptr;
	double temp;
	count = 0;
	ptr = 0;

	int[][] CDtemp =  new  int[StriatalArraySize][StriatalArraySize];

	// very dirty trick, don't try it at home
	_savei= new  int[1];
	_savej= new  int[1];

	while (centers[count][0]!=0||centers[count][1]!=0) 
	{
		// Loop while nonzero values exist in centers
		xstart = (centers[count][0]-offset>0)? centers[count][0]-offset: 0;
		ystart = (centers[count][1]-offset>0)? centers[count][1]-offset: 0;
		xend = (centers[count][0]+offset+1<StriatalArraySize-1)? centers[count][0]+offset+1:StriatalArraySize-1;
		yend = (centers[count][1]+offset+1<StriatalArraySize-1)? centers[count][1]+offset+1:StriatalArraySize-1;

		for(i=xstart; i<xend; i++)
		{
			for(j=ystart; j<yend; j++) 
			{
				// Determine nslConnectections
				temp = NslRandom.eval();
				if ((temp<fill)&&(ptr<MaxConnections)) 
				{
					if(CDtemp[i][j]==0 ) 
					{
						CDtemp[i][j]++;
						xmap[iindex][jindex][ptr] = i;
						ymap[iindex][jindex][ptr] = j;
						ptr++;
					}
					else 
					{
						_savei[0]=0;
						_savej[0] = 0;
						if(CheckNeighbors(CDtemp, i, j,StriatalArraySize, _savei, _savej)) 
						{
							CDtemp[_savei[0]][_savej[0]]++;
							xmap[iindex][jindex][ptr] = _savei[0];
							ymap[iindex][jindex][ptr] = _savej[0];
							ptr++;
						}
					}
				}
			}
		}
		count++;
	}
	return ptr;
}
  
public boolean CheckNeighbors( int[][] arr, int x, int y, int max, int[] outx, int[] outy) 
{
	int index, xindex, yindex;
	int[] xarr= {-1, 0, 1, 1, 1, 0,-1,-1};
	int[] yarr= {-1,-1,-1, 0, 1, 1, 1, 0};
	int count;
	count = 0;
	index = (int)(NslRandom.eval()*8);

	while ((count<8)) 
	{
		xindex = minmax( x+xarr[index], 0, max-1);
		yindex = minmax(y+yarr[index],0,max-1);
		if (arr[xindex][yindex]==0) 
		{
			outx[0] = xindex;
			outy[0] = yindex;
			return true;
		}
		count++;
		index = (index+1)%8;
	} 
	return false;
}

public int SNRMapping(MedBulk medbulk1, int[][][] xmap, int[][][] ymap, int numzones, int size) 
{
	/*
	This function maps the CD nslConnectections to the SNr
	size contains the length of each side of a CD zone.
	*/
	int i, j, k, l, kindex;
	int ptr;

	int numside = (int)java.lang.Math.sqrt(numzones);
	int zonesize =(int)( size/numside  ) ;
	int imin, jmin, xDiff, yDiff;
	int izone, jzone;
	int SNRsize = 3;
	int SNRimin, SNRjmin, SNRimax, SNRjmax;
    
	int[][] temparr =  new  int[3][3];
	int numloc = 0;

	// added for repeated initialization \\
	for(i=0; i<CorticalArraySize; i++ )  
	{
		for(j=0; j<CorticalArraySize; j++ )  
		{ 
			medbulk1.SNRMapCount_bulk.set(i,j, 0);
		}	
	}	

	for(i=0; i<CorticalArraySize; i++ )  
	{
		for(j=0; j<CorticalArraySize; j++ )  
		{ 
			// First task is to read each xmap, ymap value and determine the
			// CD zone to which they belong.  Then determine which subzone
			// within that zone.  Last job is to then populate the SNr neurons
			// in the corresponding SNr zone with this CD nslConnectection.
			ptr = 0;
			while ((ptr<MaxConnections)&&((xmap[i][j][ptr]!=0)||(ymap[i][j][ptr]!=0))) 
			{
				numloc++;
				// Have a single xmap,ymap value.  Determine subzone in CD.
				imin    = (i/3)*size ;
				jmin = (j/3)*size ;
				xDiff = xmap[i][j][ptr]-imin ;
				yDiff = ymap[i][j][ptr]-jmin ;
				izone   = minmax( xDiff/zonesize , 0, numside-1 );
				jzone   = minmax( yDiff/zonesize , 0, numside-1 );
    
				// Determine zone in SNr and create mapping back to CD

				SNRimin = izone*SNRsize ; 
				SNRjmin = jzone*SNRsize ;
				SNRimax = SNRimin+SNRsize ;
				SNRjmax = SNRjmin+SNRsize ;

				temparr[izone][jzone]++;
				for ( k=SNRimin; k<SNRimax; k++ )
				{
					for ( l=SNRjmin; l<SNRjmax; l++ )
					{
						// Check probability of CD neuron nslConnectecting to SNR
						// neuron.  The chance of a successful nslConnectection is
						// established by the user in the NSL DATA object
						// ConnectionChance.  By using nslConnectection probabilities,
						// we ensure a Different set of CD neurons project to
						// each of the SNR neurons in a specific zone.  Given
						// sufficient Difference between the sets of connectections
						// it should be possible to do remapping properly to
						// a single neuron in a SNR zone.
						if ((medbulk1.SNRMapCount_bulk.get(k,l)<MaxConnections)&&(NslRandom.eval()<ConnectionChance)&&ptr<MaxConnections) 
						{
							// This CD neuron gets nslConnectected to this SNR neuron.
							// Set next available mapping array element for all neurons
							// in this region of SNr to values in xmap and ymap.
							kindex = medbulk1.SNRMapCount_bulk.get(k,l);
							medbulk1.SNRMapCount_bulk.set(k,l,medbulk1.SNRMapCount_bulk.get(k,l)+1);
							medbulk1.SNRxmap_bulk.set(k,l,kindex,xmap[i][j][ptr]);
							medbulk1.SNRymap_bulk.set(k,l,kindex,ymap[i][j][ptr]);
							medbulk1.SNRweights_bulk.set(k,l,kindex,0);
						}
						ptr ++;
					}
				}
			}
		}
	}
	return 0;
}


public int OutputMapping(int[][][] xmap, int[][][] ymap) 
{
	//  This function accepts an x,y mapping from a cortical area to striatum and
	//  outputs the nslConnectections onto striatum as an array of 0s and 1s.
	int[][] arr =  new  int[StriatalArraySize][StriatalArraySize];
	int i, j, k;

	for ( i=0; i<CorticalArraySize; i++ )
	{
		for ( j=0; j<CorticalArraySize; j++ )
		{
			for ( k=0; k<MaxConnections; k++ )
			{
				if ( (xmap[i][j][k]>0)||(ymap[i][j][k]>0)  )
				{
					arr[xmap[i][j][k]][ymap[i][j][k]] = 1;
				}
			}
		}
	}
    
	return 0;
}

public int minmax( int int1, int sint, int lint )
{
	//  Ensures int1 is between sint and lint
	if ( int1<sint  ) 
		return sint;
 
	if ( int1>lint  ) 
		return lint;
    
	return int1;
}

public int MapToFovea( int iloc, int jloc ) 
{
	//  This function establishes the weights between CD and SNr for "learned"
	//  nslConnectections
	FEFsac_mirror.set(  FEFsac_in);
	LIPmem_mirror.set(  LIPmem_in);
	PFCgo_mirror.set(  PFCgo_in);

	FEFsac_mirror.set(iloc, jloc,   60);
	LIPmem_mirror.set(iloc, jloc,   60);
	PFCgo_mirror.set(iloc, jloc,    60);

	//  Set CD excitation through xmap, ymap arrays for each cortical
	//  area

	func.SetCD( CDdirmedburst_out, medbulk1.FEFxmap_bulk, medbulk1.FEFymap_bulk, FEFsac_mirror );
	func.SetCD( CDdirmedburst_out, medbulk1.LIPxmap_bulk, medbulk1.LIPymap_bulk, LIPmem_mirror );
	func.SetCD( CDdirmedburst_out, medbulk1.PFCxmap_bulk, medbulk1.PFCymap_bulk, PFCgo_mirror );

	//  Learning:  match SNr location teaching signal
	//  with active neurons in CD and modify appropriate weight
	LearnWeights(medbulk1.SNRMapCount_bulk, medbulk1.SNRweights_bulk, medbulk1.SNRxmap_bulk, medbulk1.SNRymap_bulk, FOVEAX.get(), FOVEAY.get() );

	//  Reset FEF, PFC, LIP, and CD to 0
    	/* [Warning] Change input variable */
	FEFsac_mirror.set(  0);
	LIPmem_mirror.set(  0);
	PFCgo_mirror.set(  0);

	CDdirmedburst_out.set(  0);

	return 0;
}

public int MapToOffset( int iloc1, int jloc1, int iloc2, int jloc2 ) 
{
	//  This function establishes the weights between CD and SNr for "learned"
	//  nslConnectections

	int temp, ioff, joff;

	ioff = iloc2-iloc1 ;
	joff = jloc2-jloc1 ;
    
	//  Don't do mapping if the offset distance between the two targets is
	//  more than half the width of the array
	// some problem in placing a negative sign on a nsl numeric object
	// need refinement of nslj.src.math.Sub class
	if ( (ioff>FOVEAX.get())||(ioff<-FOVEAX.get())  )
		return 0;
	if ( (joff>FOVEAY.get())||(joff<-FOVEAY.get())  )
		return 0;

	FEFsac_mirror.set(  FEFsac_in);
	LIPmem_mirror.set(  LIPmem_in);
	PFCgo_mirror.set(  PFCgo_in);

	LIPmem_mirror.set(iloc2, jloc2,   60);
	PFCgo_mirror.set(iloc1, jloc1,    60);

	//  Set CD excitation through xmap, ymap arrays for each cortical
	//  area.  FEF not used for targets not selected for current saccade.
	//  Only use LIP and PFC go signal.
	temp = func.SetCD( CDdirmedburst_out, medbulk1.LIPxmap_bulk, medbulk1.LIPymap_bulk, LIPmem_mirror );
	temp = func.SetCD( CDdirmedburst_out, medbulk1.PFCxmap_bulk, medbulk1.PFCymap_bulk, PFCgo_mirror  );

	//  Learning:  match SNr location teaching signal
	//  with active neurons in CD and increment appropriate weights
	temp = LearnWeights(medbulk1.SNRMapCount_bulk,medbulk1.SNRweights_bulk,medbulk1.SNRxmap_bulk, medbulk1.SNRymap_bulk, ioff+FOVEAX.get(), joff+FOVEAY.get() );

	//  Reset PFC, LIP, and CD to 0
	LIPmem_mirror.set(  0);
	PFCgo_mirror.set(  0);
	CDdirmedburst_out.set(  0);

	return 0;
}

public int LearnWeights(NslInt2 SNRMapCount, NslDouble3 SNRweights, NslInt3 SNRxmap, NslInt3 SNRymap, int imap, int jmap ) 
{
	//  This function sets the nslConnectecting weights between active CD neurons and
	//  active SNR neurons.

	int k, xmaploc, ymaploc;

	for ( k=0; k<SNRMapCount.get(imap,jmap); k++ ) 
	{
		// Check all CD neurons projecting to this SNR neuron to see
		// if they are active.  If so, increase the weight between them.
		xmaploc = SNRxmap.get(imap,jmap,k);
		ymaploc = SNRymap.get(imap,jmap,k);
        
		if ( CDdirmedburst_out.get(xmaploc,ymaploc)!=0  ) 
		{
			// Found a linkage.  Increase weight for activity, but decrease
			// if remap is wrong.  Errors caused by shared nslConnectections in CD.
			SNRweights.set(imap,jmap,k,(SNRweights.get(imap,jmap,k)+LearnRate.get()));
			SNRweights.set(imap,jmap,k,(SNRweights.get(imap,jmap,k)-Teacher.Check(imap,jmap) ));
		}
	}

	return 0;
}


public void LearnConnections( _Element elem ) 
{
	int     temp;
	int     ii;
	_Element curelem;

	curelem = elem;

	while ( curelem!=null  ) 
	{
		for ( ii=0; ii<NumberIterations; ii++ )
		{
			// Set cortical excitation
			SNRmedburst_out.set(     0);
			CDdirmedburst_out.set(   0);
			FEFsac_mirror.set(          0);
			LIPmem_mirror.set(          0);
			PFCgo_mirror.set(           0);

			// Determine correct remappings for non-neural Teacher
			/* [warning] what is Teacher? */
			curelem.Remap( (int)CorticalArraySize, Teacher );

			// Increment weights between active CD neurons and SNR fovea
			temp = MapToFovea( curelem.X(), curelem.Y() );

			// Time to map the nonsaccade target as well
			FEFsac_mirror.set(          0);
			LIPmem_mirror.set(          0);
			PFCgo_mirror.set(           0);
			CDdirmedburst_out.set(  0);

			// increment weights between active CD neurons and remapped location
			temp = MapToOffset( curelem.X(),  curelem.Y(), curelem.XO(), curelem.YO() );
		}

		curelem = curelem.Next();
	}

	return;
}

public void TestConnections( ) 
{
	_Element curelem;
	int ia, ja, temp;

	curelem = LearnedElements;

	while ( curelem!=null&&curelem.X()!=-1&&curelem.Y()!=-1)
	{
		LIPmem_mirror.set(  0); 
		FEFsac_mirror.set(  0); 
		PFCgo_mirror.set(   0); 
		CDdirmedburst_out.set(  0); 
		SNRmedburst_out.set(  0);

		FEFsac_mirror.set(curelem.X(), curelem.Y(),   60);
		LIPmem_mirror.set(curelem.X(), curelem.Y(),   60);
		PFCgo_mirror.set(curelem.X(), curelem.Y(),   60);
		LIPmem_mirror.set(curelem.XO(), curelem.YO(),   60);

		// Propagate cortical activity to CD
		temp = func.SetCD( CDdirmedburst_out, medbulk1.FEFxmap_bulk, medbulk1.FEFymap_bulk, FEFsac_mirror );
		temp = func.SetCD(  CDdirmedburst_out, medbulk1.LIPxmap_bulk, medbulk1.LIPymap_bulk, LIPmem_mirror );
		temp = func.SetCD(  CDdirmedburst_out, medbulk1.PFCxmap_bulk, medbulk1.PFCymap_bulk, PFCgo_mirror );

		// Sum CD activity onto SNr through SNRxmap and SNRymap
		temp = SumCDtoSNR( CDdirmedburst_out, SNRmedburst_out );

		curelem = curelem.Next();
	}

	return;
}

public void DisplayCorticalNeuron( int[][] xarr, int[][] yarr, int max, int[][][] xmap, int[][][] ymap ) 
{
	//  Check the input cortical region to find if it projects to the
	//  neuron indicated by the xloc,yloc location

	int i, j, k,  xmaploc, ymaploc;

	for ( i=0; i<CorticalArraySize; i++ )
	{
		for (j=0; j<CorticalArraySize; j++ )
		{
			for ( k=0; k<MaxConnections; k++ ) 
			{
				xmaploc = xmap[i][j][k]; 
				ymaploc = ymap[i][j][k];
			}
		}
	}
	return;
}

public void TestFoveaMapping( ) 
{
	//  Tests the mapping of a single target onto the fovea to see if extra
	//  mappings have also been established in the learning process

	_Element curelem;
	int ia, ja, temp;
	int jj=0;

	curelem = LearnedElements;

	while ( curelem!=null&&curelem.X()!=-1&&curelem.Y()!=-1)
	{
		LIPmem_mirror.set(  0); 
		FEFsac_mirror.set(  0); 
		PFCgo_mirror.set(  0); 
		CDdirmedburst_out.set(  0); 
		SNRmedburst_out.set(  0);

		FEFsac_mirror.set(curelem.X(), curelem.Y(),   60);
		LIPmem_mirror.set(curelem.X(), curelem.Y(),   60);
		PFCgo_mirror.set(curelem.X(), curelem.Y(),   60);

		// Propagate cortical activity to CD
		func.SetCD( CDdirmedburst_out, medbulk1.FEFxmap_bulk, medbulk1.FEFymap_bulk, FEFsac_mirror );
		// see lib.h for this if ( true ) break;
		func.SetCD( CDdirmedburst_out, medbulk1.LIPxmap_bulk, medbulk1.LIPymap_bulk, LIPmem_mirror );
		func.SetCD( CDdirmedburst_out, medbulk1.PFCxmap_bulk, medbulk1.PFCymap_bulk, PFCgo_mirror );


		// Sum CD activity onto SNr through SNRxmap and SNRymap
		temp = SumCDtoSNR( CDdirmedburst_out, SNRmedburst_out );

		// Output SNR values for testing purposes
		curelem = curelem.Next();
	}
	return;
}


public int SumCDtoSNR (NslDouble2 CD, NslDouble2 SNR)
{
	return snrmedburst.SumCDtoSNR(CD, SNR);
}

/**** Menu items ****/
public void startNewElementList() 
{
	LearnedElements.Remove();
	UnlearnedElements.Remove();
}

public   void addNewRandomElementsToLearn() 
{
	int tmp = nslj.src.system.Console.readInt("Number of elements to add > ");
	addNewRandomElementsToLearn(tmp);
}

public   void addNewRandomElementsToLearn(int num) 
{
	UnlearnedElements.AddRandomElements((int)(CorticalArraySize-1), num);
}

public   void addNewSpecifiedElementsToLearn() 
{
	int tmp = nslj.src.system.Console.readInt("Number of elements to add > ");
	UnlearnedElements.AddSpecifiedElements((int)(CorticalArraySize-1), tmp );
}

public void addNewSpecifiedElementsToLearn(int x, int y, int xo, int yo) 
{
	UnlearnedElements.AddSpecifiedElements((int)(CorticalArraySize-1), x, y, xo, yo );
}

public  void learnNewElements() 
{
	LearnConnections(UnlearnedElements);
	LearnedElements.Merge( UnlearnedElements );
	UnlearnedElements.Remove();
}

public void testAllLearnedElements() 
{
	TestConnections( );
}  

public void displayLearnedAndUnlearnedElements() 
{
	system.nslPrintln("Learned elements");
	system.nslPrintln(LearnedElements);
	system.nslPrintln("Unlearned elements");
	system.nslPrintln(UnlearnedElements);
}

public void testFoveaMappingOnly() 
{
	TestFoveaMapping();
}
public void makeConn(){
    nslConnect(FEFsac_in,cdmedinh.FEFsac_in);
    nslConnect(FEFsac_in,cdmedburst.FEFsac_in);
    nslConnect(SNCdop_in,cdmedtan.SNCdop_in);
    nslConnect(SNCdop_in,cdmedburst.SNCdop_in);
    nslConnect(PFCgo_in,cdmedburst.PFCgo_in);
    nslConnect(LIPmem_in,cdmedinh.LIPmem_in);
    nslConnect(LIPmem_in,cdmedburst.LIPmem_in);
    nslConnect(cdmedtan.CDmedtan_out,cdmedburst.CDmedtan_in);
    nslConnect(cdmedtan.CDmedtan_out,CDmedtan_out);
    nslConnect(gpemedburst.GPEmedburst_out,stnmedburst.GPEmedburst_in);
    nslConnect(gpemedburst.GPEmedburst_out,GPEmedburst_out);
    nslConnect(stnmedburst.STNmedburst_out,snrmedburst.STNmedburst_in);
    nslConnect(stnmedburst.STNmedburst_out,STNmedburst_out);
    nslConnect(cdmedburst.CDindmedburst_out,gpemedburst.CDindmedburst_in);
    nslConnect(cdmedburst.CDindmedburst_out,CDindmedburst_out);
    nslConnect(cdmedburst.CDdirmedburst_out,snrmedburst.CDdirmedburst_in);
    nslConnect(cdmedburst.CDdirmedburst_out,CDdirmedburst_out);
    nslConnect(cdmedinh.CDmedinh_out,cdmedtan.CDmedinh_in);
    nslConnect(cdmedinh.CDmedinh_out,CDmedinh_out);
    nslConnect(snrmedburst.SNRmedburst_out,SNRmedburst_out);
    nslConnect(medbulk1.SNRMapCount_bulk,cdmedburst.SNRMapCount_bulk);
    nslConnect(medbulk1.SNRMapCount_bulk,snrmedburst.SNRMapCount_bulk);
    nslConnect(medbulk1.SNRweights_bulk,cdmedburst.SNRweights_bulk);
    nslConnect(medbulk1.SNRweights_bulk,snrmedburst.SNRweights_bulk);
    nslConnect(medbulk1.SNRxmap_bulk,snrmedburst.SNRxmap_bulk);
    nslConnect(medbulk1.SNRymap_bulk,snrmedburst.SNRymap_bulk);
    nslConnect(medbulk1.FEFxmap_bulk,cdmedburst.FEFxmap_bulk);
    nslConnect(medbulk1.FEFxmap_bulk,cdmedinh.FEFxmap_bulk);
    nslConnect(medbulk1.FEFymap_bulk,cdmedburst.FEFymap_bulk);
    nslConnect(medbulk1.FEFymap_bulk,cdmedinh.FEFymap_bulk);
    nslConnect(medbulk1.LIPxmap_bulk,cdmedburst.LIPxmap_bulk);
    nslConnect(medbulk1.LIPxmap_bulk,cdmedinh.LIPxmap_bulk);
    nslConnect(medbulk1.LIPymap_bulk,cdmedburst.LIPymap_bulk);
    nslConnect(medbulk1.LIPymap_bulk,cdmedinh.LIPymap_bulk);
    nslConnect(medbulk1.PFCxmap_bulk,cdmedburst.PFCxmap_bulk);
    nslConnect(medbulk1.PFCymap_bulk,cdmedburst.PFCymap_bulk);
}

	/******************************************************/
	/*                                                    */
	/* Generated by nslc.src.NslCompiler. Do not edit these lines! */
	/*                                                    */
	/******************************************************/

	/* Constructor and related methods */
	/* makeinst() declared variables */

	/* Formal parameters */
	int CorticalArraySize;
	int StriatalArraySize;

	/* Temporary variables */

	/* GENERIC CONSTRUCTOR: */
	public Med(String nslName, NslModule nslParent, int CorticalArraySize, int StriatalArraySize)
{
		super(nslName, nslParent);
		this.CorticalArraySize=CorticalArraySize;
		this.StriatalArraySize=StriatalArraySize;
		initSys();
		makeInstMed(nslName, nslParent, CorticalArraySize, StriatalArraySize);
	}

	public void makeInstMed(String nslName, NslModule nslParent, int CorticalArraySize, int StriatalArraySize)
{ 
		Object[] nslArgs=new Object[]{CorticalArraySize, StriatalArraySize};
		callFromConstructorTop(nslArgs);
		FEFsac_in = new NslDinDouble2("FEFsac_in", this, CorticalArraySize, CorticalArraySize);
		SNCdop_in = new NslDinDouble2("SNCdop_in", this, CorticalArraySize, CorticalArraySize);
		PFCgo_in = new NslDinDouble2("PFCgo_in", this, CorticalArraySize, CorticalArraySize);
		LIPmem_in = new NslDinDouble2("LIPmem_in", this, CorticalArraySize, CorticalArraySize);
		CDdirmedburst_out = new NslDoutDouble2("CDdirmedburst_out", this, StriatalArraySize, StriatalArraySize);
		SNRmedburst_out = new NslDoutDouble2("SNRmedburst_out", this, CorticalArraySize, CorticalArraySize);
		CDmedinh_out = new NslDoutDouble2("CDmedinh_out", this, StriatalArraySize, StriatalArraySize);
		CDmedtan_out = new NslDoutDouble2("CDmedtan_out", this, StriatalArraySize, StriatalArraySize);
		CDindmedburst_out = new NslDoutDouble2("CDindmedburst_out", this, CorticalArraySize, CorticalArraySize);
		GPEmedburst_out = new NslDoutDouble2("GPEmedburst_out", this, CorticalArraySize, CorticalArraySize);
		STNmedburst_out = new NslDoutDouble2("STNmedburst_out", this, CorticalArraySize, CorticalArraySize);
		cdmedinh = new CDmedinh("cdmedinh", this, CorticalArraySize, StriatalArraySize);
		cdmedtan = new CDmedtan("cdmedtan", this, CorticalArraySize, StriatalArraySize);
		cdmedburst = new CDmedburst("cdmedburst", this, CorticalArraySize, StriatalArraySize);
		gpemedburst = new GPEmedburst("gpemedburst", this, CorticalArraySize);
		stnmedburst = new STNmedburst("stnmedburst", this, CorticalArraySize);
		snrmedburst = new SNRmedburst("snrmedburst", this, CorticalArraySize, StriatalArraySize);
		medbulk1 = new MedBulk("medbulk1", this, CorticalArraySize, MaxConnections);
		LearnRate = new NslDouble0("LearnRate", this, .05);
		FEFsac_mirror = new NslDouble2("FEFsac_mirror", this, CorticalArraySize, CorticalArraySize);
		PFCgo_mirror = new NslDouble2("PFCgo_mirror", this, CorticalArraySize, CorticalArraySize);
		LIPmem_mirror = new NslDouble2("LIPmem_mirror", this, CorticalArraySize, CorticalArraySize);
		FOVEAX = new NslInt0("FOVEAX", this);
		FOVEAY = new NslInt0("FOVEAY", this);
		func = new Func("func", this, CorticalArraySize);
		LearnedElements = new _Element("LearnedElements", this);
		UnlearnedElements = new _Element("UnlearnedElements", this);
		Teacher = new _Element("Teacher", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end Med

