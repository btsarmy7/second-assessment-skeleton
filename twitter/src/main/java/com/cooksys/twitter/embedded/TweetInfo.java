package com.cooksys.twitter.embedded;

public class TweetInfo {
	
	private String content;
	private Credentials credentials;
	
	public TweetInfo() {
		
	}

	public TweetInfo(String content, Credentials credentials) {
		this.content = content;
		this.credentials = credentials;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((credentials == null) ? 0 : credentials.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TweetInfo other = (TweetInfo) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (credentials == null) {
			if (other.credentials != null)
				return false;
		} else if (!credentials.equals(other.credentials))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TweetInfo [content=" + content + ", credentials=" + credentials + "]";
	}
	
	
}
