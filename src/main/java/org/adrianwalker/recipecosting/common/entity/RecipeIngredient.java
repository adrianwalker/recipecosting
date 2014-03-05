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
import javax.validation.constraints.Digits;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "RECIPE_INGREDIENT")
public class RecipeIngredient implements RecipeCostingUserEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID", nullable = false)
  private Long id;
  @Basic(optional = false)
  @Column(name = "AMOUNT", nullable = false, scale = 10, precision = 20)
  @Digits(integer = 20, fraction = 10, message = "Invalid ratio")
  private BigDecimal amount;
  @JoinColumn(name = "UNIT_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private Unit unit;
  @XmlTransient
  @JoinColumn(name = "RECIPE_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private Recipe recipe;
  @JoinColumn(name = "INGREDIENT_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private Ingredient ingredient;
  @XmlTransient
  @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User user;

  public RecipeIngredient() {
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(final Long id) {
    this.id = id;
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

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(final Unit unit) {
    this.unit = unit;
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public void setRecipe(final Recipe recipe) {
    this.recipe = recipe;
  }

  public Ingredient getIngredient() {
    return ingredient;
  }

  public void setIngredient(final Ingredient ingredient) {
    this.ingredient = ingredient;
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
    int hash = 7;
    hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RecipeIngredient other = (RecipeIngredient) obj;
    if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RecipeIngredient{"
            + "id=" + id
            + ", amount=" + amount
            + ", unit=" + unit.getName()
            + ", ingredient=" + ingredient.getName() + '}';
  }

  @Override
  public String getName() {
    return ingredient.getName();
  }
}
