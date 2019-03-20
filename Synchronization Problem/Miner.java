import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

/**
 * Class for a miner.
 */
public class Miner extends Thread {

	public static final int ENTRANCE = -1;
	private static Set<Integer> solvedRooms;
	private static Integer numberHashing;
	
	private CommunicationChannel channel;
	
	public Miner(Integer hashCount, Set<Integer> solved, CommunicationChannel channel) {
		Miner.numberHashing = hashCount;
		Miner.solvedRooms = solved;
		this.channel = channel;
	}
	
	private static String solvepuzzle(String input) {
        String hashed = input;
        for (int i = 0; i < numberHashing; ++i) {
            hashed = encrypt(hashed);
        }
        return hashed;
    }

    private static String encrypt(String input) {
        try {
            byte[] messageDigest = MessageDigest.getInstance("SHA-256")
            									.digest(input.getBytes(StandardCharsets.UTF_8));
            
            StringBuffer hexString = new StringBuffer();
            String hex = null;
            
            for (int index = 0; index < messageDigest.length; ++index) {
            	hex = Integer.toHexString(0xff & messageDigest[index]);
            	if(hex.length() == 1) 
            		hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
    
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
	@Override
	public void run() {
		Message arrivedTask = null;
		while (true) {
			arrivedTask = channel.getMessageWizardChannel();
			
			if (arrivedTask == null) continue;
			if (arrivedTask.getData().equals(Wizard.END)) continue;
			if (arrivedTask.getData().equals(Wizard.EXIT)) break;
			
			if (!solvedRooms.contains(arrivedTask.getCurrentRoom())) {
				if (arrivedTask.getParentRoom() == ENTRANCE) {
					channel.putMessageMinerChannel(
							new Message(
									arrivedTask.getCurrentRoom(), 
									solvepuzzle(arrivedTask.getData())
									)
							);
				}
				else {
					channel.putMessageMinerChannel(
							new Message(
									arrivedTask.getParentRoom(), 
									arrivedTask.getCurrentRoom(), 
									solvepuzzle(arrivedTask.getData())
									)
							);
				}

				synchronized (solvedRooms) {
					solvedRooms.add(arrivedTask.getCurrentRoom());
				}
			}
		}
	}
}
