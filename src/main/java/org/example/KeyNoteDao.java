package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KeyNoteDao implements Dao<KeyNote> {

    private static String TABLE = "notes";

    private static KeyNoteDao instance;

    private Connection connection;
    private KeyNoteDao(Connection connection){
        this.connection = connection;
    }

    public static KeyNoteDao getInstance(Connection connection) {
        if (instance == null){
           instance = new KeyNoteDao(connection);
        }
        return instance;
    }

    private KeyNote parseObjectFromResultSet(ResultSet result) throws SQLException {
        KeyNote aNote = null;
        aNote = KeyNote.builder()
                .id(result.getInt("id"))
                .note(result.getNString("note"))
                .description(result.getNString("description"))
                .theme(result.getNString("theme"))
                .links(KeyNote.parseLinks(result.getString("links")))
                .createdOn(result.getDate("createdOn").toLocalDate())
                .build();
        return aNote;
    }

    public String buildLIKEQuery(KeyNote baseObject){
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM ").append(TABLE).append(" WHERE ");
        String[] properties = {"note", "description", "theme"};
        String[] values = {baseObject.getNote(),
                baseObject.getDescription(),
                baseObject.getTheme()};
        boolean haveAddProperty = false;
        for (int i = 0; i < properties.length; ++i){
            if(values[i] == null || values[i].isEmpty()){
                continue;
            }
            if(haveAddProperty){
                queryBuilder.append(" AND ");
            }
            queryBuilder.append(properties[i])
                    .append(" LIKE \"%")
                    .append(values[i])
                    .append("%\"");
            haveAddProperty = true;
        }
        if(haveAddProperty == false){
            return "";
        }
        return queryBuilder.append(";").toString();
    }

    @Override
    public KeyNote getById(int id) {
        String query = "SELECT * FROM "+TABLE+ " WHERE id=?;";
        KeyNote note = null;
        try{
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setInt(1, (int)id);
            ResultSet resultSet = statement.executeQuery();
            boolean notEmpty = resultSet.next();
            if(notEmpty == true)
                note = parseObjectFromResultSet(resultSet);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return note;
        }
    }

    @Override
    public List<KeyNote> findSimilarObjects(KeyNote baseObject) {
        List<KeyNote> results = new ArrayList<>();
        String query = buildLIKEQuery(baseObject);
        if (query.isEmpty()){
            return results;
        }
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                KeyNote note = parseObjectFromResultSet(resultSet);
                results.add(note);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public List<KeyNote> getAll() {
        String query = "SELECT * FROM "+TABLE+";";
        List<KeyNote> notes = new ArrayList<>();
        KeyNote aNote = null;
        try {
            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                aNote = parseObjectFromResultSet(result);
                if (aNote == null){
                    continue;
                }
                notes.add(aNote);
            }
            return notes;
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(KeyNote keyNote) {
        String query = "INSERT INTO "+TABLE+"(id, note, description, theme, links) VALUES (?, ?, ?, ?, ?);";
        try{
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setInt(1, keyNote.getId());
            statement.setString(2, keyNote.getNote());
            statement.setString(3, keyNote.getDescription());
            statement.setString(4, keyNote.getTheme());
            statement.setString(5, keyNote.compileLinks());
            statement.execute();
            return true;
        }
        catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        boolean updateResult = false;
        String query = "DELETE FROM "+TABLE+" WHERE id=?;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            updateResult = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            return updateResult;
        }
    }

    @Override
    public boolean update(KeyNote baseObject) {
        String query = "UPDATE "+TABLE+" SET note=?, description=?, theme=?, links=? WHERE id=?;";
        try{
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, baseObject.getNote());
            statement.setString(2, baseObject.getDescription());
            statement.setString(3, baseObject.getTheme());
            statement.setString(4, KeyNote.compileLinks(baseObject));
            statement.setInt(5, baseObject.getId());
            statement.execute();
            return true;
        }
        catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

}
