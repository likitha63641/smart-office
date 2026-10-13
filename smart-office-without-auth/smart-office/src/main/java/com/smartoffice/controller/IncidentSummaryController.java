package com.smartoffice.controller;

import com.smartoffice.dto.IncidentSummaryResponse;
import com.smartoffice.model.Issue;
import com.smartoffice.service.IncidentSummaryService;
import com.smartoffice.service.IssueService;
import com.smartoffice.service.UserContextService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issues")
public class IncidentSummaryController {

    private final IncidentSummaryService summaryService;
    private final IssueService issueService;
    private final UserContextService userContext;

    public IncidentSummaryController(IncidentSummaryService summaryService, IssueService issueService,
                                     UserContextService userContext) {
        this.summaryService = summaryService;
        this.issueService = issueService;
        this.userContext = userContext;
    }

    @GetMapping("/{id}/summary")
    public IncidentSummaryResponse summary(@PathVariable Long id, Authentication authentication) {
        Issue issue = issueService.getIssueById(id);
        if (!userContext.isAdmin(authentication) && !userContext.ownsIssue(issue, authentication)) {
            throw new AccessDeniedException("You do not have access to this issue");
        }
        return summaryService.generate(id);
    }
}