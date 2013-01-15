/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbEntity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author asus
 */
@Entity
@Table(name = "GROUPTABLE")
@NamedQueries({
    @NamedQuery(name = GroupTable.FIND_GROUPTABLE_BY_LOGIN, query = "SELECT m FROM GroupTable m WHERE m.login LIKE :login"),
    @NamedQuery(name = GroupTable.FIND_GROUPTABLE_BY_GROUPID, query = "SELECT m FROM GroupTable m WHERE m.grouptablePK.groupid = :group")})
public class GroupTable implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GrouptablePK grouptablePK;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "USERNAME")
    private String login;

    
    public static final String FIND_GROUPTABLE_BY_LOGIN = "GroupTable.findByLogin";
    public static final String FIND_GROUPTABLE_BY_GROUPID = "GroupTablePK.findByGroupID";
    
    
    public GroupTable() {
    }

    public GroupTable(GrouptablePK grouptablePK) {
        this.grouptablePK = grouptablePK;
    }

    public GroupTable(String username, String groupid) {
        this.grouptablePK = new GrouptablePK(username, groupid);
        this.login = username;
    }
    
    public GrouptablePK getGrouptablePK() {
        return grouptablePK;
    }

    public void setGrouptablePK(GrouptablePK grouptablePK) {
        this.grouptablePK = grouptablePK;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grouptablePK != null ? grouptablePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupTable)) {
            return false;
        }
        GroupTable other = (GroupTable) object;
        if ((this.grouptablePK == null && other.grouptablePK != null) || (this.grouptablePK != null && !this.grouptablePK.equals(other.grouptablePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "biodat.elerning.entities.Grouptable[ grouptablePK=" + grouptablePK + " ]";
    }
}
