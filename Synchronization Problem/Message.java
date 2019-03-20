
public class Message {
	private int parentRoom;
	private int currentRoom;
	private String data;

	public Message(int parentRoom, int currentRoom, String data) {
		this.parentRoom = parentRoom;
		this.currentRoom = currentRoom;
		this.data = data;
	}

	public Message(int currentRoom, String data) {
		this.currentRoom = currentRoom;
		this.data = data;
	}

	public int getParentRoom() {
		return parentRoom;
	}

	public void setParentRoom(int parentRoom) {
		this.parentRoom = parentRoom;
	}

	public int getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(int currentRoom) {
		this.currentRoom = currentRoom;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
