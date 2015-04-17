package ru.edu.pgtk.statistics;

public class Mark {

  private String subjectName;
  private int mark;
  private int markType;

  private String getMarkTypeStr() {
    switch (markType) {
      case 1:
        return " (экзамен)";
      case 2:
        return " (зачет)";
      case 3:
        return " (курсовой)";
      case 4:
        return " (практика)";
      default:
        return "";
    }
  }
  
  public String getColor() {
    if (mark < 3) return "red";
    if (mark < 4) return "yellow";
    return "green";
  }
  
  public String getMarkString() {
    switch (mark) {
      case 0:
        return "без оценки";
      case 1:
      case 2:
        return "неудовлетворительно";
      case 3:
        return "удовлетворительно";
      case 4:
        return "хорошо";
      case 5:
        return "отлично";
      case 10:
        return "не аттестован(а)";
      case 11:
        return "освобожден(а)";
      case 12:
        return "не изучал(а)";
      case 13:
        return "зачет";
      default:
        return "";
    }
  }
  
  public Mark(String subjectName, int mark, int markType) {
    this.subjectName = subjectName;
    this.mark = mark;
    this.markType = markType;
  }

  public int getMarkType() {
    return markType;
  }

  public void setMarkType(int markType) {
    this.markType = markType;
  }

  public int getMark() {
    return mark;
  }

  public void setMark(int mark) {
    this.mark = mark;
  }

  public String getSubjectName() {
    return subjectName+getMarkTypeStr();
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }
}
