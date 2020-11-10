package nachos.threads;
import java.util.LinkedList;
import java.util.Iterator;
import nachos.ag.BoatGrader;
import nachos.machine.Lib;

public class Boat
{
	static boolean Finished = false;
	static BoatGrader bg;    
    // define the locations of each island
    static final int Oahu = 0;
    static final int Molokai = 1;
    static int boatLocation = Oahu;        // boat location
    static int cntPassengers = 0;                           
    static Lock boatLock = new Lock();     // boat holds the lock
    static Condition2 OahuWaiter = new Condition2(boatLock);
    static Condition2 MolokaiWaiter = new Condition2(boatLock);
    static Condition2 FullBoatWaiter = new Condition2(boatLock);
    
    static int OahuChildren = 0;
    static int OahuAdults = 0;
    static int MolokaiChildren = 0;
    static int MolokaiAdults = 0; 
    static Communicator reporter = new Communicator();
     
    public static void selfTest(){
	BoatGrader b = new BoatGrader();	
	System.out.println("\n ***Testing Boats with only 2 children***");
	begin(0, 2, b);
	//System.out.println("\n ***Testing Boats with 2 children, 1 adult***");
	//	begin(1, 2, b);
	//System.out.println("\n ***Testing Boats with 3 children, 3 adults***");
	//begin(3, 3, b);
    }

    public static void begin( int adults, int children, BoatGrader b )
    {
	
	bg = b;// Store the externally generated auto grader in a class variable to be accessible by children.
	OahuChildren = children;
    OahuAdults = adults;
    MolokaiChildren = 0;
    MolokaiAdults = 0;
	Runnable r_child = new Runnable() {		
	    public void run() {
	    	int location = Oahu;// thread local variable, indicates location
            ChildItinerary(location);
            };
        };
    Runnable r_adult = new Runnable(){
    	public void run() {
            int location = Oahu;  // thread local variable, indicate where person is
        	AdultItinerary(location);
        	};
    	};    	
    	for(int i=0; i < children; i++){
        KThread t = new KThread(r_child);
        t.setName("Boat thread - Child - #" + (i+1));
        t.fork();
    	}  	
    	for (int i = 0; i < adults; i++) {
            KThread t = new KThread(r_adult);
            t.setName("Boat Thread - Adult - #" + (i+1));
            t.fork();
    }
	while(true){
		int recv=reporter.listen();
		System.out.println("***** receive" + recv);
		if (recv== children + adults){
			Finished= true;
			break;
		}
	}
    }
    static void AdultItinerary(int location)
    {	
       boatLock.acquire(); 

       while (true)
       {
           if (location == Oahu)
           {
               // child first, then send adults to Molokai
               // but leave one child in Oahu
               while (cntPassengers > 0 
                       || OahuChildren > 1 || boatLocation != Oahu) 
               {
                   OahuWaiter.sleep();
               }

               bg.AdultRowToMolokai();
               OahuAdults--;

               boatLocation = Molokai;
               MolokaiAdults++;

               location = Molokai; 
               reporter.speak(MolokaiChildren+MolokaiAdults);

               Lib.assertTrue(MolokaiChildren > 0);

               // adult arrives on Molokai, wake up one child in Molokai
               MolokaiWaiter.wakeAll();

               // current adult is sleeping
               MolokaiWaiter.sleep();
           }
           else if (location == Molokai)
           {
               MolokaiWaiter.sleep();
           }
           else 
           {
               Lib.assertTrue(false);
               break; 
           }
       }
       boatLock.release(); 
    }

    static void ChildItinerary(int location)
    {
       System.out.println("***** ChildItinerary, place: " + location);

       boatLock.acquire(); 

       while (true) {
            // below block is to cheat Java compiler, otherwise the code doesn't compile 
            if (location == 12345678)
            {
               // unreachable path
               Lib.assertTrue(false);
               break; // place a break to cheat JAVA compiler
            }
            if (location == Oahu)
            {
               // wait until boat's arrival and available seat on boat
               // if only one child left in Oahu, adults go first
               while (boatLocation != Oahu || cntPassengers >= 2
                       || (OahuAdults > 0 && OahuChildren == 1) ) 
               {
                   OahuWaiter.sleep();
               }
               OahuWaiter.wakeAll();
               // if no adult and only one child left in Oahu, the child row to Molokai directly 
               if (OahuAdults == 0 && OahuChildren == 1) 
               {
                   OahuChildren--;
                   bg.ChildRowToMolokai();

                   boatLocation = Molokai;
                   location = Molokai; 
                   MolokaiChildren++;

                   // clear passenger number after arrival
                   cntPassengers = 0;

                   // collate the number of people in Molokai
                   reporter.speak(MolokaiChildren+MolokaiAdults);

                   // child arrives in Molokai, to wake up one person in Molokai
                   // at this point, all the persons should be on Molokai
                   // MolokaiWaiter.wakeAll();
                    
                   // current child is sleeping in Molokai
                   MolokaiWaiter.sleep();
                    
               }
               else if (OahuChildren > 1) // send children to Molokai first
               {

                   // book the seat on boat
                   cntPassengers++;

                   // two children on boat, the second child rides to Molokai
                   if (cntPassengers == 2) 
                   {  

                        // notify the first one in row to Molokai
                        FullBoatWaiter.wake();

                        FullBoatWaiter.sleep();

                        // then ride myself to Molokai
                        OahuChildren--;
                        bg.ChildRideToMolokai();

                        // all the children get off boat, decrease passenger number
                        cntPassengers = cntPassengers - 2;

                        // note, now boat arrives on Molokai
                        boatLocation = Molokai;

                        location = Molokai; 

                        MolokaiChildren++;

                        reporter.speak(MolokaiChildren+MolokaiAdults);

                        // two children arrive in Molokai, wake up one child in Molokai
                        MolokaiWaiter.wakeAll();

                        // current child is sleeping
                        MolokaiWaiter.sleep();
                   }
                   // the first passenger(pilot) rows to Molokai
                   else if (cntPassengers == 1) 
                   {      
                        // only one child on boat, wait for next child(passenger)  coming
                        FullBoatWaiter.sleep();
                        
                        OahuChildren--;
                        
                        bg.ChildRowToMolokai();

                        location = Molokai; 
                        MolokaiChildren++;
                        
                        // notify another passenger on board to leave
                        FullBoatWaiter.wake();

                        // current child is sleeping
                        MolokaiWaiter.sleep();
                   }
               } // if OahuChildren > 1
            }
            else if (location == Molokai) 
            {
               Lib.assertTrue(MolokaiChildren > 0);
               
               while (boatLocation != Molokai|| Finished) 
               {
                   MolokaiWaiter.sleep();
               }
               //just need one child pilot back to Oahu
               MolokaiChildren--;
               bg.ChildRowToOahu();
               boatLocation = Oahu;
               location = Oahu; 
               OahuChildren++;

               OahuWaiter.wakeAll();
               OahuWaiter.sleep();
            }
        } // while (true)
        boatLock.release(); 
    }

    static void SampleItinerary()
    {
	System.out.println("\n ***Everyone piles on the boat and goes to Molokai***");
	bg.AdultRowToMolokai();
	bg.ChildRideToMolokai();
	bg.AdultRideToMolokai();
	bg.ChildRideToMolokai();
    }
}