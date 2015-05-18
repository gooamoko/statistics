package ru.edu.pgtk.statistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Student {

  private String studentCode = "";
  private String studentNumber = "";
  private double averageMark = 0;
  private int totalMissings = 0;
  private int illegalMissings = 0;
  private int index = 0;

  public String getColor() {
    String result = "green";
    if (averageMark < 4) {
      result = "yellow";
    }
    if (averageMark < 3) {
      result = "red";
    }
    return result;
  }
  
  public String getMarkStr() {
    DecimalFormat decimalStr = new DecimalFormat("#.##");
    return decimalStr.format(averageMark);
    
  }

  private List<Mark> ListMarks(ResultSet rs) throws SQLException {
    List<Mark> lst = new ArrayList<>();
    if (rs.first()) {
      do {
        lst.add(new Mark(rs.getString("sb_name"), rs.getInt("mk_mark"), rs.getInt("mk_index")));
      } while (rs.next());
    } else {
      lst.add(new Mark("Нет данных", 0, 0));
    }
    rs.close();
    return lst;
  }

  public List<Mark> getMarks(Connection connection, int semester) throws SQLException {
    if (connection != null) {
      PreparedStatement stmt = connection.prepareStatement(
              "EXEC getWebSemesterStudentsReport ?, ?",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, studentCode);
      stmt.setInt(2, semester);
      ResultSet rs = stmt.executeQuery();
      return ListMarks(rs);
    } else {
      List<Mark> lst = new ArrayList<>();
      return lst;
    }
  }

  public List<Mark> getMarks(Connection connection, int month, int year) throws SQLException {
    if (connection != null) {
      PreparedStatement stmt = connection.prepareStatement(
              "SELECT sb_name, mk_mark, 0 AS mk_index FROM subjects, marks WHERE (mk_stcode=?)"
              + " AND (mk_mounth=?) AND (mk_year=?) AND (mk_sbcode=sb_pcode) "
              + "ORDER BY sb_name;",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, studentCode);
      stmt.setInt(2, month);
      stmt.setInt(3, year);
      ResultSet rs = stmt.executeQuery();
      return ListMarks(rs);
    } else {
      List<Mark> lst = new ArrayList<>();
      return lst;
    }
  }

  public int getIndex() {
    return index;
  }
  
  public String getId() {
    return "student_" + index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public Student(int index, String studentCode,
          String studentNumber, double averageMark, int totalMissings, int illegalMissings) {
    this.index = index;
    this.studentCode = studentCode;
    this.studentNumber = studentNumber;
    this.averageMark = averageMark;
    this.totalMissings = totalMissings;
    this.illegalMissings = illegalMissings;
  }

  public int getIllegalMissings() {
    return illegalMissings;
  }

  public void setIllegalMissings(int illegalMissings) {
    this.illegalMissings = illegalMissings;
  }

  public int getTotalMissings() {
    return totalMissings;
  }

  public void setTotalMissings(int totalMissings) {
    this.totalMissings = totalMissings;
  }

  public double getAverageMark() {
    return averageMark;
  }

  public void setAverageMark(double averageMark) {
    this.averageMark = averageMark;
  }

  public String getStudentNumber() {
    return studentNumber;
  }

  public void setStudentNumber(String studentNumber) {
    this.studentNumber = studentNumber;
  }

  public String getStudentCode() {
    return studentCode;
  }

  public void setStudentCode(String pcode) {
    this.studentCode = pcode;
  }
}
