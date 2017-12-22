package evgeniy.kurinnoy.courseWorkDB;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView welcome_text;
    DataBase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_main);
        getSupportActionBar().setTitle("Добро пожаловать");
        welcome_text = (TextView)findViewById(R.id.welcome_text);
        User user = User.get();
        db = new DataBase(this);
        welcome_text.setText("Добро пожаловать " + user.getPost() + "\n" + user.getFirstName() + " " + user.getLastName());
        Button btn1 = (Button)findViewById(R.id.fillTables);
        Button btn2 = (Button)findViewById(R.id.viewTables);
        LinearLayout llayout = (LinearLayout)findViewById(R.id.firstLlayout);
        if (user.getAccessRight() == User.NORMAL){
            llayout.removeView(btn1);
            llayout.removeView(btn2);

        } else if (user.getAccessRight() == User.EXTENDS){
           llayout.removeView(btn1);
        }

    }

    public void viewTables(View v){
        db.putInJournal("Перешел на вкладку просмотра таблиц");
        startActivity(new Intent(MainActivity.this, ShowTables.class));
    }

    public void fillTables(View view) {
        db.putInJournal("Перешел на вкладку заполенния таблиц");
        startActivity(new Intent(MainActivity.this, FillTable.class));
    }

    public void queryType1(View v){
        db.putInJournal("Перешел на вкладку Запросы первого типа");
        startActivity(new Intent(MainActivity.this, QueryType1.class));
    }

    public void queryType2(View view) {
        db.putInJournal("Перешел на вкладку Запросы второго типа");
        startActivity(new Intent(MainActivity.this, QueryType2.class));

    }

    public void queryType3(View view) {
        db.putInJournal("Перешел на вкладку Запросы третьего типа");
        startActivity(new Intent(MainActivity.this, QueryType3.class));
    }

    public void queryType4(View view) {
        db.putInJournal("Перешел на вкладку Запросы четвертого типа");
        startActivity(new Intent(MainActivity.this, QueryType4.class));
    }

    public void queryType5(View view) {
        db.putInJournal("Перешел на вкладку Запросы пятого типа");
        startActivity(new Intent(MainActivity.this, QueryType5.class));
    }

    public void queryType6(View view) {
        db.putInJournal("Перешел на вкладку Запросы шестого типа");
        startActivity(new Intent(MainActivity.this, QueryType6.class));
    }
}
