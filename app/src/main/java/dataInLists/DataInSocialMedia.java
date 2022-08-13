package dataInLists;

public class DataInSocialMedia {

	public DataInSocialMedia() {
		super();
	}


	public boolean success;
	public int code;
	public String message_en;
	public String message_ar;
	public Data data = new Data();

	public class Data {
		public int id;
		public String instegram;
		public String twitter;
		public String facebook;
		public String snapchat;
		public String image;
	}
}
