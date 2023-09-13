package org.example;

import lombok.*;
import pl.mjaron.etudes.Int;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Getter
@Setter
@ToString
@Builder
public class KeyNote {

    private Integer id;
    private String note;
    private String description;
    private String theme;
    private String[] links;
    private LocalDate createdOn;

    public static String linkDelimiter = ";";

    public static KeyNote createFromInput(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        System.out.println("Note:");
        String note = scanner.nextLine().strip();
        System.out.println("Description:");
        String description = scanner.nextLine().strip();
        System.out.println("Theme:");
        String theme = scanner.nextLine().strip();
        System.out.println("Links, separate by ';'");
        String linksStr = scanner.nextLine().strip();
        String data = note+theme+description;
        return KeyNote.builder()
                .id(data.hashCode())
                .note(note)
                .description(description)
                .links(parseLinks(linksStr))
                .createdOn(LocalDate.now())
                .theme(theme).build();
    }

    public String compileLinks(){
        return compileLinks(this);
    }

    public static String compileLinks(KeyNote note){
        List<String> links = Arrays.asList(note.getLinks());
        String linksStr = links.stream()
                .reduce("",
                        (String partialResult, String element) -> partialResult + KeyNote.linkDelimiter + element
                );
        return linksStr;
    }

    public static String[] parseLinks(String linksStr){
        String[] links = linksStr.split(KeyNote.linkDelimiter);
        return Arrays.stream(links).filter(e -> !e.isBlank()).toArray(String[]::new);
    }

    public String toMDFormat(){
        StringBuilder result = new StringBuilder();
        result.append("# Meta information\n");
        result.append("## ID\n").append(this.getId()).append("\n");
        result.append("## Theme\n").append(this.getTheme()).append("\n");
        result.append("## Create On\n").append(this.getCreatedOn()).append("\n");
        result.append("\n# Content");
        result.append("\n## Note\n").append(this.getNote());
        result.append("\n## Description\n").append(this.getDescription());
        result.append("\n## Links\n");
        if(this.getLinks() == null || this.getLinks().length == 0){
            return result.toString();
        }
        for(String link: this.getLinks()){
            result.append("- ").append(link).append("\n");
        }
        return result.toString();
    }

    private void setId() {
        StringBuilder content = new StringBuilder();
        content.append(this.getNote())
                .append(this.getDescription())
                .append(this.getTheme())
                .append(this.getCreatedOn().toString());
        this.id = content.toString().hashCode();
    }

    public void setCreatedOn(){
        this.createdOn = LocalDate.now();
    }

    public static KeyNote parseFromFilePath(String filePath, boolean readID) throws IOException {
        String data = "";
        String schema = "";
        String fileContent = OSHelper.readFile(new File(filePath));
        int id = 89;
        if(readID){
            // parse ID
            data = collectDataIn(fileContent, "## ID", "## Theme");
            id = Integer.parseInt(data);
        }
        // parse theme
        data = collectDataIn(fileContent, "## Theme", "## Create On");
        String theme = data;
        // parse date
        data = collectDataIn(fileContent,"## Create On", "# Content");
        LocalDate date = LocalDate.parse(data);
        // parse note
        data = collectDataIn(fileContent, "## Note", "## Description");
        String note = data;
        // parse description
        data = collectDataIn(fileContent, "## Description", "## Links");
        String description = data;
        // parse links
        data = collectDataIn(fileContent, "## Links", "");
        data = data.replace("-", "");
        StringBuilder linksStr = new StringBuilder();
        data.lines().forEach(line ->
                linksStr.append(line.strip())
                        .append(linkDelimiter)
        );
        // return
        return KeyNote.builder().id(id)
                .createdOn(date)
                .note(note)
                .theme(theme)
                .description(description)
                .links(parseLinks(linksStr.toString()))
                .build();
    }

    public static KeyNote createFromFilePath(String filePath) throws IOException {
        KeyNote keyNote = parseFromFilePath(filePath, false);
        keyNote.setId();
        keyNote.setCreatedOn(LocalDate.now());
        return keyNote;
    }
    private static String collectDataIn(String data, String fromStr, String toStr){
        int from = 0;
        int to = 0;
        from = data.indexOf(fromStr) + fromStr.length();
        if (toStr.length() == 0){
            return data.substring(from).strip();
        }
        to = data.indexOf(toStr);
        return data.substring(from, to).strip();
    }
}