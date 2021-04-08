package logic;

import java.util.ArrayList;
import java.util.List;

public class Visitor {

	private String FirstName;
	private String LastName;
	private String VisitorID;
	private String Mail;
	private String PhoneNumber;
	private String type;
	private String CardNumber;
	private String NumOfPeople;
	private String CVVNumber;
	private String CreditCardValidity;
	private String SubscriptionNum;
	public String getSubscriptionNum() {
		return SubscriptionNum;
	}


	public void setSubscriptionNum(String subscriptionNum) {
		SubscriptionNum = subscriptionNum;
	}


	public String getCreditCardValidity() {
		return CreditCardValidity;
	}


	public void setCreditCardValidity(String creditCardValidity) {
		CreditCardValidity = creditCardValidity;
	}


	public Visitor(String FirstName, String LastName, String VisitorID, String Mail, String PhoneNumber,String type,String CardNumber,String NumOfPeople,String CVVNumber,String CreditCardValidity,String SubscriptionNum) {
		this.SubscriptionNum=SubscriptionNum;
		this.FirstName=FirstName;
		 this.LastName=LastName;
		 this.VisitorID=VisitorID;
		 this.Mail=Mail;
		 this.PhoneNumber=PhoneNumber;
		 this.type=type;
		 this.CardNumber=CardNumber;
		 this.NumOfPeople=NumOfPeople;
		 this.CVVNumber=CVVNumber;
		 this.CreditCardValidity=CreditCardValidity;
	}
	
	
	public String getCVVNumber() {
		return CVVNumber;
	}


	public void setCVVNumber(String cVVNumber) {
		CVVNumber = cVVNumber;
	}


	public String getNumOfPeople() {
		return NumOfPeople;
	}


	public void setNumOfPeople(String numOfPeople) {
		NumOfPeople = numOfPeople;
	}


	public String getCardNumber() {
		return CardNumber;
	}


	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getFirstName() {
		return FirstName;
	}





	public void setFirstName(String firstName) {
		FirstName = firstName;
	}





	public String getLastName() {
		return LastName;
	}





	public void setLastName(String lastName) {
		LastName = lastName;
	}





	public String getVisitorID() {
		return VisitorID;
	}





	public void setVisitorID(String visitorID) {
		VisitorID = visitorID;
	}





	public String getMail() {
		return Mail;
	}





	public void setMail(String mail) {
		Mail = mail;
	}





	public String getPhoneNumber() {
		return PhoneNumber;
	}





	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}


	@Override
	public String toString() {
		StringBuilder s=new StringBuilder();
		s.append(FirstName);
		s.append(' ');
		s.append(LastName);
		s.append(' ');
		s.append(VisitorID);
		s.append(' ');
		s.append(Mail);
		s.append(' ');
		s.append(PhoneNumber);
		s.append(' ');
		s.append(type);
		return s.toString();
	}
	
}
