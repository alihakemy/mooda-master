package dataInLists;

import java.io.Serializable;

public class DataInUser implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DataInUser() {
		super();
	}


	public boolean success;
	public int code;
	public String message;
	public String message_en;
	public String message_ar;
	public Data data = new Data();

	public class Data {
		public int id;
		public DataInLogin.Tokens token = new DataInLogin.Tokens();
		public String user_name;
		public String name;
		public String phone;
		public String email;
		public String phone_verified_at;
		public String password;
		public String fcm_token;
		public String verification_code;
		public String my_balance;
		public float free_balance;
		public float payed_balance;
		public int my_fav;
		public byte verified;
		public byte seen;
		public byte active;
		public int my_ads;
		public String image;
		public String updated_at;
		public String created_at;
	}

}
