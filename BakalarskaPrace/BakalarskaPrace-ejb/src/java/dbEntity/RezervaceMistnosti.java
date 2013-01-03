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
@NamedQueries({
    @NamedQuery(name = "RezervaceMistnosti.findAll", query = "SELECT r FROM RezervaceMistnosti r"),
    @NamedQuery(name = "RezervaceMistnosti.findByIDrezervace", query = "SELECT r FROM RezervaceMistnosti r WHERE r.iDrezervace = :iDrezervace"),
    @NamedQuery(name = "RezervaceMistnosti.findByDatumRezervace", query = "SELECT r FROM RezervaceMistnosti r WHERE r.datumRezervace = :datumRezervace AND r.status = :status"),
    @NamedQuery(name = "RezervaceMistnosti.findByOd", query = "SELECT r FROM RezervaceMistnosti r WHERE r.od = :od AND r.status = :status"),
    @NamedQuery(name = "RezervaceMistnosti.findByDo1", query = "SELECT r FROM RezervaceMistnosti r WHERE r.do1 = :do1 AND r.status = :status"),
    @NamedQuery(name = "RezervaceMistnosti.findByIDuser", query = "SELECT r FROM RezervaceMistnosti r WHERE r.iDuser = :iDuser AND r.status = :status"),
    @NamedQuery(name = "RezervaceMistnosti.findByIDmistnosti", query = "SELECT r FROM RezervaceMistnosti r WHERE r.iDmistnosti = :iDmistnosti AND r.status = :status"),
    @NamedQuery(name = RezervaceMistnosti.FIND_BY_MISTNOST_A_DEN, query = "SELECT r FROM RezervaceMistnosti r WHERE r.iDmistnosti = :iDmistnosti AND r.datumRezervace = :datumRezervace AND r.status = :status")})
public class RezervaceMistnosti implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column(name = "rezervovanychMist")
    private int pocetRezervovanychMist;
    @Basic(optional = false)
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "popis")
    private String popis;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "ID_user", referencedColumnName = "ID_user")
    @ManyToOne(optional = false)
    private Uzivatel iDuser;
    @JoinColumn(name = "ID_mistnosti", referencedColumnName = "ID_mistnosti")
    @ManyToOne(optional = false)
    private Mistnost iDmistnosti;

    
    public static final String FIND_BY_MISTNOST_A_DEN = "RezervaceMistnosti.findByIDmistnostiIDden";
    
    
    
    public RezervaceMistnosti() {
    }

    public RezervaceMistnosti(Integer iDrezervace) {
        this.iDrezervace = iDrezervace;
    }

    public RezervaceMistnosti(Date datumRezervace, Date od, Date do1) {
        this.datumRezervace = datumRezervace;
        this.od = od;
        this.do1 = do1;
    }
    public RezervaceMistnosti(Date datumRezervace, Date od, Date do1, int pocetMist, String popis) {
        this.datumRezervace = datumRezervace;
        this.od = od;
        this.do1 = do1;
        this.pocetRezervovanychMist = pocetMist;
        this.popis = popis;
    }

    public RezervaceMistnosti(Uzivatel uziv, Mistnost mistnost,Date datumRezervace, Date od, Date do1, int pocetMist, String popis, String status) {
        this.iDuser = uziv;
        this.iDmistnosti = mistnost;
        this.datumRezervace = datumRezervace;
        this.od = od;
        this.do1 = do1;
        this.pocetRezervovanychMist = pocetMist;
        this.popis = popis;
        this.status = status;
    }
    
    public RezervaceMistnosti(Integer id,Uzivatel uziv, Mistnost mistnost,Date datumRezervace, Date od, Date do1, int pocetMist, String popis, String status) {
        this.iDrezervace = id;
        this.iDuser = uziv;
        this.iDmistnosti = mistnost;
        this.datumRezervace = datumRezervace;
        this.od = od;
        this.do1 = do1;
        this.pocetRezervovanychMist = pocetMist;
        this.popis = popis;
        this.status = status;
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

    /**
     * @return the pocetRezervovanychMist
     */
    public int getPocetRezervovanychMist() {
        return pocetRezervovanychMist;
    }

    /**
     * @param pocetRezervovanychMist the pocetRezervovanychMist to set
     */
    public void setPocetRezervovanychMist(int pocetRezervovanychMist) {
        this.pocetRezervovanychMist = pocetRezervovanychMist;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
}
