package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ClassSchedule {
    
    private final String CSV_FILENAME = "jsu_sp24_v1.csv";
    private final String JSON_FILENAME = "jsu_sp24_v1.json";
    
    private final String CRN_COL_HEADER = "crn";
    private final String SUBJECT_COL_HEADER = "subject";
    private final String NUM_COL_HEADER = "num";
    private final String DESCRIPTION_COL_HEADER = "description";
    private final String SECTION_COL_HEADER = "section";
    private final String TYPE_COL_HEADER = "type";
    private final String CREDITS_COL_HEADER = "credits";
    private final String START_COL_HEADER = "start";
    private final String END_COL_HEADER = "end";
    private final String DAYS_COL_HEADER = "days";
    private final String WHERE_COL_HEADER = "where";
    private final String SCHEDULE_COL_HEADER = "schedule";
    private final String INSTRUCTOR_COL_HEADER = "instructor";
    private final String SUBJECTID_COL_HEADER = "subjectid";
    
    public String convertCsvToJsonString(List<String[]> csv) {
        
        // Object Creation
        JsonObject jsonMain = new JsonObject();
        JsonArray array1 = new JsonArray();
        
        // iterator initialization + skip header row
        Iterator<String[]> iterator = csv.iterator();
        if (iterator.hasNext()){
            iterator.next();
        }
        
        // Iterating through rest of the rows
        while(iterator.hasNext()){
            String[] row = iterator.next();
            JsonObject portion = new JsonObject();
            
            try{
            
        portion.put("crn", row.length > 0 ? row[0] : "");
        portion.put("subject", row.length > 1 ? row[1] : "");
        portion.put("num", row.length > 2 ? row[2] : "");
        portion.put("description", row.length > 3 ? row[3] : "");
        portion.put("section", row.length > 4 ? row[4] : "");
        portion.put("type", row.length > 5 ? row[5] : "");
        portion.put("credits", row.length > 6 ? row[6] : "");
        portion.put("start", row.length > 7 ? row[7] : "");
        portion.put("end", row.length > 8 ? row[8] : "");
        portion.put("days", row.length > 9 ? row[9] : "");
        portion.put("where", row.length > 10 ? row[10] : "");
        portion.put("schedule", row.length > 11 ? row[11] : "");
        portion.put("instructor", row.length > 12 ? row[12] : "");
        portion.put("subjectid", row.length > 13 ? row[13] : "");
            
            }
            catch (Exception e ){
                System.err.println("Processing Row Error: " + java.util.Arrays.toString(row));
            }
            
            array1.add(portion);
            
        }
        
        jsonMain.put("section", array1);
        
        return Jsoner.serialize(jsonMain); // remove this!
        
    }
    
    public String convertJsonToCsvString(JsonObject json) {
        
        StringWriter Swriter1 = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(Swriter1, '\t', '"', '\\',"\n");
        
        // Headers
        String[] headers = {"crn", "subject", "num", "description", "section"
        , "type", "credits", "start", "end", "days", "where", "schedule", "instructor",
        "subjectid"};
        
        csvWriter.writeNext(headers);
        
        // Iterating over the file
        JsonArray portions = (JsonArray) json.get("section");
        Iterator<Object> iterator = portions.iterator();
        
        while(iterator.hasNext()){
            JsonObject portion = (JsonObject) iterator.next();
            String[] row = new String[headers.length];
            
            row[0] = portion.get("crn") != null ? portion.get("crn").toString() : "";
            row[1] = portion.get("subject") != null ? portion.get("subject").toString() : "";
            row[2] = portion.get("description") != null ? portion.get("description").toString() : "";
            row[3] = portion.get("section") != null ? portion.get("section").toString() : "";
            row[4] = portion.get("type") != null ? portion.get("type").toString() : "";
            row[5] = portion.get("credits") != null ? portion.get("credits").toString() : "";
            row[6] = portion.get("start") != null ? portion.get("start").toString() : "";
            row[7] = portion.get("end") != null ? portion.get("end").toString() : "";
            row[8] = portion.get("days") != null ? portion.get("days").toString() : "";
            row[9] = portion.get("where") != null ? portion.get("where").toString() : "";
            row[10] = portion.get("schedule") != null ? portion.get("schedule").toString() : "";
            row[11] = portion.get("instructor") != null ? portion.get("instructor").toString() : "";
            row[12] = portion.get("subjectid") != null ? portion.get("subjectid").toString() : ""; 
            // Handle missing 'subjectid'
            
            csvWriter.writeNext(row);
        }

        return Swriter1.toString(); // remove this!
        
    }
    
    public JsonObject getJson() {
        
        JsonObject json = getJson(getInputFileData(JSON_FILENAME));
        return json;
        
    }
    
    public JsonObject getJson(String input) {
        
        JsonObject json = null;
        
        try {
            json = (JsonObject)Jsoner.deserialize(input);
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return json;
        
    }
    
    public List<String[]> getCsv() {
        
        List<String[]> csv = getCsv(getInputFileData(CSV_FILENAME));
        return csv;
        
    }
    
    public List<String[]> getCsv(String input) {
        
        List<String[]> csv = null;
        
        try {
            
            CSVReader reader = new CSVReaderBuilder(new StringReader(input)).withCSVParser(new CSVParserBuilder().withSeparator('\t').build()).build();
            csv = reader.readAll();
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return csv;
        
    }
    
    public String getCsvString(List<String[]> csv) {
        
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer, '\t', '"', '\\', "\n");
        
        csvWriter.writeAll(csv);
        
        return writer.toString();
        
    }
    
    private String getInputFileData(String filename) {
        
        StringBuilder buffer = new StringBuilder();
        String line;
        
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        
        try {
        
            BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("resources" + File.separator + filename)));

            while((line = reader.readLine()) != null) {
                buffer.append(line).append('\n');
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return buffer.toString();
        
    }
    
}