package com.main.project.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.main.project.entities.User;
import com.main.project.repo.UserRepository;
import java.lang.Object; 
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MyService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean checkExcelFormat(MultipartFile file) {
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    private List<User> convertToExcel(InputStream inputStream) throws IOException {
        List<User> list = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.iterator();
        int rowNumber = 0;

        while (iterator.hasNext()) {
            Row row = iterator.next();
            if (rowNumber == 0) {  // Skip header row
                rowNumber++;
                continue;
            }
            rowNumber++;

            Iterator<Cell> cells = row.iterator();
            int cid = 0;
            User user = new User();

            while (cells.hasNext()) {
                Cell cell = cells.next();
                switch (cid) {
                    case 0 -> user.setId((int) cell.getNumericCellValue());
                    case 1 -> user.setName(cell.getStringCellValue());
                    case 2 -> user.setFathername(cell.getStringCellValue());
                    case 3 -> user.setCity(cell.getStringCellValue());
                    case 4 -> user.setState(cell.getStringCellValue());
                    case 5 -> user.setContact((long) cell.getNumericCellValue());
                    case 6 -> user.setEmail(cell.getStringCellValue());
                }
                cid++;
            }
            list.add(user);
        }
        workbook.close();
        return list;
    }

    
    
    public void save(MultipartFile file) {
        try {
            List<User> users = convertToExcel(file.getInputStream());
            for(User user : users) {
			User existingUser = findbyID(user.getId());
            if(existingUser != null) {
            	insertUsers(users);}else {
            updateUsers(users);}
        }} catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        } catch (IOException e) { 
            e.printStackTrace();
        }
    }
    
    
    private User findbyID(int id) {
    	String sql = "SELECT * FROM data WHERE id = ?";
		return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(User.class),id);
    }
    
	    private void insertUsers(List<User> users) {
	        String sql = "INSERT INTO data (id, name, fathername, city, state, contact, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	        for (int i = 0; i < users.size(); i++) {  
	            User user = users.get(i);
	        	jdbcTemplate.update(sql, user.getId(), user.getName(), user.getFathername(),
	                    user.getCity(), user.getState(), user.getContact(), user.getEmail());
	        }
    }
    
	    
	    	    
	    public void updateUsers(List<User> users) {
	        for (int i = 0; i < users.size(); i++) { 
	            User user = users.get(i);
	            StringBuilder sql = new StringBuilder("UPDATE data SET ");
	            List<Object> values = new ArrayList<>();

	            if (user.getName() != null) { sql.append("name = ?, "); values.add(user.getName()); }
	            if (user.getFathername() != null) { sql.append("fathername = ?, "); values.add(user.getFathername()); }
	            if (user.getCity() != null) { sql.append("city = ?, "); values.add(user.getCity()); }
	            if (user.getState() != null) { sql.append("state = ?, "); values.add(user.getState()); }
	            if (user.getContact()>0L) { sql.append("contact = ?, "); values.add(user.getContact()); }
	            if (user.getEmail() != null) { sql.append("email = ?, "); values.add(user.getEmail()); }
	            if (values.isEmpty()) continue;

	            sql.setLength(sql.length() - 2);
	            sql.append(" WHERE id = ?");
	            values.add(user.getId());

	            jdbcTemplate.update(sql.toString(), values.toArray());
	        }
	    }
	    
	    
	    
    @Autowired UserRepository userRepository;
    
    public List<User> getallusers(){
		return userRepository.findAll();
    }
}
