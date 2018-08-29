package com.self.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.self.rest.vo.Session;

@RestController
@RequestMapping("/workshop")
public class WorkshopRestController {
	 @RequestMapping("/sessions")
	    public List<Session> getSessionList() {
		 	List<Session> sessionList = new ArrayList<Session>();
		 	
		 	Session session = new Session();
		 	session.setDate("02/18/2018");
		 	session.setEventTitle("Authentication with NgRx");
		 	session.setLocation("Chennai");
		 	session.setSpeaker("Ranjith");
		 	
		 	sessionList.add(session);
		 	
	        return sessionList;
	    }
}
