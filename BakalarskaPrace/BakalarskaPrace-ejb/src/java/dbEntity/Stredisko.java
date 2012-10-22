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
@Table(name = "stredisko")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stredisko.findAll", query = "SELECT s FROM Stredisko s"),
    @NamedQuery(name = "Stredisko.findByIDstredisko", query = "SELECT s FROM Stredisko s WHERE s.iDstredisko = :iDstredisko"),
    @NamedQuery(name = "Stredisko.findByIdentCislo", query = "SELECT s FROM Stredisko s WHERE s.identCislo = :identCislo"),
    @NamedQuery(name = "Stredisko.findByNazev", query = "SELECT s FROM Stredisko s WHERE s.nazev = :nazev")})
public class Stredisko implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_stredisko")
    private Long iDstredisko;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ident_cislo")
    private long identCislo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "nazev")
    private String nazev;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDstrediska")
    private Collection<Mistnost> mistnostCollection;

    public Stredisko() {
    }

    public Stredisko(Long iDstredisko) {
        this.iDstredisko = iDstredisko;
    }

    public Stredisko(Long iDstredisko, long identCislo, String nazev) {
        this.iDstredisko = iDstredisko;
        this.identCislo = identCislo;
        this.nazev = nazev;
    }

    public Long getIDstredisko() {
        return iDstredisko;
    }

    public void setIDstredisko(Long iDstredisko) {
        this.iDstredisko = iDstredisko;
    }

    public long getIdentCislo() {
        return identCislo;
    }

    public void setIdentCislo(long identCislo) {
        this.identCislo = identCislo;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    @XmlTransient
    public Collection<Mistnost> getMistnostCollection() {
        return mistnostCollection;
    }

    public void setMistnostCollection(Collection<Mistnost> mistnostCollection) {
        this.mistnostCollection = mistnostCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDstredisko != null ? iDstredisko.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stredisko)) {
            return false;
        }
        Stredisko other = (Stredisko) object;
        if ((this.iDstredisko == null && other.iDstredisko != null) || (this.iDstredisko != null && !this.iDstredisko.equals(other.iDstredisko))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.Stredisko[ iDstredisko=" + iDstredisko + " ]";
    }
    
}
