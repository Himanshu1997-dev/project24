package com.main.project;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.main.project.entities.User;
import com.main.project.service.MyService;


import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api")
public class Mycontroller {
	@Autowired
	public MyService myService;
	
    @PostMapping("/upload")
    public ResponseEntity<String> saveUsers(@RequestParam MultipartFile file) {
    		if(myService.checkExcelFormat(file)) {
    		myService.save(file);
			return ResponseEntity.ok("File imported successfully");
    		}
			return ResponseEntity.badRequest().body("Invalid file Selected");
        }
    
    @GetMapping("/getdata")
    public ResponseEntity<List<User>> getUsers(){
		return ResponseEntity.ok(myService.getallusers());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User users) {
    	users.setId(id);
    	List<User> usersToUpdate = List.of(users);
        myService.updateUsers(usersToUpdate);
        return ResponseEntity.ok("User updated successfully.");
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id, @RequestBody User users){
    	myService.deleteUsers(id);
    	return ResponseEntity.ok("Userid has deleted successfully");
    }
}  