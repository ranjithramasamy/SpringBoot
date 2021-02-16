package com.self.batchinsert.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.self.batchinsert.constants.GlobalConstants.BASE_PATH;
import static com.self.batchinsert.constants.GlobalConstants.CREATE_USER_PATH;

import com.self.batchinsert.model.CreateUserRequestModel;
import com.self.batchinsert.service.UserService;

@RestController
@RequestMapping(BASE_PATH)
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(CREATE_USER_PATH)
  public String createUsers(@RequestBody CreateUserRequestModel createUserRequest) {
    return userService.createUsers(createUserRequest.getCount());
  }
}
