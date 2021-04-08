package logic;

public class Discount {
	private String Park;
	private String IsApproval;
	private String TypeOfDiscount;
	private String StartDate;
	private String EndDate;
	private String DiscountValue;
	private String DiscountNum;

	public Discount(String Park, String IsApproval, String TypeOfDiscount, String StartDate, String EndDate,
			String DiscountValue, String DiscountNum) {
		this.Park = Park;
		this.IsApproval = IsApproval;
		this.TypeOfDiscount = TypeOfDiscount;
		this.StartDate = StartDate;
		this.EndDate = EndDate;
		this.DiscountNum = DiscountNum;
		this.DiscountValue = DiscountValue;

	}

	public String getPark() {
		return Park;
	}

	public void setPark(String park) {
		Park = park;
	}

	public String getIsApproval() {
		return IsApproval;
	}

	public void setIsApproval(String isApproval) {
		IsApproval = isApproval;
	}

	public String getTypeOfDiscount() {
		return TypeOfDiscount;
	}

	public void setTypeOfDiscount(String typeOfDiscount) {
		TypeOfDiscount = typeOfDiscount;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	public String getDiscountValue() {
		return DiscountValue;
	}

	public void setDiscountValue(String discountValue) {
		DiscountValue = discountValue;
	}

	public String getDiscountNum() {
		return DiscountNum;
	}

	public void setDiscountNum(String discountNum) {
		DiscountNum = discountNum;
	}
	
	
	
}
