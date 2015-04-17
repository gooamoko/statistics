package ru.edu.pgtk.statistics;

import java.text.DecimalFormat;

public class Input {

  private String specName;
  private int budgetPlan;
  private int contractPlan;
  private int budgetCount;
  private int contractCount;
  private int budgetIn;
  private int contractIn;
  private int rowNumber;
  private int index;

  public String getContractConcourse() {
    DecimalFormat decimalStr = new DecimalFormat("#.##");
    float result = 0;
    if (contractPlan > 0) result = (float)contractCount / contractPlan;
    return decimalStr.format(result);
  }

  public String getBudgetConcourse() {
    DecimalFormat decimalStr = new DecimalFormat("#.##");
    float result = 0;
    if (budgetPlan > 0) result = (float) budgetCount / budgetPlan;
    return decimalStr.format(result);
  }
  
  public String getRowClass() {
    String result = "even";
    if ((rowNumber % 2) == 0) result = "odd";
    if (2 == index) result = "summary";
    return result;
  }
  

  public Input(int rowNum, int indx, String spName, int bPlan, int cPlan, int bCount, 
          int cCount, int bIn, int cIn) {
    specName = spName;
    budgetPlan = bPlan;
    contractPlan = cPlan;
    budgetCount = bCount;
    contractCount = cCount;
    budgetIn = bIn;
    contractIn = cIn;
    index = indx;
    rowNumber = rowNum;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public int getRowNumber() {
    return rowNumber;
  }

  public void setRowNumber(int rowNumber) {
    this.rowNumber = rowNumber;
  }

  public int getContractIn() {
    return contractIn;
  }

  public void setContractIn(int contractIn) {
    this.contractIn = contractIn;
  }

  public int getBudgetIn() {
    return budgetIn;
  }

  public void setBudgetIn(int budgetIn) {
    this.budgetIn = budgetIn;
  }

  public int getContractCount() {
    return contractCount;
  }

  public void setContractCount(int contractCount) {
    this.contractCount = contractCount;
  }

  public int getBudgetCount() {
    return budgetCount;
  }

  public void setBudgetCount(int budgetCount) {
    this.budgetCount = budgetCount;
  }

  public int getContractPlan() {
    return contractPlan;
  }

  public void setContractPlan(int contractPlan) {
    this.contractPlan = contractPlan;
  }

  public int getBudgetPlan() {
    return budgetPlan;
  }

  public void setBudgetPlan(int budgetPlan) {
    this.budgetPlan = budgetPlan;
  }

  public String getSpecName() {
    return specName;
  }

  public void setSpecName(String specName) {
    this.specName = specName;
  }
}
