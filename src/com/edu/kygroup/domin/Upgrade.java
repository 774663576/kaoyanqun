package com.edu.kygroup.domin;

import java.io.Serializable;

public class Upgrade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String needupgrade;
	private Version newversion;
	
	
	public String getNeedupgrade() {
		return needupgrade;
	}


	public void setNeedupgrade(String needupgrade) {
		this.needupgrade = needupgrade;
	}


	public Version getNewversion() {
		return newversion;
	}


	public void setNewversion(Version newversion) {
		this.newversion = newversion;
	}


	public class Version implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private String description;
		private String flag;
		private String size;
		private String url;
		private String version;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getFlag() {
			return flag;
		}
		public void setFlag(String flag) {
			this.flag = flag;
		}
		public String getSize() {
			return size;
		}
		public void setSize(String size) {
			this.size = size;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
	}
	
}
