package ru.edu.pgtk.statistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Класс для тестирования класса Student
 *
 * @author user
 */
public class StudentTest {

  private static Connection connection;
  private static Student student;

  public StudentTest() {
  }

  @BeforeClass
  public static void setUpClass() {
    try {
      // Создадим экземпляр класса Student, для которого будем гонять тесты
      student = new Student(1, "0BFC5580-7858-4A70-9C53-AC0E6B2E31D1", "809", 0, 0, 0);
      // Подключимся к базе данных
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      String url = "jdbc:sqlserver://192.168.0.251:1433";
      Properties props = new Properties();
      File file = new File("jdbctest.properties");
      if (file.canRead()) {
        FileInputStream input = new FileInputStream(file);
        props.load(input);
      } else {
        System.out.println("Can't read file " + file.getName());
      }
      connection = DriverManager.getConnection(url, props);
    } catch (ClassNotFoundException | SQLException | FileNotFoundException e) {
      System.out.println("Exception with message " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IOException with message " + e.getMessage());
    }
  }

  @AfterClass
  public static void tearDownClass() {
    try {
      // Закрываем соединение с базой данных
      if (null != connection) {
        connection.close();
      }
      student = null;
    } catch (SQLException e) {
      System.out.println("SQL Exception with message " + e.getMessage());
    }
  }

  /**
   * Оценки за семестр с корректными параметрами.
   */
  @Test
  public void testGetSemesterMarksCorrect() {
    try {
      System.out.println("getMarks(connection, semester)");
      List<Mark> result = student.getMarks(connection, 8);
      assertNotNull(result);
      assertFalse(result.isEmpty());
    } catch (Exception e) {
      fail("Exception class " + e.getClass().getName() + " with message " + e.getMessage());
    }
  }

  /**
   * Оценки за семестр с отсутствием подключения.
   */
  @Test
  public void testGetSemesterMarksConnectionNull() {
    try {
      System.out.println("getMarks(null, semester)");
      List<Mark> result = student.getMarks(null, 8);
      assertNotNull(result);
      // Список должен быть пустым
      assertTrue(result.isEmpty());
    } catch (Exception e) {
      fail("Exception class " + e.getClass().getName() + " with message " + e.getMessage());
    }
  }

  /**
   * Оценки за семестр с семестром равным 0.
   */
  @Test
  public void testGetSemesterMarksSemester0() {
    try {
      System.out.println("getMarks(connection, 0)");
      List<Mark> result = student.getMarks(connection, 0);
      assertNotNull(result);
      // Список должен содержать один элемент
      assertTrue(result.size() == 1);
      assertEquals(result.get(0).getSubjectName(), "Нет данных");
    } catch (Exception e) {
      fail("Exception class " + e.getClass().getName() + " with message " + e.getMessage());
    }
  }

  /**
   * Test of getMarks method, of class Student.
   */
  @Test
  public void testGetMonthMarksCorrect() {
    try {
      System.out.println("getMarks(connection, month, year)");
      List<Mark> result = student.getMarks(connection, 9, 2014);
      assertNotNull(result);
      assertFalse(result.isEmpty());
    } catch (SQLException e) {
      fail("Exception class " + e.getClass().getName() + " with message " + e.getMessage());
    }
  }

  /**
   * Test of getMarks method, of class Student.
   */
  @Test
  public void testGetMonthMarksConnectionNull() {
    try {
      System.out.println("getMarks(null, month, year)");
      List<Mark> result = student.getMarks(null, 9, 2014);
      assertNotNull(result);
      // Список должен быть пустым
      assertTrue(result.isEmpty());
    } catch (SQLException e) {
      fail("Exception class " + e.getClass().getName() + " with message " + e.getMessage());
    }
  }

  /**
   * Test of getMarks method, of class Student.
   */
  @Test
  public void testGetMonthMarksWrongMonth() {
    try {
      System.out.println("getMarks(connection, 0, year)");
      List<Mark> result = student.getMarks(connection, 0, 2014);
      assertNotNull(result);
      // Список должен содержать один элемент
      assertTrue(result.size() == 1);
      assertEquals(result.get(0).getSubjectName(), "Нет данных");
    } catch (SQLException e) {
      fail("Exception class " + e.getClass().getName() + " with message " + e.getMessage());
    }
  }

  /**
   * Test of getMarks method, of class Student.
   */
  @Test
  public void testGetMonthMarksWrongYear() {
    try {
      System.out.println("getMarks(connection, month, 0)");
      List<Mark> result = student.getMarks(connection, 9, 0);
      assertNotNull(result);
      // Список должен содержать один элемент
      assertTrue(result.size() == 1);
      assertEquals(result.get(0).getSubjectName(), "Нет данных");
    } catch (SQLException e) {
      fail("Exception class " + e.getClass().getName() + " with message " + e.getMessage());
    }
  }
}
