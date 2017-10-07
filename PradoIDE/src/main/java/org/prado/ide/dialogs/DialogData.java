package org.prado.ide.dialogs;

/**
 * Dialog data is the data shared between the downloader and progress dialog.
 * The methods are synchronised to gurantee order of download and the progress view 
 * @author anand
 *
 */
public class DialogData {
	
	private int current;
	private String status;
	private boolean ready;
	

	public synchronized void setCurrent(int current) {
		while(ready) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.current = current;
		ready = true;
		notifyAll();		
	}
	
	public synchronized int getCurrent() {
		while(!ready) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		ready = false;
		notifyAll();
		return current;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
	
}
