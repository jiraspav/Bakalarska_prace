/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pavel
 */
@Entity
@Table(name = "update_rozvrhu")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UpdateRozvrhu.findAll", query = "SELECT u FROM UpdateRozvrhu u"),
    @NamedQuery(name = "UpdateRozvrhu.findByIDupdaterozvrhu", query = "SELECT u FROM UpdateRozvrhu u WHERE u.iDupdaterozvrhu = :iDupdaterozvrhu"),
    @NamedQuery(name = "UpdateRozvrhu.findByDatumAktualizaceRozvrhu", query = "SELECT u FROM UpdateRozvrhu u WHERE u.datumAktualizaceRozvrhu = :datumAktualizaceRozvrhu")})
public class UpdateRozvrhu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_update_rozvrhu")
    private Integer iDupdaterozvrhu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum_aktualizace_rozvrhu")
    @Temporal(TemporalType.DATE)
    private Date datumAktualizaceRozvrhu;
    @JoinColumn(name = "ID_semestru", referencedColumnName = "ID_semestru")
    @ManyToOne(optional = false)
    private Semestr iDsemestru;

    public UpdateRozvrhu() {
    }

    public UpdateRozvrhu(Integer iDupdaterozvrhu) {
        this.iDupdaterozvrhu = iDupdaterozvrhu;
    }

    public UpdateRozvrhu(Integer iDupdaterozvrhu, Date datumAktualizaceRozvrhu) {
        this.iDupdaterozvrhu = iDupdaterozvrhu;
        this.datumAktualizaceRozvrhu = datumAktualizaceRozvrhu;
    }

    public Integer getIDupdaterozvrhu() {
        return iDupdaterozvrhu;
    }

    public void setIDupdaterozvrhu(Integer iDupdaterozvrhu) {
        this.iDupdaterozvrhu = iDupdaterozvrhu;
    }

    public Date getDatumAktualizaceRozvrhu() {
        return datumAktualizaceRozvrhu;
    }
    public String getFormattedDateUpdateRozvrhu(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(this.datumAktualizaceRozvrhu);
    }
    
    public void setDatumAktualizaceRozvrhu(Date datumAktualizaceRozvrhu) {
        this.datumAktualizaceRozvrhu = datumAktualizaceRozvrhu;
    }

    public Semestr getIDsemestru() {
        return iDsemestru;
    }

    public void setIDsemestru(Semestr iDsemestru) {
        this.iDsemestru = iDsemestru;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDupdaterozvrhu != null ? iDupdaterozvrhu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UpdateRozvrhu)) {
            return false;
        }
        UpdateRozvrhu other = (UpdateRozvrhu) object;
        if ((this.iDupdaterozvrhu == null && other.iDupdaterozvrhu != null) || (this.iDupdaterozvrhu != null && !this.iDupdaterozvrhu.equals(other.iDupdaterozvrhu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.UpdateRozvrhu[ iDupdaterozvrhu=" + iDupdaterozvrhu + " ]";
    }
    
}
