package com.main.project.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.main.project.entities.User;
import com.main.project.repo.UserRepository;

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

    public void save(MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            List<User> users = convertToExcel(is);
            insertUsers(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void insertUsers(List<User> users) {
        String sql = "INSERT INTO data (id, name, fathername, city, state, contact, email) VALUES (?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (User user : users) {
            batchArgs.add(new Object[]{
                    user.getId(), user.getName(), user.getFathername(),
                    user.getCity(), user.getState(), user.getContact(), user.getEmail()
            });
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    
    @Autowired UserRepository userRepository;
    
    public List<User> getallusers(){
		return userRepository.findAll();
    }
}
