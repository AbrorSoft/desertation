package org.abror.repository;

import java.util.Optional;
import org.abror.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long>, JpaSpecificationExecutor<File> {
    /**
     *
     * @param fileName
     * @return
     */
    Optional<File> findFirstByName(String fileName);
}
