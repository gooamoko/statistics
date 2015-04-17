/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.edu.pgtk.statistics;

import java.text.DecimalFormat;

/**
 *
 * @author leonid
 */
public class Passed {

  private double groupBall = 0;
  private String studentName = null;
  private String groupName = null;
  private int rowNumber = 0;

  /**
   * Get the value of rowNumber
   *
   * @return the value of rowNumber
   */
  public int getRowNumber() {
    return rowNumber;
  }

  /**
   * Set the value of rowNumber
   *
   * @param rowNumber new value of rowNumber
   */
  public void setRowNumber(int rowNumber) {
    this.rowNumber = rowNumber;
  }

  /**
   * Get the value of groupName
   *
   * @return the value of groupName
   */
  public String getGroupName() {
    return groupName;
  }

  /**
   * Set the value of groupName
   *
   * @param groupName new value of groupName
   */
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  /**
   * Get the value of studentName
   *
   * @return the value of studentName
   */
  public String getStudentName() {
    return studentName;
  }

  /**
   * Set the value of studentName
   *
   * @param studentName new value of studentName
   */
  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  /**
   * Get the value of groupBall
   *
   * @return the value of groupBall
   */
  public String getGroupBall() {
    DecimalFormat decimalStr = new DecimalFormat("#.##");
    float result = 0;
    return decimalStr.format(groupBall);
  }

  /**
   * Set the value of groupBall
   *
   * @param groupBall new value of groupBall
   */
  public void setGroupBall(double groupBall) {
    this.groupBall = groupBall;
  }


  
  public Passed(int rowNum, String grName, String stName, float grBall) {
    studentName = stName;
    groupName = grName;
    groupBall = grBall;
    rowNumber = rowNum;
  }
  
    public String getRowClass() {
    String result = "even";
    if ((rowNumber % 2) == 0) result = "odd";
    return result;
  }
}
