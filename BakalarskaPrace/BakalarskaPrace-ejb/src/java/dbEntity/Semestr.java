/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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
@Table(name = "semestr")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Semestr.findAll", query = "SELECT s FROM Semestr s"),
    @NamedQuery(name = "Semestr.findByIDsemestru", query = "SELECT s FROM Semestr s WHERE s.iDsemestru = :iDsemestru"),
    @NamedQuery(name = "Semestr.findByKod", query = "SELECT s FROM Semestr s WHERE s.kod LIKE :kod"),
    @NamedQuery(name = "Semestr.findByZacina", query = "SELECT s FROM Semestr s WHERE s.zacina = :zacina"),
    @NamedQuery(name = "Semestr.findByKonci", query = "SELECT s FROM Semestr s WHERE s.konci = :konci")})
public class Semestr implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_semestru")
    private Integer iDsemestru;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "kod")
    private String kod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zacina")
    @Temporal(TemporalType.DATE)
    private Date zacina;
    @Basic(optional = false)
    @NotNull
    @Column(name = "konci")
    @Temporal(TemporalType.DATE)
    private Date konci;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDsemestru")
    private Collection<UpdateRozvrhu> updateRozvrhuCollection;

    public Semestr() {
    }

    public Semestr(Integer iDsemestru) {
        this.iDsemestru = iDsemestru;
    }

    public Semestr(Integer iDsemestru, String kod, Date zacina, Date konci) {
        this.iDsemestru = iDsemestru;
        this.kod = kod;
        this.zacina = zacina;
        this.konci = konci;
    }

    public Integer getIDsemestru() {
        return iDsemestru;
    }

    public void setIDsemestru(Integer iDsemestru) {
        this.iDsemestru = iDsemestru;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public Date getZacina() {
        return zacina;
    }
    public String getFormatedZacina() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        if(zacina == null)
            return sdf.format(new Date());
        return sdf.format(zacina);
    }
    public void setZacina(Date zacina) {
        this.zacina = zacina;
    }

    public Date getKonci() {
        return konci;
    }
    public String getFormatedKonci() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        if(konci == null)
            return sdf.format(new Date());
        return sdf.format(konci);
    }
    public void setKonci(Date konci) {
        this.konci = konci;
    }

    @XmlTransient
    public Collection<UpdateRozvrhu> getUpdateRozvrhuCollection() {
        return updateRozvrhuCollection;
    }

    public void setUpdateRozvrhuCollection(Collection<UpdateRozvrhu> updateRozvrhuCollection) {
        this.updateRozvrhuCollection = updateRozvrhuCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDsemestru != null ? iDsemestru.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Semestr)) {
            return false;
        }
        Semestr other = (Semestr) object;
        if ((this.iDsemestru == null && other.iDsemestru != null) || (this.iDsemestru != null && !this.iDsemestru.equals(other.iDsemestru))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.Semestr[ iDsemestru=" + iDsemestru + " ]";
    }
    
}
