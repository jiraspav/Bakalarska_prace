/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbEntity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Pavel
 */
@Entity
@Table(name = "vyucujici")
@NamedQueries({
    @NamedQuery(name = Vyucujici.FIND_ALL, query = "SELECT v FROM Vyucujici v"),
    @NamedQuery(name = Vyucujici.FIND_BY_JMENO, query = "SELECT v FROM Vyucujici v WHERE v.jmeno LIKE :jmeno"),
    @NamedQuery(name = Vyucujici.FIND_BY_ID, query = "SELECT v FROM Vyucujici v WHERE v.iDvyucujici = :iDvyucujici")})
public class Vyucujici implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_vyucujici")
    private Long iDvyucujici;
    
    @NotNull
    @Column(name = "jmeno")
    private String jmeno;
    
    @Column(name = "kontakt")
    private String kontakt;
    
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "vyucujiciCollection")
    private Collection<Rozvrhy> rozvrhyCollection;

    
    public static final String FIND_ALL = "Vyucujici.findAll";
    public static final String FIND_BY_ID = "Vyucujici.findID";
    public static final String FIND_BY_JMENO = "Vyucujici.findByJmeno";

    public Vyucujici() {
    }

    
    public Vyucujici(Long id){
        this.iDvyucujici = id;
    }    
    
    public Vyucujici(Long iDvyucujici, String jmeno, String kontakt, Collection<Rozvrhy> rozvrhyCollection) {
        this.iDvyucujici = iDvyucujici;
        this.jmeno = jmeno;
        this.kontakt = kontakt;
        this.rozvrhyCollection = rozvrhyCollection;
    }
    
    
    
    /**
     * @return the rozvrhyCollection
     */
    public Collection<Rozvrhy> getRozvrhyCollection() {
        return rozvrhyCollection;
    }

    /**
     * @param rozvrhyCollection the rozvrhyCollection to set
     */
    public void setRozvrhyCollection(Collection<Rozvrhy> rozvrhyCollection) {
        this.setRozvrhyCollection(rozvrhyCollection);
    }

    /**
     * @return the iDvyucujici
     */
    public Long getiDvyucujici() {
        return iDvyucujici;
    }

    /**
     * @param iDvyucujici the iDvyucujici to set
     */
    public void setiDvyucujici(Long iDvyucujici) {
        this.iDvyucujici = iDvyucujici;
    }

    /**
     * @return the jmeno
     */
    public String getJmeno() {
        return jmeno;
    }

    /**
     * @param jmeno the jmeno to set
     */
    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    /**
     * @return the kontakt
     */
    public String getKontakt() {
        return kontakt;
    }

    /**
     * @param kontakt the kontakt to set
     */
    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDvyucujici != null ? iDvyucujici.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vyucujici)) {
            return false;
        }
        Vyucujici other = (Vyucujici) object;
        if ((this.iDvyucujici == null && other.iDvyucujici != null) || (this.iDvyucujici != null && !this.iDvyucujici.equals(other.iDvyucujici))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.Vyucujici[ iDvyucijiciho=" + iDvyucujici + " ]";
    }
}
