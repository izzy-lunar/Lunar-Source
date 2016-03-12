package com.ownxile.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * @author Robbie
 * @description this is inaccurate so don't use till it's fixed
 */
public class SystemInformation {

	private static final long KILOBYTE = 1024L;
	private static final long MEGABYTE = KILOBYTE * KILOBYTE;

	private static String getMemoryUsage() {
		long memory = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		return memory / MEGABYTE + "KB";
	}

	public static void print() {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory
				.getOperatingSystemMXBean();
		for (Method method : operatingSystemMXBean.getClass()
				.getDeclaredMethods()) {
			if (method.getName().contains("CpuTime")
					|| method.getName().contains("Swap"))
				continue;
			method.setAccessible(true);
			if (method.getName().startsWith("get")
					&& Modifier.isPublic(method.getModifiers())) {
				Object value;
				try {
					value = method.invoke(operatingSystemMXBean);
				} catch (Exception e) {
					value = e;
				}
				Long v = (Long) value;
				if (v > 2000000000)
					System.out.println(method.getName().substring(3) + " = "
							+ v / (MEGABYTE) + "GB");
				else
					System.out.println(method.getName().substring(3) + " = "
							+ v / KILOBYTE + "MB");
			}
		}
		System.out.println("ServerMemoryUsage = " + getMemoryUsage());
	}

}