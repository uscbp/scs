package CrowleyModel._Element.v1_1_1.src;

nslClass _Element(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  _Element
//versionName: 1_1_1
//floatSubModules: true


//variables 
private int x; // 
private int y; // 
private int xo; // 
private int yo; // 
private _Element next; // 

//methods 
private final static int FOVEAX=4; // 
private final static int FOVEAY=4; // 
private final static int CorticalArraySize=9; // 
private final static int StriatalArraySize=90; // 
private final static int MaxConnections=50; // 
private final static int MaxCenters=16; // 

//methods 
// Element() 
public void initElement() 
{
	x    = 0;  // Changed to from -1 to 0 SM 3/03
	y    = 0; 
	xo   = 0; 
	yo   = 0; 
	next = null;
}

// Element(String name) 
public void initElement(charString name) 
{
	initElement();
}

// Element(int max) 
public void initElement(int max) 
{
	do 
	{
		x = (int)nslRandom()%max;
	 	y = (int)nslRandom()%max;
		//  Make sure Element location is not the fovea
	} while (x==FOVEAX && y==FOVEAY);

	do 
	{
		xo = (int)nslRandom()%max;
		yo = (int)nslRandom()%max;
		//  Make sure the offset saccade Element is not the fovea or the same as the
		//  saccade Element.
	} while ((xo==FOVEAX && yo==FOVEAY) || (xo == x && yo == y));
    	next = null;
}

// Element( int tx, int ty, int txo, int tyo )
public void initElement( int tx, int ty, int txo, int tyo ) 
{
	x    = tx;
	y    = ty;
	xo   = txo;
	yo   = tyo;
	next = null;
}

public void AddElement( int max ) 
{
	_Element cur, last;
	_Element temp(); // <- Check null constructor

	temp.initElement( max );

    	cur  = next; 
    	last = null;

	//  Find the end of the list of Elements so we can add another one

	while ( cur != null )
	{
		last = cur;
		cur  = cur.next;
	}

	if ( last == null )
		next = temp;
	else
        	last = temp;
}

public _Element AddRandomElements( int max, int count ) 
{
	_Element last, first;

	int i;

	first = null;
	last  = null;

	//  Create the new list of Elements and then Merge to existing list

	for ( i=0; i<count; i++ ) 
	{
		_Element temp( ); // <- Check null constructor
		temp.initElement( max );

		if ( first == null ) 
			first = temp;
		else
			last.next = temp;

		last = temp;
		last.next = null;
	}

	//  Call the Merge member function to append the new list of Elements
	//  to the existing list of Elements

	if (this == null)
		return first;
	else
		this.Merge( first );

	return this;
}

public _Element AddSpecifiedElements( int max, int tx, int ty, int txo, int tyo) 
{
	_Element first();  // <- Check null constructor
	first.initElement( tx, ty, txo, tyo );

	//  Call the Merge member function to append the new list of Elements
	//  to the existing list of Elements

	if ( this == null )
		return first;
	else
		this.Merge( first );

	return this;
}

public _Element AddSpecifiedElements( int max, int count ) 
{
	_Element last, first;
	int i, tx, ty, txo, tyo, tsize;

	tsize = CorticalArraySize;

	first = null;
	last  = null;

	//  Create the new list of Elements and then Merge to existing list

	for ( i=0; i<count; i++ )    
	{

	//  Prompt for the coordinates for the specified Element

// --> Have to check next lines

		tx = ty = txo = tyo = 0;

/*
            tx = nslj.src.system.Console.readInt("Enter x coordinates for first target (range:0-"
                  +(CorticalArraySize-1)+"): ");
            ty = nslj.src.system.Console.readInt("Enter y coordinates for first target (range:0-"
                  +(CorticalArraySize-1)+"): ");
            txo = nslj.src.system.Console.readInt("Enter x coordinates for second target (range:0-"
                  +(CorticalArraySize-1)+"): ");
            tyo = nslj.src.system.Console.readInt("Enter y coordinates for second target (range:0-"
                  +(CorticalArraySize-1)+"): ");
*/
		if ( ( tx < 0 )  || ( tx >  CorticalArraySize-1) || ( ty < 0 )  || ( ty >  CorticalArraySize-1) || ( txo < 0 ) || ( txo > CorticalArraySize-1) ||
			( tyo < 0 ) || ( tyo > CorticalArraySize-1) ) 
		{
			nslPrintln("Coordinates out of range.  Try again.");
		}
		else 
		{
			// Create the Element and put on the new list
			_Element temp();  // <- Check null constructor
			temp.initElement( tx, ty, txo, tyo );

			if ( first == null ) 
				first = temp;
			else
				last.next = temp;

			last = temp;
			last.next = null;
		}
	}

	//  Call the Merge member function to append the new list of Elements
	//  to the existing list of Elements

	if ( this == null )
		return first;
	else
		this.Merge( first );

	return this;
}

public _Element Merge(_Element list ) 
{
	_Element cur, last;

	if ( x == -1 ) 
	{
		x = list.x;
		y = list.y;
		xo = list.xo;
		yo = list.yo;
		next = list.next;
		return this;
	}

	cur  = next; 
	last = null;

	while ( cur != null )
	{
		last = cur;
		cur  = cur.next;
	}

	if ( last == null )
		next = list;
	else
		last.next = list;

	return this;
}

public void Remove( ) 
{
	_Element cur, last;

	last = this;
	cur = next;

	while ( cur != null ) 
	{
		last.next = null;
		last = cur;
		cur = cur.next;
	}

	x  = -1;
	y  = -1;
	xo = -1;
	yo = -1;
}

public double Check( int i, int j ) 
{
	// Checks the input i,j Element location to see if it is a correct
	// remapped location.  If so, returns 0, if not returns -1/2*LearningRate.

	if ( ( i == x  ) && ( j == y  ) ) 
		return 0.0;
	if ( ( i == xo ) && ( j == yo ) ) 
		return 0.0;

	return -0.0025;
    	// return ( -0.5 * LearnRate.elem() ) ;
}

public void Remap( int max, _Element elem ) 
{
	//  This function "remaps" the calling Element and returns an Element
	//  containing the remapped location.

	int xt, yt, xot, yot;

	xt = FOVEAX - x; yt = FOVEAY - y;
	xot = xt + xo;   yot = yt + yo;

	elem.x = FOVEAX; elem.y = FOVEAY;

	if ( ( xot > -1 ) && ( xot < max ) )
		elem.xo = xot;
	else
		elem.xo = -1;

	if ( ( yot > -1 ) && ( yot < max ) )
		elem.yo = yot;
	else
		elem.yo = -1;

	return;
}

public _Element GetElement( ) 
{
	// This method displays a list of all Elements, prompts the user
	// for which one they want and returns the pointer of the selected Element

	_Element cur;
	int     counter, selection;

	cur     = this;
	counter = 0;

	//  Display the list of Elements

	nslPrintln("\n\n      X    Y  XO  YO\n");

	while ( cur != null ) 
	{
		counter++;
		nslPrintln(counter+","+ cur.X()+"  "+ cur.Y()+"  "+ cur.XO()+"  "+cur.YO() );
		cur = cur.Next();
	}

	//  Ask for the selected Element

	selection = 0;

	while ( ( selection < 1 ) || ( selection > counter ) ) 
	{
// --> Have to check next lines

            //selection = nslj.src.system.Console.readInt("Enter the number of the selected sequence ");
		if ( ( selection < 1 ) || ( selection > counter ) )
			nslPrintln("\nInvalid choice");
	}

	//  Get the selected Element and return its pointer

	cur = this;

	for( counter=1; counter<selection; counter++ )
		cur = cur.Next();

	return cur;
}

// --> Have to check next lines

/*    public NslCharString toString() {
	_Element telem;

	if (x == -1) 
	    return "no Element to display";

	StringBuffer strbuf = new StringBuffer(1024);
	telem = this;
	strbuf.append("X\tY\tXO\tYO");
	while (telem!=null) {
	    strbuf.append( x+"\t"+y+"\t"+xo+"\t"+yo);
	}

	return strbuf.toString();
    }
*/
public _Element Next() 
{
	return next;
}

public int X() 
{
	return x;
}

public int Y() 
{
	return y;
}

public int XO() 
{
	return xo;
}

public int YO() 
{
	return yo;
}
public void makeConn(){}
}//end _Element

