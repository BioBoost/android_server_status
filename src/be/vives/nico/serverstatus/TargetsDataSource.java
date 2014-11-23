package be.vives.nico.serverstatus;

import java.util.ArrayList;

import be.vives.nico.serverstatus.ServerStatusContract.TargetEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TargetsDataSource {
	
	// Database fields
	private SQLiteDatabase database;
	private ServerStatusDbHelper dbHelper;
	
	private String[] allColumns = {
			TargetEntry.COLUMN_NAME_ID,
			TargetEntry.COLUMN_NAME_URI,
			TargetEntry.COLUMN_NAME_TYPE,
			TargetEntry.COLUMN_NAME_SUCCESSES,
			TargetEntry.COLUMN_NAME_FAILS,
			TargetEntry.COLUMN_NAME_SUBSEQUENT_FAILS
	};
	
	public TargetsDataSource(Context context) {
		dbHelper = new ServerStatusDbHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Target insertTarget(Target target) {			
		// Add fields of target to content values
		ContentValues values = new ContentValues();
		values.put(TargetEntry.COLUMN_NAME_URI, target.getUri());
		values.put(TargetEntry.COLUMN_NAME_TYPE, target.getClass().getSimpleName().toString());
		values.put(TargetEntry.COLUMN_NAME_SUCCESSES, target.getStats().getSuccesses());
		values.put(TargetEntry.COLUMN_NAME_FAILS, target.getStats().getFails());
		values.put(TargetEntry.COLUMN_NAME_SUBSEQUENT_FAILS, target.getStats().getSubsequentFails());
		
		Log.v("Database", "Trying to store: " + values.toString());

		// Insert the new row, returning the primary key value of the new row
		long insertId = database.insert(
				TargetEntry.TABLE_NAME,
				TargetEntry.COLUMN_NAME_SUCCESSES,		// Column that can be null
		        values);

		// Get the new target from the db
		return this.getTarget(insertId);
	}
	
	public Target saveTarget(Target target) {
		
		if (target.getId() != -1) {	// Target already present in database
			
			// Add fields of target to content values
			ContentValues values = new ContentValues();
			values.put(TargetEntry.COLUMN_NAME_URI, target.getUri());
			values.put(TargetEntry.COLUMN_NAME_TYPE, target.getClass().getSimpleName().toString());
			values.put(TargetEntry.COLUMN_NAME_SUCCESSES, target.getStats().getSuccesses());
			values.put(TargetEntry.COLUMN_NAME_FAILS, target.getStats().getFails());
			values.put(TargetEntry.COLUMN_NAME_SUBSEQUENT_FAILS, target.getStats().getSubsequentFails());
			
			Log.v("Database", "Trying to update: " + values.toString());

			// Update the new row, returning the number of affected rows
			int affectedRows = database.update(
				TargetEntry.TABLE_NAME,					
		        values,
		        TargetEntry.COLUMN_NAME_ID + " = " + target.getId(),
		        null
			);

			if (affectedRows == 1) {
				return this.getTarget(target.getId());
			} else {
				return null;
			}
			
		} else {	// Target needs to be inserted
			return insertTarget(target);
		}
	}

	public void deleteTarget(Target target) {
		long id = target.getId();
		Log.v("Database", "Target deleted with id: " + id);
		database.delete(TargetEntry.TABLE_NAME, TargetEntry.COLUMN_NAME_ID + " = " + id, null);
	}

	public Target getTarget(long id) {
		Cursor cursor = database.query(TargetEntry.TABLE_NAME, allColumns,
				TargetEntry.COLUMN_NAME_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		
		// Get the new target from the db
		Target newTarget;
		try {
			newTarget = cursorToTarget(cursor);
			Log.v("Database", "Created new target in db: " + newTarget.toString());
		} catch (Exception e) {
			Log.d("Database", e.getMessage());
			newTarget = null;
		} finally {
			cursor.close();
		}
		return newTarget;
	}
	
	public ArrayList<Target> getAllTargets() {
		ArrayList<Target> targets = new ArrayList<Target>();

		// Get all target records and add them to the list
		Cursor cursor = database.query(TargetEntry.TABLE_NAME, allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			try {
				Target target = cursorToTarget(cursor);
				targets.add(target);
			} catch (Exception e) {
				Log.d("Database", e.getMessage());
			}
			cursor.moveToNext();
		}
		
		// Make sure to close the cursor
		cursor.close();
		return targets;
	}
	
	public void deleteAllTargets() {
		Log.v("Database", "Deleting all target records");
		database.delete(TargetEntry.TABLE_NAME, TargetEntry.COLUMN_NAME_ID + " > " + 0, null);
	}

	private Target cursorToTarget(Cursor cursor) throws Exception {
		// TODO Column indications need to be refactored
		Target target;
		
		if (cursor.getString(2).equals(TargetHost.class.getSimpleName().toString())) {
			target = new TargetHost(cursor.getString(1));
		} else {
			throw new Exception("Unknown target type specicified in database");
		}
		
		target.setId(cursor.getLong(0));
		target.getStats().setStats(cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
		
		return target;
	}
}
