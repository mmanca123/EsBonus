/*(Nome,Cognome,Matricola) Mauro Manca 65519 */
package com.example.esbonus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChangePswActivity extends AppCompatActivity {

    Button homePage,updatePsw;
    TextView username,psw;
    EditText newPassword,confirmNewPassword;

    Utente loggedUser; //utente loggato

    public static final String USER_EXTRA="package com.example.esbonus";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);


        homePage = findViewById(R.id.backToHomeButton);
        updatePsw = findViewById(R.id.updatePasswordButton);
        username = findViewById(R.id.homeUsername);
        psw = findViewById(R.id.homePassword);
        newPassword = findViewById(R.id.newPsw);
        confirmNewPassword = findViewById(R.id.confirmNewPsw);

        // recupero l'intent mandato da HomePageActivity
        Intent intent = getIntent();
        Serializable obj = intent.getSerializableExtra(MainActivity.USER_EXTRA);

        if(obj instanceof Utente){
            loggedUser = (Utente) obj;
        }
        // se non ho compilato i campi creo un nuovo oggetto Persona vuoto
        else {
            loggedUser = new Utente();
        }

        //inserisco i dati dell'utente nelle TextView
        username.setText(loggedUser.getUsername());
        psw.setText(loggedUser.getPassword());

        //annullo l'operazione e torno alla homepage
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent home = new Intent(ChangePswActivity.this, HomePageActivity.class);

                startActivity(home);
            }
        });

        //aggiornamento password e ritorno alla homepage
        updatePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkNewPassword()) {

                    Intent update = new Intent(ChangePswActivity.this, HomePageActivity.class);
                    update.putExtra(USER_EXTRA,loggedUser);
                    startActivity(update);
                }
            }
        });

    }

    //Disabilito il tasto back. Costringo a usare il Button Logout
    @Override
    public void onBackPressed(){
        // super.onBackPressed(); commented this line in order to disable back press
        Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }

    /*Controllo che i nuovaPassword e conferma nuova password siano uguali e che siano diversi all'attuale password.
      Se tutto quadra leggo la lista utenti e modifico la password dell'oggetto dell' utente loggato prima di riscrivere la lista
      in memoria
    * */
    public boolean checkNewPassword(){

        int errors=0;
        if(newPassword.getText().toString().equals("") || newPassword==null) {
            newPassword.setError("scrivi nuova Password");
            errors++;
        }else {
            if(confirmNewPassword.getText().toString().equals("") || confirmNewPassword==null) {
                confirmNewPassword.setError("scrivi nuova Password");
                errors++;
            }else{
                if(!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())) {
                    newPassword.setError("password e conferma password diverse");
                    confirmNewPassword.setError("password e conferma password diverse");
                    errors++;
                } else {
                    if(newPassword.getText().toString().equals(loggedUser.getPassword())){
                        newPassword.setError("La nuova password non può essere uguale alla precedente");
                        errors++;
                    }
                }
            }
        }

        if(errors>0)
            return false;
        else {
            try {

                // leggo la lista utenti
                List<Utente> users = (List<Utente>) InternalStorage.readObject(this, "users");

                //rimuovo l'oggetto relativo all'utente loggato
                users.remove(loggedUser.getOrdine());

                //modifico la password all'oggetto e lo riaggiungo alla lista
                loggedUser.setPassword(newPassword.getText().toString());
                users.add(loggedUser);

                //scrivo lista aggiornata in memoria
                InternalStorage.writeObject(this, "users", users);

            } catch (IOException e) {
                Log.e("error", e.getMessage());
                return false;
            } catch (ClassNotFoundException e) {
                Log.e("error", e.getMessage());
                return false;
            }
        }


        return true;
    }
}
