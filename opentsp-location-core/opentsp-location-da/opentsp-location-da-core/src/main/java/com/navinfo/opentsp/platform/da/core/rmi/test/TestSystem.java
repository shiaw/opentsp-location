package com.navinfo.opentsp.platform.da.core.rmi.test;

public class TestSystem {

	public static void main(String[] args){

		System.out.println("Java运行时环境版本:\n"+System.getProperty("java.version"));

		System.out.println("Java 运行时环境供应商:\n"+System.getProperty("java.vendor"));

		System.out.println("Java 供应商的URL:\n"+System.getProperty("java.vendor.url"));

		System.out.println("Java安装目录:\n"+System.getProperty("java.home"));

		System.out.println("Java 虚拟机规范版本:\n"+System.getProperty("java.vm.specification.version"));

		System.out.println("Java 类格式版本号:\n"+System.getProperty("java.class.version"));

		System.out.println("Java类路径：\n"+System.getProperty("java.class.path"));

		System.out.println("加载库时搜索的路径列表:\n"+System.getProperty("java.library.path"));

		System.out.println("默认的临时文件路径:\n"+System.getProperty("java.io.tmpdir"));

		System.out.println("要使用的 JIT 编译器的名称:\n"+System.getProperty("java.compiler"));

		System.out.println("一个或多个扩展目录的路径:\n"+System.getProperty("java.ext.dirs"));

		System.out.println("操作系统的名称:\n"+System.getProperty("os.name"));

		System.out.println("操作系统的架构:\n"+System.getProperty("os.arch"));

		System.out.println("操作系统的版本:\n"+System.getProperty("os.version"));

		System.out.println("文件分隔符（在 UNIX 系统中是“/”）:\n"+System.getProperty("file.separator"));

		System.out.println("路径分隔符（在 UNIX 系统中是“:”）:\n"+System.getProperty("path.separator"));

		System.out.println("行分隔符（在 UNIX 系统中是“/n”）:\n"+System.getProperty("line.separator"));

		System.out.println("用户的账户名称:\n"+System.getProperty("user.name"));

		System.out.println("用户的主目录:\n"+System.getProperty("user.home"));

		System.out.println("用户的当前工作目录:\n"+System.getProperty("user.dir"));

	}
}