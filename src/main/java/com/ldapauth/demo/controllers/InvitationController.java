package com.ldapauth.demo.controllers;

import com.ldapauth.demo.entity.Invitation;
import com.ldapauth.demo.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class InvitationController {
    @Autowired
    private InvitationRepository invitationRepository;
    @RequestMapping(value = "accept",method = RequestMethod.GET)
    public String invitationResponseAccept(HttpServletRequest request){
    Long id = Long.valueOf(request.getParameter("id"));
    Invitation invitation = invitationRepository.getOne(id);
    invitation.setStatus("accept");
    invitationRepository.save(invitation);
    return getPreviousPageByRequest(request).orElse("/");
}
    @RequestMapping(value = "reject",method = RequestMethod.GET)
    public String invitationResponseReject(HttpServletRequest request){
        Long id = Long.valueOf(request.getParameter("id"));
        Invitation invitation = invitationRepository.getOne(id);
        invitation.setStatus("reject");
        invitationRepository.save(invitation);
        return getPreviousPageByRequest(request).orElse("/");
    }
    protected Optional<String> getPreviousPageByRequest(HttpServletRequest request)
    {
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }
}
