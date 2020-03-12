/*(Nome,Cognome,Matricola) Mauro Manca 65519 */

package com.example.esbonus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView registrazione; //link per la pagina di registrazione
    Button login;           //bottone per effettuare il login
    Utente user;
    EditText username,password;// campi utente
    public static final String USER_EXTRA="package com.example.esbonus";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       // creo un nuovo oggetto Utente
        user = new Utente();

        //bottoni Per cambiare layout
        registrazione = findViewById(R.id.registrationLink);
        login=findViewById(R.id.LoginButton);

        //editText dati
        username= findViewById(R.id.inputUserName);
        password= findViewById(R.id.inputPassword);


        registrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registration = new Intent(MainActivity.this, RegistrationActivity.class);

                startActivity(registration);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utente utente=checkInputLogin();
                if(utente!=null) {

                    Intent doLogin = new Intent(MainActivity.this, HomePageActivity.class);
                    doLogin.putExtra(USER_EXTRA,utente);
                    startActivity(doLogin);
                }
            }
        });

    }

    //Controlla l'input dell'utente sui campi,
    // return true se è andato a buon fine, false altrimenti
    private Utente checkInputLogin(){
        int errors = 0;

        if(username.getText() == null || username.getText().length() == 0){
            username.setError("Inserire lo username");
            errors++;
        }
        else {
            username.setError(null);
        }

        if(password.getText() == null || password.getText().length() == 0){
            password.setError("Inserire la password");
            errors++;
        }
        else {
            password.setError(null);
        }

        if(errors>0)
            return null;
        else{

            try {

                // lista di utenti registrati nel file "users"
                List<Utente> cachedEntries = (List<Utente>) InternalStorage.readObject(this, "users");

                // controllo se le credenziali corrispondono a un oggetto della lista di utenti registrati
                for (Utente u : cachedEntries){
                    if(u.getUsername().equals(username.getText().toString()) && u.getPassword().equals(password.getText().toString())){
                        return u; //restituisco utente trovato
                    }
                }


            } catch (IOException e) {
                Log.e("error",e.getMessage());
            } catch (ClassNotFoundException e) {
                Log.e("error", e.getMessage());
            }
        }

        password.setError("nessun match");
        username.setError("nessun match");
        //se non ci sono errori ritorna true
        return null;
    }



}
