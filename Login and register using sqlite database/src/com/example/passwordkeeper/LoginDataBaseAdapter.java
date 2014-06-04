package com.example.passwordkeeper;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class LoginDataBaseAdapter {

	static final String DATABASE_NAME = "login.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 3;
	static final String SCAN_NAME="scantext";
	
	/* private static final String TABLE_LOGIN = "LOGIN";
	 private static final String KEY_ID = "id";
	    private static final String KEY_PASSWORD = "Password";
	    private static final String KEY_REPASSWORD = "Repassword";
	    private static final String KEY_SECURITYHINT = "Securityhint";*/
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	
	
	static final String DATABASE_CREATE = "create table "+"LOGIN"+
			"( " +"ID integer primary key autoincrement,"+ "PASSWORD  text,"+"REPASSWORD text,"+ "SECURITYHINT text) ";
	
	/*String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
			+ KEY_ID + " INTEGER PRIMARY KEY autoincrement," + KEY_PASSWORD
			+ " TEXT not null," + KEY_REPASSWORD + " TEXT not null," +KEY_SECURITYHINT+ " TEXT not null)";*/
		//db.execSQL(CREATE_CONTACTS_TABLE);
	
	
	// Variable to hold the database instance
	public  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public  LoginDataBaseAdapter(Context _context) 
	{
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public  LoginDataBaseAdapter open() throws SQLException 
	{
		db = dbHelper.getWritableDatabase();
		return this;
	}
	public void close() 
	{
		db.close();
	}

	public  SQLiteDatabase getDatabaseInstance()
	{
		return db;
	}

	public void insertEntry(String password,String repassword,String securityhint)
	{
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		newValues.put("PASSWORD", password);
		newValues.put("REPASSWORD",repassword);
		newValues.put("SECURITYHINT",securityhint);

		// Insert the row into your table
		db.insert("LOGIN", null, newValues);
		///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
	}
	public int deleteEntry(String password)
	{
		//String id=String.valueOf(ID);
		String where="PASSWORD=?";
		int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{password}) ;
		// Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
		return numberOFEntriesDeleted;
	}	
	public String getSinlgeEntry(String password)
	{
		//String password = null;
		Cursor cursor=db.query("LOGIN", null, " PASSWORD=?", new String[]{password}, null, null, null);
		if(cursor.getCount()<1) // UserName Not Exist
		{
			cursor.close();
			return "NOT EXIST";
		}
		cursor.moveToFirst();
		String repassword= cursor.getString(cursor.getColumnIndex("REPASSWORD"));
		cursor.close();
		return repassword;				
	}
	public String getpassword(String security)
	{
		String selectQuery = "SELECT  * FROM " + " LOGIN " + " WHERE "+ " SECURITYHINT = " +security;
		Cursor myCursor = db.rawQuery(selectQuery, null);
		
		//Cursor myCursor=db.query("LOGIN", null, " SECURITYHINT=?", new String[]{security}, null, null, null);
		if(myCursor.getCount()<1) // UserName Not Exist
		{
			myCursor.close();
			return "NOT EXIST";
		}
		myCursor.moveToFirst();
		String SEc = myCursor.getString(myCursor.getColumnIndex("SECURITYHINT"));
		String ed ="";

		if(SEc==security)
		{
			ed= myCursor.getString(myCursor.getColumnIndex("REPASSWORD"));
			myCursor.close();
		}
		return ed;	

	}

	public String getAllTags(String a) {

		/*String selectQuery = "SELECT * from  LOGIN WHERE SECURITYHINT=" + a;
		Cursor c = db.rawQuery(selectQuery, null);*/
		
		Cursor c = db.rawQuery("SELECT * FROM " + "LOGIN" + " where SECURITYHINT = '" +a + "'" , null);
		String str = null;
		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				str = c.getString(c.getColumnIndex("PASSWORD"));
			} while (c.moveToNext());
		}
		return str;
	}


	public void  updateEntry(String password,String repassword)
	{
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put("PASSWORD", password);
		updatedValues.put("REPASSWORD",repassword);
		updatedValues.put("SECURITYHINT",repassword);

		String where="USERNAME = ?";
		db.update("LOGIN",updatedValues, where, new String[]{password});			   
	}	



	public HashMap<String, String> getAnimalInfo(String id) {
		HashMap<String, String> wordList = new HashMap<String, String>();
		//SQLiteDatabase database = getReadableDatabase();
		String selectQuery = "SELECT * FROM LOGIN where SECURITYHINT='"+id+"'";
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				//HashMap<String, String> map = new HashMap<String, String>();
				wordList.put("PASSWORD", cursor.getString(1));
				//wordList.add(map);
			} while (cursor.moveToNext());
		}				    
		return wordList;
	}	
}




