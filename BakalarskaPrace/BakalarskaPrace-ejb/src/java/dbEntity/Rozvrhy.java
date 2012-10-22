/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbEntity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pavel
 */
@Entity
@Table(name = "rozvrhy")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rozvrhy.findAll", query = "SELECT r FROM Rozvrhy r"),
    @NamedQuery(name = "Rozvrhy.findByIDrozvrhu", query = "SELECT r FROM Rozvrhy r WHERE r.iDrozvrhu = :iDrozvrhu"),
    @NamedQuery(name = "Rozvrhy.findByOd", query = "SELECT r FROM Rozvrhy r WHERE r.od = :od"),
    @NamedQuery(name = "Rozvrhy.findByDo1", query = "SELECT r FROM Rozvrhy r WHERE r.do1 = :do1"),
    @NamedQuery(name = "Rozvrhy.findByLichyTyden", query = "SELECT r FROM Rozvrhy r WHERE r.lichyTyden = :lichyTyden"),
    @NamedQuery(name = "Rozvrhy.findBySudyTyden", query = "SELECT r FROM Rozvrhy r WHERE r.sudyTyden = :sudyTyden"),
    @NamedQuery(name = "Rozvrhy.findByIDmistnosti", query = "SELECT r FROM Rozvrhy r WHERE r.iDmistnosti = :iDmistnosti"),
    @NamedQuery(name = "Rozvrhy.findByIDmistnostiAdnu", query = "SELECT r FROM Rozvrhy r WHERE r.iDmistnosti = :iDmistnosti AND r.iDdnu = :iDdnu")})
public class Rozvrhy implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_rozvrhu")
    private Long iDrozvrhu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "od")
    @Temporal(TemporalType.TIME)
    private Date od;
    @Basic(optional = false)
    @NotNull
    @Column(name = "do")
    @Temporal(TemporalType.TIME)
    private Date do1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lichy_tyden")
    private boolean lichyTyden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sudy_tyden")
    private boolean sudyTyden;
    @JoinColumn(name = "ID_mistnosti", referencedColumnName = "ID_mistnosti")
    @ManyToOne(optional = false)
    private Mistnost iDmistnosti;
    @JoinColumn(name = "ID_dnu", referencedColumnName = "ID_dnu")
    @ManyToOne(optional = false)
    private DenVTydnu iDdnu;
    @JoinColumn(name = "ID_predmetu", referencedColumnName = "ID_predmetu")
    @ManyToOne(optional = false)
    private Predmety iDpredmetu;

    public Rozvrhy() {
    }

    public Rozvrhy(Long iDrozvrhu) {
        this.iDrozvrhu = iDrozvrhu;
    }

    public Rozvrhy(Long iDrozvrhu, Date od, Date do1, boolean lichyTyden, boolean sudyTyden) {
        this.iDrozvrhu = iDrozvrhu;
        this.od = od;
        this.do1 = do1;
        this.lichyTyden = lichyTyden;
        this.sudyTyden = sudyTyden;
    }

    public Long getIDrozvrhu() {
        return iDrozvrhu;
    }

    public void setIDrozvrhu(Long iDrozvrhu) {
        this.iDrozvrhu = iDrozvrhu;
    }

    public Date getOd() {
        return od;
    }

    public void setOd(Date od) {
        this.od = od;
    }

    public Date getDo1() {
        return do1;
    }

    public void setDo1(Date do1) {
        this.do1 = do1;
    }

    public boolean getLichyTyden() {
        return lichyTyden;
    }

    public void setLichyTyden(boolean lichyTyden) {
        this.lichyTyden = lichyTyden;
    }

    public boolean getSudyTyden() {
        return sudyTyden;
    }

    public void setSudyTyden(boolean sudyTyden) {
        this.sudyTyden = sudyTyden;
    }

    public Mistnost getIDmistnosti() {
        return iDmistnosti;
    }

    public void setIDmistnosti(Mistnost iDmistnosti) {
        this.iDmistnosti = iDmistnosti;
    }

    public DenVTydnu getIDdnu() {
        return iDdnu;
    }

    public void setIDdnu(DenVTydnu iDdnu) {
        this.iDdnu = iDdnu;
    }

    public Predmety getIDpredmetu() {
        return iDpredmetu;
    }

    public void setIDpredmetu(Predmety iDpredmetu) {
        this.iDpredmetu = iDpredmetu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDrozvrhu != null ? iDrozvrhu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rozvrhy)) {
            return false;
        }
        Rozvrhy other = (Rozvrhy) object;
        if ((this.iDrozvrhu == null && other.iDrozvrhu != null) || (this.iDrozvrhu != null && !this.iDrozvrhu.equals(other.iDrozvrhu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.Rozvrhy[ iDrozvrhu=" + iDrozvrhu + " ]";
    }
    
}
