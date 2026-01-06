package com.virtual.demo.controller;

import com.virtual.demo.dto.DisplayDto;
import com.virtual.demo.dto.LoginDto;
import com.virtual.demo.dto.UpdateDto;
import com.virtual.demo.models.History;
import com.virtual.demo.models.User;
import com.virtual.demo.repositories.HistoryRepo;
import com.virtual.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    HistoryRepo historyRepo;
    @PostMapping("/register")
    public String register(@RequestBody User user)
    {
        userRepo.save(user);

        History h1 = new History();
        h1.setDescription("User Self Created : "+user.getUsername());
        historyRepo.save(h1);
        return "Signup Successful";
    }
    @PostMapping("/login")
    public String login(@RequestBody LoginDto u)
    {
        User user = userRepo.findByUsername(u.getUsername());
        if(user == null)
        {
            return"User Not Found";
        }
        if(!u.getPassword().equals(user.getPassword()))
        {
            return"Password Incorrect";
        }
        if(!u.getRole().equals(user.getRole()))
        {
            return"Role Incorrect";
        }
        return String.valueOf(user.getId());
    }
    @GetMapping("/get-details/{id}")
    public DisplayDto display(@PathVariable int id)
    {
        User user = userRepo.findById(id).orElseThrow(()->new RuntimeException("User Not Found") );
        DisplayDto displayDto = new DisplayDto();
        displayDto.setUsername(user.getUsername());
        displayDto.setBalance(user.getBalance());
        return displayDto;

    }

    @PostMapping("/update")
    public String update(@RequestBody UpdateDto obj)
    {
        User user = userRepo.findById(obj.getId()).orElseThrow(()->new RuntimeException("Not Found"));
        History h1 = new History();
        if(obj.getKey().equalsIgnoreCase("name"))
        {
            if(user.getName().equalsIgnoreCase(obj.getValue())) return "Cannot be same";
            h1.setDescription("User changed Name From "+user.getName()+" to "+obj.getValue());
            user.setName(obj.getValue());
        }
        else if(obj.getKey().equalsIgnoreCase("password"))
        {
            if(user.getPassword().equalsIgnoreCase(obj.getValue())) return "Cannot be same";
            h1.setDescription("User changed Password : "+user.getUsername());
            user.setPassword(obj.getValue());
        }
        else if(obj.getKey().equalsIgnoreCase("email"))
        {
            if(user.getEmail().equalsIgnoreCase(obj.getValue())) return "Cannot be same";
            User user2 = userRepo.findByEmail(obj.getValue());
            if(user2 != null) return "Email already exists";
            h1.setDescription("User changed Email From "+user.getEmail()+" to "+obj.getValue());
            user.setEmail(obj.getValue());
        }
        else
        {
            return "Invalid Key";
        }
        historyRepo.save(h1);
        userRepo.save(user);
        return "Update done Successfully";
    }
    @PostMapping("/add/{adminId}")
    public String add(@RequestBody User user,@PathVariable int adminId)
    {
        History h1 = new History();
        h1.setDescription("User "+user.getUsername()+ "Created By admin :"+adminId);
        userRepo.save(user);
        historyRepo.save(h1);
        return "Successfully added";
    }

    @GetMapping("/users")
    public List<User> getAllUsers(@RequestParam String sortBy, @RequestParam String order)
    {
        Sort sort;
        if(order.equalsIgnoreCase("desc"))
        {
            sort = Sort.by(sortBy).descending();
        }
        else
        {
            sort = Sort.by(sortBy).ascending();
        }
        return userRepo.findAllByRole("customer",sort);
    }

    @GetMapping("/users/{keyword}")
    public List<User> getUsers(@PathVariable String keyword)
    {
        return userRepo.findByUsernameContainingIgnoreCaseAndRole(keyword,"customer");
    }

    @DeleteMapping("/delete-user/{userId}/admin/{adminId}")
    public String deleteUser(@PathVariable int userId,@PathVariable int adminId)
    {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("Not Found"));
        if(user.getBalance()>0)
        {
            return "Balance should be zero";
        }
        History h1 = new History();
        h1.setDescription("User "+user.getUsername()+ " Deleted By admin :"+adminId);
        historyRepo.save(h1);
        userRepo.delete(user);
        return "User Deleted Successfully";
    }

}
