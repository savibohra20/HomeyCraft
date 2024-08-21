package com.homeycraft.UserServiceModule.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homeycraft.UserServiceModule.exceptions.EmailIdAlreadyExistException;
import com.homeycraft.UserServiceModule.exceptions.EmailIdNotExistException;
import com.homeycraft.UserServiceModule.model.User;
import com.homeycraft.UserServiceModule.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) throws EmailIdAlreadyExistException {
        User  savedUser=null;
        String mailId =user.getEmailId();
        if (userRepository.existsById(mailId)) {
            throw  new  EmailIdAlreadyExistException("EmailId Already Exist...");
        }
        else {
            savedUser =userRepository.save(user);
        }

        return savedUser;
    }


    @Override
    public User getUserByEmailId(String emailId) throws EmailIdNotExistException {
        Optional<User>  optional = userRepository.findById(emailId);
        if(optional.isEmpty()) {
            throw new EmailIdNotExistException("Invalid Email Id");
        }
        return  optional.get();

    }

    @Override
    public User updateUser(User user) throws EmailIdNotExistException {
        Optional<User>  optional = userRepository.findById(user.getEmailId());
        if (optional.isEmpty()) {
            throw new EmailIdNotExistException("Invalid User Email Id");
        }
        User  user1 = optional.get();
        if (isValid(user.getGender())) {
            user1.setGender(user.getGender());
        }
        if ( isValid(user.getMobileNumber())){
            user1.setMobileNumber(user.getMobileNumber());
        }
        if (isValid(user.getPassword())) {
            user1.setPassword(user.getPassword());
        }
        if(isValid(user.getUsername())) {
            user1.setUsername(user.getUsername());
        }
        return  userRepository.save(user1)  ;
    }

    private   boolean isValid(String value) {
        return  (value!=null && !value.isBlank())?true:false;
    }

    @Override
    public boolean deleteUser(String emailId) {
        Optional<User>  optional = userRepository.findById(emailId);
        boolean isDeleted =false;
        if (optional.isPresent()) {
            userRepository.deleteById(emailId);
            isDeleted =true;
        }
        return isDeleted;

    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public boolean validateUser(User user) {
        //	boolean isValid=false;
//		Optional<User>  optional = userRepository.findById(user.getEmailId());
// if (optional.isPresent()) {
//     User user1 = optional.get();
//                 isValid=  user1.getPassword().equals(user.getPassword())?true:false;
//             }
//             return isValid;
// }
        Optional<User> optional = userRepository.findByEmailIdAndPassword(user.getEmailId(), user.getPassword());
        //retrun optional.isPresent()?true:false;
        return  optional.isPresent();

    }


    @Override
    public List<User> getUsersByRole(String role) {
        // TODO Auto-generated method stub
        return null;
    }
}