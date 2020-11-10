package nachos.threads;
import nachos.machine.*;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Random;
/**
 * A scheduler that chooses threads based on their priorities.
 *
 * <p>
 * A priority scheduler associates a priority with each thread. The next thread
 * to be dequeued is always a thread with priority no less than any other
 * waiting thread's priority. Like a round-robin scheduler, the thread that is
 * dequeued is, among all the threads of the same (highest) priority, the
 * thread that has been waiting longest.
 *
 * <p>
 * Essentially, a priority scheduler gives access in a round-robin fashion to
 * all the highest-priority threads, and ignores all other threads. This has
 * the potential to
 * starve a thread if there's always a thread waiting with higher priority.
 *
 * <p>
 * A priority scheduler must partially solve the priority inversion problem; in
 * particular, priority must be donated through locks, and through joins.
 */
public class PriorityScheduler extends Scheduler{
	
    /**
     * Allocate a new priority scheduler.
     */
    public PriorityScheduler() {
    }    /**  Allocate a new priority thread queue.
     * @param	transferPriority	<tt>true</tt> if this queue should
     *					transfer priority from waiting threads
     *					to the owning thread.
     * @return	a new priority thread queue.
     */
    public ThreadQueue newThreadQueue(boolean transferPriority) {
	return new PriorityQueue(transferPriority);
    }
    public int getPriority(KThread thread) {
	Lib.assertTrue(Machine.interrupt().disabled());	       
	return getThreadState(thread).getPriority();
    }

    public int getEffectivePriority(KThread thread) {
	Lib.assertTrue(Machine.interrupt().disabled());
		       
	return getThreadState(thread).getEffectivePriority();
    }

    public void setPriority(KThread thread, int priority) {
	boolean intStatus = Machine.interrupt().disabled();
		       
	Lib.assertTrue(priority >= priorityMinimum &&
		   priority <= priorityMaximum);
	
	getThreadState(thread).setPriority(priority);
	Machine.interrupt().restore(intStatus);
    }

    public boolean increasePriority() {
	boolean intStatus = Machine.interrupt().disable();
		       
	KThread thread = KThread.currentThread();

	int priority = getPriority(thread);
	if (priority == priorityMaximum) {
        Machine.interrupt().restore(intStatus);
	    return false;
	}
	
	setPriority(thread, priority+1);

	Machine.interrupt().restore(intStatus);
	return true;
    }

    public boolean decreasePriority() {
	boolean intStatus = Machine.interrupt().disable();
		       
	KThread thread = KThread.currentThread();

	int priority = getPriority(thread);
	if (priority == priorityMinimum){
        Machine.interrupt().restore(intStatus);
        return false;
    }
	setPriority(thread, priority-1);

	Machine.interrupt().restore(intStatus);
	return true;
    }

    /**
     * The default priority for a new thread. Do not change this value.
     */
    public static final int priorityDefault = 1;
    /**
     * The minimum priority that a thread can have. Do not change this value.
     */
    public static final int priorityMinimum = 0;
    /**
     * The maximum priority that a thread can have. Do not change this value.
     */
    public static final int priorityMaximum = 7;    

    /**
     * Return the scheduling state of the specified thread.
     *
     * @param	thread	the thread whose scheduling state to return.
     * @return	the scheduling state of the specified thread.
     */
    protected ThreadState getThreadState(KThread thread) {
	if (thread.schedulingState == null)
	    thread.schedulingState = new ThreadState(thread);
	return (ThreadState) thread.schedulingState;
    }
    /**
     * A <tt>ThreadQueue</tt> that sorts threads by priority.
     */
    protected class PriorityQueue extends ThreadQueue {
	PriorityQueue(boolean transferPriority) {
	    this.transferPriority = transferPriority;
	}

	public void waitForAccess(KThread thread) {
		boolean intStatus = Machine.interrupt().disabled();
        getThreadState(thread).waitForAccess(this);
        // Machine.interrupt().restore(intStatus);
    }

