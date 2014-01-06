package org.adrianwalker.recipecosting.common.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "RECIPE")
public class Recipe implements RecipeCostingUserEntity {

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
  @Column(name = "SERVES", nullable = false)
  private int serves;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", fetch = FetchType.LAZY)
  private List<RecipeIngredient> recipeIngredients;
  @XmlTransient
  @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User user;

  public Recipe() {
  }

  public Recipe(final Long id) {
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

  public int getServes() {
    return serves;
  }

  public void setServes(final int serves) {
    this.serves = serves;
  }

  public List<RecipeIngredient> getRecipeIngredients() {

    if (null == recipeIngredients) {
      recipeIngredients = new ArrayList<RecipeIngredient>();
    }

    return recipeIngredients;
  }

  public void setRecipeIngredients(final List<RecipeIngredient> recipeIngredients) {
    this.recipeIngredients = recipeIngredients;
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
    if (!(object instanceof Recipe)) {
      return false;
    }
    Recipe other = (Recipe) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Recipe{" + "id=" + id + ", name=" + name + ", serves=" + serves + ", recipeIngredients=" + recipeIngredients + ", user=" + user + '}';
  }
}
