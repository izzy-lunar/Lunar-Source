package com.ownxile.rs2.world.economy;

public class StockMarket {

	private final Long DEFAULT_STOCK_TOTAL = Long.MAX_VALUE;

	private final int DEFAULT_STOCK_VALUE = 10000;
	private int stockValue;

	private Long totalStocks;

	public StockMarket(Long totalStocks) {
		this.totalStocks = totalStocks;
		updateStockValue();
	}

	public int getStockValue() {
		return stockValue;
	}

	public Long getTotalStocks() {
		return totalStocks;
	}

	public void setTotalStocks(Long totalStocks) {
		this.totalStocks = totalStocks;
	}

	private void updateStockValue() {
		stockValue = (int) (DEFAULT_STOCK_TOTAL / totalStocks * DEFAULT_STOCK_VALUE);
	}

}
