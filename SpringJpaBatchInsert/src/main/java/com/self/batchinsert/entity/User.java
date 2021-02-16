package com.self.batchinsert.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Note: MySQL does not support the creation of SEQUENCE object, thereby
 * We can create a table with one column called 'next_val' and this can be
 * used as a sequence generator for `id` field. In this way, we can
 * enable the batching to insert the bulk records into DB.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "t_users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeqGen")
  @SequenceGenerator(name = "userSeqGen", sequenceName = "t_users_seq")
  @Column(name = "id")
  private Integer id;

  @Column(name = "username")
  private String userName;

  @Column(name = "firstname")
  private String firstName;

  @Column(name = "lastname")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Column(name = "address")
  private String address;
}
