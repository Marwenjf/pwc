package com.ldapauth.demo.controllers;



import com.ldapauth.demo.entity.Groupe;
import com.ldapauth.demo.entity.User;
import com.ldapauth.demo.repository.GroupeRepository;
import com.ldapauth.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class GroupController {

        @Autowired
        private GroupeRepository groupeRepository;
        @Autowired
        private UserRepository userRepository;
        @RequestMapping(value = "/allgroup",method = RequestMethod.GET)
        public java.lang.String addGroupe(Model model){
            List<Groupe> groupes = groupeRepository.findBySearch("",new PageRequest(0,3)).getContent();
            model.addAttribute("groups",groupes);
            return "index3";
        }

    @RequestMapping(value = "/mygroups",method = RequestMethod.POST)
    public ModelAndView addMyGroup(@Valid @ModelAttribute("newGroup") Groupe newGroup, BindingResult bindingResult, @RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "search",defaultValue = "") String search, Model model){
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
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

        model.addAttribute("pages",pages);
        model.addAttribute("groups",groupes);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        ModelAndView modelAndView = new ModelAndView("mygroups","newGroup", new Groupe());
        return modelAndView;
    }
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public ModelAndView getAllGroups(Model model,@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "search",defaultValue = "") String search){
        Page<Groupe> groupes = null;
        if (search != "")
        {groupes = groupeRepository.findBySearch("%"+search+"%",new PageRequest(page,3));}
        else
        {groupes = groupeRepository.findAll(new PageRequest(page,3));}
        int totalPage = groupes.getTotalPages();
        int pages[] = new int[totalPage];
        for (int i = 0; i <totalPage ; i++) {
            pages[i] = i;
        }
        List<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        model.addAttribute("pages",pages);
        model.addAttribute("groups",groupes);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        ModelAndView modelAndView = new ModelAndView("groups");
        return modelAndView;
    }
    @RequestMapping(value = "/mygroups", method = RequestMethod.GET)
    public ModelAndView getMyGroups(Model model,@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "search",defaultValue = "") String search){
        Page<Groupe> myGroupes = null;
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        User creator = userRepository.findByUsername(name);
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
        List<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        model.addAttribute("pages",pages);
        model.addAttribute("groups",myGroupes);
        model.addAttribute("currentPage",page);
        model.addAttribute("search",search);
        ModelAndView modelAndView = new ModelAndView("mygroups","newGroup", new Groupe());
        return modelAndView;
    }
    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public ModelAndView getGroup(@RequestParam(name = "idGroupe",defaultValue = "") Long idGroupe){
            Groupe group = groupeRepository.getOne(idGroupe);
            ModelAndView modelAndView = new ModelAndView("group");
            modelAndView.addObject("group",group);
        return modelAndView;
    }
    @RequestMapping(value = "/groups/delete",method = RequestMethod.GET)
    public String deleteGroup(Model model,@RequestParam(name = "id") Long id,@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "search",defaultValue = "") String search){
        groupeRepository.deleteById(id);
        return "redirect:/groups?page="+page+"&search="+search;
    }
    @RequestMapping(value = "/mygroups/delete",method = RequestMethod.GET)
    public String deleteMyGroup(Model model,@RequestParam(name = "id") Long id,@RequestParam(name = "page",defaultValue = "0") int page,@RequestParam(name = "search",defaultValue = "") String search){
        groupeRepository.deleteById(id);
        return "redirect:/mygroups?page="+page+"&search="+search;
    }
    @RequestMapping(value = "editmygroup",method = RequestMethod.GET)
    public ModelAndView geteditPage(@RequestParam(name = "id") Long id,Model model){
        Groupe groupe = groupeRepository.getOne(id);
        List<User> users = userRepository.findAll();
        model.addAttribute("users",users);
        model.addAttribute("groupId",id);
        ModelAndView modelAndView = new ModelAndView("edit","editGroup",groupe);
      return modelAndView;
    }
    @RequestMapping(value = "mygroups/edit",method = RequestMethod.POST)
    public String editGroup(@ModelAttribute("editGroup") Groupe editGroup,@RequestParam(name = "id") Long id){
        Groupe groupe = groupeRepository.getOne(id);
        groupe.setUpdated(new Date());
        groupe.setGroupName(editGroup.getGroupName());

        groupeRepository.save(groupe);
        return "redirect:/mygroups";
    }
}
