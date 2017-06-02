package testData;

public class MailData {
	private String mailerName;
	private double mailsSent;
	private double openRate;
	private int mailsSentAvg;
	private double openRateAvg;
	private String mailSentChange;
	private String openRateChange;
	private int mailSentBenchMark;
	private double openRateBenchMark;
	
	public double getMailsSent() {
		return mailsSent;
	}
	
	public void setMailsSent(double mailsSent) {
		this.mailsSent = mailsSent;
	}

	public double getOpenRate() {
		return openRate;
	}

	public void setOpenRate(double openRate) {
		this.openRate = openRate;
	}

	public String getMailerName() {
		return mailerName;
	}

	public void setMailerName(String mailerName) {
		this.mailerName = mailerName;
	}

	public double getMailsSentAvg() {
		return mailsSentAvg;
	}

	public void setMailsSentAvg(int mailsSentAvg) {
		this.mailsSentAvg = mailsSentAvg;
	}

	public double getOpenRateAvg() {
		return openRateAvg;
	}

	public void setOpenRateAvg(double openRateAvg) {
		this.openRateAvg = openRateAvg;
	}

	public String getMailSentChange() {
		return mailSentChange;
	}

	public void setMailSentChange(String mailSentChange) {
		this.mailSentChange = mailSentChange;
	}

	public String getOpenRateChange() {
		return openRateChange;
	}

	public void setOpenRateChange(String openRateChange) {
		this.openRateChange = openRateChange;
	}

	public double getMailSentBenchMark() {
		return mailSentBenchMark;
	}

	public void setMailSentBenchMark(int mailSentBenchMark) {
		this.mailSentBenchMark = mailSentBenchMark;
	}

	public double getOpenRateBenchMark() {
		return openRateBenchMark;
	}

	public void setOpenRateBenchMark(double openRateBenchMark) {
		this.openRateBenchMark = openRateBenchMark;
	}

}
