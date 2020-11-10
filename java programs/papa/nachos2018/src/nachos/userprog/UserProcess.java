package nachos.userprog;
import nachos.machine.*;
import nachos.threads.*;
import nachos.userprog.*;
import java.util.ArrayList;
import java.io.EOFException;

/**
 * Encapsulates the state of a user process that is not contained in its
 * user thread (or threads). This includes its address translation state, a
 * file table, and information about the program being executed.
 *
 * <p>
 * This class is extended by other classes to support additional functionality
 * (such as additional system calls).
 *
 * @see	nachos.vm.VMProcess
 * @see	nachos.network.NetProcess
 */
@SuppressWarnings("unused")
public class UserProcess {
    /**
     * Allocates a new process.
     */
    public UserProcess() {
    	openFiles = new OpenFile[16];
    	openFiles[0] = UserKernel.console.openForReading();
    	openFiles[1] = UserKernel.console.openForWriting();   	
    }  
    /**
     * Allocate and return a new process of the correct class. The class name
     * is specified by the <tt>nachos.conf</tt> key
     * <tt>Kernel.processClassName</tt>.
     *
     * @return	a new process of the correct class.
     */
    public static UserProcess newUserProcess() {
	return (UserProcess)Lib.constructObject(Machine.getProcessClassName());
    }
    /**
     * Execute the specified program with the specified arguments. Attempts to
     * load the program, and then forks a thread to run it.
     *
     * @param	name	the name of the file containing the executable.
     * @param	args	the arguments to pass to the executable.
     * @return	<tt>true</tt> if the program was successfully executed.
     */
    public boolean execute(String name, String[] args) {
	if (!load(name, args))
	    return false;
	
	new UThread(this).setName(name).fork();

	return true;
    }
    /**
     * Saves the state of the process in preparation for a context switch.
     * Called by <tt>UThread.saveState()</tt>.
     */
    public void saveState() {
    }
    /**
     * Restore the state of this process after a context switch. Called by
     * <tt>UThread.restoreState()</tt>.
     */
    public void restoreState() {
	Machine.processor().setPageTable(pageTable);
    }
    /**
     * Read a null-terminated string from this process's virtual memory. Read
     * at most <tt>maxLength + 1</tt> bytes from the specified address, search
     * for the null terminator, and convert it to a <tt>java.lang.String</tt>,
     * without including the null terminator. If no null terminator is found,
     * returns <tt>null</tt>.
     *
     * @param	vaddr	the starting virtual address of the null-terminated
     *			string.
     * @param	maxLength	the maximum number of characters in the string,
     *				not including the null terminator.
     * @return	the string read, or <tt>null</tt> if no null terminator was
     *		found.
     */
    public String readVirtualMemoryString(int vaddr, int maxLength) {
	Lib.assertTrue(maxLength >= 0);

	byte[] bytes = new byte[maxLength+1];

	int bytesRead = readVirtualMemory(vaddr, bytes);

	for (int length=0; length<bytesRead; length++) {
	    if (bytes[length] == 0)
		return new String(bytes, 0, length);
	}

	return null;
    }

    /**
     * Transfer data from this process's virtual memory to all of the specified
     * array. Same as <tt>readVirtualMemory(v addr, data, 0, data.length)</tt>.
     *
     * @param	vaddr	the first byte of virtual memory to read.
     * @param	data	the array where the data will be stored.
     * @return	the number of bytes successfully transferred.
     */
    public int readVirtualMemory(int vaddr, byte[] data) {
	return readVirtualMemory(vaddr, data, 0, data.length);
    }

    /**
     * Transfer data from this process's virtual memory to the specified array.
     * This method handles address translation details. This method must
     * <i>not</i> destroy the current process if an error occurs, but instead
     * should return the number of bytes successfully copied (or zero if no
     * data could be copied).
     *
     * @param	vaddr	the first byte of virtual memory to read.
     * @param	data	the array where the data will be stored.
     * @param	offset	the first byte to write in the array.
     * @param	length	the number of bytes to transfer from virtual memory to
     *			the array.
     * @return	the number of bytes successfully transferred.
     */
    public int readVirtualMemory(int vaddr, byte[] data, int offset,
				 int length) {
	Lib.assertTrue(offset >= 0 && length >= 0 && offset+length <= data.length);

	byte[] memory = Machine.processor().getMemory();
	
	if (vaddr < 0 || vaddr + length > virtualMemorySize) // Tests for the virtual address
		return 0;
	
	int amount, nextAmount, pageNum, paddr; // Initialize variables for while loop
	amount = nextAmount = paddr = pageNum = 0;
	
	while (length > 0){
		pageNum = vaddr / pageSize; // find page number
		paddr = (pageTable[pageNum].ppn * pageSize) + (vaddr % pageSize); // generate physical address from pagetable
		
		nextAmount = pageSize - (vaddr % pageSize);
		nextAmount = Math.min(nextAmount, length);
		
		pageTable[pageNum].used = true; // mark page in page table as used
		
		System.arraycopy(memory, paddr, data, offset, nextAmount);
		
		length = length - nextAmount; // adjust variables
		amount = amount + nextAmount;
		vaddr = vaddr + nextAmount;
		offset = offset + nextAmount;
	}
	return amount;
    }

