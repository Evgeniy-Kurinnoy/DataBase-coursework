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

public class QueryType5 extends AppCompatActivity {
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
        query1.setText("Количество ПО на которых закончилась лицензия для каждого компьютера");
        query1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = 1;
                okButtonClick(query1);
            }
        });
        query2.setText("Сколько установлено Экземпляров ПО каждого типа");
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
            adapter.addAll("Номер компьютера", "Количество ПО");
            Cursor cursor = db.rawQuery("select count(*)," +
                    " C.inventoryNum as num" +
                    " from Software as S, " +
                    " Computer as C" +
                    " on  S.idComp = C.id" +
                    " where endOfLicense < " + date.getTime() +
                    " group by C.inventoryNum", null);
            if (cursor.moveToFirst()){
                int count = cursor.getColumnIndex("count(*)");
                int invNum = cursor.getColumnIndex("num");
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
            adapter.addAll("Название ПО", "количество");
            Cursor cursor = db.rawQuery("select count(*), " +
                    " TS.name as name" +
                    " from Software as S, " +
                    " TypeSoftware as TS" +
                    " on S.idTSW = TS.id" +
                    " group by TS.name", null);
            if (cursor.moveToFirst()){
                int count = cursor.getColumnIndex("count(*)");
                int name = cursor.getColumnIndex("name");
                do {
                    adapter.add("" + cursor.getString(name));
                    adapter.add("" + cursor.getInt(count));
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
