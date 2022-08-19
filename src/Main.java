import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Long.parseLong;

public class Main {

    public static void main(String[] args) {
        ArrayList<Plant>list = new ArrayList<>();
        Plants plants = new Plants(list);
            try{
                plants.proceedFile(list);
                plants.getWateringInfoForWholeList(list);   //vypis zalivky pro vsechny kvetiny
                Plant firstAddedPlant = new Plant("Bazalka v kuchyni","",LocalDate.of(2021,9,4),
                        LocalDate.of(2021,9,4),3);
                Plant secondAddedPlant = new Plant("Tymian","",LocalDate.of(2021,9,4),
                        LocalDate.of(2021,9,4),3);
                plants.addPlant(firstAddedPlant);
                plants.addPlant(secondAddedPlant);
                plants.getWateringInfoForWholeList(list); //kontrolny vypis
                plants.removePlant(secondAddedPlant);
                plants.getWateringInfoForWholeList(list); //kontrolny vypis
                plants.writeNewData(list);
            }catch(Exception ex){
                System.err.println(ex.getLocalizedMessage());
            }



    }

}

