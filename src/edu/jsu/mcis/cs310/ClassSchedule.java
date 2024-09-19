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
import java.util.Arrays;
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
    
    private static final int crn_index = 0;
    private static final int subject_index = 1;
    private static final int num_index = 2;
    private static final int description_index = 3;
    private static final int section_index = 4;
    private static final int type_index = 5;
    private static final int credits_index = 6;
    private static final int start_index = 7;
    private static final int end_index = 8;
    private static final int days_index = 9;
    private static final int where_index = 10;
    private static final int schedule_index = 11;
    private static final int instructor_index = 12;
    
    
    public String convertCsvToJsonString(List<String[]> csv) {

        // Initialize main JSON object
        JsonObject jsonMain = new JsonObject();
        
        // Json Headers objects
        JsonObject headerData = new JsonObject();
        
        // Maps for scheduletype, subject, and course
        JsonObject scheduleTypeMap = new JsonObject();  // For scheduletype (type: schedule)
        JsonObject subjectMap = new JsonObject();       // For subject (first part of num -> subject name)
        JsonObject courseMap = new JsonObject();        // For course details
        
        // Array for sections
        JsonObject sectionObject = new JsonObject();
        JsonArray instructorArray = new JsonArray();
        
        // Skip header and process each row
        for (int i = 1; i < csv.size(); i++) {
            String[] row = csv.get(i);
            
                String num = row[num_index]; // row 3
                String description = row[description_index]; // row 4
                String section = row[section_index]; // 5
                String type = row[type_index]; // 6
                String schedule = row[schedule_index]; // 12
                String instructor = row[instructor_index];
                int crn = Integer.parseInt(row[crn_index]);
            
                // Split subject and num
                String[] subjectAndNum = num.trim().split(" ");
                // 1stpart of num here
                String subjectid = subjectAndNum[0];
                // 2nd part here
                String numValue = subjectAndNum[1];
            
                // Populate the subject map if not already done
                if (!subjectMap.containsKey(subjectid)) {
                subjectMap.put(subjectid.trim(), row[subject_index].trim());
                }

                // Populate the course map if not already done
                if (!courseMap.containsKey(num)) {
                    // creates new JsonObject
                    JsonObject course = new JsonObject();
                    // Puts 1st part of num
                    course.put(SUBJECTID_COL_HEADER, subjectid.trim());
                    // Puts 2nd part of num
                    course.put(NUM_COL_HEADER, numValue.trim());
                    // Puts Description
                    course.put(DESCRIPTION_COL_HEADER, description.trim());
                    // Puts Credits
                    course.put(CREDITS_COL_HEADER, Integer.parseInt(row[credits_index]));
                    
                    courseMap.put(num, course);
                }
            
                // Populate the schedule type map if not already done
                if (!scheduleTypeMap.containsKey(type)) {
                    // puts tyoe followed by schedule 
                    scheduleTypeMap.put(type.trim(), schedule.trim());
                }
            
            
                // Create a section JSON object
                if(!sectionObject.containsKey(crn)){
                    
                sectionObject.put(CRN_COL_HEADER, crn);
                sectionObject.put(SUBJECTID_COL_HEADER, subjectid.trim());
                sectionObject.put(NUM_COL_HEADER, numValue.trim());
                sectionObject.put(SECTION_COL_HEADER, section.trim());
                sectionObject.put(TYPE_COL_HEADER, type.trim());
                sectionObject.put(START_COL_HEADER, row[start_index].trim());
                sectionObject.put(END_COL_HEADER, row[end_index].trim());
                sectionObject.put(DAYS_COL_HEADER, row[days_index].trim());
                sectionObject.put(WHERE_COL_HEADER, row[where_index].trim());
                
                // contain instructor in individual crn/ section object
                instructorArray.add(instructor);
                sectionObject.put(INSTRUCTOR_COL_HEADER, instructorArray);
                
                }
        }
        
        // Add all components to the main JSON object
        jsonMain.put("scheduletype", scheduleTypeMap);
        jsonMain.put("subject", subjectMap);
        jsonMain.put("course", courseMap);
        jsonMain.put("section", sectionObject);
        
        // Return serialized JSON string
        return Jsoner.serialize(jsonMain);
        
    }
    
    
    public String convertJsonToCsvString(JsonObject json) {
    
        return ""; // remove this!
        
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