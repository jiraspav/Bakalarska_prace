/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbEntity;

import entityFacade.GroupTableFacade;
import java.io.Serializable;
import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pavel
 */
@Entity
@Table(name = "uzivatel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = Uzivatel.FIND_ALL, query = "SELECT u FROM Uzivatel u"),
    @NamedQuery(name = Uzivatel.FIND_BY_ID, query = "SELECT u FROM Uzivatel u WHERE u.iDuser = :iDuser"),
    @NamedQuery(name = Uzivatel.FIND_BY_LOGIN, query = "SELECT u FROM Uzivatel u WHERE u.login = :login"),
    @NamedQuery(name = Uzivatel.FIND_BY_NAME, query = "SELECT u FROM Uzivatel u WHERE u.jmeno = :jmeno")})
public class Uzivatel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_user")
    private Long iDuser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "heslo")
    private String heslo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "jmeno")
    private String jmeno;
//    @JoinColumn(name = "ID_role", referencedColumnName = "ID_role")
//    @ManyToOne(optional = false)
//    private Role iDrole;

    public static final String FIND_BY_NAME = "Uzivatel.findByJmeno";
    public static final String FIND_BY_LOGIN = "Uzivatel.findByLogin";
    public static final String FIND_BY_ID = "Uzivatel.findByIDuser";
    public static final String FIND_ALL = "Uzivatel.findAll";
    
    public Uzivatel() {
    }

    public Uzivatel(Long iDuser) {
        this.iDuser = iDuser;
    }

    public Uzivatel(Long iDuser, String login, String jmeno) {
        this.iDuser = iDuser;
        this.login = login;
        this.jmeno = jmeno;
    }

    public Long getIDuser() {
        return iDuser;
    }

    public void setIDuser(Long iDuser) {
        this.iDuser = iDuser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

//    public Role getIDrole() {
//        return iDrole;
//    }
//
//    public void setIDrole(Role iDrole) {
//        this.iDrole = iDrole;
//    }
//
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDuser != null ? iDuser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Uzivatel)) {
            return false;
        }
        Uzivatel other = (Uzivatel) object;
        if ((this.iDuser == null && other.iDuser != null) || (this.iDuser != null && !this.iDuser.equals(other.iDuser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbEntity.Uzivatel[ iDuser=" + iDuser + " ]";
    }

    /**
     * @return the heslo
     */
    public String getHeslo() {
        return heslo;
    }

    /**
     * @param heslo the heslo to set
     */
    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }
    
}
