package ru.edu.pgtk.statistics.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;

/**
 * Утилиты, предназначенные для облегчения работы с JSF.
 *
 * @author Воронин Леонид
 */
class Utils {

  private final static String[] months = {
    "Январь",
    "Февраль",
    "Март",
    "Апрель",
    "Май",
    "Июнь",
    "Июль",
    "Август",
    "Сентябрь",
    "Октябрь",
    "Ноябрь",
    "Декабрь"
  };

  /**
   * Приватный конструктор, чтобы запретить создание экземпляров класса.
   */
  private Utils() {
    throw new AssertionError("This constractor should not been called!");
  }

  public static String getMonthName(int month) {
    if ((month < 0) || (month > 11)) {
      throw new IllegalArgumentException("Valid month value is 0 .. 11");
    }
    try {
      return months[month];
    } catch (IndexOutOfBoundsException e) {
      return "";
    }
  }

  /**
   * Возвращает экземпляр FacesContext и используется для других методов.
   *
   * @return FacesContext.getCurrentInstance()
   */
  private static FacesContext getFacesContext() {
    return FacesContext.getCurrentInstance();
  }

  /**
   * Добавляет сообщение об ошибке.
   *
   * @param e Исключение, из которого будет браться информация об ошибке.
   */
  public static void addMessage(final Exception e) {
    String message = "Exception class " + e.getClass().getName() + " with message " + e.getMessage();
    getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "Error"));
  }

  /**
   * Добавляет сообщение об ошибке.
   *
   * @param message сообщение об ошибке.
   */
  public static void addMessage(final String message) {
    getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "Error"));
  }

  public static ExternalContext getExternalContext() {
    return getFacesContext().getExternalContext();
  }

  /**
   * Устанавливает cookie с указанными параметрами.
   *
   * @param key ключ для доступа к cookie
   * @param value значение, доступное по ключу.
   */
  public static void setCookie(final String key, final String value) {
    getExternalContext().addResponseCookie(key, value, null);
  }

  /**
   * Получает целочисленное значение из cookie.
   *
   * @param name наименование cookie
   * @return целое типа Long
   */
  public static long getLongFromCookie(final String name) {
    long result = 0;
    Cookie cookie = (Cookie) Utils.getExternalContext().getRequestCookieMap().get(name);
    if (null != cookie) {
      try {
        result = Long.parseLong(cookie.getValue());
      } catch (NumberFormatException e) {
        // Ничего не делаем. Пусть вернется 0
      }
    }
    return result;
  }

  /**
   * Получает строку из coolie.
   *
   * @param name наименование cookie
   * @return строка.
   */
  public static String getStringFromCookie(final String name) {
    String result = null;
    Cookie cookie = (Cookie) Utils.getExternalContext().getRequestCookieMap().get(name);
    if (null != cookie) {
      try {
        result = cookie.getValue();
      } catch (NumberFormatException e) {
        addMessage(e);
      }
    }
    return result;
  }
}
