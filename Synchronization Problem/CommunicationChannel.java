import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class CommunicationChannel {
	
	public static final int SIZE = 100000;
	private static ReentrantLock wizardLocker = new ReentrantLock();
	
	private static ArrayBlockingQueue<Message> minersMessageQueue  = new ArrayBlockingQueue<>(SIZE);
	private static ArrayBlockingQueue<Message> wizardsMessageQueue = new ArrayBlockingQueue<>(SIZE);
	
	public CommunicationChannel() {}

	public void putMessageMinerChannel(Message message) {
		try {
			minersMessageQueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Message getMessageMinerChannel() {
		Message message = null;
		try {
				message = minersMessageQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return message;
	}

	public void putMessageWizardChannel(Message message) {
		wizardLocker.lock();
		try {
			wizardsMessageQueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(message.getData().equals(Wizard.END)) {
				wizardLocker.unlock();
			}
		}
	}

	public synchronized Message getMessageWizardChannel() {
		Message message = null;
		int previous;
		try {
			message = wizardsMessageQueue.take();
			if (message.getData() != Wizard.END && message.getData() != Wizard.EXIT) {
				previous = message.getCurrentRoom();
				message = wizardsMessageQueue.take();
				message.setParentRoom(previous);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return message;
	}
}
