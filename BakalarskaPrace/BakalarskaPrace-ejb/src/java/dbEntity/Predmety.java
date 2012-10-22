/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbEntity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Pavel
 */
@Entity
@Table(name = "predmety")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Predmety.findAll", query = "SELECT p FROM Predmety p"),
    @NamedQuery(name = "Predmety.findByIDpredmetu", query = "SELECT p FROM Predmety p WHERE p.iDpredmetu = :iDpredmetu"),
    @NamedQuery(name = "Predmety.findByZkratka", query = "SELECT p FROM Predmety p WHERE p.zkratka = :zkratka")})
public class Predmety implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_predmetu")
    private Long iDpredmetu;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "nazev")
    private String nazev;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "zkratka")
    private String zkratka;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDpredmetu")
    private Collection<Rozvrhy> rozvrhyCollection;

    public Predmety() {
    }

    public Predmety(Long iDpredmetu) {
        this.iDpredmetu = iDpredmetu;
    }

    public Predmety(Long iDpredmetu, String nazev, String zkratka) {
        this.iDpredmetu = iDpredmetu;
        this.nazev = nazev;
        this.zkratka = zkratka;
    }

    public Long getIDpredmetu() {
        return iDpredmetu;
    }

    public void setIDpredmetu(Long iDpredmetu) {
        this.iDpredmetu = iDpredmetu;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getZkratka() {
        return zkratka;
    }

    public void setZkratka(String zkratka) {
        this.zkratka = zkratka;
    }

    @XmlTransient
    public Collection<Rozvrhy> getRozvrhyCollection() {
        return rozvrhyCollection;
    }

    public void setRozvrhyCollection(Collection<Rozvrhy> rozvrhyCollection) {
        this.rozvrhyCollection = rozvrhyCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDpredmetu != null ? iDpredmetu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Predmety)) {
            return false;
        }
        Predmety other = (Predmety) object;
        if ((this.iDpredmetu == null && other.iDpredmetu != null) || (this.iDpredmetu != null && !this.iDpredmetu.equals(other.iDpredmetu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.Predmety[ iDpredmetu=" + iDpredmetu + " ]";
    }
    
}
