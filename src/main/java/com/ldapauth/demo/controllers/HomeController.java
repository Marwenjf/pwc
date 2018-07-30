package com.ldapauth.demo.controllers;

import com.ldapauth.demo.entity.Comment;
import com.ldapauth.demo.entity.Invitation;
import com.ldapauth.demo.entity.PersonalDocument;
import com.ldapauth.demo.entity.User;
import com.ldapauth.demo.repository.CommentRepository;
import com.ldapauth.demo.repository.InvitationRepository;
import com.ldapauth.demo.repository.PersonalDocumentRepository;
import com.ldapauth.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PersonalDocumentRepository personalDocumentRepository;
    @Autowired
    InvitationRepository invitationRepository;
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home(Model model) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User myProfile = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(myProfile,"invitation");
        model.addAttribute("invitations",invitations);
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
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(myProfile,"invitation");
        model.addAttribute("invitations",invitations);
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
            List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(myProfile,"invitation");
            model.addAttribute("invitations",invitations);
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
        if (myProfile.getPassword() != "" && myProfile.getPassword() != editUser.getPassword())
            editUser.setPassword(myProfile.getPassword());
        if (myProfile.getBiography() != "" && myProfile.getBiography() != editUser.getBiography())
        editUser.setBiography(myProfile.getBiography());
        if (myProfile.getEmail() != "" && myProfile.getEmail() != editUser.getEmail())
        editUser.setEmail(myProfile.getEmail());
        if (myProfile.getUsername() != "" && myProfile.getUsername() != editUser.getUsername())
        editUser.setUsername(myProfile.getUsername());
        userRepository.save(editUser);
        List<Comment> comments = commentRepository.findByReceiver(editUser);
        model.addAttribute("comments",comments);
        model.addAttribute("comment",new Comment());
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(myProfile,"invitation");
        model.addAttribute("invitations",invitations);
        return new ModelAndView("profile","myProfile",editUser);
    }
    @RequestMapping(value = "messages", method = RequestMethod.GET)
    public ModelAndView messages(Model model) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User myProfile = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(myProfile,"invitation");
        model.addAttribute("invitations",invitations);
        return new ModelAndView("messages");

    }
    @RequestMapping(value = "chat", method = RequestMethod.GET)
    public ModelAndView chat(Model model) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User myProfile = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(myProfile,"invitation");
        model.addAttribute("invitations",invitations);
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
    @RequestMapping(value = "getfileimgpng",produces = MediaType.IMAGE_PNG_VALUE , consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public byte[] getFileImagePng(String fileName) throws Exception{

        File file = new File(fileName);
        return org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
    }
    @RequestMapping(value = "getfileimgjpg",produces = MediaType.IMAGE_JPEG_VALUE , consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public byte[] getFileImageJpg(String fileName) throws Exception{

        File file = new File(fileName);
        return org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
    }
    @RequestMapping(value = "getfiletxt",produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public byte[] getFileTxt(String fileName) throws Exception{

        File file = new File(fileName);
        return org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
    }
    @RequestMapping(value = "comment",method = RequestMethod.POST)
    public String comment(@ModelAttribute("comment") Comment comment,Model model){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User sender = userRepository.findByUsername(name);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());
        System.out.println(sender.getUsername());
        comment.setSender(sender);
        comment.setReceiver(sender);
        commentRepository.save(comment);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(sender,"invitation");
        model.addAttribute("invitations",invitations);
        return "redirect:/profile";
    }
    @RequestMapping(value = "commnt",method = RequestMethod.POST)
    public String commnt(@ModelAttribute("comment") Comment comment,@RequestParam(name = "userName") String userName,@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "search",defaultValue = "") String search,Model model){
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
        Page<PersonalDocument> personalDocuments = null;
        if (search != ""){
            personalDocuments = personalDocumentRepository.searchByPersonalDocumentCreatorAndSearch("%"+search+"%",receiver,new PageRequest(page,3));
        }
        else {
            personalDocuments = personalDocumentRepository.findByPersonalDocumentCreator(receiver,new PageRequest(page,3));
        }
        int totalPage = personalDocuments.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
        model.addAttribute("pages",pages);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        model.addAttribute("username",userName);
        model.addAttribute("myDocuments",personalDocuments);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(sender,"invitation");
        model.addAttribute("invitations",invitations);
        return "redirect:/userprofile?username="+userName+"&page="+page+"&search="+search;
    }
    @RequestMapping(value = "userprofile", method = RequestMethod.GET)
    public ModelAndView userProfile(Model model,@RequestParam(name = "username") String userName,@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "search",defaultValue = "") String search) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User myProfile = userRepository.findByUsername(name);
        User userProfile = userRepository.findByUsername(userName);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(myProfile,"invitation");
        model.addAttribute("invitations",invitations);
        model.addAttribute("username",userName);
        System.out.println(myProfile.getProfilePicture());
        Page<PersonalDocument> personalDocuments = null;

            personalDocuments = personalDocumentRepository.searchByPersonalDocumentCreatorAndPersonalDocumentVisibilityAndSearch("%"+search+"%",userProfile,"public",new PageRequest(page,3));


        int totalPage = personalDocuments.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
        List<Comment> comments = commentRepository.findByReceiver(userProfile);
        model.addAttribute("comments",comments);
        model.addAttribute("comment",new Comment());
        model.addAttribute("pages",pages);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        model.addAttribute("myDocuments",personalDocuments);
        return new ModelAndView("userprofile","userProfile",userProfile);
    }
}

