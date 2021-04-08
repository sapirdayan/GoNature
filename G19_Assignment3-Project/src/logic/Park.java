package logic;


public class Park {
	private int Capacity;
	private int Timeofaveragevisit;
	private int maxamountoforders;
	private String nameofpark;

	
	public int getCapacity() {
		return Capacity;
	}

	public void setCapacity(int capacity) {
		Capacity = capacity;
	}

	public int getTimeofaveragevisit() {
		return Timeofaveragevisit;
	}

	public void setTimeofaveragevisit(int timeofaveragevisit) {
		Timeofaveragevisit = timeofaveragevisit;
	}

	public int getMaxamountoforders() {
		return maxamountoforders;
	}

	public void setMaxamountoforders(int maxamountoforders) {
		this.maxamountoforders = maxamountoforders;
	}

	public String getNameofpark() {
		return nameofpark;
	}

	public void setNameofpark(String nameofpark) {
		this.nameofpark = nameofpark;
	}

	public Park(String nameofpark,int Timeofaveragevisit,int maxamountoforders,int Capacity)
	{
		this.Capacity=Capacity;
		this.Timeofaveragevisit=Timeofaveragevisit;
		this.maxamountoforders=maxamountoforders;
		this.nameofpark=nameofpark;
	}

	
}
