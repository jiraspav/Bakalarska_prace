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
@Table(name = "mistnost")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mistnost.findAll", query = "SELECT m FROM Mistnost m"),
    @NamedQuery(name = "Mistnost.findByIDmistnosti", query = "SELECT m FROM Mistnost m WHERE m.iDmistnosti = :iDmistnosti"),
    @NamedQuery(name = "Mistnost.findByZkratka", query = "SELECT m FROM Mistnost m WHERE m.zkratka = :zkratka"),
    @NamedQuery(name = "Mistnost.findByIDstrediska", query = "SELECT m FROM Mistnost m WHERE m.iDstrediska = :iDstrediska")})
public class Mistnost implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_mistnosti")
    private Long iDmistnosti;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "zkratka")
    private String zkratka;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDmistnosti")
    private Collection<RezervaceMistnosti> rezervaceMistnostiCollection;
    @JoinColumn(name = "ID_strediska", referencedColumnName = "ID_stredisko")
    @ManyToOne(optional = false)
    private Stredisko iDstrediska;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDmistnosti")
    private Collection<Rozvrhy> rozvrhyCollection;
    

    public Mistnost() {
    }

    public Mistnost(Long iDmistnosti) {
        this.iDmistnosti = iDmistnosti;
    }

    public Mistnost(Long iDmistnosti, String zkratka) {
        this.iDmistnosti = iDmistnosti;
        this.zkratka = zkratka;
    }

    public Long getIDmistnosti() {
        return iDmistnosti;
    }

    public void setIDmistnosti(Long iDmistnosti) {
        this.iDmistnosti = iDmistnosti;
    }

    public String getZkratka() {
        return zkratka;
    }

    public void setZkratka(String zkratka) {
        this.zkratka = zkratka;
    }

    
    
    @XmlTransient
    public Collection<RezervaceMistnosti> getRezervaceMistnostiCollection() {
        return rezervaceMistnostiCollection;
    }

    public void setRezervaceMistnostiCollection(Collection<RezervaceMistnosti> rezervaceMistnostiCollection) {
        this.rezervaceMistnostiCollection = rezervaceMistnostiCollection;
    }

    public Stredisko getIDstrediska() {
        return iDstrediska;
    }

    public void setIDstrediska(Stredisko iDstrediska) {
        this.iDstrediska = iDstrediska;
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
        hash += (iDmistnosti != null ? iDmistnosti.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mistnost)) {
            return false;
        }
        Mistnost other = (Mistnost) object;
        if ((this.iDmistnosti == null && other.iDmistnosti != null) || (this.iDmistnosti != null && !this.iDmistnosti.equals(other.iDmistnosti))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.Mistnost[ iDmistnosti=" + iDmistnosti + " ]";
    }
    
}
