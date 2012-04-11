package com.ncond.jpower.app;

public enum PowerFlowAlg {

	NEWTON (1, "Newton's method"),
	FAST_DECOUPLED_XB (2, "Fast-Decoupled (XB)"),
	FAST_DECOUPLED_BX (3, "Fast-Decoupled (BX)"),
	GAUSS_SEIDEL (4, "Gauss-Seidel");

	private int option;
	private String str;

	private PowerFlowAlg(int option, String str) {
		this.option = option;
		this.str = str;
	}

	public int getOption() {
		return option;
	}

	@Override
	public String toString() {
		return str;
	}

}
