package ch.elexis.core.jpa.model.adapter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import ch.elexis.core.jpa.entities.AbstractDBObjectId;
import ch.elexis.core.model.Identifiable;

public abstract class AbstractIdModelAdapter<T extends AbstractDBObjectId> implements Identifiable {
	
	private T entity;
	
	public AbstractIdModelAdapter(T entity){
		this.entity = entity;
		// make sure model supports id and delete
		if (!(entity instanceof AbstractDBObjectId)) {
			throw new IllegalStateException(
				"Model " + entity + " is no subclass of "
					+ AbstractDBObjectId.class.getSimpleName());
		}
	}
	
	public T getEntity(){
		return entity;
	}
	
	@Override
	public String getId(){
		return getEntity().getId();
	}
	
	@Override
	public String getLabel(){
		return getEntity().getLabel();
	}
	
	protected Date toDate(LocalDateTime localDateTime){
		ZonedDateTime atZone = localDateTime.atZone(ZoneId.systemDefault());
		return Date.from(atZone.toInstant());
	}
	
	protected LocalDateTime toLocalDate(Date date){
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	
	// TODO maybe change to Objects 
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		return result;
	}
	
	// TODO maybe change to Objects 
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractIdModelAdapter<?> other = (AbstractIdModelAdapter<?>) obj;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		return true;
	}
}
