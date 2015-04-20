package ru.edu.pgtk.statistics.jsf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import ru.edu.pgtk.statistics.Input;
import ru.edu.pgtk.statistics.Passed;
import ru.edu.pgtk.statistics.Student;
import static ru.edu.pgtk.statistics.jsf.Utils.addMessage;
import static ru.edu.pgtk.statistics.jsf.Utils.getMonthName;

/**
 * Сессионный компонент-подложка.
 *
 * @author Воронин Леонид
 */
@ManagedBean(name = "sessionMB")
@SessionScoped
public class SessionMB {

  private final static String[] forms = {
    "month",
    "semester",
    "concurse",
    "income"
  };

  private final static String[] pageNames = {
    "успеваемость и посещаемость за месяц",
    "успеваемость и посещаемость за семестр",
    "количество поданных заявлений",
    "списки зачисленных"
  };

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
  private final Date currentDate = new Date();
  private int pageCode;
  private boolean extramural;
  private boolean commercial;
  private List<SelectItem> reportsList;
  private List<SelectItem> monthList;
  private List<SelectItem> yearList;

  private Connection connection = null;
  private int semester = 0;
  private int month = 0;
  private int year = 0;
  private String groupCode = null;
  private String specCode = null;

  @PostConstruct
  private void construct() {
    reportsList = new ArrayList<>(4);
    for (int x = 0; x < 4; x++) {
      reportsList.add(new SelectItem(x + 1, pageNames[x]));
    }
    monthList = new ArrayList<>(12);
    for (int x = 0; x < 12; x++) {
      monthList.add(new SelectItem(x, getMonthName(x)));
    }
    yearList = new ArrayList<>(2);
    GregorianCalendar calendar = new GregorianCalendar();
    int currentYear = calendar.get(GregorianCalendar.YEAR);
    for (int x = 0; x < 2; x++) {
      int y = currentYear - x;
      yearList.add(new SelectItem(y, "" + y));
    }
    // Подключимся к СУБД
    try {
      InitialContext initialContext = new InitialContext();
      DataSource dataSource = (DataSource) initialContext.lookup("jdbc/pgtk_mssql");
      if (null != dataSource) {
        connection = dataSource.getConnection();
      }
    } catch (SQLException | NamingException e) {
      addMessage("Ошибка! " + e.toString());
    }
  }

  @PreDestroy
  private void destroy() {
    try {
      if ((null != connection) && (!connection.isClosed())) {
        connection.close();
      }
    } catch (Exception e) {
      // Ничего не делаем
    }
  }

  public String getCurrentTime() {
    return dateFormat.format(currentDate);
  }

  public boolean isReportSelected() {
    return (pageCode > 0);
  }

  public List<SelectItem> getYearsList() {
    return yearList;
  }

  public List<SelectItem> getMonthsList() {
    return monthList;
  }

  public List<SelectItem> getSemestersList() {
    List<SelectItem> lst = new ArrayList<>();
    for (int x = 1; x < 9; x++) {
      lst.add(new SelectItem(x, x + "-й семестр"));
    }
    return lst;
  }

  public List<SelectItem> getGroupsList() {
    List<SelectItem> lst = new ArrayList<>();
    try {
      if (connection != null) {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT gr_pcode, gr_name FROM groups WHERE (gr_Attributes = 0) "
                + "AND (gr_isZaoch = 0) ORDER BY gr_Course, gr_Name;",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery();
        if (rs.first()) {
          do {
            lst.add(new SelectItem(rs.getString("gr_pcode"), rs.getString("gr_name")));
          } while (rs.next());
        } else {
          addMessage("В базе данных нет ни одной группы!");
        }
        rs.close();
      } else {
        addMessage("Проблема с подключением к базе данных!");
      }
    } catch (SQLException e) {
      addMessage("Ошибка при обработке запроса. " + e.toString());
    }
    return lst;
  }

  public List<SelectItem> getSpecList() {
    List<SelectItem> lst = new ArrayList<>();
    try {
      if (connection != null) {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT sp_pcode, sp_comment FROM Specialities WHERE (sp_actual = 1) "
                + " ORDER BY sp_comment;",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery();
        if (rs.first()) {
          do {
            lst.add(new SelectItem(rs.getString("sp_pcode"), rs.getString("sp_comment")));
          } while (rs.next());
        } else {
          addMessage("В базе данных нет ни одной специальности!");
        }
        rs.close();
      } else {
        addMessage("Проблема с подключением к базе данных!");
      }
    } catch (SQLException e) {
      addMessage("Ошибка при обработке запроса. " + e.toString());
    }
    return lst;
  }

