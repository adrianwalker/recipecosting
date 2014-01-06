package org.adrianwalker.recipecosting.common.entity;

import java.math.BigDecimal;
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
@Table(name = "UNIT_CONVERSION")
public class UnitConversion implements RecipeCostingUserEntity {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID", nullable = false)
  private Long id;
  @Basic(optional = false)
  @Column(name = "RATIO", nullable = false, scale = 10, precision = 20)
  private BigDecimal ratio;
  @JoinColumn(name = "FROM_UNIT_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Unit unitFrom;
  @JoinColumn(name = "TO_UNIT_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Unit unitTo;
  @XmlTransient
  @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User user;

  public UnitConversion() {
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(final Long id) {
    this.id = id;
  }

  public BigDecimal getRatio() {

    if (null == ratio) {
      ratio = BigDecimal.ZERO;
    }

    return ratio;
  }

  public void setRatio(final BigDecimal ratio) {
    this.ratio = ratio;
  }

  public Unit getUnitFrom() {
    return unitFrom;
  }

  public void setUnitFrom(final Unit unitFrom) {
    this.unitFrom = unitFrom;
  }

  public Unit getUnitTo() {
    return unitTo;
  }

  public void setUnitTo(final Unit unitTo) {
    this.unitTo = unitTo;
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
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof UnitConversion)) {
      return false;
    }
    UnitConversion other = (UnitConversion) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "UnitConversion{" + "id=" + id + ", ratio=" + ratio + ", unitFrom=" + unitFrom + ", unitTo=" + unitTo + ", user=" + user + '}';
  }
}
