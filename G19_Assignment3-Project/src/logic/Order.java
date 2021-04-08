package logic;

public class Order {

	private String requestedpark;
	private String Dateofvisit;
	private String Visittime;
	private String numberofvisitors;
	private String Email;
	private String Type;
	private String IsDone;
	private String NoOfOrder;
	private String VisitorID;
	private String IsPaid;
	private String GapPrice;
	public String getGapPrice() {
		return GapPrice;
	}

	public void setGapPrice(String gapPrice) {
		GapPrice = gapPrice;
	}

	public String getIsPaid() {
		return IsPaid;
	}

	public void setIsPaid(String isPaid) {
		IsPaid = isPaid;
	}

	private String price;
	

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Order(String requestedpark, String Dateofvisit, String Visittime, String numberofvisitors, String Email,
			String Type, String IsDone,String NoOfOrder,String VisitorID,String GapPrice) {
		this.requestedpark = requestedpark;
		this.Dateofvisit = Dateofvisit;
		this.Visittime = Visittime;
		this.numberofvisitors = numberofvisitors;
		this.Email = Email;
		this.Type = Type;
		this.IsDone = "false";
		this.NoOfOrder=NoOfOrder;
		this.VisitorID=VisitorID;
		this.price="0";
		this.IsPaid="false";
		this.GapPrice=GapPrice;
	}

	public String getVisitorID() {
		return VisitorID;
	}

	public void setVisitorID(String visitorID) {
		VisitorID = visitorID;
	}

	public String getNoOfOrder() {
		return NoOfOrder;
	}

	public void setNoOfOrder(String noOfOrder) {
		NoOfOrder = noOfOrder;
	}

	public String getRequestedpark() {
		return requestedpark;
	}

	public void setRequestedpark(String string) {
		this.requestedpark = string;
	}

	public String getDateofvisit() {
		return Dateofvisit;
	}

	public void setDateofvisit(String dateofvisit) {
		Dateofvisit = dateofvisit;
	}

	public String getVisittime() {
		return Visittime;
	}

	public void setVisittime(String visittime) {
		Visittime = visittime;
	}

	public String getNumberofvisitors() {
		return numberofvisitors;
	}

	public void setNumberofvisitors(String numberofvisitors) {
		this.numberofvisitors = numberofvisitors;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getIsDone() {
		return IsDone;
	}

	public void setIsDone(String isDone) {
		IsDone = isDone;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(NoOfOrder);
		s.append(' ');
		s.append(requestedpark);
		s.append(' ');
		s.append(Dateofvisit);
		s.append(' ');
		s.append(Visittime);
		s.append(' ');
		s.append(numberofvisitors);
		s.append(' ');
		s.append(Email);
		s.append(' ');
		s.append(Type);
		s.append(' ');
		s.append(IsDone);
		s.append(' ');
		s.append(VisitorID);
		return s.toString();
	}

}