  public List<Student> getStudentsList() {
    List<Student> lst = new ArrayList<>();
    try {
      if (connection != null) {
        PreparedStatement stmt = null;
        if (semester > 0) {
          stmt = connection.prepareStatement(
                  "EXEC getWebSemesterGroupReport ?, ?",
                  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
          stmt.setString(1, groupCode);
          stmt.setInt(2, semester);
        } else {
          stmt = connection.prepareStatement(
                  "EXEC getWebStudentsReport ?, ?, ?",
                  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
          stmt.setString(1, groupCode);
          stmt.setInt(2, month);
          stmt.setInt(3, year);
        }
        ResultSet rs = stmt.executeQuery();
        if (rs.first()) {
          int count = 1;
          do {
            lst.add(new Student(count++, rs.getString("st_pcode"),
                    rs.getString("st_studnumber"), rs.getFloat("st_mark"),
                    rs.getInt("st_all"), rs.getInt("st_illegal")));
          } while (rs.next());
        } else {
          addMessage("В базе данных нет ни одной группы!");
        }
        rs.close();
      } else {
        addMessage("Проблема с подключением к базе данных!");
      }
    } catch (SQLException e) {
      addMessage("Ошибка при обработке запроса. " + e.toString());
    }
    return lst;
  }

  public List<Input> getInputList() {
    List<Input> input = new ArrayList<>();
    try {
      if (connection != null) {
        PreparedStatement stmt = connection.prepareStatement(
                "EXEC GetINInfoReport;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        if (extramural) {
          stmt = connection.prepareStatement(
                  "EXEC GetZaochINInfoReport;",
                  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        ResultSet rs = stmt.executeQuery();
        if (rs.first()) {
          int count = 1;
          do {
            input.add(new Input(count++, rs.getInt("rp_index"), rs.getString("rp_SpName"), rs.getInt("rp_bplan"),
                    rs.getInt("rp_cplan"), rs.getInt("rp_bask"), rs.getInt("rp_cask"),
                    rs.getInt("rp_bcome"), rs.getInt("rp_ccome")));
          } while (rs.next());
        } else {
          addMessage("В базе данных нет ни одной подходящей записи!");
        }
        rs.close();
      } else {
        addMessage("Проблема с подключением к базе данных!");
      }

    } catch (SQLException e) {
      addMessage("Ошибка при обработке запроса. " + e.toString());
    }
    return input;

  }

  public List<Passed> getPassedList() {
    List<Passed> input = new ArrayList<>();
    try {
      if (connection != null) {
        PreparedStatement stmt = null;
        stmt = connection.prepareStatement(
                "SELECT gr_Name, ab_Name, ab_GroupBall FROM Groups, Abiturients "
                + "WHERE (ab_grcode = gr_pcode) AND (ab_InYear = year(getdate())) AND (ab_Attributes = 0) "
                + "AND (ab_isZaoch = ?) AND (ab_isCommercial = ?) ORDER BY ab_Name, gr_Name;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.setBoolean(1, extramural);
        stmt.setBoolean(2, commercial);
        ResultSet rs = stmt.executeQuery();
        if (rs.first()) {
          int count = 1;
          do {
            input.add(new Passed(count++, rs.getString("gr_Name"), rs.getString("ab_Name"), rs.getFloat("ab_GroupBall")));
          } while (rs.next());
        } else {
          addMessage("Данных, удовлетворяющих запросу пока нет.");
        }
        rs.close();
      } else {
        addMessage("Проблема с подключением к базе данных!");
      }

    } catch (SQLException e) {
      addMessage("Ошибка при обработке запроса. " + e.toString());
    }
    return input;
  }

  public String processRequest() {
    try {
      if ((pageCode > 0) && (pageCode < 3)) {
        if (pageCode == 1) {
          semester = 0;
        }
        return "/marks?faces-redirect=true";
      } else {
        return "/" + forms[pageCode - 1] + "?faces-redirect=true";
      }
    } catch (Exception e) {
      return "/error?faces-redirect=true";
    }
  }

  public List<SelectItem> getReports() {
    return reportsList;
  }

  public String getLink() {
    try {
      return "/WEB-INF/include/" + forms[pageCode - 1] + "form.xhtml";
    } catch (Exception e) {
      return "/WEB-INF/include/error.xhtml";
    }
  }

  public String getSpecCode() {
    return specCode;
  }

  public void setSpecCode(String specCode) {
    this.specCode = specCode;
  }

  public Connection getConnection() {
    return connection;
  }

  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getSemester() {
    return semester;
  }

  public void setSemester(int semester) {
    this.semester = semester;
  }

  public int getPageCode() {
    return pageCode;
  }

  public void setPageCode(int pageCode) {
    this.pageCode = pageCode;
  }

  public boolean isExtramural() {
    return extramural;
  }

  public void setExtramural(boolean extramural) {
    this.extramural = extramural;
  }

  public boolean isCommercial() {
    return commercial;
  }

  public void setCommercial(boolean commercial) {
    this.commercial = commercial;
  }
}
