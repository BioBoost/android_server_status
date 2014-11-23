package be.vives.nico.serverstatus;

import android.provider.BaseColumns;

public final class ServerStatusContract {
	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	public ServerStatusContract() {}

	/* Inner class that defines the table contents */
	public static abstract class TargetEntry implements BaseColumns {
		public static final String TABLE_NAME = "targets";
		public static final String COLUMN_NAME_ID = "_id";
		public static final String COLUMN_NAME_URI = "uri";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_SUCCESSES = "successes";
		public static final String COLUMN_NAME_FAILS = "fails";
		public static final String COLUMN_NAME_SUBSEQUENT_FAILS = "subsequent_fails";
	}
}
