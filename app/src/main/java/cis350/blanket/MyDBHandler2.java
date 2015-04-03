package cis350.blanket;

/**
 * Created by aashishlalani on 4/2/15.
 */
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class MyDBHandler2 extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fbPersonDB.db";
    private static final String TABLE_PERSONS = "persons";
    private static final String TABLE_GIFT = "gifts";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FULLNAME = "fullname";
    public static final String COLUMN_DONATIONAMOUNT = "donationamount";
    public static final String COLUMN_PIN = "pin";
    public static final String COLUMN_CCNUMBER = "ccnumber";
    public static final String COLUMN_DONATIONINFO = "donationinfo";
    public static final String COLUMN_GIFT = "gift";

    public MyDBHandler2(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PERSONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_FULLNAME
                + " TEXT," + COLUMN_DONATIONAMOUNT + " DOUBLE, " + COLUMN_PIN + " TEXT,"
                + COLUMN_CCNUMBER + " TEXT," + COLUMN_DONATIONINFO + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONS);
        onCreate(db);
    }

    public void addPerson(fbPerson person) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_FULLNAME, person.getPersonName());
        values.put(COLUMN_DONATIONAMOUNT, person.getDonationAmount());
        values.put(COLUMN_PIN, person.getPin());
        values.put(COLUMN_CCNUMBER, person.getCcNumber());
        values.put(COLUMN_DONATIONINFO, person.getDonationInfo());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PERSONS, null, values);
        db.close();
    }

    public fbPerson findProduct(String personName) {
        String query = "Select * FROM " + TABLE_PERSONS + " WHERE " +
                COLUMN_FULLNAME + " =  \"" + personName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        fbPerson product = new fbPerson();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setDonationAmount(Integer.parseInt(cursor.getString(1)));
            product.setPin(cursor.getString(2));
            product.setCcNumber(cursor.getString(3));
            product.setDonationInfo(cursor.getString(4));
            cursor.close();
        } else {
            product = null;
        }
        db.close();
        return product;
    }

    public boolean deleteProduct(String personName) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PERSONS + " WHERE " + COLUMN_FULLNAME +
                " =  \"" + personName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        fbPerson product = new fbPerson();

        if (cursor.moveToFirst()) {
            product.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PERSONS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(product.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

}
