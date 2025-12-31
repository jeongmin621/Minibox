package com.jmk.movie.controllers;

import com.jmk.movie.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    // Home 화면 보여주는 페이지
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex(@SessionAttribute(value = "user", required = false) UserEntity user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("home/index");
        return modelAndView;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String getLogout(HttpSession session, HttpServletRequest request) {
        session.setAttribute("user", null);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    // 회원가입 1단계 보여주는 페이지
    @RequestMapping(value = "/join", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getJoin() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/join");
        return modelAndView;
    }

    // 회원가입 2단계 보여주는 페이지
    @RequestMapping(value = "/agree", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAgree() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/agree");
        return modelAndView;
    }

    // 회원가입 3단계 보여주는 페이지
    @RequestMapping(value = "/main", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMain() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/main");
        return modelAndView;
    }

    // 회원가입 4단계 보여주는 페이지
    @RequestMapping(value = "/success", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getSuccess() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/success");
        return modelAndView;
    }

    @RequestMapping(value = "/user-find", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getUserFind() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/user-find");
        return modelAndView;
    }

    @RequestMapping(value = "/pass-find", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getPassFind() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/pass-find");
        return modelAndView;
    }
}
