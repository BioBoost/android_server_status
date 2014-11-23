package be.vives.nico.serverstatus;

public abstract class Target {
	
	private long id;	// For database

	// Uri can be IP address, host name, website url
	private String uri;
	private TargetStats stats;
	
	public Target(String uri) {
		this.setUri(uri);
	}
	
	public void setUri(String uri) {
		this.uri = uri;
		this.stats = new TargetStats();
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public TargetStats getStats() {
		return this.stats;
	}
	
	public String getFailedStatusReport() {
		return this.getUri() + " failed to respond " + this.getStats().getSubsequentFails()
				+ " times in a row.";
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return "{" + this.getId() + "}[" + this.getUri() + "] => " + this.getStats().toString();
	}
	
	abstract Boolean doStatusCheck() throws Exception;
}