	public void acquire(KThread thread) {
        boolean intStatus = Machine.interrupt().disabled();
            
        Lib.debug('t', "[PriorityQueue.acquire] thread: " + thread
                    + " holder: " + this.holder );
        ThreadState state = getThreadState(thread); 
         
        this.holder = state;          
         
        state.acquire(this);
        // Machine.interrupt().restore(intStatus);
    }

    public KThread nextThread() {
        
        boolean intStatus = Machine.interrupt().disabled();

        if (waitQueue.isEmpty()) {
            // Machine.interrupt().restore(intStatus);
            return null;
        }
        
        
        if (this.transferPriority)  
        {
            this.holder.myResource.remove(this);
            this.holder = null;
        }

        KThread pickedThread = pickNextThread();
        if (pickedThread != null) {
            waitQueue.remove(pickedThread);
            this.acquire(pickedThread);       
        }     
        //Machine.interrupt().restore(intStatus);
        return pickedThread;
    }
    /**
     * Return the next thread that <tt>nextThread()</tt> would return,
     * without modifying the state of this queue.
     *
     * @return  the next thread that <tt>nextThread()</tt> would
     *      return.
     */
    protected KThread pickNextThread() {
        KThread nextThread = null;
        //this.print(); // debug
        // System.out.print("Inside 'pickNextThread:' transferPriority: " + transferPriority + "\n"); // debug
        for (Iterator<KThread> ts = waitQueue.iterator(); ts.hasNext();) {  
            KThread thread = ts.next(); 
            int priority = getThreadState(thread).getEffectivePriority();
            
            if (nextThread == null || priority > getThreadState(nextThread).getEffectivePriority()) { 
                nextThread = thread;
            }
        }
        // System.out.print("Inside 'pickNextThread:' return Thread: " 
        //        + nextThread + "\n"); // debug     
        return nextThread;
    }
    public int getEffectivePriority() {
        // System.out.print("[Inside getEffectivePriority] transferPriority: " + transferPriority + "\n"); // debug
        // if do not transfer priority, return minimum priority
        if (transferPriority == false) {
        // System.out.print("Inside 'getEffectivePriority:' false branch\n" ); // debug
            // return priorityMinimum;
            return priorityMinimum;
        }
        if (Used) {
            effectivePriority = priorityMinimum; 
            for (Iterator<KThread> it = waitQueue.iterator(); it.hasNext();) {  
                KThread thread = it.next(); 
                int priority = getThreadState(thread).getEffectivePriority();
                if ( priority > effectivePriority) { 
                    effectivePriority = priority;
                }
            }
            Used = false;
        }
        return effectivePriority;
    }
    public void setUsed() {
        if (transferPriority == false) {
            return;
        }
        Used = true;

        if (holder != null) {
            holder.setUsed();
        }
    }
    public void print() { 
        for (Iterator<KThread> it = waitQueue.iterator(); it.hasNext();) {  
            KThread currentThread = it.next(); 
            int  priority = getThreadState(currentThread).getPriority();

            System.out.print("Thread: " + currentThread 
                                + "\t  Priority: " + priority + "\n");
        }
    }
    /**
     * <tt>true</tt> if this queue should transfer priority from waiting
     * threads to the owning thread.
     */
    public boolean transferPriority;
    /** The queue  waiting on this resource */
    protected LinkedList<KThread> waitQueue = new LinkedList<KThread>();   
    /** The ThreadState corresponds to the holder of the resource */
    protected ThreadState holder = null;              
    /** Set to true when a new thread is added to the queue, 
     *  or any of the queues in the waitQueue flag themselves as Used */
    protected boolean Used;                    
    /** The cached highest of the effective priorities in the waitQueue. 
     *  This value is invalidated while Used is true */
    protected int effectivePriority; 
} 
protected class ThreadState {
/**
 * Allocate a new <tt>ThreadState</tt> object and associate it with the
 * specified thread.
 *
 * @param   thread  the thread this state belongs to.
 */
public ThreadState(KThread thread) {
    this.thread = thread;
    setPriority(priorityDefault);
}
/**
 * Return the priority of the associated thread.
 *
 * @return  the priority of the associated thread.
 */
public int getPriority() {
    return priority;
}
/**
 * Return the effective priority of the associated thread.
 * @return  the effective priority of the associated thread.
 */
public int getEffectivePriority() {
    int maxEffective = this.priority;

    if (Used) {
        for (Iterator<ThreadQueue> it = myResource.iterator(); it.hasNext();) {  
            PriorityQueue pg = (PriorityQueue)(it.next()); 
            int effective = pg.getEffectivePriority();
            if (maxEffective < effective) {
                maxEffective = effective;
            }
        }
    }     
    return maxEffective;
}
/**
 * Set the priority of the associated thread to the specified value.
 *
 * @param   priority    the new priority.
 */
public void setPriority(int priority) {
    if (this.priority == priority)
        return;
    this.priority = priority;   
    setUsed();
}
/**
 * Called when <tt>waitForAccess(thread)</tt> (where <tt>thread</tt> is
 * the associated thread) is invoked on the specified priority queue.
 * The associated thread is therefore waiting for access to the
 * resource guarded by <tt>waitQueue</tt>. This method is only called
 * if the associated thread cannot immediately obtain access.
 *
 * @param   waitQueue   the queue that the associated thread is
 *              now waiting on.
 *
 * @see nachos.threads.ThreadQueue#waitForAccess
 */
public void waitForAccess(PriorityQueue waitQueue) {   
    Lib.assertTrue(waitQueue.waitQueue.indexOf(thread) == -1);
    waitQueue.waitQueue.add(thread);
    waitQueue.setUsed();
    // set wtngOn
    wtngOn = waitQueue;
    // if the waitQueue was previously in myResource, remove it 
    // and set its holder to null
    // When will this IF statement be executed?
    if (myResource.indexOf(waitQueue) != -1) {
        myResource.remove(waitQueue);
        waitQueue.holder = null;        
    }
}
/**
 * Called when the associated thread has acquired access to whatever is
 * guarded by <tt>waitQueue</tt>. This can occur either as a result of
 * <tt>acquire(thread)</tt> being invoked on <tt>waitQueue</tt> (where
 * <tt>thread</tt> is the associated thread), or as a result of
 * <tt>nextThread()</tt> being invoked on <tt>waitQueue</tt>.

 */
public void acquire(PriorityQueue waitQueue) {
    Lib.assertTrue(waitQueue.waitQueue.indexOf(this.thread) == -1);
   // Lib.assertTrue(myResource.indexOf(waitQueue) == -1);
    // add waitQueue to myResource list
    myResource.add(waitQueue);
    // clean wtngOn if waitQueue is just waiting on
    if (waitQueue == wtngOn) {
        wtngOn = null;
    }
    // effective priority may be varied, set Used flag
    setUsed();
}   
/**
 * ThreadState.setUsed Set the Used flag, then call setUsed() on each thread
 * of priority queue that the thread is waiting for.
 * ThreadState.setUsed and PriorityQueue.setUsed would invoke each other, they are mutually recursive.
 */
public void setUsed() {
    if (Used) {
        return;
    }
    Used = true;
    PriorityQueue pg = (PriorityQueue)wtngOn;
    if (pg != null) {
        pg.setUsed();
    }
}
protected KThread thread;/** The thread with which this object is associated. */    
protected int priority;/** The priority of the associated thread. */
protected int effectivePriority;          
/** Collection of PriorityQueues that signify the Locks or other
 *  resource that this thread currently holds */
protected LinkedList<ThreadQueue> myResource = new LinkedList<ThreadQueue>();  //
/** PriorityQueue corresponding to resources that this thread has attempted to acquire but could not */
protected ThreadQueue wtngOn; 
/** Set to true when this thread's priority is changed, 
 * or when one of the queues in myResources flags itself as Used */
protected boolean Used = false;
	}
}