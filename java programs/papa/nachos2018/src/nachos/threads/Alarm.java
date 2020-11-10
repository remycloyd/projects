package nachos.threads;
import java.util.ArrayList;
import java.util.List;
import nachos.machine.*;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    public Alarm() {
	Machine.timer().setInterruptHandler(new Runnable() {
		public void run() { timerInterrupt();
		} 
			});
    }
    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {
    	List<Toop> removeList =new ArrayList<Toop>();
    		for (Toop element : threadTimePairs) {
    			if (element.waitTime< Machine.timer().getTime()&& !removeList.contains(element)){
    				boolean intStatus = Machine.class.desiredAssertionStatus();
    				element.aThread.ready();
    				removeList.add(element);
    				Machine.interrupt().restore(intStatus);
    			}
    		}
    		threadTimePairs.removeAll(removeList);
    }
    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param	x	the minimum number of clock ticks to wait.
     *
     * @see	nachos.machine.Timer#getTime()
     */
    public void waitUntil(long x) {
	// for now, cheat just to get something working (busy waiting is bad)
	long wakeTime = Machine.timer().getTime() + x;
	
	threadTimePairs.add(new Toop(wakeTime, KThread.currentThread()));
	
	boolean intStatus = Machine.interrupt().disable();
	
	KThread.currentThread().sleep();
	
	Machine.interrupt().restore(intStatus);   
	
    }
    private static List<Toop> threadTimePairs = new ArrayList<Toop>();
    
    public class Toop{
		private final long waitTime;
		private KThread aThread;
	public Toop(long x, KThread y){
		this.waitTime = x;
		this.aThread = y;
		}
	
    }
    
}

















