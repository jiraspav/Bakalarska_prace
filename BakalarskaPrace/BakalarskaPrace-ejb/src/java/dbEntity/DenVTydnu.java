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
@Table(name = "den_v_tydnu")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DenVTydnu.findAll", query = "SELECT d FROM DenVTydnu d"),
    @NamedQuery(name = "DenVTydnu.findByIDdnu", query = "SELECT d FROM DenVTydnu d WHERE d.iDdnu = :iDdnu"),
    @NamedQuery(name = "DenVTydnu.findByNazev", query = "SELECT d FROM DenVTydnu d WHERE d.nazev = :nazev")})
public class DenVTydnu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_dnu")
    private Integer iDdnu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "nazev")
    private String nazev;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDdnu")
    private Collection<Rozvrhy> rozvrhyCollection;

    public DenVTydnu() {
    }

    public DenVTydnu(Integer iDdnu) {
        this.iDdnu = iDdnu;
    }

    public DenVTydnu(Integer iDdnu, String nazev) {
        this.iDdnu = iDdnu;
        this.nazev = nazev;
    }

    public Integer getIDdnu() {
        return iDdnu;
    }

    public void setIDdnu(Integer iDdnu) {
        this.iDdnu = iDdnu;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
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
        hash += (iDdnu != null ? iDdnu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DenVTydnu)) {
            return false;
        }
        DenVTydnu other = (DenVTydnu) object;
        if ((this.iDdnu == null && other.iDdnu != null) || (this.iDdnu != null && !this.iDdnu.equals(other.iDdnu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.DenVTydnu[ iDdnu=" + iDdnu + " ]";
    }
    
}
