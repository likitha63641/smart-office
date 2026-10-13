package com.smartoffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartoffice.model.Issue;
import com.smartoffice.model.IssuePriority;
import com.smartoffice.model.IssueStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueRepository extends JpaRepository<Issue, Long> {
	
	 
	   List<Issue> findByEmployeeId(Long employeeId);
	   List<Issue> findByStatus(IssueStatus status);
	   List<Issue> findByPriority(IssuePriority priority);
	   
	   List<Issue> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
		        String title,
		        String description);
	   List<Issue> findByCategoryIgnoreCase(String category);
	   
	   @Query("""
			    SELECT i FROM Issue i
			    WHERE (:category IS NULL OR LOWER(i.category) = LOWER(:category))
			      AND (:priority IS NULL OR i.priority = :priority)
			      AND (:status IS NULL OR i.status = :status)
			""")
			List<Issue> findIssuesByOptionalFilters(
			        @Param("category") String category,
			        @Param("priority") IssuePriority priority,
			        @Param("status") IssueStatus status);
	   
	   @Query("""
			    SELECT i FROM Issue i
			    WHERE (:category IS NULL OR LOWER(i.category) = LOWER(:category))
			      AND (:priority IS NULL OR i.priority = :priority)
			      AND (:status IS NULL OR i.status = :status)
			""")
			Page<Issue> findIssuesByOptionalFiltersWithPagination(
			        @Param("category") String category,
			        @Param("priority") IssuePriority priority,
			        @Param("status") IssueStatus status,
			        Pageable pageable);
	   @Query("""
			    SELECT i.category, COUNT(i)
			    FROM Issue i
			    GROUP BY i.category
			    ORDER BY COUNT(i) DESC
			""")
			List<Object[]> countIssuesByCategory();
			
			@Query("""
				    SELECT i.technician.id,
				           i.technician.name,
				           i.technician.specialization,
				           COUNT(i)
				    FROM Issue i
				    WHERE i.technician IS NOT NULL
				    GROUP BY i.technician.id,
				             i.technician.name,
				             i.technician.specialization
				    ORDER BY COUNT(i) DESC
				""")
				List<Object[]> countIssuesByTechnician();
				@Query("""
					    SELECT i FROM Issue i
					    ORDER BY
					        CASE
					            WHEN i.priority = com.smartoffice.model.IssuePriority.CRITICAL THEN 1
					            WHEN i.priority = com.smartoffice.model.IssuePriority.HIGH THEN 2
					            WHEN i.priority = com.smartoffice.model.IssuePriority.MEDIUM THEN 3
					            WHEN i.priority = com.smartoffice.model.IssuePriority.LOW THEN 4
					        END,
					        i.title ASC
					""")
					Page<Issue> findAllByPriorityOrder(Pageable pageable);
				
				@Query("""
					    SELECT i
					    FROM Issue i
					    LEFT JOIN FETCH i.employee
					    LEFT JOIN FETCH i.technician
					""")
					List<Issue> findAllWithEmployeeAndTechnician();
				
				// EntityGraph approach
				@EntityGraph(attributePaths = {"employee", "technician"})
				@Query("SELECT i FROM Issue i")
				List<Issue> findAllWithEmployeeAndTechnicianUsingEntityGraph();
				
	   long countByStatus(IssueStatus status);

	   long countByPriority(IssuePriority priority);
	   
	     
}