    /**
     * Transfer all data from the specified array to this process's virtual
     * memory.
     * Same as <tt>writeVirtualMemory(vaddr, data, 0, data.length)</tt>.
     *
     * @param	vaddr	the first byte of virtual memory to write.
     * @param	data	the array containing the data to transfer.
     * @return	the number of bytes successfully transferred.
     */
    public int writeVirtualMemory(int vaddr, byte[] data) {
	return writeVirtualMemory(vaddr, data, 0, data.length);
    }

    /**
     * Transfer data from the specified array to this process's virtual memory.
     * This method handles address translation details. This method must
     * <i>not</i> destroy the current process if an error occurs, but instead
     * should return the number of bytes successfully copied (or zero if no
     * data could be copied).
     *
     * @param	vaddr	the first byte of virtual memory to write.
     * @param	data	the array containing the data to transfer.
     * @param	offset	the first byte to transfer from the array.
     * @param	length	the number of bytes to transfer from the array to
     *			virtual memory.
     * @return	the number of bytes successfully transferred.
     */
    public int writeVirtualMemory(int vaddr, byte[] data, int offset,
				  int length) {
	Lib.assertTrue(offset >= 0 && length >= 0 && offset+length <= data.length);

	byte[] memory = Machine.processor().getMemory();
	
	int amount, nextAmount, pageNum, paddr;
	amount = nextAmount = pageNum = paddr = 0;
	
	while (length > 0){
		pageNum = vaddr / pageSize; // determine page num
		if(pageTable[pageNum].readOnly) // return if we cannot write to page
			return amount;
	
		pageTable[pageNum].dirty = true; // mark page a dirty
		pageTable[pageNum].used = true; // mark page as used
	
		nextAmount = pageSize - (vaddr % pageSize);
		nextAmount = Math.min(nextAmount, length);
		
		length = length - nextAmount;
		paddr = (pageTable[pageNum].ppn * pageSize) + (vaddr % pageSize);
		System.arraycopy(data, offset, memory, paddr, nextAmount);
		amount = amount + nextAmount;
		vaddr  = vaddr  + nextAmount;
		offset = offset + nextAmount;
	}
	return amount;
    }
    /**
     * Load the executable with the specified name into this process, and
     * prepare to pass it the specified arguments. Opens the executable, reads
     * its header information, and copies sections and arguments into this
     * process's virtual memory.
     *
     * @param	name	the name of the file containing the executable.
     * @param	args	the arguments to pass to the executable.
     * @return	<tt>true</tt> if the executable was successfully loaded.
     */
    private boolean load(String name, String[] args) {
	Lib.debug(dbgProcess, "UserProcess.load(\"" + name + "\")");
	
	OpenFile executable = ThreadedKernel.fileSystem.open(name, false);
	if (executable == null) {
	    Lib.debug(dbgProcess, "\topen failed");
	    return false;
	}

	try {
	    coff = new Coff(executable);
	}
	catch (EOFException e) {
	    executable.close();
	    Lib.debug(dbgProcess, "\tcoff load failed");
	    return false;
	}

	// make sure the sections are contiguous and start at page 0
	numPages = 0;
	for (int s=0; s<coff.getNumSections(); s++) {
	    CoffSection section = coff.getSection(s);
	    if (section.getFirstVPN() != numPages) {
		coff.close();
		Lib.debug(dbgProcess, "\tfragmented executable");
		return false;
	    }
	    numPages += section.getLength();
	}

	// make sure the argv array will fit in one page
	byte[][] argv = new byte[args.length][];
	int argsSize = 0;
	for (int i=0; i<args.length; i++) {
	    argv[i] = args[i].getBytes();
	    // 4 bytes for arrgv[] pointer; then string plus one for null byte
	    argsSize += 4 + argv[i].length + 1;
	}
	if (argsSize > pageSize) {
	    coff.close();
	    Lib.debug(dbgProcess, "\targuments too long");
	    return false;
	}
	// program counter initially points at the program entry point
	initialPC = coff.getEntryPoint();	

	// next comes the stack; stack pointer initially points to top of it
	numPages += stackPages;
	initialSP = numPages*pageSize;

	// and finally reserve 1 page for arguments
	numPages++;

	if (!loadSections())
	    return false;

	// store arguments in last page
	int entryOffset = (numPages-1)*pageSize;
	int stringOffset = entryOffset + args.length*4;

	this.argc = args.length;
	this.argv = entryOffset;
	
	for (int i=0; i<argv.length; i++) {
	    byte[] stringOffsetBytes = Lib.bytesFromInt(stringOffset);
	    Lib.assertTrue(writeVirtualMemory(entryOffset,stringOffsetBytes) == 4);
	    entryOffset += 4;
	    Lib.assertTrue(writeVirtualMemory(stringOffset, argv[i]) ==
		       argv[i].length);
	    stringOffset += argv[i].length;
	    Lib.assertTrue(writeVirtualMemory(stringOffset,new byte[] { 0 }) == 1);
	    stringOffset += 1;
	}
	return true;
    }
    /**
     * Allocates memory for this process, and loads the COFF sections into
     * memory. If this returns successfully, the process will definitely be
     * run (this is the last step in process initialization that can fail).
     *
     * @return	<tt>true</tt> if the sections were successfully loaded.
     */
    protected boolean loadSections() {
    	
	if (numPages > Machine.processor().getNumPhysPages() || numPages > UserKernel.freePages.size()) { //added freepages check
	    coff.close();
	    return false;
	}
	
	pageTable = new TranslationEntry[numPages];
	for (int x = 0; x < numPages; x++){
		pageTable[x] = (TranslationEntry)UserKernel.freePages.removeFirst();
		pageTable[x].valid = true;
		pageTable[x].vpn = x;
	}
	
	virtualMemorySize = numPages * pageSize;
	int x = 0;
			
	// load sections
	for (int s = 0; s < coff.getNumSections(); s++) {
	    CoffSection section = coff.getSection(s);
	   
	    for (int i = 0; i < section.getLength(); i++) {
	    	int ppn = pageTable[x].ppn;
	    	x++;
	    	section.loadPage(i, ppn);
	    	if(section.isReadOnly())
	    		pageTable[i].readOnly = true;
	    }
	}
	
	return true;
    }

