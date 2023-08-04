package com.gatech.gameswap.model;

import java.util.List;

public class History {
	SwapHistorySummary proposer_summary;
	SwapHistorySummary counterparty_summary;
	List<SwapHistory> history;
	
	
	public SwapHistorySummary getProposer_summary() {
		return proposer_summary;
	}
	public void setProposer_summary(SwapHistorySummary proposer_summary) {
		this.proposer_summary = proposer_summary;
	}
	public SwapHistorySummary getCounterparty_summary() {
		return counterparty_summary;
	}
	public void setCounterparty_summary(SwapHistorySummary counterparty_summary) {
		this.counterparty_summary = counterparty_summary;
	}
	public List<SwapHistory> getHistory() {
		return history;
	}
	public void setHistory(List<SwapHistory> history) {
		this.history = history;
	}

	
	
}
