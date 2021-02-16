package com.self.batchinsert.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import static com.self.batchinsert.constants.GlobalConstants.MSG_USER_CREATION_SUCCESSFUL;

import com.self.batchinsert.entity.User;
import com.self.batchinsert.repository.UserRepository;

@Service
public class UserService {
  private static final int BATCH_SIZE = 1000;
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public String createUsers(Integer totalCount) {
    List<User> usersList = new ArrayList<>();

    for (int bIdx = 0; bIdx < totalCount; bIdx++) {
      User entity = User.builder()
        .userName("userName-" + bIdx)
        .firstName("firstName-" + bIdx)
        .lastName("lastName-" + bIdx)
        .email("email-" + bIdx + "@gmail.com")
        .address("address-" + bIdx)
        .build();

      usersList.add(entity);

      if ((bIdx + 1) % BATCH_SIZE == 0 || (bIdx + 1) == totalCount) {
        userRepository.saveAll(usersList);
        usersList.clear();
      }
    }

    return String.format(MSG_USER_CREATION_SUCCESSFUL, totalCount);
  }
}
