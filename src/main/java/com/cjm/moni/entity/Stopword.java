package com.cjm.moni.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author agp
 * @since 2018-11-30
 */
public class Stopword extends Model<Stopword> {

    private static final long serialVersionUID = 1L;

	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected Serializable pkVal() {
		return this.name;
	}

	@Override
	public String toString() {
		return "Stopword{" +
			", name=" + name +
			"}";
	}
}
