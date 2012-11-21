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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pavel
 */
@Entity
@Table(name = "rezervace_mistnosti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RezervaceMistnosti.findAll", query = "SELECT r FROM RezervaceMistnosti r"),
    @NamedQuery(name = "RezervaceMistnosti.findByIDrezervace", query = "SELECT r FROM RezervaceMistnosti r WHERE r.iDrezervace = :iDrezervace"),
    @NamedQuery(name = "RezervaceMistnosti.findByDatumRezervace", query = "SELECT r FROM RezervaceMistnosti r WHERE r.datumRezervace = :datumRezervace"),
    @NamedQuery(name = "RezervaceMistnosti.findByOd", query = "SELECT r FROM RezervaceMistnosti r WHERE r.od = :od"),
    @NamedQuery(name = "RezervaceMistnosti.findByDo1", query = "SELECT r FROM RezervaceMistnosti r WHERE r.do1 = :do1"),
    @NamedQuery(name = "RezervaceMistnosti.findByNaCelouMistnost", query = "SELECT r FROM RezervaceMistnosti r WHERE r.naCelouMistnost = :naCelouMistnost"),
    @NamedQuery(name = "RezervaceMistnosti.findByIDuser", query = "SELECT r FROM RezervaceMistnosti r WHERE r.iDuser = :iDuser"),
    @NamedQuery(name = "RezervaceMistnosti.findByIDmistnosti", query = "SELECT r FROM RezervaceMistnosti r WHERE r.iDmistnosti = :iDmistnosti")})
public class RezervaceMistnosti implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_rezervace")
    private Integer iDrezervace;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum_rezervace")
    @Temporal(TemporalType.DATE)
    private Date datumRezervace;
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
    @Column(name = "na_celou_mistnost")
    private boolean naCelouMistnost;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "popis")
    private String popis;
    @JoinColumn(name = "ID_user", referencedColumnName = "ID_user")
    @ManyToOne(optional = false)
    private Uzivatel iDuser;
    @JoinColumn(name = "ID_mistnosti", referencedColumnName = "ID_mistnosti")
    @ManyToOne(optional = false)
    private Mistnost iDmistnosti;

    public RezervaceMistnosti() {
    }

    public RezervaceMistnosti(Integer iDrezervace) {
        this.iDrezervace = iDrezervace;
    }

    public RezervaceMistnosti(Integer iDrezervace, Date datumRezervace, Date od, Date do1, boolean naCelouMistnost, String popis) {
        this.iDrezervace = iDrezervace;
        this.datumRezervace = datumRezervace;
        this.od = od;
        this.do1 = do1;
        this.naCelouMistnost = naCelouMistnost;
        this.popis = popis;
    }

    public RezervaceMistnosti(Uzivatel uziv, Mistnost mistnost,Date datumRezervace, Date od, Date do1, boolean naCelouMistnost, String popis) {
        this.iDuser = uziv;
        this.iDmistnosti = mistnost;
        this.datumRezervace = datumRezervace;
        this.od = od;
        this.do1 = do1;
        this.naCelouMistnost = naCelouMistnost;
        this.popis = popis;
    }
    
    public Integer getIDrezervace() {
        return iDrezervace;
    }

    public void setIDrezervace(Integer iDrezervace) {
        this.iDrezervace = iDrezervace;
    }

    public Date getDatumRezervace() {
        return datumRezervace;
    }
    
    public String getFormatedDatumRezervace() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(datumRezervace);
    }
    
    public void setDatumRezervace(Date datumRezervace) {
        this.datumRezervace = datumRezervace;
    }

    public Date getOd() {
        return od;
    }

    public String getFormatedOd(){
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        return sdf.format(od);
    }
    
    public void setOd(Date od) {
        this.od = od;
    }

    public Date getDo1() {
        return do1;
    }

    public String getFormatedDo1(){
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        return sdf.format(do1);
    }
    
    public void setDo1(Date do1) {
        this.do1 = do1;
    }

    public boolean getNaCelouMistnost() {
        return naCelouMistnost;
    }

    public void setNaCelouMistnost(boolean naCelouMistnost) {
        this.naCelouMistnost = naCelouMistnost;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public Uzivatel getIDuser() {
        return iDuser;
    }

    public void setIDuser(Uzivatel iDuser) {
        this.iDuser = iDuser;
    }

    public Mistnost getIDmistnosti() {
        return iDmistnosti;
    }

    public void setIDmistnosti(Mistnost iDmistnosti) {
        this.iDmistnosti = iDmistnosti;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDrezervace != null ? iDrezervace.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RezervaceMistnosti)) {
            return false;
        }
        RezervaceMistnosti other = (RezervaceMistnosti) object;
        if ((this.iDrezervace == null && other.iDrezervace != null) || (this.iDrezervace != null && !this.iDrezervace.equals(other.iDrezervace))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.RezervaceMistnosti[ iDrezervace=" + iDrezervace + " ]";
    }
    
}
