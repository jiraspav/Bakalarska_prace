/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbEntity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Pavel
 */
@Entity
@Table(name = "typMistnosti")
@NamedQueries({
    @NamedQuery(name = TypMistnosti.FIND_BY_NAME, query = "SELECT m FROM TypMistnosti m WHERE m.nazev LIKE :nazev"),
    @NamedQuery(name = TypMistnosti.FIND_BY_ID, query = "SELECT m FROM TypMistnosti m WHERE m.iDTypMistnosti = :iDTypMistnosti")})
public class TypMistnosti implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_typ_mistnosti")
    private Integer iDTypMistnosti;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nazev")
    private String nazev;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDtyp")
    private Collection<Mistnost> mistnostCollection;
    
    
    
    public static final String FIND_BY_NAME = "TypMistnosti.findByName";
    public static final String FIND_BY_ID = "TypMistnosti.findById";
    
    
    public TypMistnosti() {
    }

    public TypMistnosti(Integer id, String nazev) {
        this.iDTypMistnosti = id;
        this.nazev = nazev;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getiDTypMistnosti() != null ? getiDTypMistnosti().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypMistnosti)) {
            return false;
        }
        TypMistnosti other = (TypMistnosti) object;
        if ((this.getiDTypMistnosti() == null && other.iDTypMistnosti != null) || (this.getiDTypMistnosti() != null && !this.iDTypMistnosti.equals(other.iDTypMistnosti))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.Mistnost[ iDmistnosti=" + iDTypMistnosti + " ]";
    }

    /**
     * @return the iDTypMistnosti
     */
    public Integer getiDTypMistnosti() {
        return iDTypMistnosti;
    }

    /**
     * @param iDTypMistnosti the iDTypMistnosti to set
     */
    public void setiDTypMistnosti(Integer iDTypMistnosti) {
        this.iDTypMistnosti = iDTypMistnosti;
    }

    /**
     * @return the nazev
     */
    public String getNazev() {
        return nazev;
    }

    /**
     * @param nazev the nazev to set
     */
    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    /**
     * @return the mistnostCollection
     */
    @XmlTransient
    public Collection<Mistnost> getMistnostCollection() {
        return mistnostCollection;
    }

    /**
     * @param mistnostCollection the mistnostCollection to set
     */
    public void setMistnostCollection(Collection<Mistnost> mistnostCollection) {
        this.mistnostCollection = mistnostCollection;
    }
    
}
