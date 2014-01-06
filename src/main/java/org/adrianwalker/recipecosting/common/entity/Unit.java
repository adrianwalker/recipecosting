package org.adrianwalker.recipecosting.common.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "UNIT")
public class Unit implements RecipeCostingUserEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "NAME", length = 1000, nullable = false)
  private String name;
  @XmlTransient
  @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User user;

  public Unit() {
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(final Long id) {
    this.id = id;
  }

  public String getName() {

    if (null == name) {
      name = "";
    }

    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(final Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Unit)) {
      return false;
    }
    Unit other = (Unit) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Unit{" + "id=" + id + ", name=" + name + ", user=" + user + '}';
  }
}
