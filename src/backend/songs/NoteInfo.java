package backend.songs;

public class NoteInfo {

	private int value;
	private String name;
	
	private NoteInfo(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public static NoteInfo of(int value, String name) {
		return new NoteInfo(value, name);
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
}