    /**
     * Release any resources allocated by <tt>loadSections()</tt>.
     */
    @SuppressWarnings("unchecked")
	protected void unloadSections() {
    	for (int i = 0; i < pageTable.length; i++) {
    		pageTable[i].valid = pageTable[i].readOnly = pageTable[i].used = pageTable[i].dirty = false;
    	   	UserKernel.freePages.add(pageTable[i]);
    	   	pageTable[i] = null;
		}
    }    

    /**
     * Initialize the processor's registers in preparation for running the
     * program loaded into this process. Set the PC register to point at the
     * start function, set the stack pointer register to point at the top of
     * the stack, set the A0 and A1 registers to argc and argv, respectively,
     * and initialize all other registers to 0.
     */
    @SuppressWarnings("static-access")
	public void initRegisters() {
	Processor processor = Machine.processor();

	// by default, everything's 0
	for (int i=0; i<processor.numUserRegisters; i++)
	    processor.writeRegister(i, 0);

	// initialize PC and SP according
	processor.writeRegister(Processor.regPC, initialPC);
	processor.writeRegister(Processor.regSP, initialSP);

	// initialize the first two argument registers to argc and argv
	processor.writeRegister(Processor.regA0, argc);
	processor.writeRegister(Processor.regA1, argv);
    }

    /**
     * Handle the halt() system call. 
     */
    private int handleHalt() {

    	if (processID > 0){
    		return -1;
    	}
    	Machine.halt();
    	Lib.assertNotReached("Machine.halt() did not halt machine!");
    	return 0;
    }
    
