package helpers;

public class UpdateHolder {
	private boolean FaceID;

	public boolean getData() {
		return FaceID;
	}

	public void setData(boolean data) {
		this.FaceID = data;
	}

	private static final UpdateHolder holder = new UpdateHolder();

	public static UpdateHolder getInstance() {
		return holder;
	}

}
