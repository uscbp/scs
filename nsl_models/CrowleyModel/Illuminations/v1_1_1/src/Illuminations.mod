package CrowleyModel.Illuminations.v1_1_1.src;
nslImport CrowleyModel.Illumination.v1_1_1.src.*;

nslClass Illuminations(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  Illuminations
//versionName: 1_1_1
//floatSubModules: true


//variables 
private int startingvectorsize=100; // 
private java.util.Vector eventlist; // 
public Illumination dummy(); // 

//methods 
public Illuminations()
{
	super();
	initIlluminations();
}

public void callFromConstructorTop() 
{
	eventlist = new java.util.Vector(startingvectorsize);
}

public void initIlluminations() 
{
	eventlist = new java.util.Vector(startingvectorsize);
}

Illumination elementAt(int i) 
{
	return (Illumination)eventlist.elementAt(i);
}

public void addIllumination(Illumination i)
{
	eventlist.add(i);
}

/**
* RemoveIlluminations
*/
public void RemoveIlluminations( ) 
{
	eventlist.clear();
}

/**
* FixationIllumination
*/
/*
//  This function creates a fixation event and places it at the front of the
//  calling object.
public Illuminations FixationIllumination( ) {
  int tx;
  int ty;
  double tstart;
  double tval;
  double tstop;
  Illumination temp;

  temp = Illumination.queryForFixationIllumination();  // query user for input

	// this is a obj with a vector
    if ( eventlist.size() == 0 ) {
//      No events already exist, create a new list
  	eventlist.addElement(temp);
        return this; //return temp;
    }
    else {
      //  Change values for current Fixation Illumination
      //      eventlist.elementAt(0).changeValues(temp.tx,temp.ty,
      //	temp.tval,temp.tstart,temp.tstop);
      ((Illumination)eventlist.elementAt(0)).changeValues(temp.x,temp.y,
	temp.activation,temp.start,temp.stop);
       return this;  // return a list
     }
}
*/
/**
* TargetIllumination
*/
/*
public Illuminations TargetIllumination() {
//  This function places a target and its associated start and stop times
//  at the end of the current list of events.

Illumination cur, last;

int tx, ty;
double tstart, tval, tstop;
Illumination temp;

//  Find the end of the current list of events

    if ( this.eventlist.size() == 0 ) {
//        No Fixation event.  Must create head of event list.
      temp = Illumination.queryForFixationIllumination();
      eventlist.addElement(temp);
      return this;
    }
    
    //    temp = Illumination.queryForValStartStop();
    temp = Illumination.queryForIllumination();
    eventlist.addElement(temp);
    return this;
}
*/
/**
* DisplayIlluminations
*/
/*
public void DisplayIlluminations( PrintStream os ) {
// os needs to be opened as:
// PrintStream os = new PrintStream(new BufferedOutputStream("new FileOutputStream("displayevents.out");
// Thisffunction displays all input events

Illumination cur;
int   count;

    // this is an event list
    count = 0;

    if ( this.eventlist.size() == 0 ) {
        os.println("No input visual events");
        return;
    }
    else {
        os.println();
        os.println("                               Start     Stop
                X     Y   Activation   Time      Time");
        os.println();
    }

    cur=(Illumination)this.eventlist.elementAt(count);
    while ( count <= this.eventlist.size() ) {
        count++;
        os.print(setiosflags(ios.right) + 
           + setw(2) + count + "."
           + setiosflags(ios.fixed)
           + setw(6) + cur.getX()
           + setw(6) + cur.getY()
           + setw(11) + setprecision(1) + cur.getValue()
           + setw(10) + setprecision(1) + (cur.getStart()*1000)
           + setw(10) + setprecision(1) + (cur.getStop()*1000)
           + endl + resetiosflags(ios.right | ios.fixed);

        cur = (Illumination)this.elementAt(count);
    }

    os.println();
    os.println();

    return;
}
*/


/**
* IlluminationsFromElement
*/
/*
public Illuminations IlluminationsFromElement( Element elem ) {

//This member function creates 2 Illuminations from the input element by
//prompting the user for the activation value and the start and stop times.
//It uses the x,y locations from the input element.


Illumination temp;
Illumination first,next;

int tx, ty;
double tstart, tval, tstop;

    // this is Illuminations

    if ( eventlist.size() == 0 ) {
//        No Fixation event.  Must create this first

        temp = Illumination.queryForFixationIllumination();
	eventlist.addElement(temp);
    }
// 	Now create the event and put it in the event list
    System.out.println("Creating a 2 target sequence");
    first = Illumination.queryForValStartStop(elem.X(), elem.Y(),
			 "Enter activation for first target (0 to 90) at (",
			 "Enter start/stop times (in msec, separate with space): ");
    if(first!=null)eventlist.addElement(first);

    next = Illumination.queryForValStartStop(elem.XO(), elem.YO(),
			 "Enter activation for second target (0 to 90) at (",
			 "Enter start/stop times (in msec, separate with space): ");
    if(next!=null)eventlist.addElement(next);

    return this;
}
*/
/**
* SetIlluminations
*/
/*
public Illuminations SetIlluminations( Element lelem ) {

//  This function establishes the visual events that are to occur during
//  an experiment.

double  tstart, tstop, tvalue;
int     tx, ty, num, i;
String    answer;
String  numstr;  
Element elem;
Illuminations   tevents= null;  // just a pointer to an event list
  DataInputStream ins = new DataInputStream(System.in);
  try {
// the "this list should have been created outside this method

//  Ask if to use learned sequences or user-specified targets;

    System.out.println("Use learned target sequences (y/n)?");
    answer=ins.readLine();

    if ( ( answer.equals("N") ) || ( answer.equals("n")) ) {

//      Use user-specified visual targets

        System.out.println("How many visual targets?");
	numstr=ins.readLine();
	num = Integer.valueOf(numstr).intValue();


        for ( i=0; i<num; i++ )  //this is an eventlist
	  tevents = TargetIllumination();
    }
    else {

//      Get learned element selected by user

        if ( lelem == null ) {
//          No learned elements

            System.out.println("No learned elements");
            return null;
        }

        elem = lelem.GetElement();

//      Create Illuminations from this element

        tevents = IlluminationsFromElement( elem );
    }

    } catch (java.io.IOException e) {
      System.err.println("IO error for input stream. Return null");
      return null;
    }

    return tevents;
}

*/

public java.util.Vector getIlluminationList() 
{
	return eventlist;
}
public void makeConn(){
}
}//end Illuminations

