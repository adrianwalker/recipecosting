package org.adrianwalker.recipecosting.common.entity;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Save<T extends RecipeCostingUserEntity> implements Serializable {

  private List<T> changed;
  private List<Long> ids;

  public Save() {
  }

  public List<T> getChanged() {
    return changed;
  }

  public void setChanged(List<T> changed) {
    this.changed = changed;
  }

  public List<Long> getIds() {
    return ids;
  }

  public void setIds(List<Long> ids) {
    this.ids = ids;
  }
}
