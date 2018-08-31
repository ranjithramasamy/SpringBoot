package com.self.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.self.rest.vo.Schedules;
import com.self.rest.vo.Session;
import com.self.rest.vo.Speakers;
import com.self.rest.vo.Topics;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
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
	 	
	 	Session session1 = new Session();
	 	session1.setDate("02/20/2018");
	 	session1.setEventTitle("Angular Service Workers & Cows — a love story");
	 	session1.setLocation("Bangalore");
	 	session1.setSpeaker("Pranav");
	 	
	 	Session session2 = new Session();
	 	session2.setDate("02/20/2018");
	 	session2.setEventTitle("Modules are not what you think they are");
	 	session2.setLocation("Pune");
	 	session2.setSpeaker("Andrew");
	 	
	 	Session session3 = new Session();
	 	session3.setDate("02/19/2018");
	 	session3.setEventTitle("Reusable Animations");
	 	session3.setLocation("Pune");
	 	session3.setSpeaker("Pranav & Vijay");
	 	
	 	sessionList.add(session);
	 	sessionList.add(session1);
	 	sessionList.add(session2);
	 	sessionList.add(session3);
	 	
        return sessionList;
    }
	
	@RequestMapping("/schedules")
    public List<Schedules> getSchedulesList() {
	 	List<Schedules> schedulesList = new ArrayList<Schedules>();
	 	
	 	Schedules schedules = new Schedules();
	 	List<Topics> topicsList = new ArrayList<Topics>();
	 	
	 	Topics topics = new Topics();	 	
	 	topics.setHandleBy("John Papa & Dan Wahlin");
	 	topics.setTitle("The Ultimate Angular 2 Workshop");
	 	
	 	Topics topics1 = new Topics();	 	
	 	topics1.setHandleBy("Joe Eames");
	 	topics1.setTitle("Ultimate Workshop – NGRX");
	 	
	 	topicsList.add(topics);
	 	topicsList.add(topics1);
	 	
	 	schedules.setTopics(topicsList);
	 	schedules.setDate("02/18/2018");
	 	
	 	Schedules schedules1 = new Schedules();
	 	List<Topics> topicsList1 = new ArrayList<Topics>();
	 	
	 	Topics topics11 = new Topics();	 	
	 	topics11.setHandleBy("John Papa & Dan Wahlin");
	 	topics11.setTitle("Taking Angular to Production");
	 	
	 	Topics topics12 = new Topics();	 	
	 	topics12.setHandleBy("Joe Eames");
	 	topics12.setTitle("Unit Testing Angular like a Boss");
	 	
	 	topicsList1.add(topics11);
	 	topicsList1.add(topics12);
	 	
	 	schedules1.setTopics(topicsList1);
	 	schedules.setDate("02/19/2018");
	 	
	 	schedulesList.add(schedules);
	 	schedulesList.add(schedules1);
	 	
        return schedulesList;
    }
	
	@RequestMapping("/speakers")
    public List<Speakers> getSpeakersList() {
	 	List<Speakers> speakersList = new ArrayList<Speakers>();
	 	
	 	Speakers speakers = new Speakers();
	 	speakers.setRole("Angular Material Core Team");
	 	speakers.setSpeaker("Ranjith");
	 	
	 	Speakers speakers1 = new Speakers();
	 	speakers1.setRole("UI Designer");
	 	speakers1.setSpeaker("Pranav");
	 	
	 	Speakers speakers2 = new Speakers();
	 	speakers2.setRole("Angular Core Manager");
	 	speakers2.setSpeaker("Andrew");
	 	
	 	Speakers speakers3 = new Speakers();
	 	speakers3.setRole("Technology Director");
	 	speakers3.setSpeaker("Alex Carbel");
	 	
	 	speakersList.add(speakers);
	 	speakersList.add(speakers1);
	 	speakersList.add(speakers2);
	 	speakersList.add(speakers3);
	 	
        return speakersList;
    }
}
