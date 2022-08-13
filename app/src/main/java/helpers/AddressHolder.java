package helpers;

public class AddressHolder {
	private int FaceID;

	public int getData() {
		return FaceID;
	}

	public void setData(int data) {
		this.FaceID = data;
	}

	private static final AddressHolder holder = new AddressHolder();

	public static AddressHolder getInstance() {
		return holder;
	}

}
