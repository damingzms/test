package cn.sam.commontest.beanutils.bean;

import java.util.List;

public class Apple {
	private String color;
	private Integer weight;
	private Double price;
	
	private Orange orange;
	private List<Orange> oranges;
	
	private Integer attr;

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Orange getOrange() {
		return orange;
	}

	public void setOrange(Orange orange) {
		this.orange = orange;
	}

	public List<Orange> getOranges() {
		return oranges;
	}

	public void setOranges(List<Orange> oranges) {
		this.oranges = oranges;
	}

	public Integer getAttr() {
		return attr;
	}

	public void setAttr(Integer attr) {
		this.attr = attr;
	}

}
