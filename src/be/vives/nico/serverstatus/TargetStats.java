package be.vives.nico.serverstatus;

public class TargetStats {
	
	private int successes;
	private int fails;	
	private int subsequentFails;
	
	public TargetStats() {
		this.clearStats();
	}
	
	public void clearStats() {
		this.fails = 0;
		this.successes = 0;
		this.subsequentFails = 0;
	}
	
	public void incrementSuccesses() {
		this.successes++;
		this.subsequentFails = 0;
	}
	
	public void incrementFails() {
		this.fails++;
		this.subsequentFails++;
	}
	
	public int getSuccesses() {
		return this.successes;
	}
	
	public int getFails() {
		return this.fails;
	}
	
	public int getSubsequentFails() {
		return this.subsequentFails;
	}
	
	public void resetSubsequentFails() {
		this.subsequentFails = 0;
	}
	
	@Override
	public String toString() {
		return "[ " + this.getSuccesses() + " | "
				 + this.getFails() + " | " + this.getSubsequentFails() + " ]";
	}
}
