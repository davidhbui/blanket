package cis350.blanket;

/**
 * Created by aashishlalani on 4/2/15.
 */
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "panhandlerDB.db";
    private static final String TABLE_PERSONS = "persons";
    private static final String TABLE_GIFT = "gifts";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PERSONNAME = "personname";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String[] COLUMN_GIFT = {"gift"};

    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PERSONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PERSONNAME
                + " TEXT," + COLUMN_QUANTITY + " INTEGER, " + COLUMN_GIFT + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONS);
        onCreate(db);
    }

    public void addPerson(Person person) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSONNAME, person.getProductName());
        values.put(COLUMN_QUANTITY, person.getQuantity());
        //values.put(COLUMN_GIFT, person.get_giftBasket());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PERSONS, null, values);
        db.close();
    }

    public Person findProduct(String personName) {
        String query = "Select * FROM " + TABLE_PERSONS + " WHERE " +
                COLUMN_PERSONNAME + " =  \"" + personName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Person product = new Person();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setID(cursor.getString(0));
            product.setProductName(cursor.getString(1));
            product.setQuantity(Integer.parseInt(cursor.getString(2)));
            //product.set_giftBasket(cursor.getString(3));
            cursor.close();
        } else {
            product = null;
        }
        db.close();
        return product;
    }

    public boolean deleteProduct(String personName) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PERSONS + " WHERE " + COLUMN_PERSONNAME +
                " =  \"" + personName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Person product = new Person();

        if (cursor.moveToFirst()) {
            product.setID(cursor.getString(0));
            db.delete(TABLE_PERSONS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(product.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

}