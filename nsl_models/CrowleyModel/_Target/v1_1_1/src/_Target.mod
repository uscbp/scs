package CrowleyModel._Target.v1_1_1.src;

nslClass _Target(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  _Target
//versionName: 1_1_1
//floatSubModules: true


//variables 
public double xcor; // 
public double ycor; // 
private _Target next; // 

//methods 
_Target(String name)
{
	this();
} 
 
_Target( ) 
{ 
	xcor = 0; ycor = 0; next = null;
}

_Target( int x, int y )
{
	xcor = (double)x;
	ycor = (double)y;
	next = null; 
}

_Target( int x, int y, _Target list )
{
	xcor = (double)x;
	ycor = (double)y;
	next = list;
}

public void Move( NslDouble1 invec )
{
	// This method applies the input movement vector to all of the Targets in
	// the linked list.  The x,y-coordinates of each Target have the input
	// movement vector subtracted from their corner coordinates as the motion
	// of the Targets across the visual space is in the opposite direction to
	// the movement of the eyes.
	_Target cur;

	//  Do the first target as it always exists
 	xcor = xcor - invec.get(0);
	ycor = ycor - invec.get(1);
 
	cur = next;  //get pointer to next Target
 
	//  The do while will "move" the second and higher Targets if they exist
	while ( cur != null )      
	{
		cur.xcor = cur.xcor - invec.get(0);
		cur.ycor = cur.ycor - invec.get(1);
		cur = cur.next;
	}
}

void RemoveTarget( _Target cur )
{
	// This method removes the cur Target from the list of Targets
 	_Target tempcur, prev;
    
	tempcur = next;
	prev    = null;
    
	if ( cur.next == next )  
	{
		// Remove first target from list by copying values from second target to
		// first target and deleting the memory for the second target.
      
		//      Check for NULL pointers
		/* just use gc       
		if ( tempcur == null ) {delete cur;return;}
		*/
		if(next == null) 
		{
			xcor = -1;
			ycor = -1;
			next = null;
		}
		//      Otherwise set the pointers to the new first element in the list 
		xcor = tempcur.xcor;
		ycor = tempcur.ycor;
		next = tempcur.next;
		//        delete tempcur;
		return;
	}
    
	//      Target to be removed is not the first Target.  Need to find the
	//      Target previous to the one to be removed
	while ( tempcur != cur )
	{
		prev    = tempcur;
		tempcur = tempcur.next;
	} 
    
	// cur is second in list
	if ( prev == null )
	{  
		next = cur.next;
		//delete cur;
	}
	else  
	{
		prev.next = cur.next;
		//delete cur;
	}
}

//        Target *GetTarget( void );
double X()
{
	return xcor;
}

double Y()
{
	return ycor;
}

_Target Next()
{
	return next;
}
 
public static _Target MakeTargets(NslDouble2 inmat2d) 
{
	double[][] inmat = inmat2d.get();
	int size1 = inmat.length;
	int size2 = inmat[0].length;
	_Target FirstTarget = null;
	for(int i=0; i<size1; i++)
	{
		for(int j=0; j<size2; j++) 
		{
			if(inmat[i][j]>0) 
			{
				if(FirstTarget == null)
					FirstTarget = new _Target (i,j)  ;
				else
					FirstTarget = new _Target (i,j,FirstTarget) ;
			}
		}
	}
	return FirstTarget;
}

public static NslDouble2 MoveEye (_Target Tlist, NslDouble2 inmat) 
{
	/*
	This function performs a "floating-point" remapping of the input matrix
	inmat using the x,y offsets specified in the input Target list Tlist.
	Movement of less than one array element is handled by spreading the
	activity over up to 4 array elements.  Targets have a size of one
	array element.
	*/
    
	int     maxi, maxj;
	int     intTx, intTy;
    
	_Target cur, prev, next;
    
	double Tx, Ty;
    
	prev = Tlist;
	cur  = Tlist;
    
	//No targets in list
	if ( cur == null )
		return inmat;       
	next = Tlist.Next();
    
	maxi      = (int)inmat.getSize1();
	maxj      = (int)inmat.getSize2();
    
	NslDouble2 tempmat2d (maxi, maxj);
	double[][] tempmat = tempmat2d.get();
    
	//  Main loop to remap the current values in inmat based on the new target
	//  location.
	while ( cur != null)
	{
		Tx        = cur.X();
		Ty        = cur.Y();
		intTx     = (int)Tx;
		intTy     = (int)Ty;
	
		if ( ( ( intTx < 0 ) || ( intTx >= maxi ) ) || ( ( intTy < 0 ) || ( intTy >= maxj ) ) )
		{
			if(Tlist!=null)
				Tlist.RemoveTarget( cur );
			// if (Tlist == NULL ) cout <<endl<<"Tlist is NULL";
		}
		else
		{
			tempmat[intTx][intTy] = ( ( 1 + intTx - Tx ) * ( 1 + intTy - Ty ) ) + tempmat[intTx][intTy];

			if ( intTx+1 < maxi ) 
			{
				tempmat[intTx+1][intTy] = ( ( Tx - intTx ) * ( 1 + intTy - Ty ) )  + tempmat[intTx+1][intTy];
			}
  
			if ( intTy+1 < maxj ) 
			{
                		tempmat[intTx][intTy+1] = ( ( 1 + intTx - Tx ) * ( Ty - intTy ) ) + tempmat[intTx][intTy+1];
			}
 
			if ( ( intTx+1 < maxi ) && ( intTy+1 < maxj ) )
			{
				tempmat[intTx+1][intTy+1] = ( ( Tx - intTx ) * ( Ty - intTy ) ) + tempmat[intTx+1][intTy+1];
			}
 
			//      Need to make sure that any overlap between Targets does not produce
			//      an activation greater than 1, since an array element is "full"
			//      when completely covered by any number of targets.
 			if ( tempmat[intTx][intTy] > 1)
				tempmat[intTx][intTy]     = 1;
			if ( tempmat[intTx][intTy] > 1)
				tempmat[intTx+1][intTy] = 1;
			if ( tempmat[intTx][intTy] > 1)
				tempmat[intTx][intTy+1] = 1;
			if ( tempmat[intTx][intTy] > 1)
				tempmat[intTx+1][intTy+1] = 1;

			/*
			if ( tempmat[intTx][intTy] > 1)
				tempmat[intTx][intTy]     = 1;
			if ( tempmat[intTx+1][intTy] > 1)
				tempmat[intTx+1][intTy] = 1;
			if ( tempmat[intTx][intTy+1] > 1)
				tempmat[intTx][intTy+1] = 1;
			if ( tempmat[intTx+1][intTy+1] > 1)
				tempmat[intTx+1][intTy+1] = 1;
			*/
		}
		cur = cur.Next();
	}
	return tempmat2d;
}
public void makeConn(){}
}//end _Target

