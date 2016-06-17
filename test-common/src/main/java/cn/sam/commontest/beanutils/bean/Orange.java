package cn.sam.commontest.beanutils.bean;

import java.util.List;

public class Orange {
	private String color;
	private Integer weight;
	
	private Apple apple;
	private List<Apple> apples;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Apple getApple() {
		return apple;
	}

	public void setApple(Apple apple) {
		this.apple = apple;
	}

	public List<Apple> getApples() {
		return apples;
	}

	public void setApples(List<Apple> apples) {
		this.apples = apples;
	}

}
