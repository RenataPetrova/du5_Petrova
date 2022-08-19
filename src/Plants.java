import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Long.parseLong;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Plants {
    private final String FILEPATH = "C:\\Users\\Lukas\\IdeaProjects\\du5_Petrova\\kvetiny.txt";
    private final String OUTPUTFILE = "kvetiny.txt";
    private List<Plant> list;
    public Plants(ArrayList<Plant> list){
        this.list = list;
    }
    public void addPlant(Plant newPlant) {
        list.add(newPlant);
    }
    public Plant getPlant(int index){
        return list.get(index);
    }
    public void removePlant(Plant plant){
        list.remove(plant);
    }
    public List<Plant> proceedFile(List<Plant> list) throws PlantException {
        Scanner scanner = readFile();
        list = convertStringToNewDataType(list, scanner);
        scanner.close();
        return list;
    }
    private Scanner readFile() throws PlantException{
        File file = new File(FILEPATH);
        try{
            Scanner scanner = new Scanner(file);    //muze nastat chyba pri nacitani
            return scanner;
        }catch(Exception ex) {
            throw new PlantException("Soubor nenalezen. " + ex.getLocalizedMessage());
        }
    }
    private List<Plant> convertStringToNewDataType(List<Plant> list, Scanner scanner) throws PlantException{
        String name, notes;
        LocalDate planted, watering;
        LocalDate nonDate = LocalDate.of(1900,1,1);
        long frequencyOfWatering;
        int line = 1;

        while (scanner.hasNextLine()) {
            String record = scanner.nextLine();
            String[] recordSplit = record.split("\t");
            name = recordSplit[0];
            notes = recordSplit[1];
            try{
                watering = LocalDate.parse(recordSplit[3]); //muze nastat chyba v konverzi
                planted = LocalDate.parse(recordSplit[4]);  //muze nastat chyba v konverzi
            }catch(Exception ex){
                planted = nonDate;
                watering = nonDate;
                throw new PlantException("Zaliti, nebo zasazeni na radku: " + line + " neni format datumu. "
                        + ex.getLocalizedMessage());
                //System.err.println("Zaliti, nebo Zasazeni neni format datumu. " + ex.getLocalizedMessage());
            }
            //provede se jenom kdyz konverze na datumy probehla v poradku
            if (planted != nonDate ||
                    watering != nonDate) {
                //rozhodne jestli jsou vlozena datumy logicke, ne v budoucnosti, atd.
                if (analyzeDate(planted, watering) == false) {
                    throw new PlantException("Datum zasazeni nebo zaliti na radku: " + line + " neni logicky.");
                }
            }
            try{
                frequencyOfWatering = parseLong(recordSplit[2]);    //muze nastat chyba v konverzi
                if (frequencyOfWatering <= 0) {
                    throw new PlantException("Frekvence zalevani na radku: " + line + " neni prirozene cislo.");
                }
            }catch(Exception ex){
                throw new PlantException("Frekvence zalevani na radku: " + line + " neni prirozene cislo.");

            }

            Plant plant = new Plant(name, notes, planted, watering, frequencyOfWatering);
            list.add(plant);
            line++;
        }
        return list;
    }

    public void getWateringInfoForWholeList(List<Plant> list){
        for(Plant plant:list){
            plant.getWateringInfo();
        }
        System.out.println();
    }
    private boolean analyzeDate(LocalDate planted,LocalDate watering) {
        boolean result = true;
        if (planted.isAfter(LocalDate.now()) || watering.isAfter(LocalDate.now()) ||
                    planted.isAfter(watering)) {
                result =  false;
            }
        return result;
    }
    public void writeNewData(List<Plant> list){
        String recordToWrite;
        try (PrintWriter outputWriter =
                     new PrintWriter(new FileWriter(FILEPATH))) {

            for (Plant plant : list) {
                recordToWrite = plant.getName() + "\t" +
                                plant.getNotes() + "\t" +
                                plant.getFrequencyOfWatering() + "\t" +
                                plant.getWatering() + "\t" +
                                plant.getPlanted() + "\n";

                outputWriter.print(recordToWrite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
