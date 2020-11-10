package nachos.threads;

import nachos.machine.*;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>, and multiple
 * threads can be waiting to <i>listen</i>. But there should never be a time
 * when both a speaker and a listener are waiting, because the two threads can
 * be paired off at this point.
 */
public class Communicator {
	private Lock mLock = new Lock();
	private boolean fullInbox = false;
	private Integer message;
	private int listenerNum;
	private Condition speaker = new Condition(mLock);
	private Condition listener = new Condition(mLock);

	/**
	 * Allocate a new communicator.
	 */
	public Communicator() {
		fullInbox = false;
		listenerNum = 0;
		mLock = new Lock();
		speaker = new Condition(mLock);
		listener = new Condition(mLock);
	}

	/**
	 * Wait for a thread to listen through this communicator, and then transfer
	 * <i>message</i> to the listener.
	 *
	 * <p>
	 * Does not return until this thread is paired up with a listening thread.
	 * Exactly one listener should receive <i>message</i>.
	 *
	 * @param message
	 *            the integer to transfer.
	 */
	public void speak(int word) {
		if (!mLock.isHeldByCurrentThread()) {
			mLock.acquire();
		}

		while (listenerNum == 0 || fullInbox) {
			speaker.sleep();
		}
		message = word;
		fullInbox = true; // will place message
		listener.wake(); // wake up the listener(s)
		mLock.release();
	}

	/** Wait for a thread to speak through this communicator, and then return the <i>message</i> that thread passed to <tt>speak()</tt>.
	 * @return the integer transferred.
	 */
	public int listen() {
		if (!mLock.isHeldByCurrentThread()) {
			mLock.acquire();
		}
		++listenerNum;

		while (!fullInbox)
		{// no messages
			speaker.wakeAll();// wake speakers to check for listeners
			listener.sleep();// wait
		}
		int word = message; // update inbox with what is recieved
		fullInbox = false;
		--listenerNum;

		speaker.wakeAll();// wake speakers to check for listeners
		mLock.release();
		return word;
	}
}