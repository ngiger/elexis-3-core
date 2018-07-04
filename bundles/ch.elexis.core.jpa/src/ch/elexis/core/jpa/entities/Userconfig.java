package ch.elexis.core.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "userconfig")
public class Userconfig extends AbstractDBObjectId {

	@ManyToOne()
	@JoinColumn(name = "UserID")
	private Kontakt owner;
	
	@Id
	@Column(unique = true, nullable = false, length = 80)
	private String param;
	
	@Lob
	private String value;

	public Kontakt getOwner() {
		return owner;
	}

	public void setOwner(Kontakt owner) {
		this.owner = owner;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String getId(){
		return getParam();
	}
	
	@Override
	public void setId(String id){
		setParam(id);
	}
	
	@Override
	public String getLabel(){
		return toString();
	}
	
	@Override
	public String toString(){
		return super.toString() + "owner=[" + getOwner() + "] param=[" + getParam() + "] wert=["
			+ getValue() + "]";
	}
}
