/*(Nome,Cognome,Matricola) Mauro Manca 65519 */
package com.example.esbonus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class HomePageActivity extends AppCompatActivity {

    TextView changePsw;
    Button logout;
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    TextView username,password,city,date;
    Utente user;

    public static final String USER_EXTRA="package com.example.esbonus";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);


        changePsw = findViewById(R.id.changePassword);
        logout = findViewById(R.id.logoutButton);

        username=findViewById(R.id.homeUsername);
        password=findViewById(R.id.homePassword);
        city=findViewById(R.id.homeCitta);
        date=findViewById(R.id.homeDataNascita);

        // recupero l'intent mandato dall MainActivity
       Intent intent = getIntent();
        Serializable obj = intent.getSerializableExtra(MainActivity.USER_EXTRA);

        if(obj instanceof Utente){
            user = (Utente) obj;
        }
        // se non ho compilato i campi creo un nuovo oggetto Persona vuoto
        else {
            user = new Utente();
        }

        username.setText(user.getUsername());
        password.setText(user.getPassword());
        city.setText(user.getCity());
        date.setText(format.format(user.getBornDate().getTime()));

        changePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(user!=null) {

                    Intent changePassword = new Intent(HomePageActivity.this, ChangePswActivity.class);
                    changePassword.putExtra(USER_EXTRA,user);
                    startActivity(changePassword);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent logout = new Intent(HomePageActivity.this, MainActivity.class);

                startActivity(logout);
            }
        });


    }

    //Disabilito il tasto back. Costringo a usare il Button Logout
    @Override
    public void onBackPressed(){
        // super.onBackPressed(); commented this line in order to disable back press
        Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }
}