    //OPEN
    public int handleOpen(int a0){
    	String fileName = this.readVirtualMemoryString(a0, MAX_SIZE);
    	if (fileName == null)
    		return -1;
    	
    	OpenFile newFile = ThreadedKernel.fileSystem.open(fileName, false);
    	if (newFile == null)
    		return -1;
    	
    	int index = getAvailIndex();
    	if (index != -1){
    		openFiles[index] = newFile;
    		return index;
    	}
    	return -1;
    }
    //CREATE
    public int handleCreate(int a0){
    	if (a0 < 0)
    		return -1;
    	
    	String fileName = this.readVirtualMemoryString(a0, MAX_SIZE);
    	if (fileName == null)
    		return -1;
    	
    	OpenFile newFile = ThreadedKernel.fileSystem.open(fileName, true);
    	if(newFile == null)
    		return -1;
    	
    	int index = getAvailIndex();
    	if (index != -1){
    		openFiles[index] = newFile;
    		return index;
    	}
    	
    	return -1;
    }
    //UNLINK
    public int handleUnlink(int a0){
    	
    	String fileName = readVirtualMemoryString(a0, MAX_SIZE);
    	if (fileName != null){
    		OpenFile aFile = ThreadedKernel.fileSystem.open(fileName, false);
    		if (aFile != null){
    			aFile.close();
    			boolean closed = ThreadedKernel.fileSystem.remove(fileName);
    			if (closed)
    				return 0;
    		}
    	}
    	return -1;
    }
    //CLOSE
    public int handleClose(int a0){
    	
    	if((a0 >= 0) && (a0 < openFiles.length) && (openFiles[a0] != null)){
	    	openFiles[a0].close(); // close the file			
	    	openFiles[a0] = null;
	    	return 0;
	    	}
	    return -1;
    }
    //WRITE
    public int handleWrite(int fileDescriptor, int buffLocation, int length){
    	if(fileDescriptor < 0 || fileDescriptor >= openFiles.length || buffLocation < 0) 
    		return -1; //return -1 if any of these tests fail
    	
		OpenFile file = openFiles[fileDescriptor];
		if(file == null) 
			return -1;
		
		byte[] buffer= new byte[length];
		int bRead = readVirtualMemory(buffLocation, buffer, 0, length);
		int numberR = file.write(buffer,0,bRead);
		if(numberR!=0 )
			return numberR;		
		//If above return is never reached, return -1
		return -1;
    }
    //READ
    public int handleRead(int fileDescriptor, int buffLocation, int length){
    	if(fileDescriptor < 0 || fileDescriptor >= openFiles.length || buffLocation < 0 || length < 0) 
    		return -1; // If any of these fail, return -1
    	OpenFile file = openFiles[fileDescriptor];
        if (file == null) return -1;

        byte[] buff = new byte[length];
        int sizeRead; 
        sizeRead = file.read(buff, 0, length);

        writeVirtualMemory(buffLocation, buff);

        return sizeRead;
    }
    //EXEC
    public int handleExec(int fileDescriptor, int argc, int argv){
    	if (fileDescriptor < 0 || argc < 0 || argv < 0)
    		return -1;
    	
    	String name = this.readVirtualMemoryString(fileDescriptor, MAX_SIZE);
    	String[] args = new String[argc];
    	byte[] dataArray = new byte[(args.length)*4];
    	int numberRead = readVirtualMemory(argv, dataArray);
    	
    	if (numberRead < dataArray.length)
    		return -1;
    	
    	for (int i = 0; i < dataArray.length / 4; i++) {
    		args[i] = readVirtualMemoryString(Lib.bytesToInt(dataArray,i*4),MAX_SIZE);
   			if(args[i] == null)
   			 return -1;
    	}
    	if(name == null)
    		return -1;
    	OpenFile execfile = ThreadedKernel.fileSystem.open(name, false);
       	if(execfile == null)
    		return -1;
    	execfile.close();
      	UserProcess newProc = UserProcess.newUserProcess();
       	if(newProc.execute(name, args)==false)
       	{
       			return -1;
       	}       		
   		pChildList.add(newProc);
   		newProc.parent = (UThread) UThread.currentThread();
    	
    	return newProc.processID;
    }
    //JOIN
    @SuppressWarnings("static-access")
	public int handleJoin(int processID, int status){
    	boolean initStatus = Machine.interrupt().disable(); //disable interrupts
    	
    	UserProcess currentProcess = null;
		for(UserProcess child : pChildList)
		{
			if(child.processID == processID)
			{
				currentProcess = child;
				break;
			}
		}	
	 	if(currentProcess == null)
	 		return -1; //return -1 if process does not exist
	 	
	   	currentProcess.waiting = (UThread)UThread.currentThread();
	   	
    	UThread.currentThread().sleep(); //send current thread to sleep
    	pChildList.remove(currentProcess);
   					 						
    	byte[] array = new byte[4];
  		Lib.bytesFromInt(array, 0, currentProcess.Exit_Status);
  		int write = writeVirtualMemory(status, array);
  		
   		Machine.interrupt().restore(initStatus); //re-enable interrupts
   		
  		if(write != array.length)
  		 return -1;
 		else if(currentProcess.Exit_Status == 0)
  		 return 1;
  		
  		return 0;
    }
    //EXIT
    @SuppressWarnings("static-access")
	public void handleExit(){
    	boolean status = Machine.interrupt().disable();
    	closeAllFiles();
    	unloadSections();
    	if (waiting != null)
			waiting.ready();
    	for (UserProcess p : pChildList) {
			p.parent = null;
			p.waiting = null;
		}
    	
    	Machine.interrupt().restore(status);
    	UThread.currentThread().finish();
    	
    	return;
    }
    
