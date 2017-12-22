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

public class QueryType1 extends AppCompatActivity {
    private DataBase db;
    private ExpandableHeightGridView  table;
    private AutoCompleteTextView input_x;
    private int query = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_type1);
        getSupportActionBar().setTitle("Запросы");
        db = new DataBase(this);
        table = (ExpandableHeightGridView)findViewById(R.id.computer_table);
        table.setExpanded(true);
        input_x = (AutoCompleteTextView) findViewById(R.id.input_x);
        input_x.setAdapter(FillTable.getAdapter(this, "Room", "numRoom"));
        Button query1 = (Button)findViewById(R.id.query1);
        Button query2 = (Button)findViewById(R.id.query2);
        query1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = 1;
                input_x.setAdapter(FillTable.getAdapter(QueryType1.this, "Room", "numRoom"));
                input_x.setHint("номер кабинета");
            }
        });
        query2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = 2;
                input_x.setAdapter(FillTable.getAdapter(QueryType1.this, "Computer", "inventoryNum"));
                input_x.setHint("инвентарный номер");
            }
        });
    }


    public void okButtonClick(View view) {
        table.setAdapter(null);
        SQLiteDatabase db = this.db.getReadableDatabase();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        if (query == 1){

            table.setNumColumns(2);
            String sqlquery = "select E.name as name, E.surname as surname, Room.numRoom as nRoom" +
                    " from Employee as E inner join WorkPlace as WP inner join Room as Room" +
                    " on E.idWP = WP.id and WP.idRoom = Room.id" +
                    " where nRoom = ?";
            Cursor cursor = db.rawQuery(sqlquery, new String[]{input_x.getText().toString()});
            adapter.addAll("Имя", "Фамилия");
            if (cursor.moveToFirst()){
                int name = cursor.getColumnIndex("name");
                int surname = cursor.getColumnIndex("surname");
                do {
                    adapter.addAll(cursor.getString(name), cursor.getString(surname));
                }while(cursor.moveToNext());
            }
            else{
                Toast toast = Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
        else if (query == 2){
            table.setAdapter(null);
            table.setNumColumns(1);
            String sqlquery = "select CS.marka as compMarka," +
                    " CS.model as compModel," +
                    " CS.ROM as rom," +
                    " CS.RAM as ram," +
                    " CPU.marka as cpuMarka," +
                    " CPU.model as cpuModel," +
                    " CPU.freq as freq," +
                    " CPU.core as core," +
                    " GPU.marka as gpuMarka," +
                    " GPU.model as gpuModel," +
                    " E1.TEname as TEname," +
                    " E1.TEmodel as TEmodel," +
                    " E1.TEmarka as TEmarka," +
                    " C.inventoryNum as invNum" +
                    " from GPU as GPU" +
                    " inner join CompSpec as CS" +
                    " inner join CPU as CPU" +
                    " inner join Computer as C" +
                    " inner join (select E.idComp as EidComp," +
                    " TE.name as TEname," +
                    " TE.model as TEmodel," +
                    " TE.marka as TEmarka" +
                    " from Equip as E" +
                    " inner join TypeEquip as TE" +
                    " on E.idTE = TE.id) as E1" +
                    " on C.idCS = CS.id" +
                    " and CS.idCPU = CPU.id" +
                    " and CS.idGPU = GPU.id" +
                    " and C.id = E1.EidComp" +
                    " where C.inventoryNum = ?";
            Cursor cursor = db.rawQuery(sqlquery, new String[]{input_x.getText().toString()});
            adapter.addAll("Характеристики: ");
            if (cursor.moveToFirst()){
               adapter.add("Марка компьютера: " + cursor.getString(cursor.getColumnIndex("compMarka")));
               adapter.add("Модель компьютера: " + cursor.getString(cursor.getColumnIndex("compModel")));
               adapter.add("Оперативная память: " + cursor.getString(cursor.getColumnIndex("ram")) + " Гб");
               adapter.add("Постоянная память: " + cursor.getString(cursor.getColumnIndex("rom")) + " Гб");
               adapter.add("Процессор: ");
               adapter.add("Марка процессора: " + cursor.getString(cursor.getColumnIndex("cpuMarka")));
               adapter.add("Модель процессора: " + cursor.getString(cursor.getColumnIndex("cpuModel")));
               adapter.add("Видеокарта: ");
               adapter.add("Марка видеокарты: " + cursor.getString(cursor.getColumnIndex("gpuMarka")));
               adapter.add("Модель видеокарты: " + cursor.getString(cursor.getColumnIndex("gpuModel")));
               adapter.add("Дополнителньое оборудование: ");
               int i = 1;
                do {
                    String nameTE = cursor.getString(cursor.getColumnIndex("TEname"));
                    String markaTE = cursor.getString(cursor.getColumnIndex("TEmarka"));
                    String modelTE = cursor.getString(cursor.getColumnIndex("TEmodel"));
                    if (nameTE != null && markaTE != null && modelTE != null) {
                        adapter.add("     № " + i);
                        adapter.add("Название: " + nameTE);
                        adapter.add("Марка: " + markaTE);
                        adapter.add("Модель: " + modelTE);
                    }
                   i++;
               }while(cursor.moveToNext());

            }
            else{
                Toast toast = Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }
}
