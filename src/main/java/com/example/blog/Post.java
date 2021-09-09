package com.example.blog;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Blog Post 데이터.   
 */
@Getter
@Setter
public class Post {
  private String title; 
  private String contents;
  private String postId;
  private Date updDate;

}///~