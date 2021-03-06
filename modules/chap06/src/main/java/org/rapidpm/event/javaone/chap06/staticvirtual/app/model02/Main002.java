package org.rapidpm.event.javaone.chap06.staticvirtual.app.model02;

import org.rapidpm.event.javaone.chap06.staticvirtual.Concurrency;
import org.rapidpm.event.javaone.chap06.staticvirtual.ProxyGenerator;

/**
 * Created by svenruppert on 23.10.15.
 */
public class Main002 {
  public static void main(String[] args) {

    final Parent parent = new Parent();
    final FirstChild firstChild = new FirstChild();
    final SecondChild secondChild = new SecondChild();
    final ThirdChild thirdChild = new ThirdChild();

    parent.firstChild = firstChild;
    firstChild.secondChild = secondChild;
    secondChild.thirdChild = thirdChild;

    final String hello = parent.doWork("Hello");
    System.out.println("hello = " + hello);
    System.out.println("value = " + parent.firstChild.secondChild.thirdChild.getValue());

    final Parent orig = new Parent();

    // NPE
    // orig.getFirstChild().getSecondChild().getThirdChild().doWork("ups NPE");

    // ugly code
    if (orig.getFirstChild() != null)
      if(orig.getFirstChild().getSecondChild() != null)
        if(orig.getFirstChild().getSecondChild().getThirdChild() != null)
          orig.getFirstChild().getSecondChild().getThirdChild().doWork("ups NPE");

    final Parent proxy = proxy(orig);
    final ThirdChild child = proxy.getFirstChild().getSecondChild().getThirdChild();
    if (child != null) {
      final String value = child.getValue();
      System.out.println("value = " + value);

      //extend the proxy ;-)
      //final String proxy1 = child.doWork("Proxy");
      //System.out.println("proxy1 = " + proxy1);
    }

  }


  private static <T> T proxy( T orig) {
    final Class<T> aClass = (Class<T>) orig.getClass();
    final T demo = ProxyGenerator.make(aClass, aClass, Concurrency.OnExistingObject);

    final Class<?> aClassProxy = demo.getClass();
    try {
      aClassProxy.getDeclaredField("realSubject").set(demo, orig);
    } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
      e.printStackTrace();
    }
    return demo;
  }


}
