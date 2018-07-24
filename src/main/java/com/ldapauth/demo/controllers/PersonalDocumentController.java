package com.ldapauth.demo.controllers;

import com.ldapauth.demo.entity.PersonalDocument;
import com.ldapauth.demo.entity.User;
import com.ldapauth.demo.repository.PersonalDocumentRepository;
import com.ldapauth.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.core.env.Environment;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

@Controller
public class PersonalDocumentController {
    @Autowired
    private PersonalDocumentRepository personalDocumentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Environment env;
    @RequestMapping(value = "/mydocuments",method = RequestMethod.POST)
    public ModelAndView postDocument(@Valid @ModelAttribute("newPersonalDocument") PersonalDocument personalDocument,@RequestParam(name = "uploadfile") MultipartFile file ,BindingResult bindingResult, @RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "search",defaultValue = "") String search,Model model){
        Page<PersonalDocument> personalDocuments = null;
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
        if (bindingResult.hasErrors()){
            return new ModelAndView("mydocuments","newPersonalDocument",new PersonalDocument());
        }
        if (!file.isEmpty()){

            try {
                file.transferTo(new File("/home/marwen/Bureau/uploads/"+file.getOriginalFilename()));
                personalDocument.setPersonalDocumentName(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
            personalDocument.setCreated(new Date());
            personalDocument.setUpdated(new Date());

        }

        personalDocument.setPersonalDocumentCreator(creator);
        personalDocumentRepository.save(personalDocument);
        if (search != ""){
            personalDocuments = personalDocumentRepository.searchByPersonalDocumentCreatorAndSearch("%"+search+"%",creator,new PageRequest(page,4));
        }
        else {
            personalDocuments = personalDocumentRepository.findByPersonalDocumentCreator(creator,new PageRequest(page,4));
        }
        int totalPage = personalDocuments.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
        ModelAndView modelAndView = new ModelAndView("mydocuments","newPersonalDocument",new PersonalDocument());
        model.addAttribute("pages",pages);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        model.addAttribute("myDocuments",personalDocuments);;
        return modelAndView;
    }
    @RequestMapping(value = "/mydocuments",method = RequestMethod.GET)
    public ModelAndView getMyDocuments(@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "search",defaultValue = "") String search,Model model){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
        Page<PersonalDocument> personalDocuments = null;
        ModelAndView modelAndView = new ModelAndView("mydocuments","newPersonalDocument",new PersonalDocument());
    if (search != ""){
     personalDocuments = personalDocumentRepository.searchByPersonalDocumentCreatorAndSearch("%"+search+"%",creator,new PageRequest(page,4));
    }
    else {
        personalDocuments = personalDocumentRepository.findByPersonalDocumentCreator(creator,new PageRequest(page,4));
    }
    int totalPage = personalDocuments.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
        model.addAttribute("pages",pages);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        model.addAttribute("myDocuments",personalDocuments);

    return modelAndView;
    }
    @RequestMapping(value = "/mydocument", method = RequestMethod.GET)
    public ModelAndView getDocumentById(@RequestParam(name = "documentId") Long documentId) {
    PersonalDocument personalDocument = personalDocumentRepository.getOne(documentId);
    ModelAndView modelAndView = new ModelAndView("mydocument");
    modelAndView.addObject("personalDocument",personalDocument);
    return modelAndView;
    }
    @RequestMapping(value = "/mydocuments/delete",method = RequestMethod.GET)
    public String deleteDocument(@RequestParam(name = "id") Long id,@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "search",defaultValue = "") String search){
        personalDocumentRepository.deleteById(id);
        return "redirect:/mydocuments";//?page="+page+"&search="+search;
    }

    @RequestMapping(value = "mydocuments/edit",method = RequestMethod.GET)
    public ModelAndView getEditPage(@RequestParam(name = "id") Long id){
        PersonalDocument personalDocument = personalDocumentRepository.getOne(id);
        ModelAndView modelAndView = new ModelAndView("editdocument","editDocument",personalDocument);
        modelAndView.addObject("documentId",id);
        return modelAndView;
    }
    @RequestMapping(value = "mydocuments/edit",method = RequestMethod.POST)
    public String editDocument(@ModelAttribute("editDocument") PersonalDocument editDocument,@RequestParam(name = "id") Long id){
        PersonalDocument personalDocument = personalDocumentRepository.getOne(id);
        personalDocument.setPersonalDocumentName(editDocument.getPersonalDocumentName());
        personalDocument.setPersonalDocumentDescription(editDocument.getPersonalDocumentDescription());
        personalDocument.setUpdated(new Date());
        personalDocumentRepository.save(personalDocument);
        return "redirect:/mydocuments";
    }
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("uploadfile") MultipartFile uploadfile) {

        try {
            // Get the filename and build the local file path
            String filename = uploadfile.getOriginalFilename();
            String directory = env.getProperty("demo.file.paths"); //"/home/marwen/Bureau/uploads";
            String filepath = Paths.get(directory, filename).toString();

            // Save the file locally
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadfile.getBytes());
            stream.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
