package evgeniy.kurinnoy.courseWorkDB;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class QueryType3 extends AppCompatActivity {
    private TextView startDate;
    private TextView endDate;
    private int query = 1;
    private DataBase db;
    private ExpandableHeightGridView  table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_type3);
        getSupportActionBar().setTitle("Запросы");
        db = new DataBase(this);
        table = (ExpandableHeightGridView)findViewById(R.id.computer_table);
        table.setExpanded(true);
        final Button query1 = (Button)findViewById(R.id.query1);
        final Button query2 = (Button)findViewById(R.id.query2);
        User user = User.get();
        if (user.getAccessRight() == User.NORMAL){
            query2.setActivated(false);
            query2.setVisibility(View.INVISIBLE);
            query2.setHeight(0);
        }
        query1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = 1;
            }
        });

        query2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = 2;
            }
        });
        startDate = (TextView)findViewById(R.id.startDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });
        endDate = (TextView)findViewById(R.id.endDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(2);
            }
        });

    }

    private Calendar currtime = Calendar.getInstance();
    private int firstYear = currtime.get(Calendar.YEAR);
    private int firstMonth = currtime.get(Calendar.MONTH);
    private int firstDay = currtime.get(Calendar.DAY_OF_MONTH);
    private int secondYear = currtime.get(Calendar.YEAR);
    private int secondMonth = currtime.get(Calendar.MONTH);
    private int secondDay = currtime.get(Calendar.DAY_OF_MONTH);
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, firstYear, firstMonth, firstDay);
            return tpd;
        }
        else if (id == 2){
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack2, secondYear, secondMonth, secondDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }
    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            firstYear = year;
            firstMonth = monthOfYear+1;
            firstDay = dayOfMonth;
            startDate.setText(firstYear + "-" + firstMonth + "-" + firstDay);
        }
    };
    DatePickerDialog.OnDateSetListener myCallBack2 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            secondYear = year;
            secondMonth = monthOfYear+1;
            secondDay = dayOfMonth;
            endDate.setText(secondYear + "-" + secondMonth + "-" + secondDay);
        }
    };


    public void okButtonClick(View view) {
        long secondDate;
        long firstDate;
        try {
            firstDate = java.sql.Date.valueOf(startDate.getText().toString()).getTime();
            secondDate = java.sql.Date.valueOf(endDate.getText().toString()).getTime();
        }
        catch (IllegalArgumentException e){
            Toast toast = Toast.makeText(this, "Некорректная дата", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        table.setAdapter(null);
        SQLiteDatabase db = this.db.getReadableDatabase();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        if (query ==1 ){
            table.setNumColumns(4);
            adapter.addAll("№каб", "№раб. места", "название", "конец лицензии");
            Cursor cursor = db.rawQuery("select R.numRoom as room, " +
                    " WP.numWP as numWP, " +
                    " TS.name as name, " +
                    " S.endOfLicense as license" +
                    " from Software as S" +
                    " inner join TypeSoftware as TS" +
                    " inner join Computer as C" +
                    " inner join WorkPlace as WP" +
                    " inner join Room as R" +
                    " on S.idTSW = TS.id" +
                    " and S.idComp = C.id" +
                    " and WP.idComp = C.id" +
                    " and WP.idRoom = R.id" +
                    " where S.endOfLicense BETWEEN " + firstDate +" and " + secondDate, null);
            if (cursor.moveToFirst()){
                int room = cursor.getColumnIndex("room");
                int numWP = cursor.getColumnIndex("numWP");
                int name = cursor.getColumnIndex("name");
                int license = cursor.getColumnIndex("license");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                do {
                    adapter.add(cursor.getString(room));
                    adapter.add(cursor.getString(numWP));
                    adapter.add(cursor.getString(name));
                    adapter.add(dateFormat.format(cursor.getLong(license)));
                }while(cursor.moveToNext());
            }
            else{
                Toast toast = Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if (query == 2){
            table.setNumColumns(3);
            adapter.addAll("id", "Имя", "Фамилия");
            Cursor cursor = db.rawQuery("select distinct E.id as id, " +
                    " E.name as name," +
                    " E.surname as surname" +
                    " from Journal as J " +
                    " inner join User as U " +
                    " inner join Employee as E" +
                    " on J.idUser = U.id and U.idEmpl = E.id" +
                    " where J.entryDate BETWEEN " + firstDate + " and " + secondDate, null);
            if (cursor.moveToFirst()){
                int name = cursor.getColumnIndex("name");
                int surname = cursor.getColumnIndex("surname");
                int id = cursor.getColumnIndex("id");
                do {
                    adapter.add(cursor.getString(id));
                    adapter.add(cursor.getString(name));
                    adapter.add(cursor.getString(surname));
                }while(cursor.moveToNext());
            }
            else{
                Toast toast = Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        table.setAdapter(adapter);
    }
}
