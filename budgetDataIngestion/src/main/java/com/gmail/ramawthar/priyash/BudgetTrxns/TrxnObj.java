package com.gmail.ramawthar.priyash.BudgetTrxns;

public class TrxnObj {
	public TrxnObj(String transaction) {
		super();
		this.transaction = transaction;
	}

	private String transaction;

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
	
}
