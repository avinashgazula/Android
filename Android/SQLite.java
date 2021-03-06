

//create a class for the table  ex- bands

public class Bands {

    private int _id;
    private String _bandName;

    public Bands(){
    }

    public Bands(String bandName) {
        this._bandName = bandName;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_bandName(String _bandName) {
        this._bandName = _bandName;
    }

    public int get_id() {
        return _id;
    }

    public String get_bandName() {
        return _bandName;
    }
}

//Create a database handler for all the sql operations

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MyDBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
	//name of the database
    public static final String DATABASE_NAME = "bands.db";
	//name of the table
    public static final String TABLE_NAME = "Bands";
    //constant for every single column in the table
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_BAND = "bandName";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        //name is the name of the database and version is database version
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //Adding a new row
    public void addRow(Bands band){
        ContentValues values = new ContentValues();
        values.put(COLUMN_BAND, band.get_bandName());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //Deleting a row
    public void deleteRow(String band){
        SQLiteDatabase db = getWritableDatabase();
        String query = " DELETE FROM "+ TABLE_NAME +" WHERE "+ COLUMN_BAND +" = \""+ band +"\";";
        db.execSQL(query);
    }

    //Converting database to a string
    public String convertDatabaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE 1 ";

        //Cursor to a database
        Cursor c = db.rawQuery(query, null);
        //move to the first position
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_BAND))!= null)
                dbString += c.getString(c.getColumnIndex(COLUMN_BAND)) + "\n";
            c.moveToNext();
        }
        db.close();
        return dbString;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //When the database is called a table is created
        String query = "CREATE TABLE "+ TABLE_NAME +" ( "
                + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ COLUMN_BAND + " TEXT);";
        //Executes the SQL Query
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME );
        onCreate(db);

    }
}


//To access MyDBHandler in main activity

MyDBHandler dbHandler;
dbHandler = new MyDBHandler(this , "", null, 1);

dbHandler.addRow(band);

dbHandler.deleteRow(bandName);


