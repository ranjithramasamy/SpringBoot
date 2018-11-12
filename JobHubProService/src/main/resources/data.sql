-- t_employers
insert into t_employers values(1,'BNY Mellon Technologies', 'Chennai, TN', '78');
insert into t_employers values(2,'Cognizant Technology Solutions', 'Bangalore, KA', '65');
insert into t_employers values(3,'Amdocs', 'Pune, MH', '74');

-- t_job_loc
insert into t_job_loc values(1,1, 'Pune, MH');
insert into t_job_loc values(2,1, 'Chennai, TN');
insert into t_job_loc values(3,2, 'Chennai, TN');
insert into t_job_loc values(4,3, 'Gurugram, HR');
insert into t_job_loc values(5,3, 'Pune, MH');
insert into t_job_loc values(6,2, 'Bangalore, KA');

-- t_job_types
insert into t_job_types values(1,'Software Engineer');
insert into t_job_types values(2,'Technical Lead');
insert into t_job_types values(3,'Program Management');
insert into t_job_types values(4,'Project Lead');
insert into t_job_types values(5,'QA Automation Lead');
insert into t_job_types values(6,'Business Analyst');
insert into t_job_types values(7,'Architect');

-- t_skills
insert into t_skills values(1,'Java');
insert into t_skills values(2,'PHP');
insert into t_skills values(3,'.Net');
insert into t_skills values(4,'Backbone');
insert into t_skills values(5,'Mobile');
insert into t_skills values(6,'JQuery');
insert into t_skills values(7,'Oracle');
insert into t_skills values(8,'Spring');
insert into t_skills values(9,'Javascript');
insert into t_skills values(10,'HTML5');
insert into t_skills values(11,'Big Data');
insert into t_skills values(12,'Python');
insert into t_skills values(13,'Node');

-- t_jobs
insert into t_jobs values(1,1,2, 'Java/J2EE|Hibernate|Spring|Oracle', '40', sysdate, '71', 'Demonstrable professional expertise in Java programming. Demonstrable expertise with HTML5/CSS3/JavaScript. Proficiency in Angular JS and other similar JavaScript framework like React/NodeJS. Proficiency with data structures, algorithms, multi-threading, object-oriented programming and design patterns.');
insert into t_jobs values(2,2,1, 'HTML|Javascript|Angular|Bootstrap|Backbone|JQuery', '20', sysdate, '61', 'Proficiency with data structures, algorithms, multi-threading, object-oriented programming and design patterns. Working knowledge of one or more client-side frameworks such as Angular, Polymer, Backbone, JQuery. Strong diagnostic and problem-solving skills. Familiarity with software development for the Windows and/or Linux operating systems. Experience working with Web Services (REST) with an understanding of RESTful service design.');
insert into t_jobs values(3,3,4, 'Core Java|HTML|Javascript|AngularJs|Spring MVC|Springboot', '40', sysdate, '76', 'Experience in UI, Angular JS 1.X, Javascript, HTML,CSS. Experience in Web services, agile. Responsible for the execution of the projects from UI end. Driving innovative solutions within platform constraints and technical limitations. Develop interaction models and conceptual frameworks of user experience. Be a hands-on developer who will deliver along with team. Acting as a coach, mentor and lead for technical team as well as an advocate for products needs.');
insert into t_jobs values(4,5,3, 'HTML|Javascript|CSS|React|Node', '1', sysdate, '67', 'Looking for a MTS2 Software Engineer, with the hard core Java skills, strong experience in Node.js, React.js, angular.js technologies. The candidate must have complete understanding with hands-on experience in architecture, design, coding and testing aspect of both front end and backend services. The job requires close interaction with the business in transforming requests into a product feature along with great attitude, excellent communication and presentation skills');