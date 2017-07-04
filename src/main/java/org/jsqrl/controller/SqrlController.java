package org.jsqrl.controller;

import lombok.extern.slf4j.Slf4j;
import org.jsqrl.config.SqrlConfig;
import org.jsqrl.model.SqrlAuthResponse;
import org.jsqrl.model.SqrlClientRequest;
import org.jsqrl.server.JSqrlServer;
import org.jsqrl.util.SqrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Brent Nichols on 7/10/2016.
 */
@Controller
@Slf4j
public class SqrlController {

    public static final String SQRL_ROOT = "/sqrl";

    @Autowired
    private SqrlConfig config;

    @Autowired
    private JSqrlServer jSqrlServer;

    @RequestMapping("/login")
    public String login(Model model,
                        HttpServletRequest request) {

        String sqrlNut = jSqrlServer.createAuthenticationRequest(request.getRemoteAddr(), true);

        model.addAttribute("nut", sqrlNut);
        model.addAttribute("sfn", SqrlUtil.unpaddedBase64UrlEncoded(config.getSfn()));

        return "sqrl-login";
    }

    @RequestMapping(value = SQRL_ROOT,
            method = RequestMethod.POST)
    public ResponseEntity sqrl(@ModelAttribute SqrlClientRequest request,
                               @RequestParam("nut") String nut,
                               HttpServletRequest httpRequest){

        log.debug("Client ({}): {}", httpRequest.getRemoteAddr(), request.getDecodedClientData());

        SqrlAuthResponse sqrlAuthResponse = jSqrlServer.handleClientRequest(request, nut, httpRequest.getRemoteAddr());

        return new ResponseEntity(sqrlAuthResponse.toEncodedString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/authcheck", method = RequestMethod.GET)
    public ResponseEntity checkAuthentication(@RequestParam("nut") String nut,
                                              HttpServletRequest httpRequest) {
        if (jSqrlServer.checkAuthenticationStatus(nut, httpRequest.getRemoteAddr())) {
            //User is authenticated
            return new ResponseEntity(HttpStatus.RESET_CONTENT);
        } else{
            return new ResponseEntity(HttpStatus.OK);
        }
    }

}
