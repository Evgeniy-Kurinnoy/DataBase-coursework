package evgeniy.kurinnoy.courseWorkDB;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class ShowTables extends AppCompatActivity {
    private DataBase db;
    private ExpandableHeightGridView  table;
    private TextView table_name;
    private int columnsNumber = 3;
    private String tableName = "Computer";
    private String[] columnsName = {"id", "inventoryNum", "idCS"};
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tables);
        getSupportActionBar().setTitle("Таблицы");
        db = new DataBase(this);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.lLayout);
        LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.lLayout2);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        table = (ExpandableHeightGridView)findViewById(R.id.computer_table);
        table.setExpanded(true);
        User user = User.get();
        if (user.getAccessRight() != User.ADMIN){
            Button clearJournal = (Button)findViewById(R.id.clearJournal);
            Button usersButton = (Button)findViewById(R.id.usersButton);
            linearLayout.removeView(clearJournal);
            linearLayout2.removeView(usersButton);
        }
        if (user.getAccessRight() == User.ADMIN) {
            table.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    final int idRow = i - (i % columnsNumber);
                    int idColumn = i % columnsNumber;
                    final String itemColumnName = columnsName[idColumn];
                    final int itemId;
                    try {
                        itemId = Integer.parseInt(adapter.getItem(idRow));
                    } catch (NumberFormatException e) {
                        return;
                    }
                    LayoutInflater li = LayoutInflater.from(ShowTables.this);
                    View promptsView = li.inflate(R.layout.prompt, null);

                    //Создаем AlertDialog
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(ShowTables.this);

                    //Настраиваем prompt.xml для нашего AlertDialog:
                    mDialogBuilder.setView(promptsView);

                    //Настраиваем отображение поля для ввода текста в открытом диалоге:
                    final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
                    userInput.setText(adapter.getItem(i));
                    //вызов клавиатуры
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(userInput, InputMethodManager.SHOW_FORCED);
                    userInput.requestFocus();
                    userInput.selectAll();
                    //Настраиваем сообщение в диалоговом окне:
                    mDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // запрос в бд на изменение данных
                                            ContentValues cv = new ContentValues();
                                            SQLiteDatabase database = db.getWritableDatabase();
                                            cv.put(itemColumnName, userInput.getText().toString());
                                            int r = database.updateWithOnConflict(tableName, cv,
                                                    columnsName[0] + " = ?" +
                                                            " and " + itemColumnName + " = ?",
                                                    new String[]{itemId + "", adapter.getItem(i)},
                                                    SQLiteDatabase.CONFLICT_IGNORE);
                                            if (r == 0){
                                                Toast toast = Toast.makeText(ShowTables.this,
                                                        "Ошибка", Toast.LENGTH_SHORT);
                                                toast.show();
                                                return;
                                            }
                                            ArrayAdapter<String> newAdapter =
                                                    new ArrayAdapter<String>(ShowTables.this, android.R.layout.simple_list_item_1);
                                            for (int k = 0; k < adapter.getCount(); k++) {
                                                if (k == i)
                                                    newAdapter.add(userInput.getText().toString());
                                                else
                                                    newAdapter.add(adapter.getItem(k));
                                            }
                                            adapter = newAdapter;
                                            table.setAdapter(adapter);
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                        }
                                    }).setNeutralButton("Удалить",
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            SQLiteDatabase database = db.getWritableDatabase();
                            database.delete(tableName,
                                    columnsName[0] + " = ?" +
                                            " and " + itemColumnName + " = ?",
                                    new String[]{itemId + "", adapter.getItem(i)});
                            ArrayAdapter<String> newAdapter =
                                    new ArrayAdapter<String>(ShowTables.this, android.R.layout.simple_list_item_1);
                            for (int k = 0; k < adapter.getCount(); k++) {
                                if (k < idRow)
                                    newAdapter.add(adapter.getItem(k));
                                if (k >= idRow + columnsNumber)
                                    newAdapter.add(adapter.getItem(k));

                            }
                            adapter = newAdapter;
                            table.setAdapter(adapter);
                        }
                    });
                    AlertDialog alertDialog = mDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }
        table_name = (TextView) findViewById(R.id.table_name);
        showComputerTable();
    }

    public void showComputerTable(View v){
        showComputerTable();
    }
    public void showCompSpecTable(View v){
        showCompSpecTable();
    }
    public void showCPUTable(View v){
        showCPUTable();
    }
    public void showGPUTable(View v){
        showGPUTable();
    }
    public void showComputerTable(){
        table.setAdapter(null);
        columnsNumber = 3;
        tableName = "Computer";
        columnsName = new String[]{"id", "inventoryNum", "idCS"};
        table.setNumColumns(columnsNumber);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("Computer", null, null, null, null, null, null);
       table_name.setText("Компьютеры");
        adapter.addAll("id", "инвертарный №", "id характеристик");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int invNumIndex = cursor.getColumnIndex("inventoryNum");
            int idCSIndex = cursor.getColumnIndex("idCS");

            do {
               adapter.addAll(""+cursor.getInt(idColIndex), ""+cursor.getInt(invNumIndex), ""+cursor.getInt(idCSIndex));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);

    }

    public void showCompSpecTable(){
        table.setAdapter(null);
        columnsNumber = 7;
        tableName = "CompSpec";
        columnsName = new String[]{"id", "marka", "model", "ROM", "RAM", "idCPU", "idGPU"};
        table.setNumColumns(columnsNumber);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("CompSpec", null, null, null, null, null, null);
        table_name.setText("Характеристики");
        adapter.addAll("id", "marka", "model", "ROM", "RAM", "idCPU", "idGPU");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int markaIndex = cursor.getColumnIndex("marka");
            int modelIndex = cursor.getColumnIndex("model");
            int RAM = cursor.getColumnIndex("RAM");
            int ROM = cursor.getColumnIndex("ROM");
            int idCPU = cursor.getColumnIndex("idCPU");
            int idGPU = cursor.getColumnIndex("idGPU");

            do {
                adapter.addAll(""+cursor.getInt(idColIndex), cursor.getString(markaIndex),
                        cursor.getString(modelIndex),
                        "" + cursor.getInt(ROM),
                        "" + cursor.getInt(RAM),
                        "" + cursor.getInt(idCPU),
                        "" + cursor.getInt(idGPU));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);

    }

    public void showCPUTable(){
        table.setAdapter(null);
        columnsNumber = 5;
        tableName = "CPU";
        columnsName = new String[]{"id", "marka", "model", "freq", "core"};
        table.setNumColumns(columnsNumber);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("CPU", null, null, null, null, null, null);
        table_name.setText("Процессоры");
        adapter.addAll("id", "марка", "модель", "частота", "ядра");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int marka = cursor.getColumnIndex("marka");
            int model = cursor.getColumnIndex("model");
            int freq = cursor.getColumnIndex("freq");
            int core = cursor.getColumnIndex("core");

            do {
                adapter.addAll(""+cursor.getInt(idColIndex), cursor.getString(marka),
                        cursor.getString(model),
                        "" + cursor.getDouble(freq),
                        "" + cursor.getInt(core));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void showGPUTable(){
        table.setAdapter(null);
        columnsNumber = 3;
        tableName = "GPU";
        columnsName = new String[]{"id", "marka", "model"};
        table.setNumColumns(columnsNumber);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("GPU", null, null, null, null, null, null);
        table_name.setText("Видеокарты");
        adapter.addAll("id", "марка", "модель");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int marka = cursor.getColumnIndex("marka");
            int model = cursor.getColumnIndex("model");

            do {
                adapter.addAll(""+cursor.getInt(idColIndex), ""+cursor.getString(marka), ""+cursor.getString(model));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);

    }

    public  void showTypeEquipTable(View v){
        table.setAdapter(null);
        columnsNumber = 4;
        tableName = "TypeEquip";
        columnsName = new String[]{"id", "name", "marka", "model"};
        table.setNumColumns(4);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("TypeEquip", null, null, null, null, null, null);
        table_name.setText("Типы оборудования");
        adapter.addAll("id", "название", "марка", "модель");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int marka = cursor.getColumnIndex("marka");
            int model = cursor.getColumnIndex("model");
            int name = cursor.getColumnIndex("name");

            do {
                adapter.addAll(""+cursor.getInt(idColIndex),
                        cursor.getString(name),
                        cursor.getString(marka),
                        cursor.getString(model));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void showEquipTable(View v){
        table.setAdapter(null);
        columnsNumber = 4;
        tableName = "Equip";
        columnsName = new String[]{"id", "inventoryNum", "idComp", "idTE"};
        table.setNumColumns(columnsNumber);
         adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("Equip", null, null, null, null, null, null);
        table_name.setText("Оборудование");
        adapter.addAll("id", "инв.№", "id комп.", "id типа");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int inventoryNum = cursor.getColumnIndex("inventoryNum");
            int idComp = cursor.getColumnIndex("idComp");
            int idTE = cursor.getColumnIndex("idTE");

            do {
                adapter.addAll(""+cursor.getInt(idColIndex),
                        ""+cursor.getInt(inventoryNum),
                        ""+cursor.getInt(idComp),
                        ""+cursor.getInt(idTE));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void showRoomTable(View v){
        table.setAdapter(null);
        columnsNumber = 3;
        tableName = "Room";
        columnsName = new String[]{"id", "numRoom", "area"};
        table.setNumColumns(columnsNumber);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("Room", null, null, null, null, null, null);
        table_name.setText("Кабинеты");
        adapter.addAll("id", "номер", "площадь");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int numRoom = cursor.getColumnIndex("numRoom");
            int area = cursor.getColumnIndex("area");

            do {
                adapter.addAll(""+cursor.getInt(idColIndex), ""+cursor.getInt(numRoom), ""+cursor.getInt(area));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);

    }

    public void showWorkPlaceTable(View v){
        table.setAdapter(null);
        columnsNumber = 4;
        tableName = "WorkPlace";
        columnsName = new String[]{"id", "numWP", "idRoom", "idComp"};
        table.setNumColumns(columnsNumber);
         adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("WorkPlace", null, null, null, null, null, null);
        table_name.setText("Рабочие места");
        adapter.addAll("id", "номер", "id кабинета", "id комп.");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int numWP = cursor.getColumnIndex("numWP");
            int idRoom = cursor.getColumnIndex("idRoom");
            int idComp = cursor.getColumnIndex("idComp");

            do {
                adapter.addAll(""+cursor.getInt(idColIndex), ""+cursor.getInt(numWP), ""+cursor.getInt(idRoom), ""+cursor.getInt(idComp));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void showEmployeeTable (View v ){
        table.setAdapter(null);
        columnsNumber = 6;
        tableName = "Employee";
        columnsName = new String[]{"id", "name", "surname", "phone", "idWP", "idPos"};
        table.setNumColumns(columnsNumber);
         adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("Employee", null, null, null, null, null, null);
        table_name.setText("Сотрудники");
        adapter.addAll("id", "имя", "фамилия", "телефон", "id раб.места", "id должн.");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int name = cursor.getColumnIndex("name");
            int surname = cursor.getColumnIndex("surname");
            int phone = cursor.getColumnIndex("phone");
            int idWP = cursor.getColumnIndex("idWP");
            int idPos = cursor.getColumnIndex("idPos");

            do {
                adapter.addAll(""+cursor.getInt(idColIndex), cursor.getString(name),
                        cursor.getString(surname),
                        "" + cursor.getString(phone),
                        "" + cursor.getInt(idWP),
                        "" + cursor.getInt(idPos));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void showUsersTable(View v){
        table.setAdapter(null);
        columnsNumber = 5;
        tableName = "User";
        columnsName = new String[]{"id", "login", "password", "access", "idEmpl"};
        table.setNumColumns(columnsNumber);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("User", null, null, null, null, null, null);
        table_name.setText("Пользователи");
        adapter.addAll("id", "логин", "пароль", "доступ", "id сотр.");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int login = cursor.getColumnIndex("login");
            int password = cursor.getColumnIndex("password");
            int access = cursor.getColumnIndex("access");
            int idEmpl = cursor.getColumnIndex("idEmpl");

            do {
                adapter.addAll(""+cursor.getInt(idColIndex), cursor.getString(login),
                        cursor.getString(password),
                        "" + cursor.getInt(access),
                        "" + cursor.getInt(idEmpl));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void showSoftwareTable(View v){
        table.setAdapter(null);
        columnsNumber = 4;
        tableName = "Software";
        columnsName = new String[]{"id", "endOfLicense", "idTSW", "idComp"};
        table.setNumColumns(columnsNumber);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("Software", null, null, null, null, null, null);
        table_name.setText("Программное обеспечение");
        adapter.addAll("id", "конец лицензии", "id типа", "id комп.");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int endOfLicense = cursor.getColumnIndex("endOfLicense");
            int idTSW = cursor.getColumnIndex("idTSW");
            int idComp = cursor.getColumnIndex("idComp");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            do {
                adapter.addAll(""+cursor.getInt(idColIndex),
                        dateFormat.format(cursor.getLong(endOfLicense)),
                        ""+cursor.getInt(idTSW),
                        ""+cursor.getInt(idComp));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void showTypeSWTable(View v){
        table.setAdapter(null);
        columnsNumber = 3;
        tableName = "TypeSoftware";
        columnsName = new String[]{"id", "name", "version"};
        table.setNumColumns(columnsNumber);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("TypeSoftware", null, null, null, null, null, null);
        table_name.setText("Типы ПО");
        adapter.addAll("id", "название", "версия");
        if (cursor.moveToFirst()){
            int idColIndex = cursor.getColumnIndex("id");
            int name = cursor.getColumnIndex("name");
            int version = cursor.getColumnIndex("version");

            do {
                adapter.addAll(""+cursor.getInt(idColIndex), ""+cursor.getString(name), ""+cursor.getString(version));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void showPostTypeSWTable(View v){
        table.setAdapter(null);
        columnsNumber = 2;
        tableName = "PosTypeSW";
        columnsName = new String[]{"idPos", "idTSW"};
        table.setNumColumns(columnsNumber);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("PosTypeSW", null, null, null, null, null, null);
        table_name.setText("ПО + Должности");
        adapter.addAll("id ПО", "id Должности");
        if (cursor.moveToFirst()){
            int idPos = cursor.getColumnIndex("idPos");
            int idTSW = cursor.getColumnIndex("idTSW");

            do {
                adapter.addAll(""+cursor.getInt(idPos), ""+cursor.getInt(idTSW));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void showPostTable(View v){
        table.setAdapter(null);
        columnsNumber = 2;
        tableName = "Position";
        columnsName = new String[]{"id", "name"};
        table.setNumColumns(columnsNumber);
       adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("Position", null, null, null, null, null, null);
        table_name.setText("Должности");
        adapter.addAll("id", "название");
        if (cursor.moveToFirst()){
            int id = cursor.getColumnIndex("id");
            int name = cursor.getColumnIndex("name");

            do {
                adapter.addAll(""+cursor.getInt(id), ""+cursor.getString(name));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void showJournalTable(View view) {
        table.setAdapter(null);
        table.setNumColumns(3);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query("Journal", null, null, null, null, null, null);
        table_name.setText("Журнал");
        adapter.addAll("дата", "id Пользователя", "действия");
        if (cursor.moveToFirst()){
            int entryDate = cursor.getColumnIndex("entryDate");
            int name = cursor.getColumnIndex("idUser");
            int version = cursor.getColumnIndex("act");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm");

            do {
                adapter.addAll(dateFormat.format(cursor.getLong(entryDate)), ""+cursor.getString(name), ""+cursor.getString(version));
            }while(cursor.moveToNext());
        }
        else{
            adapter.add("Пусто");
        }
        table.setAdapter(adapter);
        table.scrollListBy(0);
    }

    public void clearJournal(View view) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        db.delete("Journal", null, null);

        Toast toast = Toast.makeText(this, "Очищено", Toast.LENGTH_SHORT);
        toast.show();
    }
}
