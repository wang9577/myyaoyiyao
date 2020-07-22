package com.ustudy.test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChartTest {
	public static void main(String[] args) throws Exception {
		System.out.println("开始计数：");
		int i=1;
		for (; i <=1000; i++) {
			System.out.print("\r目前计数：["+i+"...]                                       ");
			Thread.sleep(20);
		}
		System.out.println("\r总计数："+i+"                                     ");
		System.out.println("计数完毕");
	}
	public static final String MARK=",";
	
	public static String toMark(String p) {
		if(p.contains(",")) {
			return Stream.of(p.split(",")).map(c->ChartTest.toMark(c, 4, MARK)).collect(Collectors.joining(","));
		}else {
			return toMark(p,4,MARK);
		}
	}
	
	public static String toMark(String p,Integer i,String mark) {
		return p;
	}
	
}
