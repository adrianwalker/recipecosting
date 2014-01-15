package org.adrianwalker.recipecosting.common.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "USERS")
public class User implements RecipeCostingEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "USERNAME", length = 1000, nullable = false, unique = true)
  private String username;
  @Column(name = "PASSWORD", length = 1000, nullable = false)
  private String password;
  @Column(name = "EMAIL", length = 1000, nullable = false)
  private String email;
  @Column(name = "ENABLED", nullable = false)
  private Boolean enabled;
  @Column(name = "UUID", length = 36, nullable = false)
  private String uuid;

  public User() {
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(final Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final User other = (User) obj;
    if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + '}';
  }
}
