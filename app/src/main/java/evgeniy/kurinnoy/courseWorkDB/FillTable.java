package evgeniy.kurinnoy.courseWorkDB;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.autofill.AutofillId;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Date;
import java.util.Calendar;


public class FillTable extends AppCompatActivity {
    private EditText et_computer_inv_num;
    private EditText et_computer_rom;
    private EditText et_computer_ram;
    private AutoCompleteTextView et_computer_marka;
    private AutoCompleteTextView et_computer_model;
    private AutoCompleteTextView et_markaCPU;
    private AutoCompleteTextView et_modelCPU;
    private EditText et_freqCPU;
    private EditText et_coreCPU;
    private AutoCompleteTextView et_markaGPU;
    private AutoCompleteTextView et_modelGPU;
    private AutoCompleteTextView et_compInv_num_for_equip;
    private EditText et_equip_inv_num;
    private AutoCompleteTextView et_typeEquip_name;
    private AutoCompleteTextView et_typeEquip_marka;
    private AutoCompleteTextView et_typeEquip_model;
    private EditText et_room_numRoom;
    private EditText et_room_area;
    private EditText et_workPlace_num;
    private AutoCompleteTextView et_workPlace_numRoom;
    private EditText et_employee_name;
    private EditText et_employee_surname;
    private EditText et_employee_phone;
    private AutoCompleteTextView et_employee_post;
    private EditText et_user_login;
    private EditText et_user_password;
    private EditText et_typeSW_version;
    private AutoCompleteTextView et_typeSW_name;
    private EditText et_software_endLicense;
    private AutoCompleteTextView et_software_compInvNum;
    private AutoCompleteTextView et_PosTypeSW_post;
    private AutoCompleteTextView et_PosTypeSW_name;
    private AutoCompleteTextView et_PosTypeSW_version;
    private AutoCompleteTextView et_workPlace_invNumComp;
    private int access = User.NORMAL;
    private DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_table);
        getSupportActionBar().setTitle("Добавить записи");
        db = new DataBase(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        et_computer_inv_num = (EditText)findViewById(R.id.et_computer_inv_num);
        et_software_endLicense = (EditText)findViewById(R.id.et_software_endLicense);
        et_software_endLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });
        et_computer_marka = (AutoCompleteTextView) findViewById(R.id.et_computer_marka);
        et_computer_marka.setAdapter(getAdapter(this, "CompSpec", "marka"));
        et_computer_model = (AutoCompleteTextView) findViewById(R.id.et_computer_model);
        et_computer_model.setAdapter(getAdapter(this, "CompSpec", "model"));
        et_markaCPU = (AutoCompleteTextView) findViewById(R.id.et_markaCPU);
        et_modelCPU = (AutoCompleteTextView) findViewById(R.id.et_modelCPU);
        et_markaCPU.setAdapter(getAdapter(this, "CPU", "marka"));
        et_modelCPU.setAdapter(getAdapter(this,"CPU", "marka"));
        et_markaGPU = (AutoCompleteTextView) findViewById(R.id.et_markaGPU);
        et_modelGPU = (AutoCompleteTextView) findViewById(R.id.et_modelGPU);
        et_modelGPU.setAdapter(getAdapter(this,"GPU", "model"));
        et_markaGPU.setAdapter(getAdapter(this,"GPU", "marka"));
        et_compInv_num_for_equip = (AutoCompleteTextView) findViewById(R.id.et_compInv_num_for_equip);
        et_compInv_num_for_equip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_compInv_num_for_equip.setAdapter(getAdapter(FillTable.this,"Computer", "inventoryNum"));
                et_compInv_num_for_equip.showDropDown();
            }
        });
        et_typeEquip_name = (AutoCompleteTextView)findViewById(R.id.et_typeEquip_name);
        et_typeEquip_name.setAdapter(getAdapter(this,"TypeEquip", "name"));
        et_typeEquip_marka = (AutoCompleteTextView)findViewById(R.id.et_typeEquip_marka);
        et_typeEquip_marka.setAdapter(getAdapter(this,"TypeEquip", "marka"));
        et_typeEquip_model = (AutoCompleteTextView)findViewById(R.id.et_typeEquip_model);
        et_typeEquip_model.setAdapter(getAdapter(this,"TypeEquip", "model"));
        et_equip_inv_num = (EditText)findViewById(R.id.et_equip_inv_num);
        et_workPlace_numRoom = (AutoCompleteTextView) findViewById(R.id.et_workPlace_numRoom);
        et_workPlace_numRoom.setAdapter(getAdapter(this,"Room", "numRoom"));
        et_workPlace_numRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_workPlace_numRoom.showDropDown();
            }
        });
        et_workPlace_invNumComp = (AutoCompleteTextView) findViewById(R.id.et_workPlace_invNumComp);
        et_workPlace_invNumComp.setAdapter(getAdapter(this,"Computer", "inventoryNum"));
        et_workPlace_invNumComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_workPlace_invNumComp.showDropDown();
            }
        });
        et_employee_post = (AutoCompleteTextView) findViewById(R.id.et_employee_post);
        et_employee_post.setAdapter(getAdapter(this,"Position", "name"));
        et_software_compInvNum = (AutoCompleteTextView) findViewById(R.id.et_software_compInvNum);
        et_software_compInvNum.setAdapter(getAdapter(this,"Computer", "inventoryNum"));
        et_software_compInvNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_software_compInvNum.showDropDown();
            }
        });
        et_typeSW_name = (AutoCompleteTextView) findViewById(R.id.et_typeSW_name);
        et_typeSW_name.setAdapter(getAdapter(this,"TypeSoftware", "name"));
        et_PosTypeSW_post = (AutoCompleteTextView) findViewById(R.id.et_PosTypeSW_post);
        et_PosTypeSW_post.setAdapter(getAdapter(this,"Position", "name"));
        et_PosTypeSW_version = (AutoCompleteTextView) findViewById(R.id.et_PosTypeSW_version);
        et_PosTypeSW_name = (AutoCompleteTextView) findViewById(R.id.et_PosTypeSW_name);
        et_PosTypeSW_name.setAdapter(getAdapter(this,"TypeSoftware", "name"));
        et_PosTypeSW_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String whereValue = et_PosTypeSW_name.getText().toString();
                et_PosTypeSW_version.setAdapter(getAdapter("TypeSoftware", "version", "name", whereValue));
                et_PosTypeSW_version.showDropDown();

            }
        });

    }


    public void addComputerButton(View v){
        if (et_computer_rom == null){
            et_computer_rom = (EditText)findViewById(R.id.et_computer_rom);
            et_computer_ram = (EditText)findViewById(R.id.et_computer_ram);
            et_freqCPU = (EditText)findViewById(R.id.et_freqCPU);
            et_coreCPU = (EditText)findViewById(R.id.et_coreCPU);

        }
        SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //добавляем видеокарту
        long idGPU;
        Cursor cursor = db.query("GPU", null,
                "marka = ?" +
                " AND model = ?",
                new String[]{et_markaGPU.getText().toString(), et_modelGPU.getText().toString()},
                null, null, null);
        //проверяем есть ли такая видеокарта в БД
        if (cursor.moveToFirst()){
            idGPU = cursor.getInt(cursor.getColumnIndex("id"));
        }
        else {
            cv.put("marka", et_markaGPU.getText().toString());
            cv.put("model", et_modelGPU.getText().toString());
            idGPU = db.insert("GPU", null, cv);
        }
        cv.clear();

        //добавляем процессор
        long idCPU;
        cursor = db.query("CPU", null,
                "marka = ?" +
                        " AND model = ?" +
                        " AND freq = ?" +
                        " AND core = ?",
                new String[]{et_markaCPU.getText().toString(),
                        et_modelCPU.getText().toString(),
                        et_freqCPU.getText().toString(),
                        et_coreCPU.getText().toString()}, null, null, null);
        if (cursor.moveToFirst()){
            idCPU = cursor.getInt(cursor.getColumnIndex("id"));
        }
        else {
            cv.put("marka", et_markaCPU.getText().toString());
            cv.put("model", et_modelCPU.getText().toString());
            cv.put("freq",et_freqCPU.getText().toString());
            cv.put("core", et_coreCPU.getText().toString());
            idCPU = db.insert("CPU", null, cv);
        }
        cv.clear();

        //добавляем характеристики
        long compSpec;
        cursor = db.query("CompSpec", null,
                "marka = ?" +
                        " AND model = ?" +
                        " AND ROM = ?" +
                        " AND RAM = ?" +
                        " AND idCPU = ?" +
                        " AND idGPU = ?",
                new String[]{
                        et_computer_marka.getText().toString(),
                        et_computer_model.getText().toString(),
                        et_computer_rom.getText().toString(),
                        et_computer_ram.getText().toString(),
                        ""+idCPU, ""+idGPU
                }, null, null, null);
        if (cursor.moveToFirst()){
            compSpec = cursor.getInt(cursor.getColumnIndex("id"));
        }
        else {
            cv.put("marka", et_computer_marka.getText().toString());
            cv.put("model", et_computer_model.getText().toString());
            cv.put("RAM", et_computer_ram.getText().toString());
            cv.put("ROM", et_computer_rom.getText().toString());
            cv.put("idCPU", idCPU);
            cv.put("idGPU", idGPU);
            compSpec = db.insert("CompSpec", null, cv);
        }
        cv.clear();

        //добавляем компьютер
        if (!et_computer_inv_num.getText().toString().isEmpty())
            cv.put("inventoryNum",et_computer_inv_num.getText().toString());
        cv.put("idCS", compSpec);
        Toast toast;
        long idComp = db.insert("Computer", null, cv);
        if (idComp != -1){
            toast = Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_SHORT);
            this.db.putInJournal("Добавил компьютер с id: " + idComp);
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Неудачно", Toast.LENGTH_SHORT);
        }
        toast.show();
        et_computer_inv_num.setText("");
        et_computer_inv_num.setText("");
        et_computer_rom.setText("");
        et_computer_ram.setText("");
        et_computer_marka.setText("");
        et_computer_model.setText("");
        et_markaCPU.setText("");
        et_modelCPU.setText("");
        et_freqCPU.setText("");
        et_coreCPU.setText("");
        et_markaGPU.setText("");
        et_modelGPU.setText("");
    }

    public void addEquipButton(View v){

        SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //добавляем тип оборудования
        long idTE;
        Cursor cursor = db.query("TypeEquip", null,
                "marka = ?" +
                        " AND model = ?" +
                        " AND name = ?",
                new String[]{
                        et_typeEquip_marka.getText().toString(),
                        et_typeEquip_model.getText().toString(),
                        et_typeEquip_name.getText().toString()
                }, null, null, null);
        if (cursor.moveToFirst()){
            idTE = cursor.getInt(cursor.getColumnIndex("id"));
        }
        else {
            cv.put("name", et_typeEquip_name.getText().toString());
            cv.put("marka", et_typeEquip_marka.getText().toString());
            cv.put("model", et_typeEquip_model.getText().toString());
            idTE = db.insert("TypeEquip", null, cv);
        }
        cv.clear();

        cv.put("inventoryNum", et_equip_inv_num.getText().toString());
        cv.put("idTE", idTE);

        //определяем id компьютера через инвентарный номер
        cursor = db.query("Computer",
                new String[]{"id"},
                "inventoryNum = ?",
                new String[]{et_compInv_num_for_equip.getText().toString()},
                null,
                null,
                null);
        Toast toast;
        if (cursor.moveToFirst()) {
            cv.put("idComp", cursor.getInt(cursor.getColumnIndex("id")));
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Нет компьютера с таким номером", Toast.LENGTH_LONG);
            toast.show();
        }
        long idEquip = db.insert("Equip", null, cv);
        if (idEquip != -1){
            this.db.putInJournal("Добавил доп. оборудование с id: " + idEquip );
            toast = Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_SHORT);
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Неудачно", Toast.LENGTH_SHORT);
        }
        toast.show();

        et_compInv_num_for_equip.setText("");
        et_equip_inv_num.setText("");
        et_typeEquip_name.setText("");
        et_typeEquip_marka.setText("");
        et_typeEquip_model.setText("");
    }

    public void addRoomButton(View v){
        if (et_room_numRoom == null){
            et_room_area = (EditText)findViewById(R.id.et_room_area);
            et_room_numRoom = (EditText)findViewById(R.id.et_room_numRoom);
        }
        SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //добавляем кабинет
        cv.put("numRoom", et_room_numRoom.getText().toString());
        cv.put("area", et_room_area.getText().toString());
        Toast toast;
        long idRoom = db.insert("Room", null, cv);
        if (idRoom!= -1){
            toast = Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_SHORT);
            this.db.putInJournal("добавил кабинет с id: " + idRoom);
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Уже есть кабинет с таким номером", Toast.LENGTH_SHORT);
        }
        toast.show();

        et_room_area.setText("");
        et_room_numRoom.setText("");
    }

    public void addUserButton(View v){
        if (et_workPlace_num == null){
            et_workPlace_num = (EditText)findViewById(R.id.et_workPlace_num);
            et_employee_name = (EditText)findViewById(R.id.et_employee_name);
            et_employee_surname = (EditText)findViewById(R.id.et_employee_surname);
            et_employee_phone = (EditText)findViewById(R.id.et_employee_phone);
            et_user_login = (EditText)findViewById(R.id.et_user_login);
            et_user_password = (EditText)findViewById(R.id.et_user_password);
        }
        SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Toast toast;

//находим id компьютера, если такого нет - ошибка
        long idComp;
        Cursor cursor = db.query("Computer", null,
                "inventoryNum = ?",
                new String[]{et_workPlace_invNumComp.getText().toString()}, null, null, null);
        if (cursor.moveToFirst()){
            idComp = cursor.getInt(cursor.getColumnIndex("id"));
        }
        else{
            toast = Toast.makeText(this, "Нет такого компьютера", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //добавляем должность
        long idPos;
        cursor = db.query("Position", null,
                "name = ?",
                new String[]{et_employee_post.getText().toString()}, null, null, null);
        if (cursor.moveToFirst()){
            idPos = cursor.getInt(cursor.getColumnIndex("id"));
        }
        else {
            cv.put("name", et_employee_post.getText().toString());
            idPos = db.insert("Position", null, cv);
        }
        cv.clear();

        //добавляем рабочее место
        // находим id кабинета по номеру
        long idRoom;
        cursor = db.query("Room", new String[]{"id"},
                "numRoom = ?",
                new String[]{et_workPlace_numRoom.getText().toString()},
                null,
                null,
                null);
        if (cursor.moveToFirst()){
            idRoom = cursor.getInt(cursor.getColumnIndex("id"));
            cv.put("idRoom", idRoom);
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Кабинет не найден", Toast.LENGTH_SHORT);
            toast.show();
        }


        //находим id рабочего места по номеру, если такого нет - добавляем
        long idWP;
        cursor = db.query("WorkPlace", null,
                "numWP = ?",
                new String[]{et_workPlace_num.getText().toString()},
                null, null, null);
        if (cursor.moveToFirst()){
            idWP = cursor.getInt(cursor.getColumnIndex("id"));
        }else {
            if (!et_workPlace_num.getText().toString().isEmpty()) {
                cv.put("numWP", et_workPlace_num.getText().toString());
                cv.put("idComp", idComp);
                idWP = db.insert("WorkPlace", null, cv);
            }
            else{
                toast = Toast.makeText(this, "Укажите номер рабочего места", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

        }
        cv.clear();


        //добавляем сотрудника
        cv.put("name", et_employee_name.getText().toString());
        cv.put("surname", et_employee_surname.getText().toString());
        cv.put("phone", et_employee_phone.getText().toString());
        cv.put("idWP", idWP);
        cv.put("idPos", idPos);
        long idEmpl = db.insert("Employee", null, cv);
        cv.clear();


        //добавляем пользователя
        if (!et_user_login.getText().toString().isEmpty())
            cv.put("login", et_user_login.getText().toString());
        if (!et_user_password.getText().toString().isEmpty())
            cv.put("password", et_user_password.getText().toString());
        cv.put("access", access);
        cv.put("idEmpl", idEmpl);
        long idUser = db.insert("User", null, cv);
        if (idUser != -1){
            toast = Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_SHORT);
            this.db.putInJournal("Добавил пользователя с id: " + idUser);
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Неудачно", Toast.LENGTH_SHORT);
        }
        toast.show();

        et_workPlace_num.setText("");
        et_workPlace_numRoom.setText("");
        et_workPlace_invNumComp.setText("");
        et_employee_name.setText("");
        et_employee_surname.setText("");
        et_employee_phone.setText("");
        et_employee_post.setText("");
        et_user_login.setText("");
        et_user_password.setText("");
}

    public void addSWbutton(View v){
        if (et_typeSW_version == null){
            et_typeSW_version = (EditText)findViewById(R.id.et_typeSW_version);
        }
        SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //добавляем тип ПО
        long idtypeSW;
        Cursor cursor = db.query("TypeSoftware", null,
                "name = ?" +
                " AND version = ?",
                new String[]{
                        et_typeSW_name.getText().toString(),
                        et_typeSW_version.getText().toString()
                },
                null, null, null);
        if (cursor.moveToFirst()){
            idtypeSW = cursor.getInt(cursor.getColumnIndex("id"));
        }else {
            cv.put("name", et_typeSW_name.getText().toString());
            cv.put("version", et_typeSW_version.getText().toString());
            idtypeSW = db.insert("TypeSoftware", null, cv);
        }
        cv.clear();
        Toast toast;

        // определяем id компьютера по инвентарному номеру
        cursor = db.query("Computer",
                new String[]{"id"},
                "inventoryNum = ?",
                new String[]{
                    et_software_compInvNum.getText().toString()
                },
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            cv.put("idComp", cursor.getInt(cursor.getColumnIndex("id")));
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Нет компьютера с таким номером", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        // Добавляем ПО
        Date date;
        String s= new String(firstYear + "-" + firstMonth + "-" + firstDay);
        try {
            date = Date.valueOf(s);
        }
        catch (IllegalArgumentException e){
            toast = Toast.makeText(this, "Неверный формат даты", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        cv.put("endOfLicense", date.getTime());
        cv.put("idTSW", idtypeSW);
        long idSW = db.insert("Software", null, cv);
        if (idSW != -1){
            toast = Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_SHORT);
            this.db.putInJournal("Добавил ПО с id: " + idSW);
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Неудачно", Toast.LENGTH_SHORT);
        }
        toast.show();

        et_typeSW_version.setText("");
        et_typeSW_name.setText("");
        et_software_endLicense.setText("");
        et_software_compInvNum.setText("");
    }

    public void addPosTypeSW (View v){
        SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long idPos;
        long idtypeSW;
        Toast toast;

        //находим id ПО
        Cursor cursor = db.query("TypeSoftware", null,
                "name = ?" +
                        " AND version = ?",
                new String[]{
                        et_PosTypeSW_name.getText().toString(),
                        et_PosTypeSW_version.getText().toString()
                },
                null, null, null);
        if (cursor.moveToFirst()){
            idtypeSW = cursor.getInt(cursor.getColumnIndex("id"));
        }else {
            toast = Toast.makeText(getApplicationContext(), "Нет такого ПО", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //находим id должности
        cursor = db.query("Position", null,
                "name = ?" ,
                new String[]{
                    et_PosTypeSW_post.getText().toString()
                },
                null, null, null);
        if (cursor.moveToFirst()){
            idPos = cursor.getInt(cursor.getColumnIndex("id"));
        }
        else {
            toast = Toast.makeText(getApplicationContext(), "Нет такой должности", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        cv.put("idPos", idPos);
        cv.put("idTSW", idtypeSW);
        long idPTSW = db.insert("PosTypeSW", null, cv);
        if (idPTSW != -1){
            toast = Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_SHORT);
            this.db.putInJournal("Добавил связь ПО-Должность с id: " + idPTSW);
        }
        else{
            toast = Toast.makeText(getApplicationContext(), "Связь уже существует", Toast.LENGTH_SHORT);
        }
        toast.show();

        et_PosTypeSW_version.setText("");
        et_PosTypeSW_name.setText("");
        et_PosTypeSW_post.setText("");
    }

    private Calendar currtime = Calendar.getInstance();
    private int firstYear = currtime.get(Calendar.YEAR);
    private int firstMonth = currtime.get(Calendar.MONTH);
    private int firstDay = currtime.get(Calendar.DAY_OF_MONTH);
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, firstYear, firstMonth, firstDay);
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
            et_software_endLicense.setText( firstDay + "-" + firstMonth + "-" + firstYear);
        }
    };

    public void radioButtonClickListener(View v){
        RadioButton rb = (RadioButton)v;

        switch (rb.getId()){
            case R.id.adminAccess:
                    access = User.ADMIN;
                break;
            case R.id.exAccess:
                    access = User.EXTENDS;
                break;
            case R.id.normalAccess:
                    access = User.NORMAL;
                break;
            default: break;
        }

    }

    public static ArrayAdapter<String> getAdapter(Context context, String table, String column){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line);
        DataBase dataBase = new DataBase(context);
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor cursor = db.rawQuery("select " + column + " from " + table, null);
        if (cursor.moveToFirst()){
            do {
                adapter.add(cursor.getString(cursor.getColumnIndex(column)));
            }while (cursor.moveToNext());
        }
        return adapter;
    }

    public ArrayAdapter<String> getAdapter(String table, String column, String whereColumn, String whereValue){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        SQLiteDatabase db = this.db.getWritableDatabase();
        Cursor cursor = db.rawQuery("select " + column + " from " + table +
                " where " + whereColumn + " = ?", new String[]{whereValue});
        if (cursor.moveToFirst()){
            do {
                adapter.add(cursor.getString(cursor.getColumnIndex(column)));
            }while (cursor.moveToNext());
        }
        return adapter;
    }
}
