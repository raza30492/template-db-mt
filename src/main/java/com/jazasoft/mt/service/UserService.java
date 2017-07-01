package com.jazasoft.mt.service;

import com.jazasoft.mt.entity.master.User;
import com.jazasoft.mt.entity.tenant.MyRevisionEntity;
import com.jazasoft.mt.repository.master.UserRepository;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager")
public class UserService {

    UserRepository userRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOne(Long id){
        return userRepository.findOne(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        if (user.getPassword() == null) {
            user.setPassword(user.getMobile());
        }
        return userRepository.save(user);
    }

    public User update(User user){
        User user2 = userRepository.findOne(user.getId());

        if (user.getName() != null) user2.setName(user.getName());
        if (user.getUsername() != null) user2.setUsername(user.getUsername());
        if (user.getEmail() != null) user2.setEmail(user.getEmail());
        if (user.getMobile() != null) user2.setMobile(user.getMobile());

        return user2;
    }

    public void findLastChangeRevision(Long id) {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        List<Object[]> list = reader.createQuery()
                .forRevisionsOfEntity(User.class,false,true)
                .add(AuditEntity.id().eq(id))
                .getResultList();

        list.forEach(l -> {
            User user = (User) l[0];
            MyRevisionEntity ur = (MyRevisionEntity)l[1];
            RevisionType revisionType = (RevisionType)l[2];

            System.out.println("user = " + user);
            System.out.println("user_rev = " + ur);
            System.out.println("rev_type = " + revisionType.name());
        });


        Revision<Integer,User> revision = userRepository.findLastChangeRevision(id);

        System.out.println("Last Change Revision: ");
        printRevision(revision);

        System.out.println("All revisions: ");
        Revisions<Integer, User> revisions = userRepository.findRevisions(id);
        revisions.iterator().forEachRemaining(revisin -> {
            printRevision(revisin);
        });
    }

    private void printRevision(Revision<Integer,User> revision) {
        System.out.println("Revision no = " +revision.getRevisionNumber());
        System.out.println("Revision date = " + revision.getRevisionDate());
        System.out.println("revision data = " + revision.getEntity());
        if (revision.getMetadata().getDelegate() != null){
            MyRevisionEntity entity = (MyRevisionEntity)revision.getMetadata().getDelegate();
            System.out.println("modifiedBy = " + entity.getUsername());
        }
    }

    public long count() {
        return userRepository.count();
    }
}
