package com.ldapauth.demo.controllers;



import com.ldapauth.demo.entity.*;
import com.ldapauth.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class GroupController {

        @Autowired
        private GroupeRepository groupeRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private GroupDocumentRepository groupDocumentRepository;
        @Autowired
        private GroupCommentRepository groupCommentRepository;
        @Autowired
        private InvitationRepository invitationRepository;


    @RequestMapping(value = "/mygroups",method = RequestMethod.POST)
    public ModelAndView addMyGroup(@Valid @ModelAttribute("newGroup") Groupe newGroup,BindingResult bindingResult, @RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "search",defaultValue = "") String search, Model model){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(creator,"invitation");
        model.addAttribute("invitations",invitations);
        List<User> users = userRepository.findAll();
        users.remove(creator);
        model.addAttribute("users",users);

        if (bindingResult.hasErrors())
        {
            Page<Groupe> groupes = groupeRepository.findByCreator(creator,new PageRequest(page,3));

            int totalPage = groupes.getTotalPages();
            int pages[] = new int[totalPage];
            for (int i = 0; i <totalPage ; i++) {
                pages[i] = i;
            }

            model.addAttribute("pages",pages);
            model.addAttribute("groups",groupes);
            model.addAttribute("currentPage",page);
            model.addAttribute("search",search);
            return new ModelAndView("mygroups","newGroup", new Groupe());
        }
        newGroup.setCreated(new Date());
        newGroup.setCreator(creator);
        groupeRepository.save(newGroup);
        Page<Groupe> groupes = groupeRepository.findByCreator(creator,new PageRequest(page,3));

        int totalPage = groupes.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("pages",pages);
        model.addAttribute("groups",groupes);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        List<User> chosenUser = new ArrayList<User>();
        model.addAttribute("chosenUsers",chosenUser);
        ModelAndView modelAndView = new ModelAndView("mygroups","newGroup", new Groupe());
        return modelAndView;
    }


    @RequestMapping(value = "/mygroups", method = RequestMethod.GET)
    public ModelAndView getMyGroups(Model model,@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "search",defaultValue = "") String search){
        Page<Groupe> myGroupes = null;
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(creator,"invitation");
        model.addAttribute("invitations",invitations);
        if (search != "")
        {myGroupes = groupeRepository.searchByCreatorAndSearch("%"+search+"%",creator,new PageRequest(page,3));
        }
        else
        {myGroupes = groupeRepository.findByCreator(creator,new PageRequest(page,3));}
        int totalPage = myGroupes.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
        model.addAttribute("totalPage",totalPage);
        List<User> users = userRepository.findAll();
        users.remove(creator);
        List<User> chosenUsers = new ArrayList<User>();
        model.addAttribute("chosenUsers",chosenUsers);
        model.addAttribute("users",users);
        model.addAttribute("pages",pages);
        model.addAttribute("groups",myGroupes);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        ModelAndView modelAndView = new ModelAndView("mygroups","newGroup", new Groupe());
        return modelAndView;
    }
    @RequestMapping(value = "group", method = RequestMethod.GET)
    public String getGroup(@RequestParam(name = "idGroupe") Long idGroupe,@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "search",defaultValue = "") String search,Model model){
            Groupe group = groupeRepository.getOne(idGroupe);
            Page<GroupDocument> groupDocuments = null;
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(creator,"invitation");
        model.addAttribute("invitations",invitations);
        Invitation invitation = new Invitation();
        model.addAttribute("invitation",invitation);

        if (creator.getMyGroups().contains(group) || creator.getReceiverInvitations().contains(invitationRepository.findByGroupeAndStatusAndReceiver(group,"accept",creator))){

        if (search !=""){
            groupDocuments = groupDocumentRepository.searchByGroupeAndSearch("%"+search+"%",group,new PageRequest(page,4));
        }
        else {
            groupDocuments = groupDocumentRepository.findByGroupe(group,new PageRequest(page,4));
        }

        int totalPage = groupDocuments.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
            List<User> users = userRepository.findAll();
            users.remove(creator);
            for (int i = 0;i<users.size();i++){
                for (Invitation groupInvitation:group.getInvitations()) {
                    if (groupInvitation.getReceiver() == users.get(i) && groupInvitation.getStatus() == "accept"){
                        users.remove(i);
                    }
                    if (groupInvitation.getReceiver() == users.get(i) && groupInvitation.getStatus() == "invitation"){
                        users.remove(i);
                    }
                }
            }
           model.addAttribute("users",users);
            System.out.println(users.size());
        model.addAttribute("totalPage",totalPage);
        List<GroupComment> groupComments = groupCommentRepository.findByGroupe(group);
        model.addAttribute("groupComments",groupComments);
        model.addAttribute("pages",pages);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        model.addAttribute("groupDocuments",groupDocuments);
        model.addAttribute("groupDocument",new GroupDocument());
        model.addAttribute("group",group);
        model.addAttribute("idGroupe",idGroupe);
        model.addAttribute("gComment",new GroupComment());
        model.addAttribute("groupDocument",new GroupDocument());
            return "group";
        }
       return "redirect:/groups";
    }

    @RequestMapping(value = "/uploadfile" , method = RequestMethod.POST)
    public String addFile(@RequestParam(name = "idGroupe") Long idGroupe,@ModelAttribute("groupDocument") GroupDocument groupDocument,@RequestParam(name = "uploadfilegroup") MultipartFile file,@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "search",defaultValue = "") String search,Model model){
        Page<GroupDocument> groupDocuments = null;
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(creator,"invitation");
        model.addAttribute("invitations",invitations);
        Groupe group = groupeRepository.getOne(idGroupe);
        if (!file.isEmpty()){
            try {
                file.transferTo(new File("/home/marwen/Bureau/uploads/"+file.getOriginalFilename()));
                groupDocument.setDocumentName(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
            groupDocument.setCreated(new Date());
            groupDocument.setUpdated(new Date());
            groupDocument.setDocumentCreator(creator);
            groupDocument.setDocumentDescription("No description");
            groupDocument.setGroupe(group);
            groupDocumentRepository.save(groupDocument);

        }
        if (search !=""){
            groupDocuments = groupDocumentRepository.searchByGroupeAndSearch("%"+search+"%",group,new PageRequest(page,4));
        }
        else {
            groupDocuments = groupDocumentRepository.findByGroupe(group,new PageRequest(page,4));
        }

        int totalPage = groupDocuments.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
        List<User> users = userRepository.findAll();
        users.remove(creator);
        for (int i = 0;i<users.size();i++){
            for (Invitation groupInvitation:group.getInvitations()) {
                if (groupInvitation.getReceiver() == users.get(i) && groupInvitation.getStatus() == "accept"){
                    users.remove(i);
                }
                if (groupInvitation.getReceiver() == users.get(i) && groupInvitation.getStatus() == "invitation"){
                    users.remove(i);
                }
            }
        }
        System.out.println(users.size());
        model.addAttribute("users",users);
        model.addAttribute("totalPage",totalPage);
        List<GroupComment> groupComments = groupCommentRepository.findByGroupe(group);
        model.addAttribute("groupComments",groupComments);
        model.addAttribute("pages",pages);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        model.addAttribute("groupDocuments",groupDocuments);
        model.addAttribute("groupDocument",new GroupDocument());
        model.addAttribute("group",group);
        Invitation invitation = new Invitation();
        model.addAttribute("invitation",invitation);
        model.addAttribute("gComment",new GroupComment());

        return "redirect:/group?idGroupe="+idGroupe+"&page="+page+"&search="+search;
    }

    @RequestMapping(value = "/groups/delete",method = RequestMethod.GET)
    public String deleteGroup(Model model,@RequestParam(name = "id") Long id,@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "search",defaultValue = "") String search){
        groupeRepository.deleteById(id);
        return "redirect:/groups?page="+page+"&search="+search;
    }
    @RequestMapping(value = "/mygroups/delete",method = RequestMethod.GET)
    public String deleteMyGroup(Model model,@RequestParam(name = "id") Long id,@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "search",defaultValue = "") String search)
    {   org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(creator,"invitation");
        model.addAttribute("invitations",invitations);
        groupeRepository.deleteById(id);
        return "redirect:/mygroups?page="+page+"&search="+search;
    }
    @RequestMapping(value = "editmygroup",method = RequestMethod.GET)
    public ModelAndView geteditPage(@RequestParam(name = "id") Long id,Model model){
        Groupe groupe = groupeRepository.getOne(id);
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(creator,"invitation");
        model.addAttribute("invitations",invitations);
        List<User> users = userRepository.findAll();
        users.remove(creator);
        for (int i = 0;i<users.size();i++){
            for (Invitation groupInvitation:groupeRepository.getOne(id).getInvitations()) {
                if (groupInvitation.getReceiver() == users.get(i) && groupInvitation.getStatus() == "accept"){
                    users.remove(i);
                }
                if (groupInvitation.getReceiver() == users.get(i) && groupInvitation.getStatus() == "invitation"){
                    users.remove(i);
                }
            }
        }
        System.out.println(users.size());
        model.addAttribute("users",users);
        model.addAttribute("groupId",id);
        Invitation invitation = new Invitation();
        model.addAttribute("invitation",invitation);
        ModelAndView modelAndView = new ModelAndView("edit","editGroup",groupe);
      return modelAndView;
    }
    @RequestMapping(value = "mygroups/edit",method = RequestMethod.POST)
    public String editGroup(@ModelAttribute("editGroup") Groupe editGroup,@RequestParam(name = "id") Long id,Model model){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(creator,"invitation");
        model.addAttribute("invitations",invitations);
        Groupe groupe = groupeRepository.getOne(id);
        groupe.setUpdated(new Date());
        groupe.setGroupName(editGroup.getGroupName());
        groupeRepository.save(groupe);

        return "redirect:/mygroups";
    }
    @RequestMapping(value = "mygroups/invitation",method = RequestMethod.POST)
    public String addInvitation(@ModelAttribute("invitation") Invitation invitation,@RequestParam(name = "id") Long id,Model model){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User sender = userRepository.findByUsername(name);
        invitation.setSender(sender);
        invitation.setStatus("invitation");
        invitation.setGroupe(groupeRepository.getOne(id));
        invitation.setCreated(new Date());
        invitation.setUpdated(new Date());
        User receiver = userRepository.getOne(invitation.getReceiver().getUsername());
        invitation.setReceiver(receiver);
        invitationRepository.save(invitation);
        System.out.println(invitation.getReceiver().getEmail());
        invitationRepository.save(invitation);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(sender,"invitation");
        model.addAttribute("invitations",invitations);
        return "redirect:/editmygroup?id="+id;
    }
    @RequestMapping(value = "group/invitation",method = RequestMethod.POST)
    public String inviteUser(@ModelAttribute("invitation") Invitation invitation,@RequestParam(name = "id") Long id,Model model){
        Page<GroupDocument> groupDocuments = null;
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User sender = userRepository.findByUsername(name);
        invitation.setSender(sender);
        invitation.setStatus("invitation");
        invitation.setGroupe(groupeRepository.getOne(id));
        invitation.setCreated(new Date());
        invitation.setUpdated(new Date());
        User receiver = userRepository.getOne(invitation.getReceiver().getUsername());
        invitation.setReceiver(receiver);
        invitationRepository.save(invitation);
        System.out.println(invitation.getReceiver().getEmail());
        invitationRepository.save(invitation);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(sender,"invitation");
        model.addAttribute("invitations",invitations);
        Groupe group = groupeRepository.getOne(id);
        groupDocuments = groupDocumentRepository.findByGroupe(group,new PageRequest(0,4));
        int totalPage = groupDocuments.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
        model.addAttribute("totalPage",totalPage);
        List<GroupComment> groupComments = groupCommentRepository.findByGroupe(group);
        model.addAttribute("groupComments",groupComments);
        model.addAttribute("pages",pages);
        model.addAttribute("currentPage",0);
        List<User> users = userRepository.findAll();
        users.remove(sender);
        for (int i = 0;i<users.size();i++){
            for (Invitation groupInvitation:group.getInvitations()) {
                if (groupInvitation.getReceiver() == users.get(i) && groupInvitation.getStatus() == "accept"){
                    users.remove(i);
                }
                if (groupInvitation.getReceiver() == users.get(i) && groupInvitation.getStatus() == "invitation"){
                    users.remove(i);
                }
            }
        }
        System.out.println(users.size());
        model.addAttribute("users",users);
        model.addAttribute("groupId",id);
        model.addAttribute("groupDocuments",groupDocuments);
        model.addAttribute("groupDocument",new GroupDocument());
        model.addAttribute("group",group);
        model.addAttribute("gComment",new GroupComment());
        Invitation invitation1 = new Invitation();
        model.addAttribute("invitation",invitation1);
        return "redirect:/group?idGroupe="+id;

    }
    @RequestMapping(value = "/groupcomment" , method = RequestMethod.POST)
    public String Comment(@RequestParam(name = "idGroupe") Long idGroupe,@ModelAttribute("gComment") GroupComment groupComment,@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "search",defaultValue = "") String search,Model model){
        Page<GroupDocument> groupDocuments = null;
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
        List<Invitation> invitations = invitationRepository.findByReceiverAndStatus(creator,"invitation");
        model.addAttribute("invitations",invitations);
        Groupe group = groupeRepository.getOne(idGroupe);
        groupComment.setSender(creator);
        groupComment.setGroupe(group);
        groupComment.setCreated(new Date());
        groupComment.setUpdated(new Date());
        groupCommentRepository.save(groupComment);
        if (search !=""){
            groupDocuments = groupDocumentRepository.searchByGroupeAndSearch("%"+search+"%",group,new PageRequest(page,4));
        }
        else {
            groupDocuments = groupDocumentRepository.findByGroupe(group,new PageRequest(page,4));
        }

        int totalPage = groupDocuments.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
        List<User> users = userRepository.findAll();
        users.remove(creator);
        for (int i = 0;i<users.size();i++){
            for (Invitation groupInvitation:group.getInvitations()) {
                if (groupInvitation.getReceiver() == users.get(i) && groupInvitation.getStatus() == "accept"){
                    users.remove(i);
                }
                if (groupInvitation.getReceiver() == users.get(i) && groupInvitation.getStatus() == "invitation"){
                    users.remove(i);
                }
            }
        }
        System.out.println(users.size());
        model.addAttribute("users",users);
        model.addAttribute("totalPage",totalPage);
        List<GroupComment> groupComments = groupCommentRepository.findByGroupe(group);
        model.addAttribute("groupComments",groupComments);
        model.addAttribute("pages",pages);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        model.addAttribute("groupDocuments",groupDocuments);
        model.addAttribute("groupDocument",new GroupDocument());
        model.addAttribute("group",group);
        model.addAttribute("gComment",new GroupComment());
        Invitation invitation1 = new Invitation();
        model.addAttribute("invitation",invitation1);
        return "redirect:/group?idGroupe="+idGroupe+"&page="+page+"&search="+search;
    }

}
