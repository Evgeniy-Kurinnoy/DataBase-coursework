package evgeniy.kurinnoy.courseWorkDB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.Date;

public class DataBase extends SQLiteOpenHelper{
    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;

    public DataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table User("
                + "id integer primary key autoincrement,"
                + "login text not null unique, " +
                " password text not null,"
                + "access integer, " +
                " idEmpl integer, " +
                "foreign key (idEmpl) references Employee(id));");

        db.execSQL("create table Employee("
                + "id integer primary key autoincrement,"
                + "name text, " +
                " surname text, " +
                " phone text,"
                + "idWP integer, " +
                " idPos integer," +
                "foreign key (idWP) references WorkPlace(id)," +
                "foreign key (idPos) references Position(id));");

        db.execSQL("create table Position("
                + "id integer primary key autoincrement,"
                + "name text);");

        db.execSQL("create table WorkPlace("
                + "id integer primary key autoincrement,"
                + "numWP integer not null, " +
                " idRoom integer not null," +
                " idComp integer not null, " +
                "foreign key (idRoom) references Room(id)," +
                "foreign key (idComp) references Computer(id));");

        db.execSQL("create table Room("
                + "id integer primary key autoincrement,"
                + "numRoom integer not null unique, " +
                " area integer);");

        db.execSQL("create table Computer("
                + "id integer primary key autoincrement,"
                + "inventoryNum integer unique not null, " +
                " idCS integer," +
                "foreign key (idCS) references CompSpec(id));");

        db.execSQL("create table CompSpec("
                + "id integer primary key autoincrement,"
                + "marka text, " +
                " model text," +
                " ROM integer, " +
                " RAM integer," +
                " idCPU integer," +
                " idGPU integer," +
                "foreign key (idCPU) references CPU(id)," +
                "foreign key (idGPU) references GPU(id));");

        db.execSQL("create table CPU(" +
                " id integer primary key autoincrement, " +
                " marka text, " +
                " model text, " +
                " freq real, " +
                " core integer);");

        db.execSQL("create table GPU(id integer primary key autoincrement," +
                " marka text, " +
                " model text);");

        db.execSQL("create table Equip(id integer primary key autoincrement, " +
                "inventoryNum integer unique, " +
                "idComp integer, " +
                "idTE integer," +
                "foreign key (idComp) references Computer(id)," +
                "foreign key (idTE) references TypeEquip(id));");

        db.execSQL("create table TypeEquip(id integer primary key autoincrement, " +
                "name text, " +
                "marka text, " +
                "model text);");

        db.execSQL("create table PosTypeSW(" +
                "idPos integer not null," +
                "idTSW integer not null, " +
                "constraint new_key primary key(idPos, idTSW));");

        db.execSQL("create table TypeSoftware(" +
                "id integer primary key autoincrement," +
                "name text, " +
                "version text);");

        db.execSQL("create table Software(" +
                "id integer primary key autoincrement," +
                "endOfLicense integer," +
                "idTSW integer not null," +
                "idComp integer not null," +
                "foreign key (idComp) references Computer(id)," +
                "foreign key (idTSW) references TypeSoftware(id));");

        db.execSQL("create table Journal(" +
                "id integer primary key autoincrement," +
                "entryDate integer, " +
                "idUser integer, " +
                "act text," +
                "foreign key (idUser) references User(id));");


        ContentValues values = new ContentValues();
        values.put("name", "Администратор");
        long idPos = db.insert("Position", null, values);

        values.clear();
        values.put("name", "Евгений");
        values.put("surname", "Куринной");
        values.put("idPos", idPos);
        long idEmpl = db.insert("Employee", null, values);

        values.clear();
        values.put("login", "admin");
        values.put("password", "admin");
        values.put("access", User.ADMIN);
        values.put("idEmpl", idEmpl);
        db.insert("User", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public User login(String login, String password) {
        User user = User.get();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        cursor = db.query("User as U " +
                        "inner join Employee as E " +
                        "inner join Position as P " +
                        "on U.idEmpl = E.id " +
                        "AND E.idPos = P.id",
                        new String[]{"E.name as name", "E.surname as surname", "U.access as access", "P.name as position", "U.id as id"},
                "U.login = ? " +
                        "AND U.password = ? ",
                new String[]{login, password},
                null, null, null);
        if (cursor.moveToFirst()){
            user.setFirstName(cursor.getString(cursor.getColumnIndex("name")));
            user.setLastName(cursor.getString(cursor.getColumnIndex("surname")));
            user.setAccessRight(cursor.getInt(cursor.getColumnIndex("access")));
            user.setPost(cursor.getString(cursor.getColumnIndex("position")));
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
        }
        else{
            return null;
        }
        return user;
    }

    public void putInJournal(String action){
        User user = User.get();
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("idUser", user.getId());
        Date date = Calendar.getInstance().getTime();
        cv.put("entryDate", date.getTime());
        cv.put("act", action);
        database.insert("Journal", null, cv);
    }
}
