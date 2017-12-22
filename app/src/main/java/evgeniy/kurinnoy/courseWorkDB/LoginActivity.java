package evgeniy.kurinnoy.courseWorkDB;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button signInButton;
    DataBase db = new DataBase(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        mEmailView = (AutoCompleteTextView)findViewById(R.id.email);
        mPasswordView = (EditText)findViewById(R.id.password);
        signInButton = (Button)findViewById(R.id.email_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = db.login(mEmailView.getText().toString(), mPasswordView.getText().toString());
                if (user == null){
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.error_incorrect_password, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                else{
                    SQLiteDatabase database = db.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("idUser", user.getId());
                    Date date = Calendar.getInstance().getTime();
                    cv.put("entryDate", date.getTime());
                    cv.put("act", "Log in");
                    database.insert("Journal", null, cv);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }


}

