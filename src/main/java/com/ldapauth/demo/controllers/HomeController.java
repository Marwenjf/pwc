package com.ldapauth.demo.controllers;

import com.ldapauth.demo.entity.Comment;
import com.ldapauth.demo.entity.User;
import com.ldapauth.demo.repository.CommentRepository;
import com.ldapauth.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public ModelAndView profile(Model model) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User myProfile = userRepository.findByUsername(name);
        List<Comment> comments = commentRepository.findByReceiver(myProfile);
        model.addAttribute("comments",comments);
        model.addAttribute("comment",new Comment());

        return new ModelAndView("profile","myProfile",myProfile);
    }
    @RequestMapping(value = "profile",method = RequestMethod.POST)
    public ModelAndView editProfile(@ModelAttribute("myProfile") User myProfile, @RequestParam(name = "picture") MultipartFile file , BindingResult bindingResult,Model model){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User editUser = userRepository.findByUsername(name);
        if (bindingResult.hasErrors()){
            List<Comment> comments = commentRepository.findByReceiver(editUser);
            model.addAttribute("comments",comments);
            model.addAttribute("comment",new Comment());

            return new ModelAndView("profile","myProfile",editUser);
        }
        if (!file.isEmpty()){

            try {
                file.transferTo(new File("/home/marwen/Bureau/uploads/"+file.getOriginalFilename()));
                if (editUser.getProfilePicture() != "/home/marwen/Bureau/uploads/"+file.getOriginalFilename())
                editUser.setProfilePicture("/home/marwen/Bureau/uploads/"+file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        editUser.setUpdated(new Date());
        if (myProfile.getPassword() != "")
            editUser.setPassword(myProfile.getPassword());
        if (myProfile.getBiography() != "")
        editUser.setBiography(myProfile.getBiography());
        if (myProfile.getEmail() != "")
        editUser.setEmail(myProfile.getEmail());
        if (myProfile.getUsername() != "")
        editUser.setUsername(myProfile.getUsername());
        userRepository.save(editUser);
        List<Comment> comments = commentRepository.findByReceiver(editUser);
        model.addAttribute("comments",comments);
        model.addAttribute("comment",new Comment());

        return new ModelAndView("profile","myProfile",editUser);
    }
    @RequestMapping(value = "messages", method = RequestMethod.GET)
    public ModelAndView messages() {
        return new ModelAndView("messages");
    }
    @RequestMapping(value = "chat", method = RequestMethod.GET)
    public ModelAndView chat() {
        return new ModelAndView("chat");
    }
    @RequestMapping(value = "getpicture",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getPicture(String userName) throws Exception{
        User myProfile = userRepository.findByUsername(userName);
    File file = new File(myProfile.getProfilePicture());
    return org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
    }
    @RequestMapping(value = "getfile",produces = MediaType.APPLICATION_PDF_VALUE, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public byte[] getFile(String fileName) throws Exception{

        File file = new File(fileName);
        return org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
    }
    @RequestMapping(value = "getfileimg",produces = MediaType.IMAGE_PNG_VALUE, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public byte[] getFileImage(String fileName) throws Exception{

        File file = new File(fileName);
        return org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
    }
    @RequestMapping(value = "comment",method = RequestMethod.POST)
    public String comment(@ModelAttribute("comment") Comment comment){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User sender = userRepository.findByUsername(name);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());
        System.out.println(sender.getUsername());
        comment.setSender(sender);
        comment.setReceiver(sender);
        commentRepository.save(comment);
        return "redirect:/profile";
    }
    @RequestMapping(value = "commnt",method = RequestMethod.POST)
    public String commnt(@ModelAttribute("comment") Comment comment,@RequestParam(name = "userName") String userName,Model model){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User sender = userRepository.findByUsername(name);
        User receiver = userRepository.findByUsername(userName);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());
        System.out.println(sender.getUsername());
        comment.setSender(sender);
        comment.setReceiver(receiver);
        commentRepository.save(comment);
        model.addAttribute("username",userName);
        return "redirect:/userprofile?username"+userName;
    }
    @RequestMapping(value = "userprofile", method = RequestMethod.GET)
    public ModelAndView userProfile(Model model,@RequestParam(name = "username") String userName) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User myProfile = userRepository.findByUsername(name);
        User userProfile = userRepository.findByUsername(userName);
        model.addAttribute("username",userName);
        System.out.println(myProfile.getProfilePicture());
        List<Comment> comments = commentRepository.findByReceiver(userProfile);
        model.addAttribute("comments",comments);
        model.addAttribute("comment",new Comment());
        return new ModelAndView("userprofile","userProfile",userProfile);
    }
}

