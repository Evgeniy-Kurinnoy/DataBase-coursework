package evgeniy.kurinnoy.courseWorkDB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class QueryType2 extends AppCompatActivity {
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
        Button query1 = (Button)findViewById(R.id.query1);
        Button query2 = (Button)findViewById(R.id.query2);
        query1.setText("Найти сотрудников, которые работают за компьютерами с процессором модели Х");
        query1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = 1;
                input.setHint("начало названия модели процессора");
            }
        });
        query2.setText("Найти ПО, название которых начинается на:");
        query2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = 2;
                input.setHint("начало названия ПО");
            }
        });
        input = (AutoCompleteTextView) findViewById(R.id.input_x);
        input.setHint("начало названия модели процессора");
        input.setInputType(View.AUTOFILL_TYPE_TEXT);
    }

    public void okButtonClick(View view) {
        table.setAdapter(null);
        SQLiteDatabase db = this.db.getReadableDatabase();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        if (query ==1 ){
            table.setNumColumns(2);
            adapter.addAll("Имя", "Фамилия");
            Cursor cursor = db.rawQuery("select E.name as name, E.surname as surname" +
                    " from Employee as E" +
                    " inner join WorkPlace as WP" +
                    " inner join Computer as C" +
                    " inner join CompSpec as CS" +
                    " inner join CPU as cpu" +
                    " on E.idWP = WP.id" +
                    " and WP.idComp = C.id" +
                    " and C.idCS = CS.id" +
                    " and CS.idCPU = cpu.id" +
                    " where cpu.model like '"+ input.getText().toString() +"%'", null);
            if (cursor.moveToFirst()){
                int name = cursor.getColumnIndex("name");
                int surname = cursor.getColumnIndex("surname");
                do {
                    adapter.add(cursor.getString(name));
                    adapter.add(cursor.getString(surname));
                }while(cursor.moveToNext());
            }
            else{
                Toast toast = Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if (query == 2){
            table.setNumColumns(3);
            adapter.addAll("Название", "Версия", "Конец лицензии");
            Cursor cursor = db.rawQuery("select TSW.name as name," +
                    " TSW.version as version," +
                    " S.endOfLicense as license" +
                    " from Software as S " +
                    " inner join TypeSoftware as TSW" +
                    " on S.idTSW = TSW.id" +
                    " where TSW.name like '" + input.getText().toString() + "%'", null);
            if (cursor.moveToFirst()){
                int name = cursor.getColumnIndex("name");
                int version = cursor.getColumnIndex("version");
                int license = cursor.getColumnIndex("license");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                do {
                    adapter.add(cursor.getString(name));
                    adapter.add(cursor.getString(version));
                    adapter.add(dateFormat.format(cursor.getLong(license)));
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
