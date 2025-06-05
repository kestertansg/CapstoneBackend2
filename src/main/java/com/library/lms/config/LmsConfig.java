package com.library.lms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lms")
public class LmsConfig {

	private int maxBooksPerMember;
	private int loanDurationDays;
	private double finePerDay;
	private double maxFinePerBook;
	
	// Generate getters/setters:
	public int getMaxBooksPerMember() {
		return maxBooksPerMember;
	}
	public void setMaxBooksPerMember(int maxBooksPerMember) {
		this.maxBooksPerMember = maxBooksPerMember;
	}
	public int getLoanDurationDays() {
		return loanDurationDays;
	}
	public void setLoanDurationDays(int loanDurationDays) {
		this.loanDurationDays = loanDurationDays;
	}
	public double getFinePerDay() {
		return finePerDay;
	}
	public void setFinePerDay(double finePerDay) {
		this.finePerDay = finePerDay;
	}
	public double getMaxFinePerBook() {
		return maxFinePerBook;
	}
	public void setMaxFinePerBook(double maxFinePerBook) {
		this.maxFinePerBook = maxFinePerBook;
	}
	
	
	
}
