package readersWriters;

public final class RWLockLive implements ReadWriteLock
{
	private int readingReaders=0;
	private int writingWriters=0;
	private int waitingWriters=0;
	private boolean isReadersTurn=true;


	public synchronized void acquireRead() throws InterruptedException
	{
		while(this.writingWriters>0 || (!this.isReadersTurn && this.waitingWriters>0))
		{
			this.wait();
		}
		++this.readingReaders;
	}


	public synchronized void releaseRead()
	{
		--this.readingReaders;
		this.isReadersTurn=false;
		this.notifyAll();
	}


	public synchronized void acquireWrite() throws InterruptedException
	{
		++this.waitingWriters;
		while(this.readingReaders>0 || this.writingWriters>0)
		{
			this.wait();
		}
		--this.waitingWriters;
		++this.writingWriters;
	}

	public synchronized void releaseWrite()
	{
		--this.writingWriters;
		this.isReadersTurn=true;
		this.notifyAll();
	}
}
