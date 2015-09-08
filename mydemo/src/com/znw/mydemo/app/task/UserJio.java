package com.znw.mydemo.app.task;

import com.znw.mydemo.parse.jio.BasicJio;

public class UserJio extends BasicJio {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserEntity userEntity;


	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public static class UserEntity implements java.io.Serializable {
		private static final long serialVersionUID = 1L;
		public String cname;
		public String avatar;
		public String nameCode;
	}
}
