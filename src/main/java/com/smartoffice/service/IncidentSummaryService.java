package com.smartoffice.service;

import com.smartoffice.dto.IncidentSummaryResponse;
import com.smartoffice.dto.IssueHistoryResponse;
import com.smartoffice.model.Issue;
import com.smartoffice.model.IssueStatus;
import com.smartoffice.model.Technician;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class IncidentSummaryService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    private final IssueService issueService;
    private final IssueHistoryService historyService;

    public IncidentSummaryService(IssueService issueService, IssueHistoryService historyService) {
        this.issueService = issueService;
        this.historyService = historyService;
    }

    @Transactional
    public IncidentSummaryResponse generate(Long issueId) {
        Issue issue = issueService.getIssueById(issueId);
        List<IssueHistoryResponse> history = historyService.get(issueId);

        String priority = issue.getPriority() != null ? issue.getPriority().name() : "MEDIUM";
        IssueStatus status = issue.getStatus() != null ? issue.getStatus() : IssueStatus.NEW;

        String reporter = issue.getEmployee() != null
                ? issue.getEmployee().getName() + " (" + issue.getEmployee().getEmail() + ")"
                : issue.getReportedBy();

        LocalDateTime reportedAt = history.isEmpty() ? null : history.get(0).getChangedAt();
        LocalDateTime finishedAt = null;
        for (IssueHistoryResponse h : history) {
            if (h.getNewStatus() == IssueStatus.RESOLVED || h.getNewStatus() == IssueStatus.CLOSED) {
                finishedAt = h.getChangedAt();
                break;
            }
        }

        String headline = "Incident #" + issue.getId() + " - " + issue.getTitle()
                + " (" + issue.getCategory() + ", " + priority + " priority)";

        StringBuilder sb = new StringBuilder();

        sb.append("OVERVIEW\n");
        if (reportedAt != null) sb.append("On ").append(FMT.format(reportedAt)).append(", ");
        sb.append(reporter).append(" reported a ").append(priority.toLowerCase())
          .append(" priority ").append(issue.getCategory())
          .append(" issue titled \"").append(issue.getTitle()).append("\".\n\n");

        sb.append("ISSUE DETAILS\n");
        sb.append(issue.getDescription()).append("\n\n");

        sb.append("CURRENT STATUS\n");
        sb.append("Status: ").append(status).append("\n");

        Technician technician = issue.getTechnician();
        if (technician != null) {
            sb.append("Technician: ").append(technician.getName())
              .append(" (").append(technician.getSpecialization()).append(")\n");
        } else {
            sb.append("Technician: not assigned yet\n");
        }

        if (reportedAt != null) {
            if (finishedAt != null) {
                sb.append("Resolution time: ").append(formatDuration(reportedAt, finishedAt)).append("\n");
            } else {
                sb.append("Open for: ").append(formatDuration(reportedAt, LocalDateTime.now())).append("\n");
            }
        }

        sb.append("Attachment: ")
          .append(issue.getAttachmentName() != null ? issue.getAttachmentName() : "none").append("\n\n");

        sb.append("TIMELINE\n");
        if (history.isEmpty()) {
            sb.append("- No history recorded\n");
        }
        for (IssueHistoryResponse h : history) {
            String when = h.getChangedAt() != null ? FMT.format(h.getChangedAt()) : "-";
            String change = h.getOldStatus() == null
                    ? "Issue created with status " + h.getNewStatus()
                    : h.getOldStatus() + " -> " + h.getNewStatus();
            sb.append("- ").append(when).append(": ").append(change)
              .append(" (by ").append(h.getChangedBy()).append(")\n");
        }

        sb.append("\nRECOMMENDED ACTION\n");
        sb.append(nextAction(status, priority));

        return new IncidentSummaryResponse(issue.getId(), headline, sb.toString());
    }

    private String nextAction(IssueStatus status, String priority) {
        boolean urgent = priority.equals("CRITICAL") || priority.equals("HIGH");
        return switch (status) {
            case NEW -> urgent
                    ? "Urgent: review this issue and assign a technician immediately."
                    : "Review this issue and assign a technician.";
            case REVIEWED -> "Assign an available technician.";
            case ASSIGNED -> "Waiting for the technician to start. Follow up if there is no progress.";
            case IN_PROGRESS -> "Work is in progress. Mark the issue RESOLVED once the fix is complete.";
            case RESOLVED -> "Confirm the problem is fixed, then close the issue.";
            case CLOSED -> "No action needed. This incident is closed.";
        };
    }

    private String formatDuration(LocalDateTime from, LocalDateTime to) {
        Duration d = Duration.between(from, to);
        long days = d.toDays();
        long hours = d.toHoursPart();
        long minutes = Math.max(d.toMinutesPart(), 0);
        if (days > 0) return days + " day(s) " + hours + " hour(s)";
        if (hours > 0) return hours + " hour(s) " + minutes + " minute(s)";
        return minutes + " minute(s)";
    }
}