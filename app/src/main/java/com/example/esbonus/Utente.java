/*(Nome,Cognome,Matricola) Mauro Manca 65519 */

package com.example.esbonus;

import java.io.Serializable;
import java.util.Calendar;

public class Utente  implements Serializable {

    private String username;
    private String password;
    private String city;
    private Calendar bornDate;
    private int ordine; // ordine nella lista di registrati

    public Utente(String username,String password,String city){
        this.username=username;
        this.password=password;
        this.city=city;
    }

    public Utente(){
        this.username="";
        this.password="";
        this.city="";
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Calendar getBornDate() {
        return bornDate;
    }

    public void setBornDate(Calendar bornDate) {
        this.bornDate = bornDate;
    }

    public int getOrdine() {
        return ordine;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }
}
