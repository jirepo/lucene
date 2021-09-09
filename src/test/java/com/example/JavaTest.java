package com.example;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;


public class JavaTest {
  
  @Test
  public void testOptionl() {
    Optional<String> opt1 = Optional.empty();
    System.out.println(opt1);
    if(opt1.isPresent()) {
      System.out.println("Present");
    }else {
      System.out.println("No Present");
    }
    Optional<List<String>> opt2 = Optional.ofNullable(null);
    if(opt2.isEmpty()) {
      System.out.println("empty");
    }
    Optional<String> opt3 = Optional.ofNullable("kim");
    System.out.println(opt3.orElse("sssss"));
  }//:

  
  @Test
  public void testDateToString() {
    Date d = Calendar.getInstance().getTime();  // 현재시간 
    System.out.println(d.toInstant().toEpochMilli());  // Epoctime
    System.out.println(d.toInstant().toString());  // UTC String 

  }
}
