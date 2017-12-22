package evgeniy.kurinnoy.courseWorkDB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class QueryType6 extends AppCompatActivity {
    private AutoCompleteTextView input;
    private int query = 1;
    private DataBase db;
    private ExpandableHeightGridView  table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_type1);
        getSupportActionBar().setTitle("Запросы");
        db = new DataBase(this);
        table = (ExpandableHeightGridView)findViewById(R.id.computer_table);
        table.setExpanded(true);
        final Button query1 = (Button)findViewById(R.id.query1);
        final Button query2 = (Button)findViewById(R.id.query2);
        final Button okbutton = (Button) findViewById(R.id.button10);
        okbutton.setVisibility(View.INVISIBLE);
        okbutton.setActivated(false);
        query1.setText("Какой кабинет имеет наиболее количество рабочих мест");
        query1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = 1;
                okButtonClick(query1);
            }
        });
        query2.setText("На каком компьютере установлено больше всего программ");
        query2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = 2;
                okButtonClick(query2);
            }
        });
        input = (AutoCompleteTextView) findViewById(R.id.input_x);
        input.setActivated(false);
        input.setVisibility(View.INVISIBLE);
    }

    public void okButtonClick(View view) {
        table.setAdapter(null);
        SQLiteDatabase db = this.db.getReadableDatabase();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        if (query ==1 ){
            table.setNumColumns(2);
            Date date = Calendar.getInstance().getTime();
            adapter.addAll("Кабинет №", "число раб мест");
            Cursor cursor = db.rawQuery("select max(N.c) as max1," +
                    " N.num as Nnum " +
                    " from (select count(*) as c, " +
                    " R.numRoom as num" +
                    " from Room as R, " +
                    " WorkPlace as WP" +
                    " on  WP.idRoom = R.id" +
                    " group by R.numRoom) as N", null);
            if (cursor.moveToFirst()){
                int count = cursor.getColumnIndex("max1");
                int invNum = cursor.getColumnIndex("Nnum");
                do {
                    adapter.add(cursor.getString(invNum));
                    adapter.add(cursor.getString(count));
                }while(cursor.moveToNext());
            }
            else{
                Toast toast = Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if (query == 2){
            table.setNumColumns(2);
            adapter.addAll("номер компьютера", "количество программ");
            Cursor cursor = db.rawQuery("select max(N.c) as max1, " +
                    " N.num as Nnum " +
                    " from (select count(*) as c," +
                    " C.inventoryNum as num" +
                    " from Computer as C, " +
                    " Software as S" +
                    " on  S.idComp = C.id" +
                    " group by C.inventoryNum) as N", null);
            if (cursor.moveToFirst()){
                int max = cursor.getColumnIndex("max1");
                int nnum = cursor.getColumnIndex("Nnum");
                do {
                    adapter.add("" + cursor.getString(nnum));
                    adapter.add("" + cursor.getInt(max));
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
