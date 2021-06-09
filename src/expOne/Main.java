package expOne;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static final Charset encoding = StandardCharsets.UTF_8;

    public static final String name = "name";
    public static final String totalSales = "totalSales";
    public static final String salesPeriod = "salesPeriod";
    public static final String experienceMultiplier = "experienceMultiplier";

    public static final String topPerformersThreshold = "topPerformersThreshold";
    public static final String useExperienceMultiplier = "useExperienceMultiplier";
    public static final String periodLimit = "periodLimit";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<String> dataLines = new ArrayList<>();
        List<String> definitionLines = new ArrayList<>();
        Definition definition = new Definition();

        Map<String, Person> persons = new HashMap<>();
        Map<String, Double> firstResults = new HashMap<>();
        Map<String, Double> lastResults = new HashMap<>();


        String pathToDataFile = "D:\\0_Experiments\\Mentormate\\data.json";
        String pathToDefinitionFile = "D:\\0_Experiments\\Mentormate\\definition.json";
        String resultsPath = "D:\\0_Experiments\\Mentormate\\results.csv";


        System.out.println("Please, enter path to JSON data file (with UTF-8 encoding): ");
        pathToDataFile = scanner.nextLine();

        System.out.println("Please, enter path to JSON report definition file (with UTF-8 encoding): ");
        pathToDefinitionFile = scanner.nextLine();
        resultsPath = pathToDataFile.substring(0, pathToDefinitionFile.lastIndexOf("\\") + 1) + "results.csv";

        try {
            dataLines = readFile(pathToDataFile, "},");
        } catch (IOException e) {
            System.out.println("Requested file can not be read!");
        }

        try {
            definitionLines = readFile(pathToDefinitionFile, "},");
        } catch (IOException e) {
            System.out.println("Requested file can not be read!");
        }


        persons = mapJSONDataToPerson(dataLines);

        definition = mapJSONDefinitionToDefinition(definitionLines);

        firstResults = calculatePersonsScore(persons, definition);

        lastResults = calculateLastResults(firstResults, definition, persons);

        writeToCSV(lastResults, resultsPath);

    }

//----------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------
    public static List<String> readFile(String path, String separator) throws IOException {
        Scanner scanner = new Scanner(Paths.get(path), encoding.name());
        List<String> lines = new ArrayList<>();

        while(scanner.hasNext()){
            String content = scanner.useDelimiter(separator).next();
            lines.add(content);

        }
        scanner.close();

        return lines;
    }
//----------------------------------------------------------------------------------------------------------------------
/*
If useExprienceMultiplier is set to true:
Score = totalSales/salesPeriod*experienceMultiplier
If useExprienceMultiplier is set to false:
Score = totalSales/salesPeriod
 */

    public static double calculateScore(Person person, Definition definition){

      if(definition.getUseExperienceMultiplier().equals("true")){
            return person.getTotalSales()/person.getSalesPeriod()*person.getExperienceMultiplier();
        } else {
            return person.getTotalSales()/ person.getSalesPeriod();
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    /*
     have sales period that is equal or less than the periodLimit property;

    have score that is within the top X percent of the results, where X is defined by the
    topPerformersThreshold property of the report definition.
     */
    public static Map<String, Double> calculateLastResults(Map<String, Double> firstResults, Definition definition, Map<String, Person> persons){
        Map<String, Double> results = new HashMap<>();
        double threshold = calculatePerformanceThreshold(firstResults, definition);

        for (var result: firstResults.entrySet()) {
            if(result.getValue() > threshold && (definition.getPeriodLimit() >= persons.get(result.getKey()).getSalesPeriod())){
                results.put(result.getKey(), result.getValue());
            }
        }

        return results;
    }

//----------------------------------------------------------------------------------------------------------------------

    private static double calculatePerformanceThreshold(Map firstResults, Definition definition){
        double sumOfScores = 0;

        for (var firstResult:firstResults.values()) {
            sumOfScores = sumOfScores + (double)firstResult;
        }

        return sumOfScores/firstResults.size() - sumOfScores/firstResults.size() * (definition.getTopPerformersThreshold()/100);
    }
//----------------------------------------------------------------------------------------------------------------------

    public static void writeToCSV(Map<String, Double> lastResults, String resultsPath){

        StringBuilder sb = new StringBuilder();

        try (PrintWriter writer = new PrintWriter(new File(resultsPath))) {

            sb.append("Name , Score");
            sb.append(System.lineSeparator());

            for (var result:lastResults.entrySet()) {
                sb.append(result.getKey());
                sb.append(", ");
                sb.append(result.getValue());
                sb.append(System.lineSeparator());
            }

            writer.write(sb.toString());

            System.out.println("File with results was created.");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    public static Map<String, Person> mapJSONDataToPerson (List<String> lines){
        Map<String, Person> persons = new HashMap<>();
        for (String line: lines) {
            String nameR = line.substring(line.indexOf(name) + name.length() + 3, line.indexOf("\"", line.indexOf(name) + name.length() + 3));
            String total = line.substring(line.indexOf(totalSales) + totalSales.length() + 2, line.indexOf(",", line.indexOf(totalSales) + totalSales.length() + 2));
            String sales = line.substring(line.indexOf(salesPeriod) + salesPeriod.length() + 2, line.indexOf(",", line.indexOf(salesPeriod) + salesPeriod.length() + 2));
            String multiplier = line.substring(line.indexOf(experienceMultiplier) + experienceMultiplier.length() + 2, line.indexOf("\n", line.indexOf(experienceMultiplier) + experienceMultiplier.length() + 2));

            Person person = new Person(nameR, Double.parseDouble(total), Double.parseDouble(sales), Double.parseDouble(multiplier));
            persons.put(person.getName(), person);
        }

        return persons;
    }
//----------------------------------------------------------------------------------------------------------------------
    public static Definition mapJSONDefinitionToDefinition(List<String> definitionLines){
            Definition definition = new Definition();

        for (String definitionLine: definitionLines) {

            String top = definitionLine.substring(definitionLine.indexOf(topPerformersThreshold) + topPerformersThreshold.length() + 2, definitionLine.indexOf(",", definitionLine.indexOf(topPerformersThreshold) + topPerformersThreshold.length() + 2));
            String use = definitionLine.substring(definitionLine.indexOf(useExperienceMultiplier) + useExperienceMultiplier.length() + 2, definitionLine.indexOf(",", definitionLine.indexOf(useExperienceMultiplier) + 2));
            String period = definitionLine.substring(definitionLine.indexOf(periodLimit) + periodLimit.length() + 2, definitionLine.indexOf("\n", definitionLine.indexOf(periodLimit) + 2));

            definition.setTopPerformersThreshold(Double.parseDouble(top));
            definition.setUseExperienceMultiplier(use);
            definition.setPeriodLimit(Double.parseDouble(period));
        }

        return definition;
    }
//----------------------------------------------------------------------------------------------------------------------
    public static Map<String, Double> calculatePersonsScore(Map<String, Person> persons, Definition definition){
        Map<String, Double> firstResults = new HashMap<>();

        for (var person:persons.values()) {
            double score = 0;
            score = calculateScore(person, definition);
            firstResults.put(person.getName(), score);

        }

        return firstResults;
    }
//----------------------------------------------------------------------------------------------------------------------

}
