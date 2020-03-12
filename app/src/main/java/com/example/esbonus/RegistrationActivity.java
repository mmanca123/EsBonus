/*(Nome,Cognome,Matricola) Mauro Manca 65519 */

package com.example.esbonus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    EditText userNameText, passwordText,confermaPasswordText, dataText,city;
    Button register;
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static int numeroUtenti=0;

    DatePickerFragment datePickerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        datePickerFragment=new DatePickerFragment();
        // recupero gli id dei vari campi
        userNameText = findViewById(R.id.inputUserName);
        passwordText = findViewById(R.id.inputPassword);
        confermaPasswordText = findViewById(R.id.inputConfPassword);
        dataText = findViewById(R.id.inputData);
        city=findViewById(R.id.inputCitta);

        //bottone invio
        register=findViewById(R.id.RegisterButton);

        dataText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //il fragment manager è colui che dirige tutti i fragment
                datePickerFragment.show(getSupportFragmentManager(),"date picker");
            }
        });

        //questa funzione permette di controllare che l'utente non scriva nella textview
        dataText.setOnFocusChangeListener(new View.OnFocusChangeListener() { //funzione di view
            @Override
            public void onFocusChange(View v, boolean hasFocus) { //metodo chiamato quando lo stato della view cambia
                if(hasFocus){
                    datePickerFragment.show(getSupportFragmentManager(), "datePicker");
                }
            }
        });

        //bottoni OK e ANNULLA
        datePickerFragment.setOnDatePickerFragmentChanged(new DatePickerFragment.DatePickerFragmentListener() {
            @Override
            public void onDatePickerFragmentOkButton(DialogFragment dialog, Calendar date) {
                //Associo il comportamento del bottone OK all'edit text della data, voglio che una
                // volta selezionata quindi ho premuto ok, l'edit text mostri la data selezionata
                // tramite il datepicker
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                dataText.setText(format.format(date.getTime()));
            }

            @Override
            public void onDatePickerFragmentCancelButton(DialogFragment dialog) {

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utente u;

                u=checkInputRegister();
                if(u!=null){

                    storeNewUser(u); //memorizza il nuovo utente

                    Intent doRegister =new Intent(RegistrationActivity.this, MainActivity.class);

                    startActivity(doRegister);
                }


            }
        });
    }

    //Controlla l'input dell'utente sui campi,
    // return true se è andato a buon fine, false altrimenti
    private Utente checkInputRegister(){
        int errors = 0;
        Utente user;

        if(passwordText.getText() == null || passwordText.getText().length() == 0){
            passwordText.setError("Inserire la password");
            errors++;
        }
        else {
            passwordText.setError(null);
        }
        if(confermaPasswordText.getText() == null || confermaPasswordText.getText().length() == 0){
            confermaPasswordText.setError("Inserire la password");
            errors++;
        }
        else {
            confermaPasswordText.setError(null);
        }

        if(errors==0 && !passwordText.getText().toString().equals(confermaPasswordText.getText().toString())){
            userNameText.setError("password e conferma password diverse");
            confermaPasswordText.setError("password e conferma password diverse");
            errors++;
        }

        if(userNameText.getText() == null || userNameText.getText().length() == 0){
            userNameText.setError("Inserire lo username");
            errors++;
        }
        else {
            userNameText.setError(null);
        }



        if(city.getText() == null || city.getText().length() == 0){
            city.setError("Inserire la citta");
            errors++;
        }
        else {
            city.setError(null);
        }

        if(numeroUtenti>0){
            try {
                List<Utente> users = (List<Utente>) InternalStorage.readObject(this, "users");

                for (Utente u : users){
                    if(u.getUsername().equals(userNameText.getText().toString())){
                        errors++;
                        userNameText.setError("Username già in uso");
                    }
                }

            }catch (IOException e) {
            Log.e("error", e.getMessage());
            }catch (ClassNotFoundException e) {
                Log.e("error", e.getMessage());
            }
        }


        if(errors==0){
            user = new Utente(userNameText.getText().toString(),passwordText.getText().toString(),city.getText().toString());
            user.setBornDate(this.datePickerFragment.getDate());
        }else
            return null;


        //se non ci sono errori ritorna true
        return user;
    }

    private boolean storeNewUser(Utente u){

        List<Utente> lista = new ArrayList<Utente>();

        try {
            if(numeroUtenti>0)
                lista = (List<Utente>) InternalStorage.readObject(this, "users");

            u.setOrdine(numeroUtenti);

            lista.add(u);

            // Save the list of entries to internal storage
            InternalStorage.writeObject(this, "users", lista);
            numeroUtenti++;

            return true;


        } catch (IOException e) {
            Log.e("error", e.getMessage());
        } catch (ClassNotFoundException e) {
        Log.e("error", e.getMessage());
    }
        return false;
    }
}
