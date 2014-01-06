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
@Table(name = "INGREDIENT")
public class Ingredient implements RecipeCostingUserEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID", nullable = false)
  private Long id;
  @Basic(optional = false)
  @Column(name = "NAME", nullable = false, length = 1000)
  private String name;
  @Basic(optional = false)
  @Column(name = "AMOUNT", nullable = false, scale = 10, precision = 20)
  private BigDecimal amount;
  @Basic(optional = false)
  @Column(name = "COST", nullable = false, scale = 10, precision = 20)
  private BigDecimal cost;
  @JoinColumn(name = "UNIT_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Unit unit;
  @XmlTransient
  @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User user;

  public Ingredient() {
  }

  public Ingredient(final Long id) {
    this.id = id;
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

  public BigDecimal getAmount() {

    if (null == amount) {
      amount = BigDecimal.ZERO;
    }
    return amount;
  }

  public void setAmount(final BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getCost() {

    if (null == cost) {
      cost = BigDecimal.ZERO;
  }

    return cost;
  }

  public void setCost(final BigDecimal cost) {
    this.cost = cost;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(final Unit unit) {
    this.unit = unit;
  }

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public void setUser(final User user) {
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
    if (!(object instanceof Ingredient)) {
      return false;
    }
    Ingredient other = (Ingredient) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Ingredient{" + "id=" + id + ", amount=" + amount + ", cost=" + cost + ", name=" + name + ", user=" + user + ", unit=" + unit + '}';
  }
}
