package com.main.project.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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
	public boolean checkExcelFormat(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        }
        return false;
    }

    public List<User> savedata(InputStream is) throws IOException{
        List<User> list = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            int rowNumber=0;
            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                if (rowNumber == 0) {
                    continue;
                }
                Iterator<Cell> cells = row.iterator();
                int cid=0;
                User user = new User();
                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    switch (cid) {
                        case 0:
                            user.setId((int) cell.getNumericCellValue());
                            break;
                        case 1:
                            user.setName((String) cell.getStringCellValue());
                            break;
                        case 2:
                            user.setFathername((String) cell.getStringCellValue());
                            break;
                        case 3:
                            user.setCity((String) cell.getStringCellValue());
                            break;
                        case 4:
                            user.setState((String) cell.getStringCellValue());
                            break;
                        case 5:
                            user.setContact((int) cell.getNumericCellValue());
                            break;
                        case 6:
                            user.setEmail((String) cell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cid++;
                }
                list.add(user);
                workbook.close();
            }
        return userRepository.saveAll(list);
}   
     
        
        
        
     @Autowired
     
     private UserRepository userRepository;
        
     
     public List<User> getallUsers(){
    	 return this.userRepository.findAll();
     }
     
}