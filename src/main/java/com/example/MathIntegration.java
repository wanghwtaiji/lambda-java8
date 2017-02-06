package com.example;

import java.util.function.Predicate;

/**
 * Created by qiuzhanghua on 2017/2/6.
 */
public class MathIntegration {

  public static void main(String[] args) {
    double d1 = integrate(x -> x, 10, 100, 1000);
    System.out.println(d1);
    double d2 = integrate(x -> x * x, 10, 100, 1000);
    System.out.println(d2);
    double d3 = integrate(x -> Math.pow(x, 3), 10, 100, 1000);
    System.out.println(d3);
    double d4 = integrate(Math::sin, 10, 100, 1000);
    System.out.println(d4);

    Predicate p ;
  }

  public static double integrate(Integrable func, double x1, double x2, int numSlices) {
    if (numSlices < 1) numSlices = 1;
    double delta = (x2 - x1) / numSlices;
    double start = x1 + delta / 2.0;
    double sum = 0.0;
    for (int i = 0; i <numSlices; i++) {
      sum += delta * func.eval(start + delta * i);
    }
    return sum;
  }
}
