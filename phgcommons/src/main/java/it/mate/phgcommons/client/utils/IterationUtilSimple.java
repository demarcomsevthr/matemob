package it.mate.phgcommons.client.utils;

import java.util.ArrayList;
import java.util.List;


public class IterationUtilSimple extends IterationUtil<Integer> {
  
  public static IterationUtilSimple create(int numberOfIterations, ItemDelegate<Integer> itemDelegate, FinishDelegate finishDelegate) {
    return new IterationUtilSimple(numberOfIterations, itemDelegate, finishDelegate);
  }
  
  public IterationUtilSimple(int numberOfIterations, ItemDelegate<Integer> itemDelegate, FinishDelegate finishDelegate) {
    super(createIterationList(numberOfIterations), itemDelegate, finishDelegate);
  }

  private static List<Integer> createIterationList(int numberOfIterations) {
    List<Integer> iterationList = new ArrayList<Integer>();
    for (int it = 0; it < numberOfIterations; it++) {
      iterationList.add(it);
    }
    return iterationList;
  }

}
