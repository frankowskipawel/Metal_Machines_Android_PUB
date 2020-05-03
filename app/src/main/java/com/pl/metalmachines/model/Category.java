package com.pl.metalmachines.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Category {

    private String id;
    private String name;
    private ArrayList serviceOperationsList;
    private HashMap<String, ArrayList<String>> trainingOperationsMap;

    public Category() {
    }

    public Category(String id, String name, ArrayList serviceOperationsList, HashMap<String, ArrayList<String>> trainingOperationsMap) {
        this.id = id;
        this.name = name;
        this.serviceOperationsList = serviceOperationsList;
        this.trainingOperationsMap = trainingOperationsMap;
    }

    public HashMap<String, ArrayList<String>> getOperationsListOfOperatorsTrainingProtocolForWypalarkiLaserowe() {
        HashMap<String, ArrayList<String>> map = new HashMap<>();

        ArrayList<String> valueList1 = new ArrayList<>();
        map.put("Urządzenia wycinarki laserowej a BHP", valueList1);
        valueList1.add("Omówienie zasad BHP (obsługa wycinarki)");
        valueList1.add("Stabilizator (obsługa)");
        valueList1.add("Szafa elektryczna (obsługa)");
        valueList1.add("Chłodnica wody (obsługa)");
        valueList1.add("Źródło wiązki laserowej (obsługa)");
        valueList1.add("Smarowanie wycinarki (obsługa)");
        valueList1.add("Głowica laserowa (obsługa)");
        valueList1.add("Wentylator / Filtrowentylacja (obsługa)");

        ArrayList<String> valueList2 = new ArrayList<>();
        map.put("Części i materiały eksploatacyjne", valueList2);
        valueList2.add("Dysza tnąca (budowa, wymiana)");
        valueList2.add("Szkiełko ochronne (wymiana, czyszczenie)");
        valueList2.add("Pierścień ceramiczny, uszczelniacz (wymiana)");
        valueList2.add("Czynnik chłodzący [DEMI](wymiana, ryzyko)");
        valueList2.add("Czynnik smarujący [kl.#68](uzupełnianie, ryzyko)");

        ArrayList<String> valueList3 = new ArrayList<>();
        map.put("Oprogramowanie", valueList3);
        valueList3.add("System kontroli cięcia CypCut");
        valueList3.add("System kontroli cięcia TubePro");
        valueList3.add("System kontroli cięcia CypTube");
        valueList3.add("System kontroli wysokości BCS100");
        valueList3.add("Ustawianie geometrii wiązki laserowej");
        valueList3.add("Oprogramowanie TubesT Lite");
        valueList3.add("Oprogramowanie CypNest");
        valueList3.add("Oprogramowanie TubesT");

        ArrayList<String> valueList4 = new ArrayList<>();
        map.put("Gazy techniczne", valueList4);
        valueList4.add("Tlen O2 (99,95%; 0,5 – 10 Bar, obsługa reduktora)");
        valueList4.add("Azot N2 (99,95%; 10 – 25 Bar, obsługa reduktora)");
        valueList4.add("Powietrze (osuszone max. 60%; 10 Bar; bez oleju)");

        ArrayList<String> valueList5 = new ArrayList<>();
        map.put("Obsługa wycinarki", valueList5);
        valueList5.add("Procedura włączania wycinarki");
        valueList5.add("Referencje maszyny, kalibracja BCS100");
        valueList5.add("Zmiana/wprowadzenie materiału do wycinarki");
        valueList5.add("Procedury ładowania i używania parametrów cięcia");
        valueList5.add("Wycinanie pokazowe");
        valueList5.add("Procedura wyłączania wycinarki laserowej");

        ArrayList<String> valueList6 = new ArrayList<>();
        map.put("Umiejętności praktyczne operatorów", valueList6);
        valueList6.add("Uruchomienie wycinarki przez operatorów");
        valueList6.add("Wczytywanie, zapisywanie rysunków");
        valueList6.add("Powielanie detali na rysunku");
        valueList6.add("Tworzenie rozkładu detali na arkuszu(nesting)");
        valueList6.add("Załadunek materiału, wymiana stołów");
        valueList6.add("Zmiana parametrów cięcia");
        valueList6.add("Uruchomienie wentylatora / filtrowentylacji");
        valueList6.add("Wycinanie próbne");
        valueList6.add("Wymiana, czyszczenie części eksploatacyjnych");
        valueList6.add("Wyłączanie wycinarki przez operatorów");

        return map;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getServiceOperationsList() {
        return serviceOperationsList;
    }

    public void setServiceOperationsList(ArrayList serviceOperationsList) {
        this.serviceOperationsList = serviceOperationsList;
    }

    public HashMap<String, ArrayList<String>> getTrainingOperationsMap() {
        return trainingOperationsMap;
    }

    public void setTrainingOperationsMap(HashMap<String, ArrayList<String>> trainingOperationsMap) {
        this.trainingOperationsMap = trainingOperationsMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(name, category.name) &&
                Objects.equals(serviceOperationsList, category.serviceOperationsList) &&
                Objects.equals(trainingOperationsMap, category.trainingOperationsMap);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", serviceOperationsList=" + serviceOperationsList +
                ", trainingOperationsMap=" + trainingOperationsMap +
                '}';
    }
}