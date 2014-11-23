package be.vives.nico.serverstatus;

public class TargetHost extends Target {

	public TargetHost(String uri) {
		super(uri);
	}

	@Override
	Boolean doStatusCheck() throws Exception {
		// Hosts get pinged
		return (PingTool.pingHost(this.getUri()) == 0);
	}
	
	@Override
	public String getFailedStatusReport() {
		return this.getUri() + " failed to respond " + this.getStats().getSubsequentFails()
				+ " times in a row to a ping request.";
	}
	
	@Override
	public String toString() {
		return "[" + this.getUri() + "] => " + this.getStats().toString();
	}
}