    public void closeAllFiles(){
    	for (int i = 0; i < openFiles.length; i++) {
			handleClose(i);
		}
    }
    
    public int getAvailIndex(){
    	for (int i = 0; i < openFiles.length; i++) {
			if (openFiles[i] == null)
				return i;
		}
    	return -1;
    }
    private static final int
    syscallHalt = 0,
	syscallExit = 1,
	syscallExec = 2,
	syscallJoin = 3,
	syscallCreate = 4,
	syscallOpen = 5,
	syscallRead = 6,
	syscallWrite = 7,
	syscallClose = 8,
	syscallUnlink = 9;

    public int handleSyscall(int syscall, int a0, int a1, int a2, int a3) {
	switch (syscall) {
		case syscallHalt:
	    	return handleHalt();
		case syscallOpen:
			return handleOpen(a0);
		case syscallCreate:
			return handleCreate(a0);
		case syscallUnlink:
			return handleUnlink(a0);
		case syscallClose:
			return handleClose(a0);
		case syscallWrite:
			return handleWrite(a0,a1,a2);
		case syscallRead:
			return handleRead(a0,a1,a2);
		case syscallExec:
			return handleExec(a0, a1, a2);
		case syscallJoin:
			return handleJoin(a0, a1);
		case syscallExit:
			handleExit();	
		default:
	    	Lib.debug(dbgProcess, "Unknown syscall " + syscall);
	    	Lib.assertNotReached("Unknown system call!");
		}
		return 0;
    }
    /**
     * Handle a user exception. Called by
     * <tt>UserKernel.exceptionHandler()</tt>. The
     * <i>cause</i> argument identifies which exception occurred; see the
     * <tt>Processor.exceptionZZZ</tt> constants.
     *
     * @param	cause	the user exception that occurred.
     */
    public void handleException(int cause) {
	Processor processor = Machine.processor();

	switch (cause) {
	case Processor.exceptionSyscall:
	    int result = handleSyscall(processor.readRegister(Processor.regV0),
				       processor.readRegister(Processor.regA0),
				       processor.readRegister(Processor.regA1),
				       processor.readRegister(Processor.regA2),
				       processor.readRegister(Processor.regA3)
				       );
	    processor.writeRegister(Processor.regV0, result);
	    processor.advancePC();
	    break;				       
				       
	default:
	    Lib.debug(dbgProcess, "Unexpected exception: " +
		      Processor.exceptionNames[cause]);
	    Lib.assertNotReached("Unexpected exception");
	}
    }
    /** The program this process is running. */
    protected Coff coff;
    /** Page table of this process. */
    protected TranslationEntry[] pageTable;
    /** # contiguous pages occupied */
    protected int numPages;
    /** The number of pages in the program's stack. */
    protected final int stackPages = 8;   
    private int initialPC, initialSP;
    private int argc, argv;
    private static final int pageSize = Processor.pageSize;
    private static final char dbgProcess = 'a';    
    private final int processID = ++UserProcess.Global_ID;
    private ArrayList<UserProcess> pChildList = new ArrayList<UserProcess>();
    public KThread parent;
    public KThread waiting;
    private OpenFile[] openFiles;
    private int Exit_Status;
    private static int Global_ID = -1;
    protected int virtualMemorySize;
    public final int MAX_SIZE = 256;   
}