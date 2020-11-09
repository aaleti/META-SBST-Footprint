package footprints.individual;

public class DataItem {
	
	private int itemId;	
	private String itemName;
	private double[] itemValues;
	private boolean normalDistributed;
	private double p;
	
	public DataItem() {
		super();
	}

	public DataItem(int itemId, String itemName, double[] itemValues) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemValues = itemValues;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double[] getItemValues() {
		return itemValues;
	}

	public void setItemValues(double[] itemValues) {
		this.itemValues = itemValues;
	}

	public Boolean isNormalDistributed() {
		return normalDistributed;
	}

	public void setNormalDistributed(Boolean normalDistributed) {
		this.normalDistributed = normalDistributed;
	}

	public Double getP() {
		return p;
	}

	public void setP(Double p) {
		this.p = p;
	}
        @Override
        public DataItem clone() throws CloneNotSupportedException{
            super.clone();
            DataItem di = new DataItem();
            di.itemId=itemId;
            di.itemName=itemName;
            di.itemValues=new double[itemValues.length];
            System.arraycopy(itemValues, 0, di.itemValues, 0, itemValues.length);
            di.normalDistributed=normalDistributed;
            di.p=p;
            return di;
        }
}
